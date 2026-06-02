package br.com.senai.projetointegrador.features.course;

import br.com.senai.projetointegrador.features.course.enrollStudent.EnrollException;
import br.com.senai.projetointegrador.features.students.Student;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "courses")
@NoArgsConstructor
@Getter
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private Shift shift;

    private int year;

    @Enumerated(EnumType.STRING)
    private Semester semester;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "course_students",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id")
    )
    private final Set<Student> students = new HashSet<>();

    public Course(String name, Shift shift, int year, Semester semester) {
        this.id = null;
        this.name = name;
        this.shift = shift;
        this.year = year;
        this.semester = semester;
    }

    public void enrollStudent(Student student) {
        if (this.students.contains(student)) {
            throw new EnrollException(student.getId(), this.getId());
        }
        this.students.add(student);
    }
}
