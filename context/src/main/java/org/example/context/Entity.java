/*
 * Сущность с динамическими свойствами.
 *
 * Хранилище — ассоциативный массив "целый id ключа -> значение". Пользователь
 * работает только с типизированной обёрткой PropertyKey<T>, поэтому компилятор
 * проверяет типы, а реестр не даёт появиться двум разным ключам с одним id.
 */
package org.example.context;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public final class Entity {
    private static final AtomicInteger NEXT_ID = new AtomicInteger(1);

    private final int id;
    private final Map<Integer, Object> values = new LinkedHashMap<>();

    public Entity() {
        this.id = NEXT_ID.getAndIncrement();
    }

    public int id() {
        return id;
    }

    public <T> Entity set(PropertyKey<T> key, T value) {
        Objects.requireNonNull(key, "key");
        if (value != null && !key.type().isInstance(value)) {
            throw new IllegalArgumentException(
                "Значение ключа " + key + " должно быть " + key.type().getSimpleName()
            );
        }
        if (value == null) {
            values.remove(key.id());
        } else {
            values.put(key.id(), value);
        }
        return this;
    }

    public <T> T get(PropertyKey<T> key) {
        Objects.requireNonNull(key, "key");
        Object value = values.get(key.id());
        return value == null ? null : key.type().cast(value);
    }

    public <T> T getOrDefault(PropertyKey<T> key, T fallback) {
        T value = get(key);
        return value == null ? fallback : value;
    }

    public <T> boolean has(PropertyKey<T> key) {
        Objects.requireNonNull(key, "key");
        return values.containsKey(key.id());
    }

    public <T> T remove(PropertyKey<T> key) {
        Objects.requireNonNull(key, "key");
        Object value = values.remove(key.id());
        return value == null ? null : key.type().cast(value);
    }

    public int size() {
        return values.size();
    }

    /** Снимок для отладки/отчёта: имя ключа вместо его внутреннего id. */
    public Map<String, Object> snapshot() {
        Map<String, Object> result = new LinkedHashMap<>();
        for (Map.Entry<Integer, Object> entry : values.entrySet()) {
            PropertyKey<?> key = KeyRegistry.byId(entry.getKey());
            String label = key == null ? "#" + entry.getKey() : key.name();
            result.put(label, entry.getValue());
        }
        return result;
    }

    @Override
    public String toString() {
        return "Entity#" + id + snapshot();
    }
}
