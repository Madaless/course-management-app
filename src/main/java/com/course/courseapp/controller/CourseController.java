package com.course.courseapp.controller;

import com.course.courseapp.dto.CourseRequestDTO;
import com.course.courseapp.dto.CourseResponseDTO;
import com.course.courseapp.entity.CourseStatus;
import com.course.courseapp.service.AiService;
import com.course.courseapp.service.CourseService;
import com.course.courseapp.util.CourseApiResponse;
import com.course.courseapp.util.CourseResponseMessages;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@Tag(name = "Courses", description = "Course Management APIs")
@RequestMapping("/api/v1/courses")
@SecurityRequirement(name = "BearerAuth")
@Validated
@RestController
@AllArgsConstructor
public class CourseController {
    private CourseService courseService;
    private AiService aiService;

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Create a new course")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Course created successfully"),
            @ApiResponse(responseCode = "400", description = "Validation error")
    })
    @PostMapping
    public ResponseEntity<CourseApiResponse<CourseResponseDTO>> create(@Valid @RequestBody CourseRequestDTO dto) {
        CourseResponseDTO created = courseService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CourseApiResponse.success(CourseResponseMessages.CREATED, created, HttpStatus.CREATED.value()));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update an existing course")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Course updated successfully"),
            @ApiResponse(responseCode = "404", description = "Course not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<CourseApiResponse<CourseResponseDTO>> update(@PathVariable UUID id, @Valid @RequestBody CourseRequestDTO dto) {
        CourseResponseDTO updated = courseService.update(id, dto);
        return ResponseEntity.ok(CourseApiResponse.success(CourseResponseMessages.UPDATED, updated, HttpStatus.OK.value()));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Publish a course")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Course published successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid course state")
    })
    @PostMapping("/{id}/publish")
    public ResponseEntity<CourseApiResponse<CourseResponseDTO>> publish(@PathVariable UUID id) {
        CourseResponseDTO published = courseService.publish(id);
        return ResponseEntity.ok(CourseApiResponse.success(CourseResponseMessages.PUBLISHED, published, HttpStatus.OK.value()));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Archive a course")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Course archived successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid course state")
    })
    @PostMapping("/{id}/archive")
    public ResponseEntity<CourseApiResponse<CourseResponseDTO>> archive(@PathVariable UUID id) {
        CourseResponseDTO archived = courseService.archive(id);
        return ResponseEntity.ok(CourseApiResponse.success(CourseResponseMessages.ARCHIVED, archived, HttpStatus.OK.value()));

    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Get all published courses within a date range")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Courses retrieved successfully")
    })
    @GetMapping("/published")
    public ResponseEntity<CourseApiResponse<List<CourseResponseDTO>>> getPublishedInRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end
    ) {
        List<CourseResponseDTO> courses = courseService.getPublishedWithinRange(start, end);
        return ResponseEntity.ok(CourseApiResponse.success(CourseResponseMessages.PUBLISHED_IN_RANGE, courses, HttpStatus.OK.value()));
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Get the average duration of all courses")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Average duration calculated")
    })
    @GetMapping("/average-duration")
    public  ResponseEntity<CourseApiResponse<BigDecimal>> averageDuration() {
        BigDecimal average = courseService.getAverageDuration();
        return ResponseEntity.ok(CourseApiResponse.success(CourseResponseMessages.AVERAGE_DURATION, average, HttpStatus.OK.value()));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Duplicate an existing course")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Course duplicated successfully"),
            @ApiResponse(responseCode = "404", description = "Course not found")
    })
    @PostMapping("/{id}/duplicate")
    public ResponseEntity<CourseApiResponse<CourseResponseDTO>> duplicate(@PathVariable UUID id) {
        CourseResponseDTO duplicated = courseService.duplicateCourse(id);
        return ResponseEntity.ok(CourseApiResponse.success(CourseResponseMessages.DUPLICATED, duplicated, HttpStatus.OK.value()));
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Get all courses that are not published")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Courses not published retrieved")
    })
    @GetMapping("/not-published")
    public ResponseEntity<CourseApiResponse<List<CourseResponseDTO>>> getNotPublishedCourses() {
        List<CourseResponseDTO> courses = courseService.getCoursesNotPublished();
        return ResponseEntity.ok(CourseApiResponse.success(CourseResponseMessages.NOT_PUBLISHED, courses, HttpStatus.OK.value()));
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Get all courses with optional status filter")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Filtered courses retrieved successfully")
    })
    @GetMapping
    public ResponseEntity<CourseApiResponse<List<CourseResponseDTO>>> getAllCourses(
            @RequestParam(value = "status", required = false) List<CourseStatus> statusList
    ) {
        List<CourseResponseDTO> courses = courseService.getAllCoursesFilteredByStatus(statusList);
        return ResponseEntity.ok(
                CourseApiResponse.success("Courses retrieved successfully", courses, HttpStatus.OK.value())
        );
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Generate a course description based on the title using AI")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Course description generated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input or title missing"),
            @ApiResponse(responseCode = "500", description = "AI generation failed")
    })
    @PostMapping("/ai/description")
    public ResponseEntity<CourseApiResponse<String>> generateDescription(@RequestParam @NotNull String title) {
        String result = aiService.generateDescription(title);
        return ResponseEntity.ok(CourseApiResponse.success("Generated description", result, 200));
    }

}
