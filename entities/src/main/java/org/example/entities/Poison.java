package org.example.entities;

import org.example.context.KeyRegistry;
import org.example.context.PropertyKey;

/** Временная часть сущности: операция сама удаляет её, когда ходы закончились. */
public final class Poison {
    public static final PropertyKey<Poison> KEY =
        KeyRegistry.register("lab4.entities.poison", Poison.class);

    private final int damage;
    private final int turnsLeft;

    private Poison(int damage, int turnsLeft) {
        if (damage < 0 || turnsLeft <= 0) {
            throw new IllegalArgumentException("Некорректный урон или число ходов");
        }
        this.damage = damage;
        this.turnsLeft = turnsLeft;
    }

    public static Poison of(int damage, int turnsLeft) {
        return new Poison(damage, turnsLeft);
    }

    public int damage() {
        return damage;
    }

    public int turnsLeft() {
        return turnsLeft;
    }

    /** Возвращает null, когда эффект закончился. */
    public Poison nextTurn() {
        return turnsLeft <= 1 ? null : new Poison(damage, turnsLeft - 1);
    }

    @Override
    public String toString() {
        return "-" + damage + " HP (" + turnsLeft + " ход.)";
    }
}
