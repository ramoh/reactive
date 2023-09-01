package com.example.fullstack.auth;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.not;

@QuarkusTest
public class AuthResourceTest {

  @Test
  void loginValidCredentials() {
    given()
        .body("{\"name\":\"admin\",\"password\":\"quarkus\"}")
        .contentType(ContentType.JSON)
        .when().post("/api/v1/auth/login")
        .then()
        .statusCode(200)
        .body(not(emptyString()));
  }

  @Test
  void loginInValidCredentials() {
    given()
        .body("{\"name\":\"admin\",\"password\":\"not-quarkus\"}")
        .contentType(ContentType.JSON)
        .when().post("/api/v1/auth/login")
        .then()
        .statusCode(401);
  }
}
