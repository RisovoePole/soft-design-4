/*
 * Операция, которую потребитель добавляет в конвейер библиотеки.
 * Решение о применимости принимается на основе данных самой сущности.
 */
package org.example.context;

@FunctionalInterface
public interface Operation {
    default boolean appliesTo(Entity entity) {
        return true;
    }

    void execute(Entity entity);
}
