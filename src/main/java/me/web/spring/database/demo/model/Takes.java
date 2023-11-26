package me.web.spring.database.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table
public class Takes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ID;

    private int student_id;

    private int section_id;

    private String status;

    @Transient
    private double final_grade;
    @Transient
    private double grade_in_four_scale;
    @Transient
    private String letter_grade;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "student_id", referencedColumnName = "ID", insertable=false, updatable=false)
    private Student student;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "section_id", referencedColumnName = "ID", insertable=false, updatable=false)
    private Section section;

    @OneToMany(mappedBy = "takes", cascade = CascadeType.ALL)
    private List<Grade> grades = new ArrayList<>();

    public Takes() {
    }

    public Takes(int ID) {
        this.ID = ID;
    }

    public Takes(int ID, int student_id, int section_id, String status) {
        this.ID = ID;
        this.student_id = student_id;
        this.section_id = section_id;
        this.status = status;
    }

    public Takes(int student_id, int section_id, List<Grade> grades) {
        this.student_id = student_id;
        this.section_id = section_id;
        this.grades = grades;
    }

    public Takes(int student_id, int section_id, Student student, Section section) {
        this.student_id = student_id;
        this.section_id = section_id;
        this.student = student;
        this.section = section;
    }

    public Takes(int student_id, int section_id) {
        this.student_id = student_id;
        this.section_id = section_id;
    }

    public Takes(int studentId, Section section) {
        this.student_id = studentId;
        this.section = section;
    }

    public String toString() {
        return "Takes{" +
                "ID=" + ID +
                ", student_id=" + student_id +
                ", section_id=" + section_id +
                '}';
    }

    public void setFinal_grade(double final_grade) {
        this.final_grade = final_grade;
        convertGrade();
    }

    private void convertGrade() {
        if (final_grade < 4) {
            grade_in_four_scale = 0;
            letter_grade = "F";
        } else if (final_grade < 5) {
            grade_in_four_scale = 1.0;
            letter_grade = "D";
        } else if (final_grade < 5.5) {
            grade_in_four_scale = 1.5;
            letter_grade = "D+";
        } else if (final_grade < 6.5) {
            grade_in_four_scale = 2.0;
            letter_grade = "C";
        } else if (final_grade < 7) {
            grade_in_four_scale = 2.5;
            letter_grade = "C+";
        } else if (final_grade < 8) {
            grade_in_four_scale = 3.0;
            letter_grade = "B";
        } else if (final_grade < 8.5) {
            grade_in_four_scale = 3.5;
            letter_grade = "B+";
        } else if (final_grade < 9) {
            grade_in_four_scale = 3.7;
            letter_grade = "A";
        } else {
            grade_in_four_scale = 4.0;
            letter_grade = "A+";
        }
    }

}

