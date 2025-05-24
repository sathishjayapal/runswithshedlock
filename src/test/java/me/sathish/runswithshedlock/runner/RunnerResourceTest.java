package me.sathish.runswithshedlock.runner;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import me.sathish.runswithshedlock.config.BaseIT;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;


public class RunnerResourceTest extends BaseIT {

    @Test
    @Sql("/data/runnerData.sql")
    void getAllRunners_success() {
        RestAssured
                .given()
                    .accept(ContentType.JSON)
                .when()
                    .get("/api/runners")
                .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("_embedded.runnerDTOList.size()", Matchers.equalTo(2))
                    .body("_embedded.runnerDTOList.get(0).id", Matchers.equalTo(1100))
                    .body("_links.self.href", Matchers.endsWith("/api/runners"));
    }

    @Test
    @Sql("/data/runnerData.sql")
    void getRunner_success() {
        RestAssured
                .given()
                    .accept(ContentType.JSON)
                .when()
                    .get("/api/runners/1100")
                .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("username", Matchers.equalTo("No sea takimata."))
                    .body("_links.self.href", Matchers.endsWith("/api/runners/1100"));
    }

    @Test
    void getRunner_notFound() {
        RestAssured
                .given()
                    .accept(ContentType.JSON)
                .when()
                    .get("/api/runners/1766")
                .then()
                    .statusCode(HttpStatus.NOT_FOUND.value())
                    .body("code", Matchers.equalTo("NOT_FOUND"));
    }

    @Test
    void createRunner_success() {
        RestAssured
                .given()
                    .accept(ContentType.JSON)
                    .contentType(ContentType.JSON)
                    .body(readResource("/requests/runnerDTORequest.json"))
                .when()
                    .post("/api/runners")
                .then()
                    .statusCode(HttpStatus.CREATED.value());
        assertEquals(1, runnerRepository.count());
    }

    @Test
    @Sql("/data/runnerData.sql")
    void updateRunner_success() {
        RestAssured
                .given()
                    .accept(ContentType.JSON)
                    .contentType(ContentType.JSON)
                    .body(readResource("/requests/runnerDTORequest.json"))
                .when()
                    .put("/api/runners/1100")
                .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("_links.self.href", Matchers.endsWith("/api/runners/1100"));
        assertEquals("Consetetur sadipscing.", runnerRepository.findById(((long)1100)).orElseThrow().getUsername());
        assertEquals(2, runnerRepository.count());
    }

    @Test
    @Sql("/data/runnerData.sql")
    void deleteRunner_success() {
        RestAssured
                .given()
                    .accept(ContentType.JSON)
                .when()
                    .delete("/api/runners/1100")
                .then()
                    .statusCode(HttpStatus.NO_CONTENT.value());
        assertEquals(1, runnerRepository.count());
    }

}
