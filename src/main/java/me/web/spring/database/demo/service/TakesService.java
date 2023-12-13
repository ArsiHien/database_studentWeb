package me.web.spring.database.demo.service;

import jakarta.transaction.Transactional;
import me.web.spring.database.demo.model.Grade;
import me.web.spring.database.demo.model.GradeType;
import me.web.spring.database.demo.model.Section;
import me.web.spring.database.demo.model.Takes;
import me.web.spring.database.demo.repository.TakesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class TakesService {
    private final TakesRepository takesRepository;
    private final GradeService gradeService;
    private final GradeTypeService gradeTypeService;

    @Autowired
    public TakesService(TakesRepository takesRepository, GradeService gradeService, GradeTypeService gradeTypeService) {
        this.takesRepository = takesRepository;
        this.gradeService = gradeService;
        this.gradeTypeService = gradeTypeService;
    }

    public List<Takes> findTakesBySection(Section section, String studentType) {
        List<Takes> takesList = takesRepository.findTakesBySection(section.getID());
        for (Takes takes : takesList) {
            takes.setFinal_grade(gradeService.calculateFinalGrade(takes));
            takes.getGrades().sort(Grade::compareTo);
        }
        switch (studentType) {
            case "excellent" -> takesList.removeIf(takes -> takes.getFinal_grade() < 9);
            case "good" -> takesList.removeIf(takes -> takes.getFinal_grade() < 7);
            case "poor" -> takesList.removeIf(takes -> takes.getFinal_grade() >= 4);
        }
        return takesList;
    }

    public List<Takes> findTakesBySectionWithSorting(Section section, String studentType, String sortField, String sortDirection) {
        List<Takes> takesList = findTakesBySection(section, studentType);

        Comparator<Takes> comparator = getTakesComparator(section.getID(),sortField, sortDirection);

        takesList.sort(comparator);

        return takesList;
    }

    private Comparator<Takes> getTakesComparator(int sectionId, String sortField, String sortDirection) {
        // Comparator logic for different fields
        List<GradeType> gradeTypeList = gradeTypeService.getGradeTypesInSection(sectionId);
        Comparator<Takes> comparator = null;

        for (GradeType gradeType : gradeTypeList) {
            if (gradeType.getName().equals(sortField)) {
                comparator = Comparator.comparing(takes -> takes.getGrades().stream()
                        .filter(grade -> grade.getGrade_type_id() == gradeType.getID())
                        .findFirst()
                        .map(Grade::getValue)
                        .orElse(Double.NaN));
                break;
            }
        }

        if (comparator == null) {
            comparator = switch (sortField) {
                case "final_grade" -> Comparator.comparing(Takes::getFinal_grade);
                case "status" -> Comparator.comparing(Takes::getStatus);
                default -> Comparator.comparing(takes -> takes.getStudent().getFirst_name());
            };
        }

        if ("desc".equals(sortDirection)) {
            comparator = comparator.reversed();
        }
        return comparator;
    }


    public Takes findStudentInSection(int studentId, int sectionId) {
        Takes takes =  takesRepository.findStudentInSection(studentId, sectionId);
        takes.getGrades().sort(Grade::compareTo);
        takes.setFinal_grade(gradeService.calculateFinalGrade(takes));
        return takes;
    }

    public Takes findTakes(Takes takes){
        return takesRepository.findTakes(takes.getStudent_id(), takes.getSection_id());
    }

    public Takes findTakes(int ID){
        return takesRepository.findTakes(ID);
    }

    @Transactional
    public void addAllTakesInSection(Section section) {
        for (Takes takes : section.getTakes()) {
            String status = getStatus(takesRepository.findStudentInCourse(takes.getStudent_id(), section.getCourse().getCourse_id()));
            if (status.isEmpty()) {
                return;
            }
            takes.setStatus(status);
            takesRepository.addTakes(takes.getStudent_id(),
                    takes.getSection_id(),
                    takes.getStatus());
            takes.setID(findTakes(takes).getID());
        }
    }

    @Transactional
    public void addOneTakes(Takes takes) {
        List<Takes> takesList = takesRepository.findStudentInCourse(takes.getStudent_id(), takes.getSection().getCourse().getCourse_id());
        String status = getStatus(takesList);
        if (status.isEmpty()) {
            return;
        }
        takes.setStatus(status);
        takesRepository.addTakes(takes.getStudent_id(),
                takes.getSection_id(),
                takes.getStatus());
        takes.setID(findTakes(takes).getID());
    }

    public String getStatus(Takes takes){
        List<Takes> takesList = takesRepository.findStudentInCourse(takes.getStudent_id(), takes.getSection().getCourse().getCourse_id());
        return getStatus(takesList);
    }

    private String getStatus(List<Takes> takesList) {
        if (takesList.isEmpty()) {
            return "Học lần đầu";
        }
        Takes lastTake = takesList.get(takesList.size() - 1);
        double finalGrade = gradeService.calculateFinalGrade(lastTake);
        if (finalGrade < 4) {
            return "Học lại";
        }
        if (finalGrade < 5.5) {
            return "Học cải thiện";
        }
        return "";
    }

    public Takes getStudentInfoInSection(String studentId, Section section) {
        Takes takes = takesRepository.findStudentInSection(Integer.parseInt(studentId), section.getID());
        if (takes != null) takes.setFinal_grade(gradeService.calculateFinalGrade(takes));
        return takes;
    }

    public List<Takes> getStudentInfoInCourse(String studentId, Section section) {
        List<Takes> takesList = takesRepository.findStudentInCourse(Integer.parseInt(studentId), section.getCourse_id());
        if (takesList != null)
            for (Takes takes : takesList) {
                takes.setFinal_grade(gradeService.calculateFinalGrade(takes));
            }
        return takesList;
    }

    List<Takes> getSectionsForStudent(int studentId){
        return takesRepository.getSectionsForStudent(studentId);
    }

    @Transactional
    public void deleteStudentInSection(int studentId, int sectionId){
        takesRepository.deleteStudentInSection(studentId, sectionId);
    }
}
