package com.course.courseapp.service;

import com.course.courseapp.dto.CourseRequestDTO;
import com.course.courseapp.dto.CourseResponseDTO;
import com.course.courseapp.entity.Course;
import com.course.courseapp.entity.CourseStatus;
import com.course.courseapp.mapper.CourseMapper;
import com.course.courseapp.repository.CourseRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class CourseServiceImpl implements CourseService {
    private final CourseMapper courseMapper;
    private final CourseRepository repo;

    public CourseServiceImpl(CourseMapper courseMapper, CourseRepository repo) {
        this.courseMapper = courseMapper;
        this.repo = repo;
    }

    @Override
    public CourseResponseDTO create(CourseRequestDTO dto) {
        log.info("Creating course: {}", dto.getTitle());
        Course course = courseMapper.toEntity(dto);
        course.setStatus(CourseStatus.DRAFT);
        return courseMapper.toDTO(repo.save(course));
    }

    @Override
    public List<CourseResponseDTO> getCoursesNotPublished() {
        List<Course> courses = repo.findByStatus(CourseStatus.DRAFT);
        return courseMapper.toDTOList(courses);
    }

    @Override
    public CourseResponseDTO update(UUID id, CourseRequestDTO dto) {
        log.info("Updating course: {}", id);
        Course course = repo.findById(id).orElseThrow(() -> new EntityNotFoundException("Course with ID " + id + " not found"));

        course.setTitle(dto.getTitle());
        course.setDuration(dto.getDuration());
        course.setDescription(dto.getDescription());
        return courseMapper.toDTO(repo.save(course));
    }


    @Override
    public List<CourseResponseDTO> findAll() {
        log.info("Retrieving all courses");
        return courseMapper.toDTOList(repo.findAll());
    }

    @Override
    public CourseResponseDTO publish(UUID id) {
        Course course = repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Course with ID " + id + " not found"));

        if (course.getStatus() == CourseStatus.ARCHIVED)
            throw new IllegalStateException("Cannot publish an archived course");
        if (course.getTitle() == null || course.getDuration() == null)
            throw new IllegalStateException("Title and duration are required to publish");

        course.setStatus(CourseStatus.PUBLISHED);
        course.setPublishedAt(LocalDateTime.now());
        return courseMapper.toDTO(repo.save(course));
    }

    @Override
    public CourseResponseDTO archive(UUID id) {
        log.info("Archiving course: {}", id);

        Course course = repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Course not found with id: " + id));

        if (course.getStatus() == CourseStatus.ARCHIVED) {
            throw new IllegalStateException("Course is already archived.");
        }

        if (course.getStatus() != CourseStatus.PUBLISHED) {
            throw new IllegalStateException("Only published courses can be archived.");
        }

        course.setStatus(CourseStatus.ARCHIVED);

        Course updated = repo.save(course);
        return courseMapper.toDTO(updated);
    }

    @Override
    public BigDecimal getAverageDuration() {
        log.info("Calculating average duration");
        BigDecimal average = repo.findAverageDuration();
        return average != null ? average : BigDecimal.ZERO;
    }

    @Override
    public List<CourseResponseDTO> getPublishedWithinRange(LocalDateTime start, LocalDateTime end) {
        log.info("Retrieving published courses within range: start={}, end={}", start, end);
        List<Course> courses = repo.findByStatusAndPublishedAtBetween(CourseStatus.PUBLISHED,start,end);
        return courseMapper.toDTOList(courses);
    }

    @Override
    public CourseResponseDTO duplicateCourse(UUID id) {
        log.info("Duplicating course: {}", id);
        Course original = repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Course with ID " + id + " not found"));
        Course copy = new Course();
        copy.setTitle(original.getTitle());
        copy.setDescription(original.getDescription());
        copy.setDuration(original.getDuration());
        copy.setStatus(CourseStatus.DRAFT);
        return courseMapper.toDTO(repo.save(copy));
    }
}
