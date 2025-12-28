package ru.ragim.petstore.client;

import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public abstract class BaseApiClient {

    protected RequestSpecification req() {
        return given()
                .contentType("application/json")
                .accept("application/json");
    }
}
