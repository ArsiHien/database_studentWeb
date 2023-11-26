package me.web.spring.database.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table
public class Grade implements Comparable<Grade>{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ID;
    private int grade_type_id;
    private int takes_id;
    private double value;

    public Grade(double value) {
        this.value = value;
    }

    @JsonIgnore
    @ManyToOne
    @JoinColumn(insertable=false, updatable=false)
    private GradeType gradeType;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "takes_id", referencedColumnName = "ID", insertable=false, updatable=false)
    private Takes takes;

    public Grade() {
    }

    public Grade(double value, GradeType gradeType, Takes takes) {
        this.value = value;
        this.gradeType = gradeType;
        this.takes = takes;
    }

    public Grade(double value, int grade_type_id, int takes_id) {
        this.value = value;
        this.grade_type_id = grade_type_id;
        this.takes_id = takes_id;
    }

    @Override
    public int compareTo(Grade other) {
        return Integer.compare(this.getGrade_type_id(), other.getGrade_type_id());
    }
}
