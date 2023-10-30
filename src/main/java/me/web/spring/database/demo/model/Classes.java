package me.web.spring.database.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table
public class Classes {
    @Id
    private String name;
    private String advisor;

    @OneToMany(mappedBy = "classes")
    private Set<Student> students = new HashSet<>();

}

