package ru.ragim.petstore.config;

import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import ru.ragim.petstore.data.TestDataFactory;
import ru.ragim.petstore.steps.UserSteps;

public abstract class AbstractIntegrationTest {

    protected UserSteps userSteps;
    protected String currentUsername;
    protected String currentPassword;

    @BeforeAll
    static void beforeAll() {
        RestAssured.baseURI = "https://petstore.swagger.io/v2";
    }

    @BeforeEach
    void setUp() {
        userSteps = new UserSteps();
        currentUsername = TestDataFactory.uniqueUsername();
        currentPassword = "pass123";
        
        // a) создается user
        userSteps.createUser(TestDataFactory.user(currentUsername, currentPassword));
        
        // b) логинимся под ним
        userSteps.login(currentUsername, currentPassword);
    }

    @AfterEach
    void tearDown() {
        // d) разлогин
        if (userSteps != null) {
            userSteps.logout();
        }
    }
}

