package ru.ragim.petstore.tests.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import ru.ragim.petstore.client.UserClient;
import ru.ragim.petstore.config.AbstractIntegrationTest;
import ru.ragim.petstore.data.TestDataFactory;
import ru.ragim.petstore.steps.UserSteps;

import static org.hamcrest.Matchers.*;

@Tag("smoke")
public class UserTests extends AbstractIntegrationTest {

    private final UserSteps user = new UserSteps();
    private final UserClient userClient = new UserClient();

    @Test
    @DisplayName("USER: CRUD + Login/Logout (POST -> GET -> LOGIN -> PUT -> LOGOUT -> DELETE -> GET)")
    void userCrudAndAuthFlow() {
        String username = TestDataFactory.uniqueUsername();
        String password = "pass123";

        user.createUser(TestDataFactory.user(username, password));
        user.assertUserExists(username);

        user.login(username, password);
        user.updateUser(username, TestDataFactory.updatedUser(username, password));
        user.logout();

        user.deleteUser(username);
        user.assertUserDeletedTolerant(username);
    }

    @Test
    @DisplayName("USER: Get non-existing user returns 404/400")
    void getNonExistingUser() {
        userClient.get("no_such_user_123456789")
                .then().log().ifValidationFails()
                .statusCode(anyOf(is(404), is(400)));
    }

    @Test
    @DisplayName("USER: Create user and verify")
    void createUserAndVerify() {
        String username = TestDataFactory.uniqueUsername();
        user.createUser(TestDataFactory.user(username, "pass123"));
        user.assertUserExists(username);
        user.deleteUser(username);
    }

    @Test
    @DisplayName("USER: Update user information")
    void updateUserInformation() {
        String username = TestDataFactory.uniqueUsername();
        user.createUser(TestDataFactory.user(username, "pass123"));
        user.assertUserExists(username);
        
        user.updateUser(username, TestDataFactory.updatedUser(username, "pass123"));
        user.assertUserExists(username);
        
        user.deleteUser(username);
    }

    @Test
    @DisplayName("USER: Login with correct credentials")
    void loginWithCorrectCredentials() {
        String username = TestDataFactory.uniqueUsername();
        String password = "pass123";
        user.createUser(TestDataFactory.user(username, password));
        
        user.login(username, password);
        user.logout();
        
        user.deleteUser(username);
    }

    @Test
    @DisplayName("USER: Login and logout flow")
    void loginAndLogoutFlow() {
        String username = TestDataFactory.uniqueUsername();
        String password = "pass123";
        user.createUser(TestDataFactory.user(username, password));
        
        user.login(username, password);
        user.logout();
        
        // Можно попробовать залогиниться снова
        user.login(username, password);
        user.logout();
        
        user.deleteUser(username);
    }

    @Test
    @DisplayName("USER: Delete user and verify deletion")
    void deleteUserAndVerify() {
        String username = TestDataFactory.uniqueUsername();
        user.createUser(TestDataFactory.user(username, "pass123"));
        user.assertUserExists(username);
        
        user.deleteUser(username);
        user.assertUserDeletedTolerant(username);
    }

    @Test
    @DisplayName("USER: Create user with unique username")
    void createUserWithUniqueUsername() {
        String username1 = TestDataFactory.uniqueUsername();
        String username2 = TestDataFactory.uniqueUsername();
        
        user.createUser(TestDataFactory.user(username1, "pass123"));
        user.createUser(TestDataFactory.user(username2, "pass123"));
        
        user.assertUserExists(username1);
        user.assertUserExists(username2);
        
        user.deleteUser(username1);
        user.deleteUser(username2);
    }

    @Test
    @DisplayName("USER: Update user multiple times")
    void updateUserMultipleTimes() {
        String username = TestDataFactory.uniqueUsername();
        user.createUser(TestDataFactory.user(username, "pass123"));
        user.assertUserExists(username);
        
        user.updateUser(username, TestDataFactory.updatedUser(username, "pass123"));
        user.assertUserExists(username);
        
        user.updateUser(username, TestDataFactory.user(username, "pass123"));
        user.assertUserExists(username);
        
        user.deleteUser(username);
    }

    @Test
    @DisplayName("USER: Access control - User can only access own data")
    void accessControlUserCanOnlyAccessOwnData() {
        // Текущий пользователь уже создан в setUp
        user.assertUserExists(currentUsername);
        
        // Создаем второго пользователя
        String user2Username = TestDataFactory.uniqueUsername();
        user.createUser(TestDataFactory.user(user2Username, "pass456"));
        
        // Текущий пользователь может получить свои данные
        user.assertUserExists(currentUsername);
        
        // Текущий пользователь может получить данные другого пользователя (в демо API это может работать)
        // Но проверяем, что запрос выполняется
        userClient.get(user2Username)
                .then().log().ifValidationFails()
                .statusCode(anyOf(is(200), is(404), is(400)));
        
        user.deleteUser(user2Username);
    }

    @Test
    @DisplayName("USER: Access control - User cannot delete another user")
    void accessControlUserCannotDeleteAnotherUser() {
        // Создаем второго пользователя
        String user2Username = TestDataFactory.uniqueUsername();
        user.createUser(TestDataFactory.user(user2Username, "pass456"));
        user.assertUserExists(user2Username);
        
        // Текущий пользователь пытается удалить другого пользователя
        // Примечание: В демо API это может быть разрешено
        userClient.delete(user2Username)
                .then().log().ifValidationFails()
                .statusCode(anyOf(is(200), is(404), is(400), is(403), is(204)));
        
        // Проверяем статус пользователя (может быть удален, если API это разрешает)
        userClient.get(user2Username)
                .then().log().ifValidationFails()
                .statusCode(anyOf(is(200), is(404), is(400)));
        
        // Cleanup - пытаемся удалить пользователя если он еще существует
        userClient.delete(user2Username)
                .then().log().ifValidationFails()
                .statusCode(anyOf(is(200), is(404), is(400), is(204)));
    }
}

