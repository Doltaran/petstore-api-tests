package ru.ragim.petstore.tests;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import ru.ragim.petstore.config.TestConfig;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@Tag("smoke")
public class PetSearchTests extends TestConfig {

    @Test
    void get_findByStatus_shouldReturnNonEmptyList() {
        given()
                .queryParam("status", "available")
                .when()
                .get("/pet/findByStatus")
                .then()
                .log().ifValidationFails()
                .statusCode(200)
                .body("$", is(not(empty())));
    }
}
