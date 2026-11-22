package org.example.app;

import org.example.context.Entity;
import org.example.context.Operation;
import org.example.entities.Health;
import org.example.entities.Name;
import org.example.entities.Position;

/** Отчёт: читает компоненты, объявленные и в entities, и в app. */
public final class TeamReportOperation implements Operation {
    @Override
    public boolean appliesTo(Entity entity) {
        return entity.has(Name.KEY) && entity.has(Team.KEY);
    }

    @Override
    public void execute(Entity entity) {
        Health health = entity.get(Health.KEY);
        Position position = entity.get(Position.KEY);
        System.out.printf("  [app] %s | %s | HP %s | позиция %s%n",
            entity.get(Team.KEY).value(),
            entity.get(Name.KEY).value(),
            health == null ? "—" : health.toString(),
            position == null ? "—" : position.toString());
    }
}
