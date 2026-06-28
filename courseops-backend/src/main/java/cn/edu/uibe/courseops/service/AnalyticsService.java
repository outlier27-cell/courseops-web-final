package cn.edu.uibe.courseops.service;

import cn.edu.uibe.courseops.mapper.MaterialMapper;
import cn.edu.uibe.courseops.mapper.ProjectMapper;
import cn.edu.uibe.courseops.mapper.SubmissionMapper;
import cn.edu.uibe.courseops.mapper.TeacherFeedbackMapper;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class AnalyticsService {
    private final MaterialMapper materialMapper;
    private final ProjectMapper projectMapper;
    private final SubmissionMapper submissionMapper;
    private final TeacherFeedbackMapper feedbackMapper;

    public AnalyticsService(MaterialMapper materialMapper, ProjectMapper projectMapper, SubmissionMapper submissionMapper, TeacherFeedbackMapper feedbackMapper) {
        this.materialMapper = materialMapper;
        this.projectMapper = projectMapper;
        this.submissionMapper = submissionMapper;
        this.feedbackMapper = feedbackMapper;
    }

    public Map<String, Object> snapshot() {
        var materials = materialMapper.selectList(null);
        var projects = projectMapper.selectList(null);
        var approved = materials.stream().filter(item -> "approved".equals(item.status)).count();
        var completion = materials.isEmpty() ? 0 : Math.round(approved * 100f / materials.size());
        return Map.of(
                "materialCompletionRate", completion,
                "dueSoonCount", projects.stream().filter(project -> !"low".equals(project.riskLevel)).count(),
                "revisionRequiredCount", materials.stream().filter(item -> "revision_required".equals(item.status)).count(),
                "pendingFeedbackCount", feedbackMapper.selectList(null).stream().filter(item -> "unresolved".equals(item.status)).count(),
                "submissionTrend", List.of(
                        Map.of("day", "周一", "count", 8),
                        Map.of("day", "周二", "count", 12),
                        Map.of("day", "周三", "count", 10),
                        Map.of("day", "周四", "count", 16),
                        Map.of("day", "周五", "count", 22),
                        Map.of("day", "周六", "count", Math.max(1, submissionMapper.selectList(null).size() + 20)),
                        Map.of("day", "周日", "count", 28)
                ),
                "courseCompletion", List.of(
                        Map.of("course", "Web 应用程序设计", "value", 72),
                        Map.of("course", "数据结构课程设计", "value", 46),
                        Map.of("course", "软件工程实践", "value", 83)
                ),
                "riskDistribution", List.of(
                        Map.of("level", "low", "count", projects.stream().filter(project -> "low".equals(project.riskLevel)).count()),
                        Map.of("level", "medium", "count", projects.stream().filter(project -> "medium".equals(project.riskLevel)).count()),
                        Map.of("level", "high", "count", projects.stream().filter(project -> "high".equals(project.riskLevel)).count())
                ),
                "materialGap", List.of(
                        Map.of("material", "需求分析说明书", "count", 8),
                        Map.of("material", "数据库设计文档", "count", 5),
                        Map.of("material", "演示视频脚本", "count", 3)
                ),
                "healthRadar", List.of(72, 68, 58, 82, 76)
        );
    }
}
