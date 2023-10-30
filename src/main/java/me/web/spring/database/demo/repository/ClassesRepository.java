package me.web.spring.database.demo.repository;

import me.web.spring.database.demo.model.Classes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassesRepository extends JpaRepository<Classes, String>{
    @Modifying
    @Query(value = "INSERT INTO classes (name, advisor) VALUES (:name, :advisor)", nativeQuery = true)
    void addClasses(@Param("name") String name, @Param("advisor") String advisor);
}
