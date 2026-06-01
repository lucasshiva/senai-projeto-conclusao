package br.com.senai.projetointegrador.features.course;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    public Course(String name, Shift shift, int year, Semester semester) {
        this.id = null;
        this.name = name;
        this.shift = shift;
        this.year = year;
        this.semester = semester;
    }
}
