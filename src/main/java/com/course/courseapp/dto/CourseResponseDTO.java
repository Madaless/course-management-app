package com.course.courseapp.dto;

import com.course.courseapp.entity.CourseStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class CourseResponseDTO {
    private UUID id;
    private String title;
    private String description;
    private Integer duration;
    private CourseStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime publishedAt;
}
