package cn.edu.uibe.courseops.controller;

import cn.edu.uibe.courseops.common.Result;
import cn.edu.uibe.courseops.dto.StatusUpdateRequest;
import cn.edu.uibe.courseops.entity.TeacherFeedbackEntity;
import cn.edu.uibe.courseops.mapper.MaterialMapper;
import cn.edu.uibe.courseops.mapper.TeacherFeedbackMapper;
import cn.edu.uibe.courseops.service.MaterialService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/materials")
public class MaterialController {
    private final MaterialMapper materialMapper;
    private final TeacherFeedbackMapper feedbackMapper;
    private final MaterialService materialService;

    public MaterialController(MaterialMapper materialMapper, TeacherFeedbackMapper feedbackMapper, MaterialService materialService) {
        this.materialMapper = materialMapper;
        this.feedbackMapper = feedbackMapper;
        this.materialService = materialService;
    }

    @GetMapping
    public Result<Object> list() {
        return Result.ok(materialMapper.selectList(null));
    }

    @GetMapping("/{id}")
    public Result<Object> detail(@PathVariable String id) {
        return Result.ok(materialMapper.selectById(id));
    }

    @PatchMapping("/{id}/status")
    public Result<Object> updateStatus(@PathVariable String id, @Valid @org.springframework.web.bind.annotation.RequestBody StatusUpdateRequest request) {
        return Result.ok(materialService.updateStatus(id, request.status(), request.actorId() == null ? "课程教师" : request.actorId()));
    }

    @PostMapping("/{id}/upload")
    public Result<Object> upload(@PathVariable String id, @RequestPart("file") MultipartFile file, @RequestParam(defaultValue = "学生用户") String submittedBy) {
        return Result.ok(materialService.upload(id, file, submittedBy));
    }

    @GetMapping("/{id}/histories")
    public Result<Object> histories(@PathVariable String id) {
        return Result.ok(materialService.histories(id));
    }

    @GetMapping("/{id}/feedback")
    public Result<Object> feedback(@PathVariable String id) {
        return Result.ok(feedbackMapper.selectList(new QueryWrapper<TeacherFeedbackEntity>().eq("material_id", id)));
    }
}
