package com.course.courseapp.mapper;

import com.course.courseapp.dto.CourseRequestDTO;
import com.course.courseapp.dto.CourseResponseDTO;
import com.course.courseapp.entity.Course;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CourseMapper {
    Course toEntity(CourseRequestDTO dto);
    CourseResponseDTO toDTO(Course entity);
    List<CourseResponseDTO> toDTOList(List<Course> entities);
}

