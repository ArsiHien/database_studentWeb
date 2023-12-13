package me.web.spring.database.demo.controller;

import me.web.spring.database.demo.DTO.GradeDTO;
import me.web.spring.database.demo.DTO.TakesDTO;
import me.web.spring.database.demo.model.Course;
import me.web.spring.database.demo.model.Grade;
import me.web.spring.database.demo.model.Section;
import me.web.spring.database.demo.model.Takes;
import me.web.spring.database.demo.service.*;
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
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(path = "v1/section")
public class SectionController {
    private final SectionService sectionService;
    private final TakesService takesService;
    private final CourseService courseService;
    private final StudentService studentService;
    private final GradeService gradeService;
    private final GradeTypeService gradeTypeService;

    @Autowired
    public SectionController(SectionService sectionService, StudentService studentService, TakesService takesService, CourseService courseService, StudentService studentService1, GradeService gradeService, GradeTypeService gradeTypeService) {
        this.sectionService = sectionService;
        this.takesService = takesService;
        this.courseService = courseService;
        this.studentService = studentService1;
        this.gradeService = gradeService;
        this.gradeTypeService = gradeTypeService;
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
        Section section = new Section();
        Course course = courseService.getCourseById(courseId);
        model.addAttribute("section", section);
        model.addAttribute("course", course);
        return "Section/addSection";
    }

    @PostMapping("/addSection")
    public String addSection(@ModelAttribute Section section,
                             @RequestParam("courseId") String courseId,
                             @RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes, Model model) throws IOException {
        Course course = courseService.getCourseById(courseId);
        section.setCourse(course);
        course.getSections().add(section);
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
        model.addAttribute("courseId", courseId);
        return "Section/successAddSection";
    }

    @GetMapping("/{courseId}/{secId}/{semester}/{year}/getAll")
    public String showStudent(@PathVariable("courseId") String courseId,
                              @PathVariable("secId") int secId,
                              @PathVariable("semester") int semester,
                              @PathVariable("year") int year,
                              @RequestParam(value = "studentType", defaultValue = "all") String studentType,
                              @RequestParam(defaultValue = "name, asc") String[] sort,
                              Model model) throws ChangeSetPersister.NotFoundException {
        Section section = sectionService.findSection(courseId, secId, semester, year);
//        List<Takes> takesList = takesService.findTakesBySection(section);
        List<Takes> takesList = takesService.findTakesBySectionWithSorting(section, studentType, sort[0], sort[1]);
        model.addAttribute("size", takesList.size());
        model.addAttribute("section", section);
        model.addAttribute("takesList", takesList);
        model.addAttribute("sortField", sort[0]);
        model.addAttribute("sortDirection", sort[1]);
        model.addAttribute("reverseSortDirection", sort[1].equals("asc") ? "desc" : "asc");
        model.addAttribute("studentType", studentType);
        return "Section/listStudentInSection";
    }

    @RequestMapping("getOneTakes")
    @ResponseBody
    public TakesDTO getOneStudentInSection(@RequestParam("studentId") int studentId,
                                           @RequestParam("sectionId") int sectionId) {
        Takes takes = takesService.findStudentInSection(studentId, sectionId);
        List<GradeDTO> gradeDTOS = new ArrayList<>();
        for (Grade grade : takes.getGrades()) {
            gradeDTOS.add(new GradeDTO(grade.getID(), grade.getGrade_type_id(), grade.getTakes_id(), grade.getValue(), grade.getGradeType().getName()));
        }

        TakesDTO takesDTO = new TakesDTO(takes.getID(), takes.getStudent_id(),
                takes.getSection_id(), takes.getStatus(), takes.getFinal_grade(), takes.getGrade_in_four_scale(),
                takes.getLetter_grade(), gradeDTOS);
        return takesDTO;
    }

    @PostMapping(value = "/updateStudentGrade")
    public String updateStudentGrade(Takes takes) {
        Takes tmpTakes = takesService.findTakes(takes.getID());
        for (int i = 0; i < takes.getGrades().size(); i++) {
            tmpTakes.getGrades().get(i).setValue(takes.getGrades().get(i).getValue());
        }
        gradeService.updateStudentGrade(tmpTakes);
        return "redirect:/v1/section/" + tmpTakes.getSection().getCourse_id() + "/" +
                tmpTakes.getSection().getSec_id() + "/" +
                tmpTakes.getSection().getSemester() + "/" +
                tmpTakes.getSection().getYear() + "/getAll";
    }

    @GetMapping("/delete")
    public String deleteStudent(@RequestParam("studentId") int studentId, @RequestParam("sectionId") int sectionId){
        Section section = sectionService.findSection(sectionId);
        takesService.deleteStudentInSection(studentId, sectionId);
        return "redirect:/v1/section/" + section.getCourse_id() + "/" +
                section.getSec_id() + "/" +
                section.getSemester() + "/" +
                section.getYear() + "/getAll";
    }

    @GetMapping("/addStudent")
    public String addStudentToSection(@RequestParam("sectionId") String sectionId,
                                      Model model) throws ChangeSetPersister.NotFoundException {
        Section section = sectionService.findSection(Integer.parseInt(sectionId));
        model.addAttribute("section", section);
        List<Integer> idList = studentService.listStudentId();
        model.addAttribute("idList", idList);
        int numberOfGradeTypes = gradeTypeService.getNumberOfGradeTypesInSection(Integer.parseInt(sectionId));
        model.addAttribute("numberOfGradeTypes", numberOfGradeTypes);
        return "Section/addStudent";
    }

    @PostMapping("/addStudent")
    public String addStudentToSection(@RequestBody Takes takes) {
        takes.setSection(sectionService.findSection(takes.getSection_id()));
        takesService.addOneTakes(takes);
        gradeService.addTakesGrade(takes);
        return "redirect:/v1/section/" +
                takes.getSection().getCourse_id() + "/" +
                takes.getSection().getSec_id() + "/" +
                takes.getSection().getSemester() + "/" +
                takes.getSection().getYear() + "/getAll";
    }
}