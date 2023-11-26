package me.web.spring.database.demo.service;

import jakarta.transaction.Transactional;
import me.web.spring.database.demo.model.Classes;
import me.web.spring.database.demo.model.Student;
import me.web.spring.database.demo.model.Takes;
import me.web.spring.database.demo.repository.StudentRepository;
import me.web.spring.database.demo.repository.TakesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class StudentService {
    private final StudentRepository studentRepository;
    private final TakesService takesService;
    private final GradeService gradeService;

    @Autowired
    public StudentService(StudentRepository studentRepository, TakesRepository takesRepository, TakesService takesService, GradeService gradeService) {
        this.studentRepository = studentRepository;
        this.takesService = takesService;
        this.gradeService = gradeService;
    }


    @Transactional
    public void addStudent(Student student) {
        studentRepository.addStudent(student.getID(),
                student.getLast_name(),
                student.getMiddle_name(),
                student.getFirst_name(),
                student.getClasses().getName());
    }

    @Transactional
    public void addStudentInClasses(Classes classes) {
        for (Student student : classes.getStudents()) {
            studentRepository.addStudent(student.getID(),
                    student.getLast_name(),
                    student.getMiddle_name(),
                    student.getFirst_name(),
                    classes.getName());
        }
    }

    public List<Student> listStudentInClasses(String className, String studentType) {
        List<Student> studentList = studentRepository.listStudentInClasses(className);
        switch (studentType) {
            case "excellent" -> studentList.removeIf(student -> student.getGPA() < 3.6);
            case "good" -> studentList.removeIf(student -> student.getGPA() < 3.2 || student.getGPA() >= 3.6);
            case "poor" -> studentList.removeIf(student -> student.getGPA() > 2);
        }
        return studentList;
    }

    public List<Student> listStudentInClassesHaveNotStudy(String className){
        return studentRepository.listStudentInClassesHaveNotStudy(className);
    }

    public List<Integer> listStudentId() {
        return studentRepository.listStudentId();
    }

    public Student getStudentById(int id) {
        return studentRepository.findStudentById(id);
    }

    public Student getStudentInfo(int id) {
        return studentRepository.getStudentInfo(id);
    }

    public List<Takes> getStudentInfo(String id) {
        List<Takes> takesList = takesService.getSectionsForStudent(Integer.parseInt(id));
        for (Takes takes : takesList) {
            takes.setFinal_grade(gradeService.calculateFinalGrade(takes));
        }
        return takesList;
    }

    public List<Student> listStudentInClassesWithSorting(String className, String studentType, String sortField, String sortDirection) {
        List<Student> studentList = listStudentInClasses(className, studentType);
        if(studentList.isEmpty()){
            return listStudentInClassesHaveNotStudy(className);
        }
        Comparator<Student> comparator = getStudentComparator(sortField, sortDirection);
        studentList.sort(comparator);
        return studentList;
    }

    private Comparator<Student> getStudentComparator(String sortField, String sortDirection) {
        Comparator<Student> comparator = switch (sortField) {
            case "GPA" -> Comparator.comparing(Student::getGPA);
            case "total_credit" -> Comparator.comparing(Student::getTotal_credit);
            case "total_cumulative_credits" -> Comparator.comparing(Student::getTotal_cumulative_credits);
            default -> Comparator.comparing(Student::getFirst_name);
        };
        if ("desc".equals(sortDirection)) {
            comparator = comparator.reversed();
        }
        return comparator;
    }
}
