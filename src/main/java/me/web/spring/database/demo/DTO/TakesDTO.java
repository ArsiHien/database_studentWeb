package me.web.spring.database.demo.DTO;

import lombok.Data;

import java.util.List;

@Data
public class TakesDTO {
    private int ID;
    private int student_id;
    private int section_id;
    private String status;
    private double final_grade;
    private double grade_in_four_scale;
    private String letter_grade;
    private List<GradeDTO> grades;

    public TakesDTO(int ID, int student_id, int section_id, String status, double final_grade, double grade_in_four_scale, String letter_grade, List<GradeDTO> grades) {
        this.ID = ID;
        this.student_id = student_id;
        this.section_id = section_id;
        this.status = status;
        this.final_grade = final_grade;
        this.grade_in_four_scale = grade_in_four_scale;
        this.letter_grade = letter_grade;
        this.grades = grades;
    }
}
