package org.example.entities;

import org.example.context.KeyRegistry;
import org.example.context.PropertyKey;

import java.util.Objects;

/** Часть сущности, определённая в проекте entities. */
public final class Name {
    public static final PropertyKey<Name> KEY =
        KeyRegistry.register("lab4.entities.name", Name.class);

    private final String value;

    private Name(String value) {
        this.value = Objects.requireNonNull(value, "value");
    }

    public static Name of(String value) {
        return new Name(value);
    }

    public String value() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}
