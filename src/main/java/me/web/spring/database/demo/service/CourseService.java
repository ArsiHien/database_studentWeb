package me.web.spring.database.demo.service;

import jakarta.transaction.Transactional;
import me.web.spring.database.demo.model.Course;
import me.web.spring.database.demo.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourseService {
    private final CourseRepository courseRepository;

    @Autowired
    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public List<Course> getCourse(){
        return courseRepository.findAll();
    }

    @Transactional
    public void addCourse(Course course){
        courseRepository.addCourse(course.getCourse_id(), course.getTitle(), course.getCredits());
    }

    public Course getCourseById(String courseId) {
        Optional<Course> optionalCourse = courseRepository.findById(courseId);
        return optionalCourse.orElse(null);
    }
}
