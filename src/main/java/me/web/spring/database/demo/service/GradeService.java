package me.web.spring.database.demo.service;

import jakarta.transaction.Transactional;
import me.web.spring.database.demo.model.Grade;
import me.web.spring.database.demo.model.Takes;
import me.web.spring.database.demo.repository.GradeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GradeService {
    private final GradeRepository gradeRepository;

    @Autowired
    public GradeService(GradeRepository gradeRepository) {
        this.gradeRepository = gradeRepository;
    }

    @Transactional
    public void addGrade(Grade grade) {
        System.out.println(grade.getValue());
        System.out.println(grade.getGrade_type_id());
        System.out.println(grade.getTakes_id());
        gradeRepository.addGrade(grade.getValue(), grade.getGrade_type_id(), grade.getTakes_id());
    }

    @Transactional
    public void addTakesGrade(Takes takes) {
        System.out.println("In addTakesGrade: " + takes.getID());
        System.out.println("%".repeat(20));
        System.out.println(takes.getGrades().size());
        for(Grade grade : takes.getGrades()){
            grade.setTakes_id(takes.getID());
            addGrade(grade);
        }
    }

    @Transactional
    public void updateStudentGrade(Takes takes) {
        for(Grade grade : takes.getGrades()){
            gradeRepository.updateStudentGrade(grade.getValue(), grade.getTakes_id(), grade.getGrade_type_id());
        }
    }

    Double calculateFinalGrade(Takes takes){
        return gradeRepository.calculateFinalGrade(takes.getID());
    }
}
