package me.web.spring.database.demo.repository;

import me.web.spring.database.demo.model.Section;
import me.web.spring.database.demo.model.SectionId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SectionRepository extends JpaRepository<Section, SectionId> {
    @Query(value = "SELECT * FROM section WHERE course_id = :course_id", nativeQuery = true)
    List<Section> listSectionInCourse(@Param("course_id") String course_id);

    @Modifying
    @Query(value = "INSERT INTO section (course_id, sec_id, semester, year)" +
            "VALUES (:course_id, :sec_id, :semester, :year)", nativeQuery = true)
    void addSection(@Param("course_id") String course_id,
                    @Param("sec_id") String sec_id,
                    @Param("semester") String semester,
                    @Param("year") int year);
}
