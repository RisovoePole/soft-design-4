package org.example.app;

import org.example.context.Entity;
import org.example.context.Operation;
import org.example.entities.Health;
import org.example.entities.Labels;

/**
 * Операция из проекта app. Использует компонент Health из другого проекта
 * и свой компонент Buff — библиотеке context не известно ни то, ни другое.
 */
public final class BuffOperation implements Operation {
    @Override
    public boolean appliesTo(Entity entity) {
        return entity.has(Health.KEY) && entity.has(Buff.KEY);
    }

    @Override
    public void execute(Entity entity) {
        Buff buff = entity.get(Buff.KEY);
        Health health = entity.get(Health.KEY);
        entity.set(Health.KEY, health.withCurrent(health.current() + buff.power()));
        entity.remove(Buff.KEY);
        System.out.printf("  [app] %s: бафф +%d использован -> %s%n",
            Labels.of(entity), buff.power(), entity.get(Health.KEY));
    }
}
