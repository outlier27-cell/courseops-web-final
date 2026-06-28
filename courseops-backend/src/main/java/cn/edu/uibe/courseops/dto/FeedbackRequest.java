package cn.edu.uibe.courseops.dto;

import jakarta.validation.constraints.NotBlank;

public record FeedbackRequest(@NotBlank String materialId, @NotBlank String content, String teacherName) {
}
