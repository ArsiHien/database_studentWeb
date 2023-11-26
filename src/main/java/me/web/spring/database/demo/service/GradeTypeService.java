package me.web.spring.database.demo.service;

import jakarta.transaction.Transactional;
import me.web.spring.database.demo.model.GradeType;
import me.web.spring.database.demo.repository.GradeTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class GradeTypeService {
    private final GradeTypeRepository gradeTypeRepository;

    @Autowired
    public GradeTypeService(GradeTypeRepository gradeTypeRepository) {
        this.gradeTypeRepository = gradeTypeRepository;
    }

    public GradeType findGradeType(GradeType gradeType){
        return gradeTypeRepository.findGradeType(gradeType.getSection_id(), gradeType.getName(), gradeType.getRatio());
    }

    @Transactional
    public void addGradeType(GradeType gradeType) {
        gradeTypeRepository.addGradeType(gradeType.getName(), gradeType.getRatio(), gradeType.getSection_id());
        gradeType.setID(findGradeType(gradeType).getID());
    }

    public Integer getNumberOfGradeTypesInSection(int sectionID){
        return gradeTypeRepository.getNumberOfGradeTypesInSection(sectionID);
    }

    public List<GradeType> getGradeTypesInSection(int sectionID){
        return gradeTypeRepository.getGradeTypesInSection(sectionID);
    }
}
