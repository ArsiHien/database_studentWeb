package me.web.spring.database.demo.repository;

import me.web.spring.database.demo.model.Grade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GradeRepository extends JpaRepository<Grade, Integer> {
    @Modifying
    @Query(value = "INSERT INTO grade (value, grade_type_id, takes_id) " +
            "VALUES (:value, :gradeTypeId, :takesId)", nativeQuery = true)
    void addGrade(@Param("value") double value,
                  @Param("gradeTypeId") int gradeTypeId,
                  @Param("takesId") int takesId);

    @Modifying
    @Query(value = "UPDATE grade SET value = :value " +
            "WHERE takes_id = :takes_id AND grade_type_id = :grade_type_id",
            nativeQuery = true)
    void updateStudentGrade(@Param("value") double value,
                            @Param("takes_id") int takes_id,
                            @Param("grade_type_id") int grade_type_id);

    @Query(value = "SELECT SUM(g.value * gt.ratio) " +
            "FROM grade g " +
            "INNER JOIN grade_type gt ON g.grade_type_id = gt.ID " +
            "WHERE g.takes_id = :takes_id", nativeQuery = true)
    Double calculateFinalGrade(@Param("takes_id") int takesId);
}
