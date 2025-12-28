package ru.ragim.petstore.data;

import java.time.Instant;
import java.util.Map;

public class TestDataFactory {

    public static long uniqueId() {
        return Instant.now().toEpochMilli() + (long) (Math.random() * 1000);
    }

    public static String uniqueUsername() {
        return "user_" + uniqueId();
    }

    public static Map<String, Object> pet(long id, String name, String status) {
        return Map.of(
                "id", id,
                "name", name,
                "status", status
        );
    }

    public static Map<String, Object> order(long id, long petId, int quantity, String status) {
        return Map.of(
                "id", id,
                "petId", petId,
                "quantity", quantity,
                "shipDate", "2025-01-01T00:00:00.000Z",
                "status", status,
                "complete", true
        );
    }

    public static Map<String, Object> user(String username, String password) {
        return Map.of(
                "id", uniqueId(),
                "username", username,
                "firstName", "Umar",
                "lastName", "Test",
                "email", username + "@test.local",
                "password", password,
                "phone", "123456",
                "userStatus", 1
        );
    }

    public static Map<String, Object> updatedUser(String username, String password) {
        return Map.of(
                "id", uniqueId(),
                "username", username,
                "firstName", "UmarUpdated",
                "lastName", "Test",
                "email", username + "@test.local",
                "password", password,
                "phone", "999",
                "userStatus", 1
        );
    }
}
