package cn.edu.uibe.courseops.controller;

import cn.edu.uibe.courseops.common.Result;
import cn.edu.uibe.courseops.entity.NotificationEntity;
import cn.edu.uibe.courseops.mapper.NotificationMapper;
import cn.edu.uibe.courseops.service.OperationLogService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
    private final NotificationMapper notificationMapper;
    private final OperationLogService logService;

    public NotificationController(NotificationMapper notificationMapper, OperationLogService logService) {
        this.notificationMapper = notificationMapper;
        this.logService = logService;
    }

    @GetMapping
    public Result<Object> list(@RequestParam(required = false) String userId) {
        if (userId == null || userId.isBlank()) {
            return Result.ok(notificationMapper.selectList(null));
        }
        return Result.ok(notificationMapper.selectList(new QueryWrapper<NotificationEntity>().eq("user_id", userId)));
    }

    @GetMapping("/unread-count")
    public Result<Long> unreadCount(@RequestParam String userId) {
        return Result.ok(notificationMapper.selectCount(new QueryWrapper<NotificationEntity>().eq("user_id", userId).eq("read_flag", false)));
    }

    @PatchMapping("/{id}/read")
    public Result<Object> markRead(@PathVariable String id) {
        var notification = notificationMapper.selectById(id);
        if (notification != null) {
            notification.readFlag = true;
            notificationMapper.updateById(notification);
            logService.write("当前用户", "student", "标记通知已读", "notification", notification.title, "未读", "已读", "通知接口");
        }
        return Result.ok(notification);
    }
}
