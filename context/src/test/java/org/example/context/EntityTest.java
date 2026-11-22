package org.example.context;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EntityTest {
    @Test
    void storesReadsAndRemovesValuesByTypedKey() {
        PropertyKey<String> name = KeyRegistry.register("lab4.test.entity.name", String.class);
        PropertyKey<Integer> hitPoints = KeyRegistry.register("lab4.test.entity.hp", Integer.class);
        Entity entity = new Entity();

        assertNull(entity.get(name));
        assertFalse(entity.has(name));

        entity.set(name, "Герой").set(hitPoints, 5);

        assertEquals("Герой", entity.get(name));
        assertEquals(Integer.valueOf(5), entity.get(hitPoints));
        assertTrue(entity.has(name));
        assertEquals(2, entity.size());
        assertEquals("Герой", entity.snapshot().get(name.name()));

        assertEquals("Герой", entity.remove(name));
        assertFalse(entity.has(name));
        assertEquals(Integer.valueOf(5), entity.getOrDefault(hitPoints, 0));
    }

    @Test
    @SuppressWarnings({"unchecked", "rawtypes"})
    void rejectsValueOfWrongRuntimeTypeEvenThroughForgedKey() {
        PropertyKey<String> key = KeyRegistry.register("lab4.test.entity.wrong-type", String.class);
        PropertyKey<Integer> forged = (PropertyKey<Integer>) (PropertyKey) key;
        Entity entity = new Entity();

        assertThrows(IllegalArgumentException.class, () -> entity.set(forged, 42));
    }

    @Test
    void conditionalOperationOnlyRunsWhenKeysArePresent() {
        PropertyKey<Integer> required = KeyRegistry.register("lab4.test.operations.required", Integer.class);
        AtomicInteger calls = new AtomicInteger();
        Operation operation = Operations.when(
            entity -> calls.incrementAndGet(),
            required
        );

        Entity empty = new Entity();
        Entity filled = new Entity().set(required, 1);

        assertFalse(operation.appliesTo(empty));
        assertTrue(operation.appliesTo(filled));

        new OperationPipeline().add(operation).runAll(List.of(empty, filled));
        assertEquals(1, calls.get());
    }
}
