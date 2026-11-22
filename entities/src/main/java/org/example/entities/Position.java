package org.example.entities;

import org.example.context.KeyRegistry;
import org.example.context.PropertyKey;

public final class Position {
    public static final PropertyKey<Position> KEY =
        KeyRegistry.register("lab4.entities.position", Position.class);

    private final int x;
    private final int y;

    private Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public static Position of(int x, int y) {
        return new Position(x, y);
    }

    public Position moved(int dx, int dy) {
        return new Position(x + dx, y + dy);
    }

    public int x() {
        return x;
    }

    public int y() {
        return y;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
