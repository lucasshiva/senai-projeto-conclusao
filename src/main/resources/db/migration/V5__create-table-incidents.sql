CREATE TABLE incidents (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    description VARCHAR(255),
    created_at DATETIME NOT NULL,
    course_id BIGINT NOT NULL,
    student_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    type VARCHAR(50) NOT NULL,

    CONSTRAINT fk_incidents_course
        FOREIGN KEY (course_id)
        REFERENCES courses(id),

    CONSTRAINT fk_incidents_student 
        FOREIGN KEY (student_id)
        REFERENCES students(id)

    CONSTRAINT fk_incidents_user
            FOREIGN KEY (user_id)
            REFERENCES users(id)
);