package me.web.spring.database.demo.repository;

import me.web.spring.database.demo.model.Section;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SectionRepository extends JpaRepository<Section, Integer> {
    @Query(value = "SELECT * FROM section WHERE course_id = :course_id", nativeQuery = true)
    List<Section> listSectionInCourse(@Param("course_id") String course_id);

    @Modifying
    @Query(value = "INSERT INTO section (course_id, sec_id, semester, year)" +
            "VALUES (:course_id, :sec_id, :semester, :year)", nativeQuery = true)
    void addSection(@Param("course_id") String course_id,
                    @Param("sec_id") int sec_id,
                    @Param("semester") int semester,
                    @Param("year") int year);


    @Query(value = "SELECT * FROM section WHERE ID = :section_id", nativeQuery = true)
    Section findSection(@Param("section_id") int sectionId);

    @Query(value = "SELECT * FROM section" +
            " WHERE course_id = :course_id AND sec_id = :sec_id AND semester = :semester AND year = :year", nativeQuery = true)
    Section findSection(@Param("course_id") String courseId,
                        @Param("sec_id") int secId,
                        @Param("semester") int semester,
                        @Param("year") int year);
}
