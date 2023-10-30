package me.web.spring.database.demo.controller;

import me.web.spring.database.demo.model.Classes;
import me.web.spring.database.demo.service.ClassesService;
import me.web.spring.database.demo.service.StudentService;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Controller
@RequestMapping(path = "v1/class")
public class ClassesController {
    private final ClassesService classesService;
    private final StudentService studentService;

    @Autowired
    public ClassesController(ClassesService classesService, StudentService studentService) {
        this.classesService = classesService;
        this.studentService = studentService;
    }

    @GetMapping("/listClasses")
    public String getClasses(Model model) {
        List<Classes> classesList = classesService.getClasses();
        model.addAttribute("classesList", classesList);
        return "Classes/listClasses";
    }

    @GetMapping("/addClasses")
    public String addClasses(Model model) {
        model.addAttribute("classes", new Classes());
        return "Classes/addClasses";
    }

    @PostMapping("/addClasses")
    public String addClasses(@ModelAttribute Classes classes, @RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) throws IOException {
        if (file != null && !file.isEmpty()) {
            try (InputStream inputStream = file.getInputStream()) {
                classesService.processClassesFile(classes, inputStream);
            } catch (InvalidFormatException e) {
                throw new RuntimeException(e);
            }
        } else {
            redirectAttributes.addFlashAttribute("alertMessage", "No file uploaded");
            return "redirect:/v1/class/addClasses";
        }
        return "Classes/successAddClasses";
    }
}
