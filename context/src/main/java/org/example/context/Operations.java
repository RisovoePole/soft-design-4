package org.example.context;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public final class Operations {
    private Operations() {
    }

    /** Операция, которая выполняется только если все перечисленные ключи есть. */
    public static Operation when(Consumer<Entity> body, PropertyKey<?>... requiredKeys) {
        Objects.requireNonNull(body, "body");
        List<PropertyKey<?>> keys = List.of(requiredKeys.clone());
        return new Operation() {
            @Override
            public boolean appliesTo(Entity entity) {
                for (PropertyKey<?> key : keys) {
                    if (!entity.has(key)) {
                        return false;
                    }
                }
                return true;
            }

            @Override
            public void execute(Entity entity) {
                body.accept(entity);
            }
        };
    }
}
