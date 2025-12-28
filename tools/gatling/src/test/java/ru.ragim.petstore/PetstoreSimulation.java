package ru.ragim.petstore.load;

import io.gatling.javaapi.core.Simulation;

import java.util.concurrent.ThreadLocalRandom;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

public class PetstoreSimulation extends Simulation {

    private static final String BASE_URL = "https://petstore.swagger.io/v2";

    private final io.gatling.javaapi.http.HttpProtocolBuilder httpProtocol =
            http.baseUrl(BASE_URL)
                    .acceptHeader("application/json")
                    .contentTypeHeader("application/json");

    // Простой сценарий нагрузки: findByStatus + inventory
    private final io.gatling.javaapi.core.ScenarioBuilder scn =
            scenario("Petstore basic load")
                    .exec(
                            http("GET findByStatus available")
                                    .get("/pet/findByStatus")
                                    .queryParam("status", "available")
                                    .check(status().in(200, 400, 404)) // демо API может флапать
                    )
                    .pause(1)
                    .exec(
                            http("GET store inventory")
                                    .get("/store/inventory")
                                    .check(status().in(200, 400, 404))
                    );

    {
        setUp(
                scn.injectOpen(
                        rampUsers(20).during(10),   // 0 -> 20 пользователей за 10 сек
                        constantUsersPerSec(10).during(20) // 10 rps 20 сек
                )
        ).protocols(httpProtocol);
    }
}
