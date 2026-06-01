package br.com.senai.projetointegrador.features.incidents;

import br.com.senai.projetointegrador.features.course.Course;
import br.com.senai.projetointegrador.features.students.Student;
import br.com.senai.projetointegrador.features.users.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "incidents")
@Getter
@NoArgsConstructor
public class Incident {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private IncidentType type;

    public Incident(Course course, Student student, User user, IncidentType type, String description) {
        this.course = course;
        this.student = student;
        this.user = user;
        this.type = type;
        this.description = description;
        this.createdAt = LocalDateTime.now();
    }
}
