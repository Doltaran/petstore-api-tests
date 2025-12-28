package ru.ragim.petstore.client;

import io.restassured.response.Response;

import java.util.Map;

public class UserApi extends BaseApi {

    public Response createUser(Map<String, Object> body) {
        return req()
                .body(body)
                .when()
                .post("/user");
    }

    public Response getUser(String username) {
        return req()
                .when()
                .get("/user/{username}", username);
    }

    public Response updateUser(String username, Map<String, Object> body) {
        return req()
                .body(body)
                .when()
                .put("/user/{username}", username);
    }

    public Response deleteUser(String username) {
        return req()
                .when()
                .delete("/user/{username}", username);
    }

    // Метод "с кучей параметров": login
    public Response login(String username, String password) {
        return req()
                .queryParam("username", username)
                .queryParam("password", password)
                .when()
                .get("/user/login");
    }

    public Response logout() {
        return req()
                .when()
                .get("/user/logout");
    }
}
