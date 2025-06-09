package com.course.courseapp.util;

import java.time.LocalDateTime;

public class CourseApiResponse<T> {
    private boolean success;
    private String message;
    private T data;
    private LocalDateTime timestamp;
    private int code;

    public CourseApiResponse(boolean success, String message, T data, int code) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.code = code;
        this.timestamp = LocalDateTime.now();
    }

    public static <T> CourseApiResponse<T> of(boolean success, String message, T data, int code) {
        return new CourseApiResponse<>(success, message, data, code);
    }

    public static <T> CourseApiResponse<T> success(String message, T data, int code) {
        return new CourseApiResponse<>(true, message, data, code);
    }

    public static <T> CourseApiResponse<T> fail(String message, int code) {
        return new CourseApiResponse<>(false, message, null, code);
    }

    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public T getData() { return data; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public int getCode() { return code; }
}
