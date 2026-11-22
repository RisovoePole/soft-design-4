package org.example.entities;

import org.example.context.KeyRegistry;
import org.example.context.PropertyKey;

/** Часть сущности: здоровье. Изменяется операциями, но ключ один на всю программу. */
public final class Health {
    public static final PropertyKey<Health> KEY =
        KeyRegistry.register("lab4.entities.health", Health.class);

    private final int current;
    private final int max;

    private Health(int current, int max) {
        if (max <= 0) {
            throw new IllegalArgumentException("max должен быть положительным");
        }
        this.max = max;
        this.current = Math.max(0, Math.min(current, max));
    }

    public static Health of(int current, int max) {
        return new Health(current, max);
    }

    public Health withCurrent(int value) {
        return new Health(value, max);
    }

    public int current() {
        return current;
    }

    public int max() {
        return max;
    }

    public boolean isAlive() {
        return current > 0;
    }

    @Override
    public String toString() {
        return current + "/" + max;
    }
}
