package com.course.courseapp.util;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CourseApiResponse<T> {
    private final boolean success;
    private final String message;
    private final T data;
    private final LocalDateTime timestamp;
    private final int code;

    public CourseApiResponse(boolean success, String message, T data, int code) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.code = code;
        this.timestamp = LocalDateTime.now();
    }

    public static <T> CourseApiResponse<T> success(String message, T data, int code) {
        return new CourseApiResponse<>(true, message, data, code);
    }

    public static <T> CourseApiResponse<T> fail(String message, int code) {
        return new CourseApiResponse<>(false, message, null, code);
    }
}
