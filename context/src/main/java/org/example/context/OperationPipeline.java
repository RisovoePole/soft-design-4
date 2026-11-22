/*
 * Конвейер операций: библиотека сама не знает, какие операции и данные
 * существуют, она только прогоняет переданные ей операции по сущностям.
 */
package org.example.context;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class OperationPipeline {
    private final List<Operation> operations = new ArrayList<>();

    public OperationPipeline add(Operation operation) {
        operations.add(Objects.requireNonNull(operation, "operation"));
        return this;
    }

    public void run(Entity entity) {
        Objects.requireNonNull(entity, "entity");
        for (Operation operation : List.copyOf(operations)) {
            if (operation.appliesTo(entity)) {
                operation.execute(entity);
            }
        }
    }

    public void runAll(Iterable<Entity> entities) {
        Objects.requireNonNull(entities, "entities");
        for (Entity entity : entities) {
            run(entity);
        }
    }

    public int size() {
        return operations.size();
    }
}
