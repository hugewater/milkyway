package com.app6768688;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.containsString;

@QuarkusTest
public class BigWaterResourceTest {

    @Test
    public void testApiInfo() {
        given()
          .when().get("/bw-api")
          .then()
             .statusCode(200)
             .body("success", is(true))
             .body("message", containsString("BigWater API is running"));
    }

    @Test
    public void testHealth() {
        given()
          .when().get("/bw-api/health")
          .then()
             .statusCode(200)
             .body("status", is("UP"));
    }

    @Test
    public void testVersion() {
        given()
          .when().get("/bw-api/version")
          .then()
             .statusCode(200)
             .body("version", is("1.0.0"))
             .body("framework", is("Quarkus"));
    }
}
