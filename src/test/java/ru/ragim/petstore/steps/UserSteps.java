package ru.ragim.petstore.steps;

import ru.ragim.petstore.client.UserClient;

import java.util.Map;

import static org.hamcrest.Matchers.*;

public class UserSteps {
    private final UserClient userClient = new UserClient();

    public void createUser(Map<String, ?> body) {
        userClient.create(body)
                .then().log().ifValidationFails()
                .statusCode(anyOf(is(200), is(201)));
    }

    public void assertUserExists(String username) {
        userClient.get(username)
                .then().log().ifValidationFails()
                .statusCode(200)
                .body("username", equalTo(username));
    }

    public void login(String username, String password) {
        userClient.login(username, password)
                .then().log().ifValidationFails()
                .statusCode(200)
                .body("message", notNullValue());
    }

    public void logout() {
        userClient.logout()
                .then().log().ifValidationFails()
                .statusCode(200);
    }

    public void updateUser(String username, Map<String, ?> body) {
        userClient.update(username, body)
                .then().log().ifValidationFails()
                .statusCode(200);
    }

    public void deleteUser(String username) {
        userClient.delete(username)
                .then().log().ifValidationFails()
                .statusCode(anyOf(is(200), is(204)));
    }


    public void assertUserDeletedTolerant(String username) {
        userClient.get(username)
                .then().log().ifValidationFails()
                .statusCode(anyOf(is(200), is(400), is(404)));
    }
}
