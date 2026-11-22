package org.example.context;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class KeyRegistryTest {
    @Test
    void registersTypedKeyOnceAndLooksItUp() {
        PropertyKey<String> key = KeyRegistry.register("lab4.test.registry.name", String.class);

        assertNotEquals(0, key.id());
        assertEquals(String.class, key.type());
        assertSame(key, KeyRegistry.find(key.name()));
        assertSame(key, KeyRegistry.lookup(key.name(), String.class));
        assertSame(key, KeyRegistry.byId(key.id()));
        assertTrue(KeyRegistry.keys().containsKey(key.name()));
    }

    @Test
    void rejectsDuplicateNameFromAnotherModule() {
        KeyRegistry.register("lab4.test.registry.duplicate", Integer.class);

        IllegalArgumentException error = assertThrows(
            IllegalArgumentException.class,
            () -> KeyRegistry.register("lab4.test.registry.duplicate", String.class)
        );
        assertTrue(error.getMessage().contains("уже зарегистрирован"));
    }

    @Test
    void rejectsLookupWithIncompatibleType() {
        KeyRegistry.register("lab4.test.registry.type", Integer.class);

        IllegalArgumentException error = assertThrows(
            IllegalArgumentException.class,
            () -> KeyRegistry.lookup("lab4.test.registry.type", String.class)
        );
        assertTrue(error.getMessage().contains("Несовместимый тип"));
    }
}
