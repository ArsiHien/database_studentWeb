package me.web.spring.database.demo.DTO;

import lombok.Data;

@Data
public class GradeDTO {
    private int ID;
    private int grade_type_id;
    private int takes_id;
    private double value;
    private String gradeTypeName;

    public GradeDTO(int ID, int grade_type_id, int takes_id, double value, String gradeTypeName) {
        this.ID = ID;
        this.grade_type_id = grade_type_id;
        this.takes_id = takes_id;
        this.value = value;
        this.gradeTypeName = gradeTypeName;
    }
}
