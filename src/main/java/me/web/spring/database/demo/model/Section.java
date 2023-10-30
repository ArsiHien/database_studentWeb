package me.web.spring.database.demo.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table
@IdClass(SectionId.class)
public class Section {
    @Id
    private String course_id;
    @Id
    private String sec_id;
    @Id
    private String semester;
    @Id
    private int year;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @OneToMany(mappedBy = "section")
    private Set<Takes> takes = new HashSet<>();
}

