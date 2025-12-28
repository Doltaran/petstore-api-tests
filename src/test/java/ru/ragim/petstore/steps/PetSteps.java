package ru.ragim.petstore.steps;

import ru.ragim.petstore.client.PetClient;

import java.util.Map;

import static org.hamcrest.Matchers.*;

public class PetSteps {
    private final PetClient petClient = new PetClient();

    public void createPet(Map<String, ?> body) {
        petClient.create(body)
                .then().log().ifValidationFails()
                .statusCode(anyOf(is(200), is(201)))
                .body("id", notNullValue());
    }

    public void assertPetExists(long id, String expectedName) {
        petClient.get(id)
                .then().log().ifValidationFails()
                .statusCode(200)
                .body("name", equalTo(expectedName));
    }

    public void updatePet(Map<String, ?> body, String expectedName) {
        petClient.update(body)
                .then().log().ifValidationFails()
                .statusCode(200)
                .body("name", equalTo(expectedName));
    }

    public void deletePet(long id) {
        petClient.delete(id)
                .then().log().ifValidationFails()
                .statusCode(anyOf(is(200), is(204)));
    }

    public void assertPetDeleted(long id) {
        petClient.get(id)
                .then().log().ifValidationFails()
                .statusCode(anyOf(is(404), is(400)));
    }

    public void assertFindByStatusNotEmpty(String status) {
        petClient.findByStatus(status)
                .then().log().ifValidationFails()
                .statusCode(200)
                .body("$", not(empty()));
    }
}
