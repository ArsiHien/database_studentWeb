package me.web.spring.database.demo.service;

import jakarta.transaction.Transactional;
import me.web.spring.database.demo.model.Section;
import me.web.spring.database.demo.model.SectionId;
import me.web.spring.database.demo.model.Takes;
import me.web.spring.database.demo.repository.SectionRepository;
import me.web.spring.database.demo.repository.TakesRepository;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
public class SectionService {
    private final SectionRepository sectionRepository;
    private final TakesService takesService;

    @Autowired
    public SectionService(SectionRepository sectionRepository, TakesRepository takesRepository, TakesService takesService) {
        this.sectionRepository = sectionRepository;
        this.takesService = takesService;
    }

    public List<Section> listSectionInCourse(String courseId) {
        return sectionRepository.listSectionInCourse(courseId);
    }

    public Section findSection(String courseId, String secId, String semester, int year) throws ChangeSetPersister.NotFoundException {
        return sectionRepository.findById(new SectionId(courseId, secId, semester, year)).orElseThrow(() -> new ChangeSetPersister.NotFoundException());
    }

    @Transactional
    public void addSection(Section section){
        sectionRepository.addSection(section.getCourse_id(), section.getSec_id(), section.getSemester(), section.getYear());
    }

    @Transactional
    public void processSectionFile(Section section, InputStream inputStream) throws IOException, InvalidFormatException {
        Workbook workbook = WorkbookFactory.create(inputStream);
        Sheet sheet = workbook.getSheetAt(0);
        boolean headerRowFound = false;
        for (Row row : sheet) {
            if (!headerRowFound && row.getCell(0).getStringCellValue().equalsIgnoreCase("STT")) {
                headerRowFound = true;
                continue;
            }
            if (headerRowFound) {
                int id = (int) row.getCell(1).getNumericCellValue();
//                String name = row.getCell(2).getStringCellValue();
                double component_grade = row.getCell(3).getNumericCellValue();
                double final_exam_grade = row.getCell(4).getNumericCellValue();
                String status = row.getCell(5).getStringCellValue();
                Takes takes = new Takes(id, section.getCourse_id(), section.getSec_id(), section.getSemester(), section.getYear(), component_grade, final_exam_grade, status);
                section.getTakes().add(takes);
            }
        }
        addSection(section);
        takesService.addTakes(section);
    }
}
