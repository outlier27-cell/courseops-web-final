package cn.edu.uibe.courseops.dto;

import java.util.List;

public record ProjectDto(
        String id,
        String courseId,
        String title,
        String description,
        String owner,
        List<String> members,
        String deadline,
        String phase,
        Integer progress,
        Integer teamProgress,
        String riskLevel,
        Integer healthScore,
        String riskReason,
        String recentFeedback
) {
}
