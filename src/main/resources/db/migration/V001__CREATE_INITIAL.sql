CREATE SEQUENCE  IF NOT EXISTS primary_sequence START WITH 10000 INCREMENT BY 1;

CREATE TABLE garmin_run_shedlock (
    id BIGINT NOT NULL,
    activityid INTEGER,
    activity_date TEXT,
    activity_type VARCHAR(25),
    activity_name VARCHAR(100),
    activity_description VARCHAR(255),
    elapsed_time VARCHAR(255),
    distance VARCHAR(255),
    max_heart_rate VARCHAR(255),
    runner_id BIGINT,
    date_created TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    last_updated TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT garmin_run_shedlock_pkey PRIMARY KEY (id)
);

CREATE TABLE runner_shedlock (
    id BIGINT NOT NULL,
    username VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    hash VARCHAR(255) NOT NULL,
    date_created TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    last_updated TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT runner_shedlock_pkey PRIMARY KEY (id)
);

CREATE TABLE run_event_shedlock (
    id BIGINT NOT NULL,
    run_id TEXT NOT NULL,
    run_event_type TEXT NOT NULL,
    run_information TEXT NOT NULL,
    date_created TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    last_updated TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT run_event_shedlock_pkey PRIMARY KEY (id)
);

ALTER TABLE garmin_run_shedlock ADD CONSTRAINT fk_garmin_run_runner_id FOREIGN KEY (runner_id) REFERENCES runner_shedlock (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE runner_shedlock ADD CONSTRAINT unique_runner_username UNIQUE (username);

ALTER TABLE runner_shedlock ADD CONSTRAINT unique_runner_email UNIQUE (email);

ALTER TABLE run_event_shedlock ADD CONSTRAINT unique_run_event_run_id UNIQUE (run_id);
