package me.web.spring.database.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table
public class Course {
    @Id
    private String course_id;

    private String title;
    private int credits;

    @OneToMany(mappedBy = "course")
    private List<Section> sections = new ArrayList<>();
    @Override
    public String toString() {
        return "Course{" +
                "course_id='" + course_id + '\'' +
                ", title='" + title + '\'' +
                ", credits=" + credits +
                '}';
    }
}
