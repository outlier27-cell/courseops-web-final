package cn.edu.uibe.courseops.dto;

import jakarta.validation.constraints.NotBlank;

public record StatusUpdateRequest(@NotBlank String status, String actorId) {
}
