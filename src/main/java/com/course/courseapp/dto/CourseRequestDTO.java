package com.course.courseapp.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CourseRequestDTO {
    @NotBlank
    private String title;

    private String description;

    @NotNull
    @Min(1)
    private Integer duration;
}