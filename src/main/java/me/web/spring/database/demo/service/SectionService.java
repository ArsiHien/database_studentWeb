package me.web.spring.database.demo.service;

import jakarta.transaction.Transactional;
import me.web.spring.database.demo.model.*;
import me.web.spring.database.demo.repository.SectionRepository;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class SectionService {
    private final SectionRepository sectionRepository;
    private final TakesService takesService;
    private final GradeService gradeService;
    private final GradeTypeService gradeTypeService;
    private final StudentService studentService;

    @Autowired
    public SectionService(SectionRepository sectionRepository, TakesService takesService, GradeService gradeService, GradeTypeService gradeTypeService, StudentService studentService) {
        this.sectionRepository = sectionRepository;
        this.takesService = takesService;
        this.gradeService = gradeService;
        this.gradeTypeService = gradeTypeService;
        this.studentService = studentService;
    }

    public List<Section> listSectionInCourse(String courseId) {
        return sectionRepository.listSectionInCourse(courseId);
    }

    public Section findSection(int sectionId) {
        Section section = sectionRepository.findSection(sectionId);
        section.getGradeTypes().sort(GradeType::compareTo);
        return section;
    }

    public Section findSection(String courseId, int secId, int semester, int year) {
        Section section =  sectionRepository.findSection(courseId, secId, semester, year);
        section.getGradeTypes().sort(GradeType::compareTo);
        return section;
    }

    public Section findSection(Section section){
        return findSection(section.getCourse().getCourse_id(), section.getSec_id(), section.getSemester(), section.getYear());
    }

    @Transactional
    public void addSection(Section section) {
        sectionRepository.addSection(section.getCourse().getCourse_id(), section.getSec_id(), section.getSemester(), section.getYear());
    }

    @Transactional
    public void processSectionFile(Section section, InputStream inputStream) throws IOException, InvalidFormatException {
        addSection(section);
        section.setID(findSection(section).getID());
        for (GradeType gradeType : section.getGradeTypes()) {
            gradeType.setSection_id(section.getID());
            gradeTypeService.addGradeType(gradeType);
        }
        int numberOfGradeTypes = section.getGradeTypes().size();
        Workbook workbook = WorkbookFactory.create(inputStream);
        Sheet sheet = workbook.getSheetAt(0);
        boolean headerRowFound = false;
        for (Row row : sheet) {
            if (!headerRowFound && row.getCell(0).getStringCellValue().equalsIgnoreCase("STT")) {
                headerRowFound = true;
                continue;
            }
            if (headerRowFound) {
                int studentId = (int) row.getCell(1).getNumericCellValue();
                Student student = studentService.getStudentById(studentId);
                if (student != null) {
                    Takes takes = new Takes(studentId, section.getID(), student, section);
                    takesService.addOneTakes(takes);
                    List<Grade> gradeList = new ArrayList<>();
                    for (int i = 0; i < numberOfGradeTypes; i++) {
                        double gradeValue = (row.getCell(i + 3).getNumericCellValue());
                        Grade grade = new Grade(gradeValue, section.getGradeTypes().get(i).getID(), takes.getID());
                        gradeList.add(grade);
                    }
                    takes.setGrades(gradeList);
                    gradeService.addTakesGrade(takes);
                }

            }
        }
    }
}
