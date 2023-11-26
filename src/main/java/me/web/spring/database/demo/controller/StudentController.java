package me.web.spring.database.demo.controller;

import me.web.spring.database.demo.model.Section;
import me.web.spring.database.demo.model.Student;
import me.web.spring.database.demo.model.Takes;
import me.web.spring.database.demo.service.SectionService;
import me.web.spring.database.demo.service.StudentService;
import me.web.spring.database.demo.service.TakesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping(path = "v1/student")
public class StudentController {
    private final StudentService studentService;
    private final SectionService sectionService;
    private final TakesService takesService;

    @Autowired
    public StudentController(StudentService studentService, SectionService sectionService, TakesService takesService) {
        this.studentService = studentService;
        this.sectionService = sectionService;
        this.takesService = takesService;
    }

    @GetMapping("/studentInfoInCourse")
    public String getStudentInfoInCourse(@RequestParam("studentId") String studentId,
                                         @RequestParam("sectionId") String sectionId,
                                         Model model) throws ChangeSetPersister.NotFoundException {
        Section section = sectionService.findSection(Integer.parseInt(sectionId));
        model.addAttribute("section", section);
        Student student = studentService.getStudentById(Integer.parseInt(studentId));
        if (student == null) {
            model.addAttribute("notFound", true);
            return "Section/addStudent";
        }
        model.addAttribute("student", student);
        boolean canAdd = false;
        Takes takes = takesService.getStudentInfoInSection(studentId, section);
        List<Takes> takesList = takesService.getStudentInfoInCourse(studentId, section);
        if (takes != null) {
            model.addAttribute("takes", takes);
        } else {
            if (takesList.isEmpty()) {
                model.addAttribute("takesList", null);
            } else model.addAttribute("takesList", takesList);
        }
        if (takes == null) {
            if (takesList.isEmpty()) {
                canAdd = true;
            } else if (takesList.get(takesList.size() - 1).getFinal_grade() < 5.5) {
                Takes takesLast = takesList.get(takesList.size() - 1);
                if (takesLast.getSection().getYear() < section.getYear()
                        || (takesLast.getSection().getYear() == section.getYear()
                        && takesLast.getSection().getSemester() < section.getSemester())) {
                    canAdd = true;
                }
            }
        }
        model.addAttribute("canAdd", canAdd);
        return "Section/addStudent";
    }

    @GetMapping("/studentInfo")
    public String getStudentInfo(@RequestParam("ID") String id, Model model) {
        List<Takes> takesList = studentService.getStudentInfo(id);
        Student student = studentService.getStudentInfo(Integer.parseInt(id));
        model.addAttribute("student", student);
        System.out.println("-".repeat(20));
        model.addAttribute("takesList", takesList);
        int rowCounter = 1;
        model.addAttribute("rowCounter", rowCounter);
        return "Classes/studentInfo";
    }

    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable int id) {
        Student student = studentService.getStudentById(id);
        if (student != null) {
            return new ResponseEntity<>(student, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}