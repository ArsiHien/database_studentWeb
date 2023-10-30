package me.web.spring.database.demo.controller;

import me.web.spring.database.demo.model.Student;
import me.web.spring.database.demo.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(path = "v1/student")
public class StudentController {
    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/listStudentInClasses")
    public String getStudentInClasses(@RequestParam("className") String className, Model model) {
        List<Student> studentInClassesList = studentService.listStudentInClasses(className);
        if (studentInClassesList.isEmpty()) return "no-entity";
        model.addAttribute("studentListInClasses", studentInClassesList);
        return "Classes/listStudentInClasses";
    }

    @RequestMapping("/listStudentHaveIdStartBy")
    @ResponseBody
    public List<Integer> listStudentHaveIdStartBy(@RequestParam(value = "id", required = false) String id, Model model) {
        return studentService.listStudentHaveIdStartBy(id);
    }

    @GetMapping("/studentInfoInCourse")
    public String getStudentInfoInCourse(@RequestParam("studentIDAdd") String id, Model model) {
        Optional<Student> student = studentService.getStudentById(id);
        System.out.println(student);
        model.addAttribute("student", student.orElse(null));
        return "Section/addStudent";
    }
}