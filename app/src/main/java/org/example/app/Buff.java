package org.example.app;

import org.example.context.KeyRegistry;
import org.example.context.PropertyKey;

public final class Buff {
    public static final PropertyKey<Buff> KEY =
        KeyRegistry.register("lab4.app.buff", Buff.class);

    private final int power;

    private Buff(int power) {
        this.power = power;
    }

    public static Buff of(int power) {
        return new Buff(power);
    }

    public int power() {
        return power;
    }

    @Override
    public String toString() {
        return "+" + power;
    }
}
