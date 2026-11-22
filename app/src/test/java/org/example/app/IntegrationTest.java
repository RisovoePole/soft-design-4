package org.example.app;

import org.example.context.Entity;
import org.example.context.KeyRegistry;
import org.example.context.OperationPipeline;
import org.example.context.PropertyKey;
import org.example.entities.HealOperation;
import org.example.entities.Health;
import org.example.entities.MoveOperation;
import org.example.entities.Name;
import org.example.entities.Poison;
import org.example.entities.PoisonOperation;
import org.example.entities.Position;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class IntegrationTest {
    @Test
    void appProcessesEntitiesDefinedInAnotherProject() {
        Entity hero = new Entity()
            .set(Name.KEY, Name.of("Герой"))
            .set(Health.KEY, Health.of(80, 100))
            .set(Position.KEY, Position.of(0, 0))
            .set(Poison.KEY, Poison.of(5, 2))
            .set(Buff.KEY, Buff.of(10));
        Entity chest = new Entity()
            .set(Name.KEY, Name.of("Сундук"))
            .set(Position.KEY, Position.of(1, 1));

        OperationPipeline pipeline = new OperationPipeline()
            .add(new HealOperation(15))    // из проекта entities
            .add(new BuffOperation())      // из проекта app
            .add(new PoisonOperation())    // из проекта entities
            .add(new MoveOperation(2, -1)); // из проекта entities

        pipeline.runAll(List.of(hero, chest));

        // 80 + 15 -> 95, бафф +10 -> 100 (ограничение max), яд -5 -> 95.
        assertEquals(95, hero.get(Health.KEY).current());
        assertEquals(1, hero.get(Poison.KEY).turnsLeft());
        assertFalse(hero.has(Buff.KEY));
        assertFalse(chest.has(Health.KEY)); // операция не применяется без компонента
        assertEquals(3, chest.get(Position.KEY).x());
        assertEquals(0, chest.get(Position.KEY).y());

        pipeline.run(hero); // яд заканчивается, компонент удаляется динамически
        assertFalse(hero.has(Poison.KEY));
        assertEquals(95, hero.get(Health.KEY).current());
        assertEquals(4, hero.get(Position.KEY).x());
        assertEquals(-2, hero.get(Position.KEY).y());
    }

    @Test
    void sharedRegistryPreventsIncompatibleKeysFromTwoProjects() {
        assertNotEquals(Health.KEY.id(), Team.KEY.id());
        assertNotEquals(Health.KEY.id(), Buff.KEY.id());

        IllegalArgumentException error = assertThrows(
            IllegalArgumentException.class,
            () -> KeyRegistry.register(Health.KEY.name(), Team.class)
        );
        assertTrue(error.getMessage().contains("уже зарегистрирован"));

        PropertyKey<Health> lookedUp = KeyRegistry.lookup(Health.KEY.name(), Health.class);
        assertSame(Health.KEY, lookedUp);
    }
}
