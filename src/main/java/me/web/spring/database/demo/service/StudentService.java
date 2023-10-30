package me.web.spring.database.demo.service;

import jakarta.transaction.Transactional;
import me.web.spring.database.demo.model.Classes;
import me.web.spring.database.demo.model.Student;
import me.web.spring.database.demo.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {
    private final StudentRepository studentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }


    @Transactional
    public void addStudent(Student student) {
        studentRepository.addStudent(student.getID(),
                student.getLast_name(),
                student.getMiddle_name(),
                student.getFirst_name(),
                student.getClass_name());
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

    public List<Student> listStudentInClasses(String className) {
        return studentRepository.listStudentInClasses(className);
    }

    public List<Integer> listStudentHaveIdStartBy(String id) {
        return studentRepository.listStudentHaveIdStartBy(id);
    }

    public List<Integer> listStudentId() {
        return studentRepository.listStudentId();
    }

    public Optional<Student> getStudentById(String id) {
        return studentRepository.findById(Integer.parseInt(id));
    }
}
