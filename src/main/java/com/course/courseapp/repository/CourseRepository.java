package com.course.courseapp.repository;

import com.course.courseapp.entity.Course;
import com.course.courseapp.entity.CourseStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface CourseRepository extends JpaRepository<Course, UUID> {
    List<Course> findByStatusAndPublishedAtBetween(
            CourseStatus status, LocalDateTime start, LocalDateTime end
    );

    @Query("SELECT AVG(c.duration) FROM Course c")
    BigDecimal findAverageDuration();

    List<Course> findByStatus(CourseStatus status);

    List<Course> findByStatusIn(List<CourseStatus> validStatuses);
}
