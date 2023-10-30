package me.web.spring.database.demo.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table
@SqlResultSetMapping(
        name = "StudentMapping",
        classes = @ConstructorResult(
                targetClass = Student.class,
                columns = {
                        @ColumnResult(name = "ID", type = Integer.class),
                        @ColumnResult(name = "last_name", type = String.class),
                        @ColumnResult(name = "middle_name", type = String.class),
                        @ColumnResult(name = "first_name", type = String.class),
                        @ColumnResult(name = "class_name", type = String.class),
                        @ColumnResult(name = "GPA", type = Double.class),
                        @ColumnResult(name = "total_credit", type = Integer.class),
                }
        )
)
@NamedNativeQuery(
        name = "Student.listStudentInClasses",
        query = "SELECT s.ID, s.last_name, s.middle_name, s.first_name, s.class_name, " +
                "SUM((t.component_grade*0.4 + t.final_exam_grade*0.6)*c.credits)/SUM(c.credits) AS GPA, " +
                "SUM(c.credits) AS total_credit " +
                "FROM student s " +
                "JOIN takes t USING (ID) " +
                "JOIN course c USING (course_id) " +
                "WHERE s.class_name = :class_name " +
                "GROUP BY s.ID",
        resultSetMapping = "StudentMapping"
)
public class Student {
    @Id
    private int ID;
    private String last_name;
    private String middle_name;
    private String first_name;
    private String class_name;

    @Transient
    private double GPA = 0d;
    @Transient
    private int total_credit = 0;

    @OneToMany(mappedBy = "student")
    private Set<Takes> takes = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "class_name", referencedColumnName = "name", insertable = false, updatable = false)
    private Classes classes;

    @OneToMany(mappedBy = "student")
    private Set<TrainingPoint> trainingPoints = new HashSet<>();

    public Student() {
    }

    public Student(int ID, String last_name, String middle_name, String first_name, String class_name, Double GPA, int total_credit) {
        this.ID = ID;
        this.last_name = last_name;
        this.middle_name = middle_name;
        this.first_name = first_name;
        this.class_name = class_name;
        this.GPA = GPA;
        this.total_credit = total_credit;
    }

    public Student(int ID, String last_name, String middle_name, String first_name, String class_name) {
        this.ID = ID;
        this.last_name = last_name;
        this.middle_name = middle_name;
        this.first_name = first_name;
        this.class_name = class_name;
    }

    public Student(int ID, String last_name, String middle_name, String first_name) {
        this.ID = ID;
        this.last_name = last_name;
        this.middle_name = middle_name;
        this.first_name = first_name;
    }
}
