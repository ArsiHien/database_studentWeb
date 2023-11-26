package me.web.spring.database.demo.repository;

import me.web.spring.database.demo.model.GradeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GradeTypeRepository extends JpaRepository<GradeType, Integer> {
    @Query(value = "SELECT * FROM grade_type WHERE section_id = :section_id AND name = :name AND ratio =:ratio", nativeQuery = true)
    GradeType findGradeType(@Param("section_id") int sectionId,@Param("name") String name,@Param("ratio") double ratio);

    @Modifying
    @Query(value = "INSERT INTO grade_type (name, ratio, section_id) " +
            "VALUES (:name, :ratio, :sectionId)", nativeQuery = true)
    void addGradeType(@Param("name") String name,
                                 @Param("ratio") double ratio,
                                 @Param("sectionId") int sectionId);

    @Query(value = "SELECT COUNT(ID) FROM grade_type WHERE section_id = :section_id", nativeQuery = true)
    Integer getNumberOfGradeTypesInSection(@Param("section_id") int sectionID);

    @Query(value = "SELECT * FROM grade_type WHERE section_id = :section_id", nativeQuery = true)
    List<GradeType> getGradeTypesInSection(@Param("section_id") int sectionID);
}
