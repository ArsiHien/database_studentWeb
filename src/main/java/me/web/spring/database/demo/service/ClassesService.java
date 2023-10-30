package me.web.spring.database.demo.service;

import jakarta.transaction.Transactional;
import me.web.spring.database.demo.model.Classes;
import me.web.spring.database.demo.model.Student;
import me.web.spring.database.demo.repository.ClassesRepository;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

@Service
public class ClassesService {
    private final ClassesRepository classesRepository;
    private final StudentService studentService;

    @Autowired
    public ClassesService(ClassesRepository classesRepository, StudentService studentService) {
        this.classesRepository = classesRepository;
        this.studentService = studentService;
    }

    public List<Classes> getClasses() {
        return classesRepository.findAll();
    }

    @Transactional
    public void addClasses(Classes classes) {
        classesRepository.addClasses(classes.getName(), classes.getAdvisor());
    }

    @Transactional
    public void processClassesFile(Classes classes, InputStream inputStream) throws IOException, InvalidFormatException {
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
                String name = row.getCell(2).getStringCellValue();
                String[] nameParts = name.split(" ");
                String middle_name = String.join(" ", Arrays.copyOfRange(nameParts, 1, nameParts.length - 1));
                Student student = new Student(id, nameParts[0], middle_name, nameParts[nameParts.length - 1]);
                classes.getStudents().add(student);
            }
        }
        addClasses(classes);
        studentService.addStudentInClasses(classes);
    }
}
