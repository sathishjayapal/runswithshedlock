CREATE SEQUENCE  IF NOT EXISTS primary_sequence START WITH 10000 INCREMENT BY 1;

CREATE TABLE garminrun_garmin_run (
    id BIGINT NOT NULL,
    activityid INTEGER,
    activity_date TEXT,
    activity_type VARCHAR(25),
    activity_name VARCHAR(100),
    activity_description VARCHAR(255),
    elapsed_time VARCHAR(255),
    distance VARCHAR(255),
    max_heart_rate VARCHAR(255),
    runner_id BIGINT NOT NULL,
    date_created TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    last_updated TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT garminrun_garmin_run_pkey PRIMARY KEY (id)
);

CREATE TABLE garminrun_runner (
    id BIGINT NOT NULL,
    username VARCHAR(255),
    email VARCHAR(255),
    date_created TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    last_updated TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT garminrun_runner_pkey PRIMARY KEY (id)
);

ALTER TABLE garminrun_garmin_run ADD CONSTRAINT fk_garmin_run_runner_id FOREIGN KEY (runner_id) REFERENCES garminrun_runner (id) ON UPDATE NO ACTION ON DELETE NO ACTION;
