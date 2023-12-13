package me.web.spring.database.demo.service;

import jakarta.transaction.Transactional;
import me.web.spring.database.demo.model.Classes;
import me.web.spring.database.demo.model.Student;
import me.web.spring.database.demo.model.Takes;
import me.web.spring.database.demo.repository.StudentRepository;
import me.web.spring.database.demo.repository.TakesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

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

//    public List<Student> listStudentInClassesHaveNotStudy(String className){
//        return studentRepository.listStudentInClassesHaveNotStudy(className);
//    }

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

    public List<Student> listStudentInClassesWithSorting(List<Student> studentList, String sortField, String sortDirection) {
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

    public LinkedHashMap<Double, Integer> countGPA(List<Student> studentList) {
        LinkedHashMap<Double, Integer> gpaCountMap = new LinkedHashMap<>();
        for (BigDecimal gpa = BigDecimal.ZERO; gpa.compareTo(BigDecimal.valueOf(4.1)) < 0; gpa = gpa.add(BigDecimal.valueOf(0.1))) {
            gpaCountMap.put(gpa.doubleValue(), 0);
        }
        for (Student student : studentList) {
            double gpa = (double) Math.round(student.getGPA() * 10) / 10;

            if (gpa >= 0.0 && gpa <= 4.0) {
                gpaCountMap.put(gpa, gpaCountMap.get(gpa) + 1);
            }
        }
        return gpaCountMap;
    }

    public Map<String, Double> countResult(List<Student> studentInClassesList) {
        LinkedHashMap<String, Integer> resultCountMap = new LinkedHashMap<>();
        resultCountMap.put("Xuất sắc", 0);
        resultCountMap.put("Giỏi", 0);
        resultCountMap.put("Khá", 0);
        resultCountMap.put("Trung bình", 0);
        resultCountMap.put("Yếu", 0);
        for (Student student : studentInClassesList) {
            if (student.getGPA() >= 3.6) {
                resultCountMap.put("Xuất sắc", resultCountMap.get("Xuất sắc") + 1);
            } else if (student.getGPA() >= 3.2) {
                resultCountMap.put("Giỏi", resultCountMap.get("Giỏi") + 1);
            } else if (student.getGPA() >= 2.5) {
                resultCountMap.put("Khá", resultCountMap.get("Khá") + 1);
            } else if (student.getGPA() >= 2.0) {
                resultCountMap.put("Trung bình", resultCountMap.get("Trung bình") + 1);
            } else {
                resultCountMap.put("Yếu", resultCountMap.get("Yếu") + 1);
            }
        }
        Map<String, Double> resultRatioMap = new HashMap<>();
        int totalStudents = studentInClassesList.size();

        for (Map.Entry<String, Integer> entry : resultCountMap.entrySet()) {
            String category = entry.getKey();
            int count = entry.getValue();
            double ratio = (double) count * 100 / totalStudents;
            resultRatioMap.put(category, ratio);
        }

        return resultRatioMap;
    }
}
