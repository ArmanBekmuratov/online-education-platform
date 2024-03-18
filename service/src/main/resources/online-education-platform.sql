CREATE TABLE users
(
    id BIGINT PRIMARY KEY ,
    firstname VARCHAR (128) NOT NULL,
    lastname VARCHAR(128) NOT NULL,
    email VARCHAR(128) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(128) NOT NULL DEFAULT 'STUDENT',
    registration_date TIMESTAMP NOT NULL
);

CREATE TABLE profile
(
     id BIGINT PRIMARY KEY ,
     user_id INTEGER  UNIQUE NOT NULL REFERENCES users (id) ON DELETE CASCADE,
     phone_number VARCHAR(20),
     language VARCHAR(2),
     bio VARCHAR(255)
);

CREATE TABLE course
(
    id BIGINT PRIMARY KEY,
    name VARCHAR(256) NOT NULL ,
    description VARCHAR(256) NOT NULL ,
    price NUMERIC(10,2) NOT NULL ,
    level VARCHAR(256) NOT NULL CHECK (level IN ('BEGINNER', 'INTERMEDIATE', 'ADVANCED')),
    category VARCHAR(256) NOT NULL,
    teacher_id INTEGER NOT NULL REFERENCES users (id),
    start_date DATE NOT NULL ,
    end_date DATE NOT NULL,
    image VARCHAR(256) NOT NULL
);

CREATE TABLE lesson
(
    id BIGINT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL,
    video VARCHAR(255) NOT NULL,
    slides VARCHAR(255) NOT NULL,
    test VARCHAR(255) NOT NULL,
    course_id INTEGER REFERENCES course (id),
    order_number INTEGER NOT NULL
);

CREATE TABLE progress
(
    id BIGINT PRIMARY KEY ,
    user_id INTEGER REFERENCES users (id) ON DELETE CASCADE,
    course_id INTEGER REFERENCES course (id),
    completed_lessons VARCHAR(256) NOT NULL ,
    grade INTEGER NOT NULL CHECK (grade >= 0 AND grade <= 100)
);

CREATE TABLE lesson_completions (
  id BIGINT PRIMARY KEY,
  progress_id INTEGER REFERENCES progress(id) ON DELETE CASCADE,
  lesson_id INTEGER REFERENCES lesson(id) ON DELETE CASCADE
);


CREATE TABLE certificate
(
    id BIGINT PRIMARY KEY,
    user_id INTEGER REFERENCES users (id) ON DELETE CASCADE,
    course_id INTEGER REFERENCES course (id),
    issue_date DATE NOT NULL,
    grade INTEGER NOT NULL CHECK (grade >= 0 AND grade <= 100)
);



