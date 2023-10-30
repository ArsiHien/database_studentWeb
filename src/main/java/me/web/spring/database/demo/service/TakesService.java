package me.web.spring.database.demo.service;

import jakarta.transaction.Transactional;
import me.web.spring.database.demo.model.Section;
import me.web.spring.database.demo.model.Takes;
import me.web.spring.database.demo.repository.TakesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TakesService {
    private final TakesRepository takesRepository;

    @Autowired
    public TakesService(TakesRepository takesRepository) {
        this.takesRepository = takesRepository;
    }

    public List<Takes> findTakesBySection(Section section) {
        List<Takes> takesList = takesRepository.findTakesBySection(section.getCourse_id(), section.getSec_id(), section.getSemester(), section.getYear());
        for (Takes takes : takesList) {
            takes.calculateFinalGrade();
        }
        return takesList;
    }

    public Takes findTakes(int ID,
                           String course_id,
                           String sec_id,
                           String semester,
                           int year) {
        return takesRepository.findTakes(ID, course_id, sec_id, semester, year);
    }

    @Transactional
    public void addTakes(Section section) {
        for (Takes takes : section.getTakes()) {
            takesRepository.addTakes(takes.getID(),
                    takes.getCourse_id(),
                    takes.getSec_id(),
                    takes.getSemester(),
                    takes.getYear(),
                    takes.getComponent_grade(),
                    takes.getFinal_exam_grade(),
                    takes.getStatus());
        }
    }

    @Transactional
    public void addOneTakes(String id, Section section) {
        for (Takes takes : section.getTakes()) {
            takesRepository.addTakes(takes.getID(),
                    takes.getCourse_id(),
                    takes.getSec_id(),
                    takes.getSemester(),
                    takes.getYear(),
                    takes.getComponent_grade(),
                    takes.getFinal_exam_grade(),
                    takes.getStatus());
        }
    }

    @Transactional
    public void updateStudentGrade(Takes takes) {
//        System.out.println(takes.getID());
//        System.out.println(takes.getCourse_id());
//        System.out.println(takes.getSec_id());
//        System.out.println(takes.getSemester());
//        System.out.println(takes.getYear());
//        System.out.println(takes.getComponent_grade());
//        System.out.println(takes.getFinal_exam_grade());
        takesRepository.updateStudentGrade(takes.getID(), takes.getCourse_id(), takes.getSec_id(), takes.getSemester(), takes.getYear(), takes.getComponent_grade(), takes
                .getFinal_exam_grade());
    }
}
