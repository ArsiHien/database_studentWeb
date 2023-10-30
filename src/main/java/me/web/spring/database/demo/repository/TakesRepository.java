package me.web.spring.database.demo.repository;

import me.web.spring.database.demo.model.Takes;
import me.web.spring.database.demo.model.TakesId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TakesRepository extends JpaRepository<Takes, TakesId> {

    @Query(value = "SELECT * FROM takes WHERE course_id = :course_id AND sec_id = :sec_id AND semester = :semester AND year = :year",
            nativeQuery = true)
    List<Takes> findTakesBySection(@Param("course_id") String course_id,
                                   @Param("sec_id") String sec_id,
                                   @Param("semester") String semester,
                                   @Param("year") int year);

    @Query(value = "SELECT * FROM takes WHERE ID = :ID AND course_id = :course_id AND sec_id = :sec_id AND semester = :semester AND year = :year",
            nativeQuery = true)
    Takes findTakes(@Param("ID") int ID,
                    @Param("course_id") String course_id,
                    @Param("sec_id") String sec_id,
                    @Param("semester") String semester,
                    @Param("year") int year);

    @Modifying
    @Query(value = "INSERT INTO takes (ID, course_id, sec_id, semester, year, component_grade, final_exam_grade, status)" +
            "VALUES (:ID, :course_id, :sec_id, :semester, :year, :component_grade, :final_exam_grade, :status)",
            nativeQuery = true)
    void addTakes(@Param("ID") int ID,
                  @Param("course_id") String course_id,
                  @Param("sec_id") String sec_id,
                  @Param("semester") String semester,
                  @Param("year") int year,
                  @Param("component_grade") double component_grade,
                  @Param("final_exam_grade") double final_exam_grade,
                  @Param("status") String status);

    @Modifying
    @Query(value = "UPDATE takes SET component_grade = :component_grade, final_exam_grade = :final_exam_grade " +
            "WHERE ID = :ID AND course_id = :course_id AND sec_id = :sec_id AND semester = :semester AND year = :year",
            nativeQuery = true)
    void updateStudentGrade(@Param("ID") int ID,
                            @Param("course_id") String course_id,
                            @Param("sec_id") String sec_id,
                            @Param("semester") String semester,
                            @Param("year") int year,
                            @Param("component_grade") double component_grade,
                            @Param("final_exam_grade") double final_exam_grade);
}
