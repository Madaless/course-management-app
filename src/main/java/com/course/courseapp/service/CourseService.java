package com.course.courseapp.service;

import com.course.courseapp.dto.CourseRequestDTO;
import com.course.courseapp.dto.CourseResponseDTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface CourseService {
    CourseResponseDTO create(CourseRequestDTO dto);
    CourseResponseDTO update(UUID id, CourseRequestDTO dto);
    List<CourseResponseDTO> findAll();
    CourseResponseDTO publish(UUID id);
    CourseResponseDTO archive(UUID id);
    BigDecimal getAverageDuration();
    List<CourseResponseDTO> getPublishedWithinRange(LocalDateTime start, LocalDateTime end);
    CourseResponseDTO duplicateCourse(UUID id);
    List<CourseResponseDTO> getCoursesNotPublished();

}
