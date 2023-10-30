package me.web.spring.database.demo.repository;

import me.web.spring.database.demo.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, String> {

    @Modifying
    @Query(value = "INSERT INTO course (course_id, title, credits) VALUES (:course_id, :title, :credits)", nativeQuery = true)
    void addCourse(@Param("course_id") String course_id, @Param("title") String title, @Param("credits") int credits);

}
