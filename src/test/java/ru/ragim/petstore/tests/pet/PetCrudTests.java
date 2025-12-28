package ru.ragim.petstore.tests.pet;

import org.junit.jupiter.api.Test;
import ru.ragim.petstore.client.PetApi;
import ru.ragim.petstore.config.TestConfig;

import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

public class PetCrudTests extends TestConfig {

    private final PetApi petApi = new PetApi();

    @Test
    void pet_crud_flow() {
        long petId = System.currentTimeMillis() + (long)(Math.random() * 1000);

        // CREATE
        petApi.createPet(Map.of("id", petId, "name", "Barsik", "status", "available"))
                .then()
                .log().ifValidationFails()
                .statusCode(anyOf(is(200), is(201)))
                .body("id", notNullValue())
                .body("name", equalTo("Barsik"));

        // READ
        petApi.getPet(petId)
                .then()
                .log().ifValidationFails()
                .statusCode(200)
                .body("name", equalTo("Barsik"));

        // UPDATE
        petApi.updatePet(Map.of("id", petId, "name", "Barsik-upd", "status", "sold"))
                .then()
                .log().ifValidationFails()
                .statusCode(200)
                .body("name", equalTo("Barsik-upd"));

        // DELETE
        petApi.deletePet(petId)
                .then()
                .log().ifValidationFails()
                .statusCode(anyOf(is(200), is(204)));

        // VERIFY DELETED
        petApi.getPet(petId)
                .then()
                .log().ifValidationFails()
                .statusCode(anyOf(is(404), is(400)));
    }
}
