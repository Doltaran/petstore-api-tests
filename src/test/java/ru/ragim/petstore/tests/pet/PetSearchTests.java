package ru.ragim.petstore.tests.pet;

import org.junit.jupiter.api.Test;
import ru.ragim.petstore.client.PetApi;
import ru.ragim.petstore.config.TestConfig;

import static org.hamcrest.Matchers.*;

public class PetSearchTests extends TestConfig {

    private final PetApi petApi = new PetApi();

    @Test
    void findByStatus_shouldReturnList() {
        petApi.findByStatus("available")
                .then()
                .log().ifValidationFails()
                .statusCode(200)
                .body("$", is(not(empty())));
    }
}
