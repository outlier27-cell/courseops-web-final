package cn.edu.uibe.courseops.service;

import cn.edu.uibe.courseops.common.BusinessException;
import cn.edu.uibe.courseops.config.CourseOpsProperties;
import cn.edu.uibe.courseops.dto.UploadResponse;
import cn.edu.uibe.courseops.entity.MaterialEntity;
import cn.edu.uibe.courseops.entity.MaterialHistoryEntity;
import cn.edu.uibe.courseops.entity.NotificationEntity;
import cn.edu.uibe.courseops.entity.SubmissionEntity;
import cn.edu.uibe.courseops.mapper.MaterialHistoryMapper;
import cn.edu.uibe.courseops.mapper.MaterialMapper;
import cn.edu.uibe.courseops.mapper.NotificationMapper;
import cn.edu.uibe.courseops.mapper.SubmissionMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class MaterialService {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final MaterialMapper materialMapper;
    private final SubmissionMapper submissionMapper;
    private final MaterialHistoryMapper historyMapper;
    private final NotificationMapper notificationMapper;
    private final OperationLogService logService;
    private final CourseOpsProperties properties;

    public MaterialService(
            MaterialMapper materialMapper,
            SubmissionMapper submissionMapper,
            MaterialHistoryMapper historyMapper,
            NotificationMapper notificationMapper,
            OperationLogService logService,
            CourseOpsProperties properties
    ) {
        this.materialMapper = materialMapper;
        this.submissionMapper = submissionMapper;
        this.historyMapper = historyMapper;
        this.notificationMapper = notificationMapper;
        this.logService = logService;
        this.properties = properties;
    }

    public MaterialEntity updateStatus(String id, String status, String actor) {
        var material = getMaterial(id);
        var before = material.status;
        material.status = status;
        material.progress = "approved".equals(status) ? 100 : Math.max(material.progress, 72);
        materialMapper.updateById(material);

        var action = "approved".equals(status) ? "审核通过材料" : "要求补充修改";
        createHistory(id, actor, action, before, status, null);
        logService.write(actor, "teacher", action, "material", material.title, before, status, "材料流转模块");
        createNotification(
                "u-student-001",
                material.title + " 状态已更新",
                "材料状态由 " + before + " 更新为 " + status,
                "approved".equals(status) ? "submission" : "feedback",
                id
        );
        return material;
    }

    public UploadResponse upload(String id, MultipartFile file, String submittedBy) {
        var material = getMaterial(id);
        if (file == null || file.isEmpty()) {
            throw new BusinessException("请选择需要上传的文件");
        }
        if (file.getSize() > 50L * 1024 * 1024) {
            throw new BusinessException("上传文件不能超过 50MB");
        }
        var originalName = file.getOriginalFilename() == null ? "unknown" : file.getOriginalFilename();
        var extension = extension(originalName);
        if (!material.format.equalsIgnoreCase(extension)) {
            throw new BusinessException("文件格式不符合要求，请上传 " + material.format + " 文件");
        }
        try {
            Files.createDirectories(Path.of(properties.getUploadDir()));
            var safeName = System.currentTimeMillis() + "-" + originalName.replaceAll("[\\\\/:*?\"<>|]", "_");
            var target = Path.of(properties.getUploadDir()).resolve(safeName);
            file.transferTo(target);

            var before = material.status;
            material.status = "submitted";
            material.latestFile = originalName;
            material.progress = Math.max(material.progress, 88);
            materialMapper.updateById(material);

            var now = LocalDateTime.now().format(FORMATTER);
            var submission = new SubmissionEntity();
            submission.id = "s-" + System.currentTimeMillis();
            submission.materialId = id;
            submission.fileName = originalName;
            submission.fileUrl = "/uploads/courseops/" + safeName;
            submission.fileSize = file.getSize();
            submission.submittedBy = submittedBy == null || submittedBy.isBlank() ? material.assignee : submittedBy;
            submission.submittedAt = now;
            submission.status = "submitted";
            submission.feedback = "材料已提交，等待教师审核";
            submissionMapper.insert(submission);

            createHistory(id, submission.submittedBy, "上传材料文件", before, "submitted", originalName);
            logService.write(submission.submittedBy, "student", "上传材料文件", "submission", material.title, before, originalName, "材料流转模块");
            createNotification("u-teacher-001", material.title + " 有新的提交", submission.submittedBy + " 上传了 " + originalName, "submission", id);

            return new UploadResponse(id, originalName, file.getSize(), file.getContentType(), now, submission.submittedBy, "submitted", "材料已提交，等待教师审核");
        } catch (BusinessException exception) {
            throw exception;
        } catch (Exception exception) {
            throw new BusinessException("文件上传失败：" + exception.getMessage());
        }
    }

    public MaterialEntity getMaterial(String id) {
        var material = materialMapper.selectById(id);
        if (material == null) {
            throw new BusinessException("材料不存在");
        }
        return material;
    }

    public Object histories(String materialId) {
        return historyMapper.selectList(new QueryWrapper<MaterialHistoryEntity>().eq("material_id", materialId));
    }

    private void createHistory(String materialId, String actor, String action, String fromStatus, String toStatus, String fileName) {
        var history = new MaterialHistoryEntity();
        history.id = "mh-" + System.currentTimeMillis();
        history.materialId = materialId;
        history.actor = actor == null || actor.isBlank() ? "系统用户" : actor;
        history.action = action;
        history.fromStatus = fromStatus;
        history.toStatus = toStatus;
        history.fileName = fileName;
        history.createdAt = LocalDateTime.now().format(FORMATTER);
        historyMapper.insert(history);
    }

    private void createNotification(String userId, String title, String description, String type, String relatedTargetId) {
        var notification = new NotificationEntity();
        notification.id = "n-" + System.currentTimeMillis();
        notification.userId = userId;
        notification.title = title;
        notification.description = description;
        notification.type = type;
        notification.relatedTargetId = relatedTargetId;
        notification.readFlag = false;
        notification.createdAt = LocalDateTime.now().format(FORMATTER);
        notificationMapper.insert(notification);
    }

    private String extension(String filename) {
        var index = filename.lastIndexOf('.');
        if (index < 0) {
            return "";
        }
        return filename.substring(index + 1).toUpperCase(Locale.ROOT);
    }
}
