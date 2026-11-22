package org.example.entities;

import org.example.context.Entity;
import org.example.context.Operation;

public final class MoveOperation implements Operation {
    private final int dx;
    private final int dy;

    public MoveOperation(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }

    @Override
    public boolean appliesTo(Entity entity) {
        return entity.has(Position.KEY);
    }

    @Override
    public void execute(Entity entity) {
        Position position = entity.get(Position.KEY);
        entity.set(Position.KEY, position.moved(dx, dy));
        System.out.printf("  [entities] %s: перемещение -> %s%n",
            Labels.of(entity), entity.get(Position.KEY));
    }
}
