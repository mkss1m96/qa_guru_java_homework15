package tests;

import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;

import java.io.File;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchema;
import static org.hamcrest.Matchers.*;
import static io.restassured.RestAssured.given;

public class WdHubStatusTests extends TestBase {
    @Test
    public void statusTest() {
        given()
                .log().all()
                .auth().basic("user1", "1234")
                .when()
                .get("https://selenoid.autotests.cloud/wd/hub/status")
                .then()
                .log().all()
                .statusCode(200)
                .body("value.message", containsString("Selenoid 1.11.3 built at"))
                .body("value.ready", is(true));
    }

    @Test
    public void statusTestWithJsonSchema() {
        given()
                .log().all()
                .auth().basic("user1", "1234")
                .when()
                .get("https://selenoid.autotests.cloud/wd/hub/status")
                .then()
                .log().all()
                .statusCode(200)
                .body(matchesJsonSchema(new File("src/test/resources/schemas/status-schema.json")));
    }

    @Test
    public void statusTestWithExpectedMessage() {
        given()
                .log().all()
                .auth().basic("user1", "1234")
                .when()
                .get("https://selenoid.autotests.cloud/wd/hub/status")
                .then()
                .log().all()
                .statusCode(200)
                .body("value.message", startsWith("Selenoid"))
                .body("value.message", endsWith("PM"))
                .body("value.ready", is(true));
    }

    @Test
    public void unauthorizedStatusNegativeTest() {
        given()
                .log().all()
                .when()
                .get("/wd/hub/status")
                .then()
                .log().all()
                .statusCode(401)
                .body("code", notNullValue())
                .body("message", notNullValue());
    }

    @Test
    public void wrongCredentialsNegativeTest() {
        given()
                .log().all()
                .auth().basic("wronguser", "wrongpassword")
                .when()
                .get("/wd/hub/status")
                .then()
                .log().all()
                .statusCode(401);
    }
}
