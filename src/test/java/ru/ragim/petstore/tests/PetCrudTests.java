package ru.ragim.petstore.tests;

import org.junit.jupiter.api.Test;
import ru.ragim.petstore.config.TestConfig;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class PetCrudTests extends TestConfig {

    @Test
    void post_createPet() {
        long petId = newPetId();
        var body = Map.of("id", petId, "name", "Barsik", "status", "available");

        given()
                .contentType("application/json")
                .body(body)
                .when()
                .post("/pet")
                .then()
                .log().ifValidationFails()
                .statusCode(anyOf(is(200), is(201)))
                .body("id", notNullValue())
                .body("name", equalTo("Barsik"));
    }

    @Test
    void get_petById() {
        long petId = newPetId();
        createPet(petId, "Barsik");

        given()
                .when()
                .get("/pet/{id}", petId)
                .then()
                .log().ifValidationFails()
                .statusCode(200)
                .body("id", notNullValue())
                .body("name", equalTo("Barsik"));
    }

    @Test
    void put_updatePet() {
        long petId = newPetId();
        createPet(petId, "Barsik");

        var body = Map.of("id", petId, "name", "Barsik-upd", "status", "sold");

        given()
                .contentType("application/json")
                .body(body)
                .when()
                .put("/pet")
                .then()
                .log().ifValidationFails()
                .statusCode(200)
                .body("id", notNullValue())
                .body("name", equalTo("Barsik-upd"));
    }

    @Test
    void delete_removePet() {
        long petId = newPetId();
        createPet(petId, "Barsik");

        given()
                .when()
                .delete("/pet/{id}", petId)
                .then()
                .log().ifValidationFails()
                .statusCode(anyOf(is(200), is(204)));

        given()
                .when()
                .get("/pet/{id}", petId)
                .then()
                .log().ifValidationFails()
                .statusCode(anyOf(is(404), is(400)));
    }

    private void createPet(long petId, String name) {
        var body = Map.of("id", petId, "name", name, "status", "available");

        given()
                .contentType("application/json")
                .body(body)
                .when()
                .post("/pet")
                .then()
                .log().ifValidationFails()
                .statusCode(anyOf(is(200), is(201)));
    }

    private long newPetId() {
        // Уникально даже при быстром прогоне
        return System.currentTimeMillis() + (long) (Math.random() * 1000);
    }
}
