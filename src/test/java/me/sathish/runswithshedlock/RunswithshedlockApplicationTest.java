package me.sathish.runswithshedlock;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import me.sathish.runswithshedlock.config.BaseIT;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

public class RunswithshedlockApplicationTest extends BaseIT {

    @Test
    void contextLoads() {}

    @Test
    void springSessionWorks() {
        final String sessionId = RestAssured.given()
                .accept(ContentType.JSON)
                .when()
                .get("/sessionCreate")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .sessionId();
        RestAssured.given()
                .sessionId(sessionId)
                .accept(ContentType.JSON)
                .when()
                .get("/sessionRead")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(Matchers.equalTo("test"));
        ;
    }
}
