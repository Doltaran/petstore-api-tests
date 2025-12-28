package ru.ragim.petstore.steps;

import ru.ragim.petstore.client.StoreClient;

import java.util.Map;

import static org.hamcrest.Matchers.*;

public class StoreSteps {
    private final StoreClient storeClient = new StoreClient();

    public void createOrder(Map<String, ?> body) {
        storeClient.createOrder(body)
                .then().log().ifValidationFails()
                .statusCode(anyOf(is(200), is(201)))
                .body("id", notNullValue());
    }

    public void assertOrderExists(long id) {
        storeClient.getOrder(id)
                .then().log().ifValidationFails()
                .statusCode(200)
                .body("id", notNullValue());
    }

    public void deleteOrder(long id) {
        storeClient.deleteOrder(id)
                .then().log().ifValidationFails()
                .statusCode(anyOf(is(200), is(204)));
    }

    public void assertInventoryIsMap() {
        storeClient.inventory()
                .then().log().ifValidationFails()
                .statusCode(200)
                .body("$", notNullValue());
    }
}
