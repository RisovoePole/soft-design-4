/*
 * Типизированный ключ свойства. Идентичность ключа определяется целым id,
 * выданным центральным реестром, поэтому два ключа с одним именем не могут
 * существовать одновременно.
 */
package org.example.context;

import java.util.Objects;

public final class PropertyKey<T> {
    private final int id;
    private final String name;
    private final Class<T> type;

    // Создать ключ может только реестр.
    PropertyKey(int id, String name, Class<T> type) {
        this.id = id;
        this.name = Objects.requireNonNull(name, "name");
        this.type = Objects.requireNonNull(type, "type");
    }

    public int id() {
        return id;
    }

    public String name() {
        return name;
    }

    public Class<T> type() {
        return type;
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof PropertyKey<?> key && id == key.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }

    @Override
    public String toString() {
        return name + "#" + id + "<" + type.getSimpleName() + ">";
    }
}
