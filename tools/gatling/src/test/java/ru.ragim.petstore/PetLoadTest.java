package ru.ragim.petstore.load;

import io.gatling.javaapi.core.Simulation;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

public class PetLoadTest extends Simulation {

    private static final String BASE_URL = "https://petstore.swagger.io/v2";

    private final io.gatling.javaapi.http.HttpProtocolBuilder httpProtocol =
            http.baseUrl(BASE_URL)
                    .acceptHeader("application/json")
                    .contentTypeHeader("application/json");

    private final io.gatling.javaapi.core.ScenarioBuilder scn =
            scenario("Pet findByStatus load test")
                    .exec(
                            http("GET findByStatus available")
                                    .get("/pet/findByStatus")
                                    .queryParam("status", "available")
                                    .check(status().in(200, 400, 404))
                    );

    {
        setUp(
                scn.injectOpen(
                        constantUsersPerSec(1).during(60) // 1 RPS в течение 60 секунд
                )
        ).protocols(httpProtocol);
    }
}

