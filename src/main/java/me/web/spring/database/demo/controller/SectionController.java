package me.web.spring.database.demo.controller;

import me.web.spring.database.demo.model.Course;
import me.web.spring.database.demo.model.Section;
import me.web.spring.database.demo.model.Takes;
import me.web.spring.database.demo.service.CourseService;
import me.web.spring.database.demo.service.SectionService;
import me.web.spring.database.demo.service.StudentService;
import me.web.spring.database.demo.service.TakesService;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Controller
@RequestMapping(path = "v1/section")
public class SectionController {
    private final SectionService sectionService;
    private final TakesService takesService;
    private final CourseService courseService;
    private final StudentService studentService;

    @Autowired
    public SectionController(SectionService sectionService, StudentService studentService, TakesService takesService, CourseService courseService, StudentService studentService1) {
        this.sectionService = sectionService;
        this.takesService = takesService;
        this.courseService = courseService;
        this.studentService = studentService1;
    }

    @GetMapping("/listSectionInCourse")
    public String getSectionInCourse(@RequestParam("courseId") String courseId, Model model) {
        List<Section> sectionListInCourse = sectionService.listSectionInCourse(courseId);
        model.addAttribute("courseId", courseId);
        if (sectionListInCourse.isEmpty()) {
            return "Section/noSection";
        }
        model.addAttribute("sectionListInCourse", sectionListInCourse);
        return "Section/listSectionInCourse";
    }

    @GetMapping("/addSection")
    public String addSection(@RequestParam("courseId") String courseId, Model model) {
        if (courseId == null) System.out.println(45657899);
        Section section = new Section();
        Course course = courseService.getCourseById(courseId);
        section.setCourse(course);
        model.addAttribute("section", section);
        model.addAttribute("course", course);
        return "Section/addSection";
    }

    @PostMapping("/addSection")
    public String addSection(@ModelAttribute Section section, @RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) throws IOException {
        if (file != null && !file.isEmpty()) {
            try (InputStream inputStream = file.getInputStream()) {
                sectionService.processSectionFile(section, inputStream);
            } catch (InvalidFormatException e) {
                throw new RuntimeException(e);
            }
        } else {
            redirectAttributes.addFlashAttribute("alertMessage", "No file uploaded");
            return "redirect:/v1/section/addSection";
        }
        return "Section/successAddSection";
    }

    @GetMapping("/{courseId}/{secId}/{semester}/{year}/getAll")
    public String showStudent(@PathVariable("courseId") String courseId,
                              @PathVariable("secId") String secId,
                              @PathVariable("semester") String semester,
                              @PathVariable("year") int year,
                              Model model) throws ChangeSetPersister.NotFoundException {
        Section section = sectionService.findSection(courseId, secId, semester, year);
        List<Takes> takesList = takesService.findTakesBySection(section);
        model.addAttribute("section", section);
        model.addAttribute("takesList", takesList);
        return "Section/listStudentInSection";
    }

    @RequestMapping("getOneTakes")
    @ResponseBody
    public Takes getOneTakes(@RequestParam("ID") int ID,
                             @RequestParam("courseId") String course_id,
                             @RequestParam("secId") String sec_id,
                             @RequestParam("semester") String semester,
                             @RequestParam("year") int year) {
        Takes takes = takesService.findTakes(ID, course_id, sec_id, semester, year);
        return takes;
    }

    @PostMapping(value = "/updateStudentGrade")
    public String updateStudentGrade(Takes takes) {
        takesService.updateStudentGrade(takes);
        return "redirect:/v1/section/" + takes.getCourse_id() + "/" + takes.getSec_id() + "/" + takes.getSemester() + "/" + takes.getYear() + "/getAll";
    }

    @GetMapping("/addStudent")
    public String addStudent(
                             Model model) {
        List<Integer> idList = studentService.listStudentId();
        for(Integer i : idList){
            System.out.println(i);
        }
        model.addAttribute("idList", idList);
        return "Section/addStudent";
    }

    @PostMapping(value = "/addStudent")
    public String addStudentToSection(String id, @ModelAttribute Section section){
//        takesService.addTakes();
        return "redirect:/v1/section/" + section.getCourse_id() + "/" + section.getSec_id() + "/" + section.getSemester() + "/" + section.getYear() + "/getAll";
    }
}
