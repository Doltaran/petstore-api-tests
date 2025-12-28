package ru.ragim.petstore.client;

import io.restassured.response.Response;

import java.util.Map;

public class StoreClient extends BaseApiClient {

    public Response createOrder(Map<String, ?> body) {
        return req().body(body).when().post("/store/order");
    }

    public Response getOrder(long id) {
        return req().when().get("/store/order/{id}", id);
    }

    public Response deleteOrder(long id) {
        return req().when().delete("/store/order/{id}", id);
    }

    public Response inventory() {
        return req().when().get("/store/inventory");
    }
}
