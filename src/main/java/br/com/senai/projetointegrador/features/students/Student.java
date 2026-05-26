package br.com.senai.projetointegrador.features.students;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "students")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String picture;
    private LocalDate birthDate;

    public Student(String name, String picturePath, LocalDate birthDate) {
        this.name = name;
        this.picture = picturePath;
        this.birthDate = birthDate;
    }
}
