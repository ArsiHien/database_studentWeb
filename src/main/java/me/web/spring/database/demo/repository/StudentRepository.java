package me.web.spring.database.demo.repository;

import me.web.spring.database.demo.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {
    @Modifying
    @Query(value = "INSERT INTO student (id, last_name, middle_name, first_name, class_name)" +
            "VALUES (:id, :last_name, :middle_name, :first_name, :class_name)", nativeQuery = true)
    void addStudent(@Param("id") int id,
                    @Param("last_name") String last_name,
                    @Param("middle_name") String middle_name,
                    @Param("first_name") String first_name,
                    @Param("class_name") String class_name);

    @Query(nativeQuery = true)
    List<Student> listStudentInClasses(@Param("class_name") String class_name);

    @Query(value = "SELECT ID FROM student", nativeQuery = true)
    List<Integer> listStudentId();

    @Query(value = "SELECT * FROM student WHERE ID = :student_id", nativeQuery = true)
    Student findStudentById(@Param("student_id") int studentId);

    @Query(nativeQuery = true)
    Student getStudentInfo(@Param("student_id") int studentId);

    @Query(value = "SELECT * FROM student where class_name = :class_name", nativeQuery = true)
    List<Student> listStudentInClassesHaveNotStudy(@Param("class_name") String className);
}