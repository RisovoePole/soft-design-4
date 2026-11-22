package org.example.entities;

import org.example.context.Entity;
import org.example.context.Operation;

/** Уменьшает здоровье и динамически удаляет компонент, когда эффект истёк. */
public final class PoisonOperation implements Operation {
    @Override
    public boolean appliesTo(Entity entity) {
        return entity.has(Health.KEY)
            && entity.has(Poison.KEY)
            && entity.get(Health.KEY).isAlive();
    }

    @Override
    public void execute(Entity entity) {
        Health health = entity.get(Health.KEY);
        Poison poison = entity.get(Poison.KEY);
        entity.set(Health.KEY, health.withCurrent(health.current() - poison.damage()));

        Poison next = poison.nextTurn();
        if (next == null) {
            entity.remove(Poison.KEY);
        } else {
            entity.set(Poison.KEY, next);
        }
        System.out.printf("  [entities] %s: яд %d -> %s%n",
            Labels.of(entity), poison.damage(), entity.get(Health.KEY));
    }
}
