package org.example.entities;

import org.example.context.Entity;

public final class Labels {
    private Labels() {
    }

    public static String of(Entity entity) {
        Name name = entity.get(Name.KEY);
        return name == null ? "сущность #" + entity.id() : name.value();
    }
}
