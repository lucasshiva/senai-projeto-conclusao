CREATE TABLE courses
(
    id       BIGINT AUTO_INCREMENT PRIMARY KEY,
    name     VARCHAR(255) NOT NULL,
    shift    VARCHAR(30)  NOT NULL,
    year     INTEGER      NOT NULL,
    semester VARCHAR(30)  NOT NULL
)