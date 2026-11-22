package org.example.app;

import org.example.context.KeyRegistry;
import org.example.context.PropertyKey;

/** Часть сущности, определённая в потребляющем проекте app. */
public final class Team {
    public static final PropertyKey<Team> KEY =
        KeyRegistry.register("lab4.app.team", Team.class);

    private final String value;

    private Team(String value) {
        this.value = value;
    }

    public static Team of(String value) {
        return new Team(value);
    }

    public String value() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}
