package me.web.spring.database.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
                        @ColumnResult(name = "total_cumulative_credits", type = Integer.class),
                }
        )
)
@NamedNativeQuery(
        name = "Student.listStudentInClasses",
        query = "SELECT " +
                "s.ID, " +
                "s.last_name, " +
                "s.middle_name, " +
                "s.first_name, " +
                "s.class_name, " +
                "COALESCE(( " +
                "    SELECT SUM(GPA_4_Scale * credits) " +
                "    FROM ( " +
                "        SELECT " +
                "            credits, " +
                "            CASE " +
                "                WHEN SUM(grade.value * grade_type.ratio) >= 9 THEN 4 " +
                "                WHEN SUM(grade.value * grade_type.ratio) >= 8.5 THEN 3.7 " +
                "                WHEN SUM(grade.value * grade_type.ratio) >= 8 THEN 3.5 " +
                "                WHEN SUM(grade.value * grade_type.ratio) >= 7 THEN 3 " +
                "                WHEN SUM(grade.value * grade_type.ratio) >= 6.5 THEN 2.5 " +
                "                WHEN SUM(grade.value * grade_type.ratio) >= 5.5 THEN 2 " +
                "                WHEN SUM(grade.value * grade_type.ratio) >= 5 THEN 1.5 " +
                "                WHEN SUM(grade.value * grade_type.ratio) >= 4 THEN 1 " +
                "                ELSE 0 " +
                "            END as GPA_4_Scale " +
                "        FROM takes " +
                "        JOIN grade ON grade.takes_id = takes.ID " +
                "        JOIN ( " +
                "            SELECT s1.* " +
                "            FROM section s1 " +
                "            JOIN ( " +
                "                SELECT course_id, MAX(year*10 + semester) AS last_year_semester " +
                "                FROM section " +
                "                WHERE EXISTS ( " +
                "                    SELECT 1 " +
                "                    FROM takes " +
                "                    WHERE takes.student_id = s.ID " +
                "                    AND takes.section_id = section.ID " +
                "                ) " +
                "                GROUP BY course_id " +
                "            ) s2 ON s1.course_id = s2.course_id AND s1.year*10 + s1.semester = s2.last_year_semester " +
                "        ) AS last_section ON last_section.ID = takes.section_id " +
                "        JOIN grade_type ON grade.grade_type_id = grade_type.ID " +
                "        JOIN course ON last_section.course_id = course.course_id " +
                "        WHERE student_id = s.ID " +
                "        GROUP BY last_section.ID " +
                "    ) as totalGPA " +
                ") / ( " +
                "    SELECT SUM(DISTINCT_COURSES.credits) " +
                "    FROM ( " +
                "        SELECT DISTINCT " +
                "            c.course_id, " +
                "            c.credits " +
                "        FROM takes t " +
                "        JOIN section se ON t.section_id = se.ID " +
                "        JOIN course c ON se.course_id = c.course_id " +
                "        WHERE t.student_id = s.ID " +
                "    ) AS DISTINCT_COURSES " +
                "), 0) AS GPA, " +
                "( " +
                "    SELECT SUM(all_course.credits) " +
                "    FROM ( " +
                "        SELECT " +
                "            c.course_id, c.credits " +
                "        FROM takes t " +
                "        JOIN section se ON t.section_id = se.ID " +
                "        JOIN course c ON se.course_id = c.course_id " +
                "        WHERE t.student_id = s.ID " +
                "    ) AS all_course " +
                ") AS total_credit, " +
                "( " +
                "    SELECT SUM(DISTINCT_COURSES.credits) " +
                "    FROM ( " +
                "        SELECT DISTINCT " +
                "            c.course_id, " +
                "            c.credits " +
                "        FROM takes t " +
                "        JOIN section se ON t.section_id = se.ID " +
                "        JOIN course c ON se.course_id = c.course_id " +
                "        WHERE t.student_id = s.ID " +
                "    ) AS DISTINCT_COURSES " +
                ") AS total_cumulative_credits " +
                "FROM student s " +
                "JOIN takes t ON s.ID = t.student_id " +
                "JOIN grade g ON t.ID = g.takes_id " +
                "JOIN grade_type gt ON g.grade_type_id = gt.ID " +
                "JOIN section se ON t.section_id = se.ID " +
                "JOIN course c ON se.course_id = c.course_id " +
                "WHERE s.class_name = :class_name " +
                "GROUP BY s.ID;",
        resultSetMapping = "StudentMapping"
)
@NamedNativeQuery(
        name = "Student.getStudentInfo",
        query = "SELECT " +
                "s.ID, " +
                "s.last_name, " +
                "s.middle_name, " +
                "s.first_name, " +
                "s.class_name, " +
                "COALESCE(( " +
                "    SELECT SUM(GPA_4_Scale * credits) " +
                "    FROM ( " +
                "        SELECT " +
                "            credits, " +
                "            CASE " +
                "                WHEN SUM(grade.value * grade_type.ratio) >= 9 THEN 4 " +
                "                WHEN SUM(grade.value * grade_type.ratio) >= 8.5 THEN 3.7 " +
                "                WHEN SUM(grade.value * grade_type.ratio) >= 8 THEN 3.5 " +
                "                WHEN SUM(grade.value * grade_type.ratio) >= 7 THEN 3 " +
                "                WHEN SUM(grade.value * grade_type.ratio) >= 6.5 THEN 2.5 " +
                "                WHEN SUM(grade.value * grade_type.ratio) >= 5.5 THEN 2 " +
                "                WHEN SUM(grade.value * grade_type.ratio) >= 5 THEN 1.5 " +
                "                WHEN SUM(grade.value * grade_type.ratio) >= 4 THEN 1 " +
                "                ELSE 0 " +
                "            END as GPA_4_Scale " +
                "        FROM takes " +
                "        JOIN grade ON grade.takes_id = takes.ID " +
                "        JOIN ( " +
                "            SELECT s1.* " +
                "            FROM section s1 " +
                "            JOIN ( " +
                "                SELECT course_id, MAX(year*10 + semester) AS last_year_semester " +
                "                FROM section " +
                "                WHERE EXISTS ( " +
                "                    SELECT 1 " +
                "                    FROM takes " +
                "                    WHERE takes.student_id = s.ID " +
                "                    AND takes.section_id = section.ID " +
                "                ) " +
                "                GROUP BY course_id " +
                "            ) s2 ON s1.course_id = s2.course_id AND s1.year*10 + s1.semester = s2.last_year_semester " +
                "        ) AS last_section ON last_section.ID = takes.section_id " +
                "        JOIN grade_type ON grade.grade_type_id = grade_type.ID " +
                "        JOIN course ON last_section.course_id = course.course_id " +
                "        WHERE student_id = s.ID " +
                "        GROUP BY last_section.ID " +
                "    ) as totalGPA " +
                ") / ( " +
                "    SELECT SUM(DISTINCT_COURSES.credits) " +
                "    FROM ( " +
                "        SELECT DISTINCT " +
                "            c.course_id, " +
                "            c.credits " +
                "        FROM takes t " +
                "        JOIN section se ON t.section_id = se.ID " +
                "        JOIN course c ON se.course_id = c.course_id " +
                "        WHERE t.student_id = s.ID " +
                "    ) AS DISTINCT_COURSES " +
                "), 0) AS GPA, " +
                "( " +
                "    SELECT SUM(all_course.credits) " +
                "    FROM ( " +
                "        SELECT " +
                "            c.course_id, c.credits " +
                "        FROM takes t " +
                "        JOIN section se ON t.section_id = se.ID " +
                "        JOIN course c ON se.course_id = c.course_id " +
                "        WHERE t.student_id = s.ID " +
                "    ) AS all_course " +
                ") AS total_credit, " +
                "( " +
                "    SELECT SUM(DISTINCT_COURSES.credits) " +
                "    FROM ( " +
                "        SELECT DISTINCT " +
                "            c.course_id, " +
                "            c.credits " +
                "        FROM takes t " +
                "        JOIN section se ON t.section_id = se.ID " +
                "        JOIN course c ON se.course_id = c.course_id " +
                "        WHERE t.student_id = s.ID " +
                "    ) AS DISTINCT_COURSES " +
                ") AS total_cumulative_credits " +
                "FROM student s " +
                "JOIN takes t ON s.ID = t.student_id " +
                "JOIN grade g ON t.ID = g.takes_id " +
                "JOIN grade_type gt ON g.grade_type_id = gt.ID " +
                "JOIN section se ON t.section_id = se.ID " +
                "JOIN course c ON se.course_id = c.course_id " +
                "WHERE s.ID = :student_id " +
                "GROUP BY s.ID;",
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
    @Transient
    private int total_cumulative_credits = 0;

    @OneToMany(mappedBy = "student")
    private Set<Takes> takes = new HashSet<>();

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "class_name", insertable = false, updatable = false)
    private Classes classes;

    public Student() {
    }


    public Student(int ID, String last_name, String middle_name, String first_name) {
        this.ID = ID;
        this.last_name = last_name;
        this.middle_name = middle_name;
        this.first_name = first_name;
    }
}
