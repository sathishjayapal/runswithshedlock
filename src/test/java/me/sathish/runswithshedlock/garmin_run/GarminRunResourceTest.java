package me.sathish.runswithshedlock.garmin_run;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import me.sathish.runswithshedlock.config.BaseIT;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;


public class GarminRunResourceTest extends BaseIT {

    @Test
    @Sql("/data/garminRunData.sql")
    void getAllGarminRuns_success() {
        RestAssured
                .given()
                    .accept(ContentType.JSON)
                .when()
                    .get("/api/garminRuns")
                .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("page.totalElements", Matchers.equalTo(2))
                    .body("_embedded.garminRunDTOList.get(0).id", Matchers.equalTo(1000))
                    .body("_links.self.href", Matchers.endsWith("/api/garminRuns?page=0&size=20&sort=id,asc"));
    }

    @Test
    @Sql("/data/garminRunData.sql")
    void getAllGarminRuns_filtered() {
        RestAssured
                .given()
                    .accept(ContentType.JSON)
                .when()
                    .get("/api/garminRuns?filter=1001")
                .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("page.totalElements", Matchers.equalTo(1))
                    .body("_embedded.garminRunDTOList.get(0).id", Matchers.equalTo(1001));
    }

    @Test
    @Sql("/data/garminRunData.sql")
    void getGarminRun_success() {
        RestAssured
                .given()
                    .accept(ContentType.JSON)
                .when()
                    .get("/api/garminRuns/1000")
                .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("activityID", Matchers.equalTo(26))
                    .body("_links.self.href", Matchers.endsWith("/api/garminRuns/1000"));
    }

    @Test
    void getGarminRun_notFound() {
        RestAssured
                .given()
                    .accept(ContentType.JSON)
                .when()
                    .get("/api/garminRuns/1666")
                .then()
                    .statusCode(HttpStatus.NOT_FOUND.value())
                    .body("code", Matchers.equalTo("NOT_FOUND"));
    }

    @Test
    void createGarminRun_success() {
        RestAssured
                .given()
                    .accept(ContentType.JSON)
                    .contentType(ContentType.JSON)
                    .body(readResource("/requests/garminRunDTORequest.json"))
                .when()
                    .post("/api/garminRuns")
                .then()
                    .statusCode(HttpStatus.CREATED.value());
        assertEquals(1, garminRunRepository.count());
    }

    @Test
    @Sql("/data/garminRunData.sql")
    void updateGarminRun_success() {
        RestAssured
                .given()
                    .accept(ContentType.JSON)
                    .contentType(ContentType.JSON)
                    .body(readResource("/requests/garminRunDTORequest.json"))
                .when()
                    .put("/api/garminRuns/1000")
                .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("_links.self.href", Matchers.endsWith("/api/garminRuns/1000"));
        assertEquals(41, garminRunRepository.findById(((long)1000)).orElseThrow().getActivityID());
        assertEquals(2, garminRunRepository.count());
    }

    @Test
    @Sql("/data/garminRunData.sql")
    void deleteGarminRun_success() {
        RestAssured
                .given()
                    .accept(ContentType.JSON)
                .when()
                    .delete("/api/garminRuns/1000")
                .then()
                    .statusCode(HttpStatus.NO_CONTENT.value());
        assertEquals(1, garminRunRepository.count());
    }

}
