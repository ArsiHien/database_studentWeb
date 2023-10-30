package me.web.spring.database.demo.controller;

import me.web.spring.database.demo.model.Course;
import me.web.spring.database.demo.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping(path = "v1/course")
public class CourseController {
    private final CourseService courseService;

    @Autowired
    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping("/listCourse")
    public String getCourse(Model model){
        List<Course> courseList = courseService.getCourse();
        model.addAttribute("courseList", courseList);
        return "Course/listCourse";
    }

    @GetMapping("/addCourse")
    public String addCourse(Model model) {
        model.addAttribute("course", new Course());
        return "Course/addCourse";
    }

    @PostMapping("/addCourse")
    public String addCourse(@ModelAttribute Course course) {
        courseService.addCourse(course);
        return "Course/successAddCourse";
    }
}
