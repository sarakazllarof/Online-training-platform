package com.eduwise.repository;

import com.eduwise.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findByInstructorId(Long instructorId);
    List<Course> findByCategoryId(Long categoryId);
    
    @Query("SELECT c FROM Course c WHERE c.title LIKE %:searchTerm% OR c.description LIKE %:searchTerm%")
    List<Course> searchCourses(String searchTerm);
    
    List<Course> findByPublishedTrue();
    long countByInstructorUsername(String username);
    List<Course> findByInstructorUsername(String username);
} 