package me.web.spring.database.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table
public class Section {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ID;

    private String course_id;

    private int sec_id;

    private int semester;

    private int year;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "course_id", insertable=false, updatable=false)
    private Course course;

    @OneToMany(mappedBy = "section", cascade = CascadeType.ALL)
    private List<Takes> takes = new ArrayList<>();

    @OneToMany(mappedBy = "section", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GradeType> gradeTypes = new ArrayList<>();
}

