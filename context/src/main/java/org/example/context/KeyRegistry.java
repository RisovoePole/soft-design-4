/*
 * Централизованный реестр ключей. Гарантирует, что два проекта, которые
 * используют один и тот же контекст, не смогут записать несовместимые данные
 * под одним и тем же именем ключа.
 */
package org.example.context;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public final class KeyRegistry {
    private static final Map<String, PropertyKey<?>> BY_NAME = new ConcurrentHashMap<>();
    private static final Map<Integer, PropertyKey<?>> BY_ID = new ConcurrentHashMap<>();
    private static int nextId = 1;

    private KeyRegistry() {
    }

    /**
     * Регистрирует новый ключ. Повторная регистрация того же имени — ошибка:
     * именно так мы не даём двум модулям «переопределить» чужой ключ.
     */
    public static synchronized <T> PropertyKey<T> register(String name, Class<T> type) {
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(type, "type");

        PropertyKey<?> existing = BY_NAME.get(name);
        if (existing != null) {
            throw new IllegalArgumentException(
                "Ключ уже зарегистрирован: " + name + " -> " + existing
            );
        }

        PropertyKey<T> key = new PropertyKey<>(nextId++, name, type);
        BY_NAME.put(name, key);
        BY_ID.put(key.id(), key);
        return key;
    }

    public static PropertyKey<?> find(String name) {
        return BY_NAME.get(name);
    }

    /**
     * Достаёт ключ по имени с проверкой типа. Возвращает тот же типизированный
     * ключ, который был зарегистрирован, поэтому каст безопасен.
     */
    public static <T> PropertyKey<T> lookup(String name, Class<T> type) {
        PropertyKey<?> key = BY_NAME.get(name);
        if (key == null) {
            throw new IllegalArgumentException("Неизвестный ключ: " + name);
        }
        if (!key.type().equals(type)) {
            throw new IllegalArgumentException(
                "Несовместимый тип для ключа " + name + ": ожидался "
                    + type.getSimpleName() + ", зарегистрирован " + key.type().getSimpleName()
            );
        }
        @SuppressWarnings("unchecked")
        PropertyKey<T> typed = (PropertyKey<T>) key;
        return typed;
    }

    public static PropertyKey<?> byId(int id) {
        return BY_ID.get(id);
    }

    public static Map<String, PropertyKey<?>> keys() {
        return Map.copyOf(BY_NAME);
    }
}
