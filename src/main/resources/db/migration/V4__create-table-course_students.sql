CREATE TABLE course_students
(
    course_id  BIGINT NOT NULL,
    student_id BIGINT NOT NULL,

    PRIMARY KEY (course_id, student_id),
    CONSTRAINT fk_course_students_course FOREIGN KEY (course_id) REFERENCES courses (id),
    CONSTRAINT fk_course_students_students FOREIGN KEY (student_id) REFERENCES students (id)
)