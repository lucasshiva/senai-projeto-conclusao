CREATE TABLE students
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    name       VARCHAR(255) NOT NULL,
    picture    VARCHAR(255) NOT NULL,
    birth_date DATE         NOT NULL
);