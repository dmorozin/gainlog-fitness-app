CREATE TABLE workouts
(
    id          BIGSERIAL PRIMARY KEY,
    user_id     BIGSERIAL    NOT NULL,
    name        VARCHAR(100) NOT NULL,
    description TEXT,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_user_workout_name UNIQUE (user_id, name)
);
CREATE INDEX ix_workouts_user_id ON workouts (user_id);

CREATE TABLE exercises
(
    id              BIGSERIAL PRIMARY KEY,
    exercise_api_id VARCHAR(50)  NOT NULL,
    name            VARCHAR(100) NOT NULL,
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE workout_exercises
(
    workout_id  BIGSERIAL REFERENCES workouts (id) ON UPDATE CASCADE ON DELETE CASCADE,
    exercise_id BIGSERIAL REFERENCES exercises (id) ON UPDATE CASCADE,
    sequence    INT NOT NULL,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT workout_exercises_pkey PRIMARY KEY (workout_id, exercise_id),
    CONSTRAINT uq_workout_exercises_workout_id_sequence UNIQUE (workout_id, sequence)
);
CREATE INDEX ix_workout_exercises_exercise_id ON workout_exercises (exercise_id);

CREATE TABLE workout_logs
(
    id              BIGSERIAL PRIMARY KEY,
    workout_id      BIGSERIAL REFERENCES workouts (id) ON UPDATE CASCADE ON DELETE CASCADE,
    start_date_time TIMESTAMP NOT NULL,
    end_date_time   TIMESTAMP,
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX ix_workout_logs_user_id ON workout_logs (workout_id);

CREATE TABLE workout_log_sets
(
    id             BIGSERIAL PRIMARY KEY,
    workout_log_id BIGSERIAL REFERENCES workout_logs (id) ON UPDATE CASCADE ON DELETE CASCADE,
    exercise_id    BIGSERIAL REFERENCES exercises (id) ON UPDATE CASCADE,
    set_number     INT NOT NULL,
    repetitions    INT,
    weight         NUMERIC,
    time           BIGINT,
    created_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX ix_workout_log_sets_workout_log_id ON workout_log_sets (workout_log_id);
CREATE INDEX ix_workout_log_sets_log_exercise ON workout_log_sets (workout_log_id, exercise_id);
