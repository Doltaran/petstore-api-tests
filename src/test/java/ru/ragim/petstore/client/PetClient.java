package ru.ragim.petstore.client;

import io.restassured.response.Response;

import java.util.Map;

public class PetClient extends BaseApiClient {

    public Response create(Map<String, ?> body) {
        return req().body(body).when().post("/pet");
    }

    public Response get(long id) {
        return req().when().get("/pet/{id}", id);
    }

    public Response update(Map<String, ?> body) {
        return req().body(body).when().put("/pet");
    }

    public Response delete(long id) {
        return req().when().delete("/pet/{id}", id);
    }

    public Response findByStatus(String status) {
        return req().queryParam("status", status).when().get("/pet/findByStatus");
    }
}
