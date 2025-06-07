package me.sathish.runswithshedlock.config;

import io.restassured.RestAssured;
import io.restassured.config.SessionConfig;
import io.restassured.http.ContentType;
import jakarta.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import lombok.SneakyThrows;
import me.sathish.runswithshedlock.RunswithshedlockApplication;
import me.sathish.runswithshedlock.garmin_run.GarminRunRepository;
import me.sathish.runswithshedlock.run_event.RunEventRepository;
import me.sathish.runswithshedlock.runner.RunnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlMergeMode;
import org.springframework.util.StreamUtils;
import org.testcontainers.containers.PostgreSQLContainer;

/**
 * Abstract base class to be extended by every IT test. Starts the Spring Boot context with a
 * Datasource connected to the Testcontainers Docker instance. The instance is reused for all tests,
 * with all data wiped out before each test.
 */
@SpringBootTest(classes = RunswithshedlockApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("it")
@Sql("/data/clearAll.sql")
@SqlMergeMode(SqlMergeMode.MergeMode.MERGE)
public abstract class BaseIT {

    @ServiceConnection
    private static final PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:17.5");

    public static final String ADMIN = "admin";
    public static final String USER = "user";
    public static final String PASSWORD = "Bootify!";
    private static final HashMap<String, String> usersecuritySessions = new HashMap<>();

    static {
        postgreSQLContainer.withReuse(true).start();
    }

    @LocalServerPort
    public int serverPort;

    @Autowired
    public GarminRunRepository garminRunRepository;

    @Autowired
    public RunnerRepository runnerRepository;

    @Autowired
    public RunEventRepository runEventRepository;

    @PostConstruct
    public void initRestAssured() {
        RestAssured.port = serverPort;
        RestAssured.urlEncodingEnabled = false;
        RestAssured.config = RestAssured.config().sessionConfig(new SessionConfig().sessionIdName("SESSION"));
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @SneakyThrows
    public String readResource(final String resourceName) {
        return StreamUtils.copyToString(getClass().getResourceAsStream(resourceName), StandardCharsets.UTF_8);
    }

    public String usersecuritySession(final String username) {
        String usersecuritySession = usersecuritySessions.get(username);
        if (usersecuritySession == null) {
            // init session
            usersecuritySession = RestAssured.given()
                    .accept(ContentType.HTML)
                    .when()
                    .get("/login")
                    .sessionId();

            // perform login
            usersecuritySession = RestAssured.given()
                    .sessionId(usersecuritySession)
                    .csrf("/login")
                    .accept(ContentType.HTML)
                    .contentType(ContentType.URLENC)
                    .formParam("login", username)
                    .formParam("password", PASSWORD)
                    .when()
                    .post("/login")
                    .sessionId();
            usersecuritySessions.put(username, usersecuritySession);
        }
        return usersecuritySession;
    }
}
