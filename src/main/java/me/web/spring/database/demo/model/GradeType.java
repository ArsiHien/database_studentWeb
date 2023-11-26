package me.web.spring.database.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "grade_type")
public class GradeType implements Comparable<GradeType>{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ID;
    private int section_id;
    private String name;

    private double ratio;

    public GradeType() {
    }

    public GradeType(String name, double ratio) {
        this.name = name;
        this.ratio = ratio;
    }

    public GradeType(String name, double ratio, Section section) {
        this.name = name;
        this.ratio = ratio;
        this.section = section;
    }

    @Override
    public String toString() {
        return "ID: " + ID + " name: " + name + " ratio: " + ratio + "\n";
    }

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "section_id", insertable=false, updatable=false)
    private Section section;

    @OneToMany(mappedBy = "gradeType")
    private List<Grade> grades = new ArrayList<>();

    @Override
    public int compareTo(GradeType other) {
        return Integer.compare(this.ID, other.ID);
    }
}
