package me.web.spring.database.demo.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@IdClass(TakesId.class)
public class Takes {
    @Id
    private int ID;

    @Id
    private String course_id;

    @Id
    private String sec_id;

    @Id
    private String semester;

    @Id
    private int year;

    private double component_grade;
    private double final_exam_grade;
    private String status;

    @Transient
    private double final_grade;

    @ManyToOne
    @MapsId
    @JoinColumn(name = "ID", referencedColumnName = "ID")
    private Student student;

    @ManyToOne
    @MapsId
    @JoinColumns({
            @JoinColumn(name = "course_id", referencedColumnName = "course_id", insertable = false, updatable = false),
            @JoinColumn(name = "sec_id", referencedColumnName = "sec_id", insertable = false, updatable = false),
            @JoinColumn(name = "semester", referencedColumnName = "semester", insertable = false, updatable = false),
            @JoinColumn(name = "year", referencedColumnName = "year", insertable = false, updatable = false)
    })
    private Section section;

    public Takes() {
    }

    public Takes(int ID, String course_id, String sec_id, String semester, int year, double component_grade, double final_exam_grade, String status) {
        this.ID = ID;
        this.course_id = course_id;
        this.sec_id = sec_id;
        this.semester = semester;
        this.year = year;
        this.component_grade = component_grade;
        this.final_exam_grade = final_exam_grade;
        this.status = status;
    }

    public void calculateFinalGrade(){
        final_grade = 0.6*final_exam_grade + 0.4*component_grade;
    }
}

