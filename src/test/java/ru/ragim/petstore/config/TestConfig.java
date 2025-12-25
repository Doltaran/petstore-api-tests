package ru.ragim.petstore.config;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;

public class TestConfig {
    @BeforeAll
    static void setup() {
        RestAssured.baseURI = "https://petstore.swagger.io/v2";
    }
}
