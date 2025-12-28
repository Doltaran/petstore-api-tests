package ru.ragim.petstore.tests;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import ru.ragim.petstore.config.TestConfig;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@Tag("smoke")
public class StoreOrderTests extends TestConfig {

    @Test
    void orderLifecycle_postGetDelete() {
        long orderId = System.currentTimeMillis() + (long) (Math.random() * 1000);

        var order = Map.of(
                "id", orderId,
                "petId", 1,
                "quantity", 2,
                "shipDate", "2025-01-01T00:00:00.000Z",
                "status", "placed",
                "complete", true
        );

        // POST /store/order
        given()
                .contentType("application/json")
                .body(order)
                .when()
                .post("/store/order")
                .then()
                .log().ifValidationFails()
                .statusCode(anyOf(is(200), is(201)))
                .body("id", notNullValue());

        // GET /store/order/{id}
        given()
                .when()
                .get("/store/order/{id}", orderId)
                .then()
                .log().ifValidationFails()
                .statusCode(200)
                .body("id", notNullValue());

        // DELETE /store/order/{id}
        given()
                .when()
                .delete("/store/order/{id}", orderId)
                .then()
                .log().ifValidationFails()
                .statusCode(anyOf(is(200), is(204)));
    }
}
