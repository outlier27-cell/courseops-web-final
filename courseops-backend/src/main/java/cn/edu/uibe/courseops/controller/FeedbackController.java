package cn.edu.uibe.courseops.controller;

import cn.edu.uibe.courseops.common.Result;
import cn.edu.uibe.courseops.dto.FeedbackRequest;
import cn.edu.uibe.courseops.entity.TeacherFeedbackEntity;
import cn.edu.uibe.courseops.mapper.MaterialMapper;
import cn.edu.uibe.courseops.mapper.TeacherFeedbackMapper;
import cn.edu.uibe.courseops.service.OperationLogService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/feedback")
public class FeedbackController {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final TeacherFeedbackMapper feedbackMapper;
    private final MaterialMapper materialMapper;
    private final OperationLogService logService;

    public FeedbackController(TeacherFeedbackMapper feedbackMapper, MaterialMapper materialMapper, OperationLogService logService) {
        this.feedbackMapper = feedbackMapper;
        this.materialMapper = materialMapper;
        this.logService = logService;
    }

    @GetMapping
    public Result<Object> list() {
        return Result.ok(feedbackMapper.selectList(null));
    }

    @PostMapping
    public Result<Object> create(@Valid @RequestBody FeedbackRequest request) {
        var material = materialMapper.selectById(request.materialId());
        var feedback = new TeacherFeedbackEntity();
        feedback.id = "fb-" + System.currentTimeMillis();
        feedback.materialId = request.materialId();
        feedback.projectId = material == null ? "" : material.projectId;
        feedback.teacherName = request.teacherName() == null ? "课程教师" : request.teacherName();
        feedback.content = request.content();
        feedback.status = "unresolved";
        feedback.createdAt = LocalDateTime.now().format(FORMATTER);
        feedbackMapper.insert(feedback);
        logService.write(
                feedback.teacherName,
                "teacher",
                "新增教师反馈",
                "feedback",
                material == null ? request.materialId() : material.title,
                "无",
                feedback.content,
                "教师反馈模块"
        );
        return Result.ok(feedback);
    }

    @PatchMapping("/{id}/resolve")
    public Result<Object> resolve(@PathVariable String id) {
        var feedback = feedbackMapper.selectById(id);
        if (feedback != null) {
            feedback.status = "resolved";
            feedbackMapper.updateById(feedback);
            logService.write("学生用户", "student", "处理教师反馈", "feedback", feedback.content, "unresolved", "resolved", "教师反馈模块");
        }
        return Result.ok(feedback);
    }

    @GetMapping("/project/{projectId}")
    public Result<Object> byProject(@PathVariable String projectId) {
        return Result.ok(feedbackMapper.selectList(new QueryWrapper<TeacherFeedbackEntity>().eq("project_id", projectId)));
    }
}
