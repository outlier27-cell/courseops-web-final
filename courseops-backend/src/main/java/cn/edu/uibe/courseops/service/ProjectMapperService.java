package cn.edu.uibe.courseops.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import cn.edu.uibe.courseops.dto.ProjectDto;
import cn.edu.uibe.courseops.entity.ProjectEntity;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ProjectMapperService {
    private final ObjectMapper objectMapper;

    public ProjectMapperService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public ProjectDto toDto(ProjectEntity entity) {
        return new ProjectDto(
                entity.id,
                entity.courseId,
                entity.title,
                entity.description,
                entity.owner,
                parseMembers(entity.membersJson),
                entity.deadline,
                entity.phase,
                entity.progress,
                entity.teamProgress,
                entity.riskLevel,
                entity.healthScore,
                entity.riskReason,
                entity.recentFeedback
        );
    }

    private List<String> parseMembers(String membersJson) {
        try {
            return objectMapper.readValue(membersJson, new TypeReference<>() {});
        } catch (Exception ignored) {
            return List.of();
        }
    }
}
