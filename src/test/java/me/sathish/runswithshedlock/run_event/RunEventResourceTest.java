package me.sathish.runswithshedlock.run_event;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import me.sathish.runswithshedlock.config.BaseIT;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;

public class RunEventResourceTest extends BaseIT {

    @Test
    @Sql("/data/runEventData.sql")
    void getAllRunEvents_success() {
        RestAssured.given()
                .accept(ContentType.JSON)
                .when()
                .get("/api/runEvents")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("page.totalElements", Matchers.equalTo(2))
                .body("_embedded.runEventDTOList.get(0).id", Matchers.equalTo(1200))
                .body("_links.self.href", Matchers.endsWith("/api/runEvents?page=0&size=20&sort=id,asc"));
    }

    @Test
    @Sql("/data/runEventData.sql")
    void getAllRunEvents_filtered() {
        RestAssured.given()
                .accept(ContentType.JSON)
                .when()
                .get("/api/runEvents?filter=1201")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("page.totalElements", Matchers.equalTo(1))
                .body("_embedded.runEventDTOList.get(0).id", Matchers.equalTo(1201));
    }

    @Test
    @Sql("/data/runEventData.sql")
    void getRunEvent_success() {
        RestAssured.given()
                .accept(ContentType.JSON)
                .when()
                .get("/api/runEvents/1200")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("runId", Matchers.equalTo("Sed faucibus turpis in eu mi bibendum neque."))
                .body("_links.self.href", Matchers.endsWith("/api/runEvents/1200"));
    }

    @Test
    void getRunEvent_notFound() {
        RestAssured.given()
                .accept(ContentType.JSON)
                .when()
                .get("/api/runEvents/1866")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("code", Matchers.equalTo("NOT_FOUND"));
    }

    @Test
    void createRunEvent_success() {
        RestAssured.given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(readResource("/requests/runEventDTORequest.json"))
                .when()
                .post("/api/runEvents")
                .then()
                .statusCode(HttpStatus.CREATED.value());
        assertEquals(1, runEventRepository.count());
    }

    @Test
    void createRunEvent_missingField() {
        RestAssured.given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(readResource("/requests/runEventDTORequest_missingField.json"))
                .when()
                .post("/api/runEvents")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("code", Matchers.equalTo("VALIDATION_FAILED"))
                .body("fieldErrors.get(0).property", Matchers.equalTo("runId"))
                .body("fieldErrors.get(0).code", Matchers.equalTo("REQUIRED_NOT_NULL"));
    }

    @Test
    @Sql("/data/runEventData.sql")
    void updateRunEvent_success() {
        RestAssured.given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(readResource("/requests/runEventDTORequest.json"))
                .when()
                .put("/api/runEvents/1200")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("_links.self.href", Matchers.endsWith("/api/runEvents/1200"));
        assertEquals(
                "Pellentesque nec nam aliquam sem. Risus viverra adipiscing at in tellus.",
                runEventRepository.findById(((long) 1200)).orElseThrow().getRunId());
        assertEquals(2, runEventRepository.count());
    }

    @Test
    @Sql("/data/runEventData.sql")
    void deleteRunEvent_success() {
        RestAssured.given()
                .accept(ContentType.JSON)
                .when()
                .delete("/api/runEvents/1200")
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
        assertEquals(1, runEventRepository.count());
    }
}
