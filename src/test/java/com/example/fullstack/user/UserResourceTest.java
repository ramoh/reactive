package com.example.fullstack.user;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;

@QuarkusTest
public class UserResourceTest {

  @Test
  @TestSecurity(user = "admin", roles = "admin")
  void list() {
    given()
        .when().get("/api/v1/users")
        .then()
        .statusCode(200)
        .body("$.size()", greaterThanOrEqualTo(1),
            "[0].name", is("user"),
            "[0].password", nullValue());
  }

  @Test
  @TestSecurity(user = "admin", roles = "admin")
  void create() {
    given()
        .body("""
                {
                  "name":"test",
                  "password":"test",
                  "roles":["user"]
                }

            """).contentType(ContentType.JSON).when()
        .post("/api/v1/users").then().statusCode(201)
        .body(
            "name", is("test"),
            "password", nullValue(),
            "created", not(emptyString()));
  }

  @Test
  @TestSecurity(user = "user", roles = "user")
  void createUnauthorized() {
    given()
        .body("""
              {
                "name":"test-unauthorized",
                "password":"test",
                "roles":["user"]
              }
            """)
        .contentType(ContentType.JSON)
        .when().post("/api/v1/users")
        .then()
        .statusCode(403);
  }

  @Test
  @TestSecurity(user = "admin", roles = "admin")
  void createDuplicate() {
    given()
        .body("""
            {
              "name":"user",
              "password":"test",
              "roles":["user"]
            }
            """)
        .contentType(ContentType.JSON)
        .when().post("/api/v1/users")
        .then()
        .statusCode(409);
  }

  @Test
  @TestSecurity(user = "admin", roles = "admin")
  void update() {
    var user = given()
        .body("""
            {
              "name":"to-update",
              "password":"test",
              "roles": ["user"]
            }
              """).contentType(ContentType.JSON)
        .when().post("/api/v1/users")
        .as(User.class);

    user.name = "updated";

    given()
        .body(user)
        .contentType(ContentType.JSON)
        .when().put("/api/v1/users/" + user.id)
        .then()
        .statusCode(200)
        .body(
            "name", is("updated"),
            "version", is(user.version + 1));

  }

  @Test
  @TestSecurity(user = "admin", roles = "admin")
  void updateOptimisticLock() {
    given()
        .body("""
            {
              "name":"updated",
              "version":1337
            }
            """)
        .contentType(ContentType.JSON)
        .when().put("/api/v1/users/0")
        .then()
        .statusCode(409);

  }

  @Test
  @TestSecurity(user = "admin", roles = "admin")
  void delete() {

    var toDelete = given()
        .body("""
              {
                "name":"to-delete",
                "password":"test",
                "roles":["user"]
              }
            """)
        .contentType(ContentType.JSON)
        .when().post("/api/v1/users")
        .as(User.class);

    given()
        .when().delete("/api/v1/users/" + toDelete.id)
        .then()
        .statusCode(204);

  }

  @Test
  @TestSecurity(user = "admin", roles = "user")
  void getCurrentUser() {
    given()
        .when().get("/api/v1/users/self")
        .then()
        .statusCode(200)
        .body("name", is("admin"));
  }

  @Test
  @TestSecurity(user = "admin", roles = "user")
  void changedPassword() {
    given()
        .body("""
            {
              "currentPassword":"quarkus",
              "newPassword":"changed"
            }
            """)
        .contentType(ContentType.JSON)
        .when().put("/api/v1/users/self/password")
        .then()
        .statusCode(200);

  }

  @Test
  @TestSecurity(user = "admin", roles = "user")
  void changePasswordDoesntMatch() {
    given()
        .body("{\"currentPassword\": \"wrong\", \"newPassword\": \"changed\"}")
        .contentType(ContentType.JSON)
        .when().put("/api/v1/users/self/password")
        .then()
        .statusCode(409);
  }

}
