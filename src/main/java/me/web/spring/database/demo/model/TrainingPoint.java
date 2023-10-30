package me.web.spring.database.demo.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table
@IdClass(TrainingPointId.class)
public class TrainingPoint {
    @Id
    private int student_id;

    @Id
    private String semester;

    @Id
    private int year;

    private int point;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

}

