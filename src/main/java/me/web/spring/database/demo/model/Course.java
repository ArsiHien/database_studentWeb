package me.web.spring.database.demo.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table
public class Course {
    @Id
    private String course_id;

    private String title;
    private int credits;

    @OneToMany(mappedBy = "course")
    private Set<Section> sections = new HashSet<>();
}
