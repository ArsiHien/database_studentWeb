package me.web.spring.database.demo.repository;

import me.web.spring.database.demo.model.Takes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TakesRepository extends JpaRepository<Takes, Integer> {

    @Query(value = "SELECT * FROM takes WHERE section_id = :section_id",
            nativeQuery = true)
    List<Takes> findTakesBySection(@Param("section_id") int sectionId);

    @Query(value = "SELECT * FROM takes WHERE student_id = :student_id AND section_id = :section_id",
            nativeQuery = true)
    Takes findStudentInSection(@Param("student_id") int studentId,
                               @Param("section_id") int sectionId);

    @Query(value = "SELECT takes.* FROM takes JOIN section ON takes.section_id = section.ID " +
            "WHERE takes.student_id = :student_id AND section.course_id = :course_id " +
            "ORDER BY year, semester DESC",
            nativeQuery = true)
    List<Takes> findStudentInCourse(@Param("student_id") int studentId,
                                    @Param("course_id") String courseId
    );

    @Modifying
    @Query(value = "INSERT INTO takes (student_id, section_id, status)" +
            "VALUES (:student_id, :section_id, :status)",
            nativeQuery = true)
    void addTakes(@Param("student_id") int studentId,
                  @Param("section_id") int sectionId,
                  @Param("status") String status);

    @Query(value = "SELECT takes.* FROM takes JOIN section ON takes.section_id = section.ID " +
            "WHERE takes.student_id = :student_id ORDER BY year, semester, course_id", nativeQuery = true)
    List<Takes> getSectionsForStudent(@Param("student_id") int studentId);

    @Query(value = "SELECT * FROM takes WHERE student_id = :student_id AND section_id = :section_id", nativeQuery = true)
    Takes findTakes(@Param("student_id") int studentId, @Param("section_id") int sectionId);

    @Query(value = "SELECT * FROM takes WHERE ID = :ID", nativeQuery = true)
    Takes findTakes(@Param("ID") int ID);

    @Modifying
    @Query(value = "DELETE FROM takes WHERE student_id = :student_id AND section_id = :section_id", nativeQuery = true)
    void deleteStudentInSection(@Param("student_id") int studentId, @Param("section_id") int sectionId);
}
