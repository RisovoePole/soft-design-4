package org.example.entities;

import org.example.context.Entity;
import org.example.context.Operation;

/** Операция из проекта entities: работает только с сущностями, у которых есть Health. */
public final class HealOperation implements Operation {
    private final int amount;

    public HealOperation(int amount) {
        this.amount = amount;
    }

    @Override
    public boolean appliesTo(Entity entity) {
        return entity.has(Health.KEY);
    }

    @Override
    public void execute(Entity entity) {
        Health health = entity.get(Health.KEY);
        entity.set(Health.KEY, health.withCurrent(health.current() + amount));
        System.out.printf("  [entities] %s: лечение +%d -> %s%n",
            Labels.of(entity), amount, entity.get(Health.KEY));
    }
}
