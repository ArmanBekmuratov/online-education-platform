CREATE TABLE users
(
    id SERIAL PRIMARY KEY ,
    firstname VARCHAR (128) NOT NULL,
    lastname VARCHAR(128) NOT NULL,
    email VARCHAR(128) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(128) NOT NULL DEFAULT 'STUDENT',
    registration_date DATE NOT NULL
);

CREATE TABLE courses
(
    id SERIAL PRIMARY KEY,
    name VARCHAR(256) NOT NULL ,
    description VARCHAR(256) NOT NULL ,
    price NUMERIC(10,2) NOT NULL ,
    level VARCHAR(256) NOT NULL CHECK (level IN ('BEGINNER', 'INTERMEDIATE', 'ADVANCED')),
    category VARCHAR(256) NOT NULL,
    teacher_id INTEGER NOT NULL REFERENCES users (id),
    start_date DATE NOT NULL ,
    end_date DATE NOT NULL
);

CREATE TABLE lessons
(
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL,
    video VARCHAR(255) NOT NULL,
    slides VARCHAR(255) NOT NULL,
    test VARCHAR(255) NOT NULL,
    course_id INTEGER REFERENCES courses (id),
    order_number INTEGER NOT NULL
);

CREATE TABLE progress
(
    id SERIAL PRIMARY KEY ,
    user_id INTEGER REFERENCES users (id),
    course_id INTEGER REFERENCES courses (id),
    start_date DATE NOT NULL ,
    end_date DATE NOT NULL ,
    completed_lessons VARCHAR(256) NOT NULL ,
    grade INTEGER NOT NULL
);

CREATE TABLE certificates
(
    id SERIAL PRIMARY KEY,
    user_id INTEGER REFERENCES users (id),
    course_id INTEGER REFERENCES courses (id),
    issue_date DATE NOT NULL,
    grade INTEGER NOT NULL
);

DROP TABLE IF EXISTS certificates;
DROP TABLE IF EXISTS progress;
DROP TABLE IF EXISTS lessons;
DROP TABLE IF EXISTS courses;
DROP TABLE IF EXISTS users;


