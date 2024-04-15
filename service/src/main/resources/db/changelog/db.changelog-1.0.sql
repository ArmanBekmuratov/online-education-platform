--liquibase formatted sql

--changeset abekmuratov:1
CREATE TABLE users
(
    id                BIGSERIAL PRIMARY KEY,
    firstname         VARCHAR(128) NOT NULL,
    lastname          VARCHAR(128) NOT NULL,
    email             VARCHAR(128) NOT NULL UNIQUE,
    password          VARCHAR(255) NOT NULL,
    role              VARCHAR(128) NOT NULL DEFAULT 'STUDENT',
    registration_date TIMESTAMP    NOT NULL
);
--rollback DROP TABLE users;

--changeset abekmuratov:2
CREATE TABLE profile
(
    id           BIGSERIAL PRIMARY KEY,
    user_id      BIGINT UNIQUE NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    phone_number VARCHAR(20),
    language     VARCHAR(2),
    bio          VARCHAR(255)
);
--rollback DROP TABLE profile;

--changeset abekmuratov:3
CREATE TABLE course
(
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(256)   NOT NULL,
    description VARCHAR(256)   NOT NULL,
    price       NUMERIC(10, 2) NOT NULL,
    level       VARCHAR(256)   NOT NULL CHECK (level IN ('BEGINNER', 'INTERMEDIATE', 'ADVANCED')),
    category    VARCHAR(256)   NOT NULL,
    teacher_id  BIGINT        NOT NULL REFERENCES users (id),
    start_date  DATE           NOT NULL,
    end_date    DATE           NOT NULL,
    image       VARCHAR(256)   NOT NULL
);
--rollback DROP TABLE course;

--changeset abekmuratov:4
CREATE TABLE lesson
(
    id           BIGSERIAL PRIMARY KEY,
    name         VARCHAR(255) NOT NULL,
    description  VARCHAR(255) NOT NULL,
    video        VARCHAR(255) NOT NULL,
    slides       VARCHAR(255) NOT NULL,
    test         VARCHAR(255) NOT NULL,
    course_id    BIGINT REFERENCES course (id),
    order_number INTEGER      NOT NULL
);
--rollback DROP TABLE lesson;

--changeset abekmuratov:5
CREATE TABLE progress
(
    id                BIGSERIAL PRIMARY KEY,
    user_id           BIGINT REFERENCES users (id) ON DELETE CASCADE,
    course_id         BIGINT REFERENCES course (id),
    completed_lessons VARCHAR(256) NOT NULL,
    grade             INTEGER      NOT NULL CHECK (grade >= 0 AND grade <= 100)
);
--rollback DROP TABLE progress;

--changeset abekmuratov:6
CREATE TABLE lesson_completions
(
    id          BIGSERIAL PRIMARY KEY,
    progress_id BIGINT REFERENCES progress (id) ON DELETE CASCADE,
    lesson_id   BIGINT REFERENCES lesson (id) ON DELETE CASCADE
);
--rollback DROP TABLE lesson_completions;

--changeset abekmuratov:7
CREATE TABLE certificate
(
    id         BIGSERIAL PRIMARY KEY,
    user_id    BIGINT REFERENCES users (id) ON DELETE CASCADE,
    course_id  BIGINT REFERENCES course (id),
    issue_date DATE    NOT NULL,
    grade      INTEGER NOT NULL CHECK (grade >= 0 AND grade <= 100)
);
--rollback DROP TABLE certificate;
