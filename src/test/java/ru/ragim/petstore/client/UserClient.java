package ru.ragim.petstore.client;

import io.restassured.response.Response;

import java.util.Map;

public class UserClient extends BaseApiClient {

    public Response create(Map<String, ?> body) {
        return req().body(body).when().post("/user");
    }

    public Response get(String username) {
        return req().when().get("/user/{username}", username);
    }

    public Response update(String username, Map<String, ?> body) {
        return req().body(body).when().put("/user/{username}", username);
    }

    public Response delete(String username) {
        return req().when().delete("/user/{username}", username);
    }

    public Response login(String username, String password) {
        return req()
                .queryParam("username", username)
                .queryParam("password", password)
                .when()
                .get("/user/login");
    }

    public Response logout() {
        return req().when().get("/user/logout");
    }
}
