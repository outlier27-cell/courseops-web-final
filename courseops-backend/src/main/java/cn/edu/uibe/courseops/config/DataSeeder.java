package cn.edu.uibe.courseops.config;

import cn.edu.uibe.courseops.entity.CourseEntity;
import cn.edu.uibe.courseops.entity.MaterialEntity;
import cn.edu.uibe.courseops.entity.MaterialHistoryEntity;
import cn.edu.uibe.courseops.entity.NotificationEntity;
import cn.edu.uibe.courseops.entity.ProjectEntity;
import cn.edu.uibe.courseops.entity.SubmissionEntity;
import cn.edu.uibe.courseops.entity.TeacherFeedbackEntity;
import cn.edu.uibe.courseops.entity.TeamMemberEntity;
import cn.edu.uibe.courseops.entity.UserEntity;
import cn.edu.uibe.courseops.mapper.CourseMapper;
import cn.edu.uibe.courseops.mapper.MaterialHistoryMapper;
import cn.edu.uibe.courseops.mapper.MaterialMapper;
import cn.edu.uibe.courseops.mapper.NotificationMapper;
import cn.edu.uibe.courseops.mapper.ProjectMapper;
import cn.edu.uibe.courseops.mapper.SubmissionMapper;
import cn.edu.uibe.courseops.mapper.TeacherFeedbackMapper;
import cn.edu.uibe.courseops.mapper.TeamMemberMapper;
import cn.edu.uibe.courseops.mapper.UserMapper;
import cn.edu.uibe.courseops.mongo.OperationLogDocument;
import cn.edu.uibe.courseops.mongo.OperationLogRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {
    private final UserMapper users;
    private final CourseMapper courses;
    private final ProjectMapper projects;
    private final TeamMemberMapper members;
    private final MaterialMapper materials;
    private final SubmissionMapper submissions;
    private final TeacherFeedbackMapper feedback;
    private final MaterialHistoryMapper histories;
    private final NotificationMapper notifications;
    private final OperationLogRepository logs;

    public DataSeeder(UserMapper users, CourseMapper courses, ProjectMapper projects, TeamMemberMapper members,
                      MaterialMapper materials, SubmissionMapper submissions, TeacherFeedbackMapper feedback,
                      MaterialHistoryMapper histories, NotificationMapper notifications, OperationLogRepository logs) {
        this.users = users;
        this.courses = courses;
        this.projects = projects;
        this.members = members;
        this.materials = materials;
        this.submissions = submissions;
        this.feedback = feedback;
        this.histories = histories;
        this.notifications = notifications;
        this.logs = logs;
    }

    @Override
    public void run(String... args) {
        seedUsers();
        seedCourses();
        seedProjects();
        seedMembers();
        seedMaterials();
        seedSubmissions();
        seedFeedback();
        seedHistories();
        seedNotifications();
        seedOperationLogs();
    }

    private void seedUsers() {
        if (users.selectById("u-student-001") != null) return;
        insertUser("u-student-001", "张同学", "student", "信息学院", "Z");
        insertUser("u-teacher-001", "张老师", "teacher", "信息学院", "T");
        insertUser("u-admin-001", "教务管理员", "admin", "教务处", "A");
    }

    private void seedCourses() {
        if (courses.selectById("c-web") != null) return;
        insertCourse("c-web", "Web 应用开发", "张老师", "陈助教", 86);
        insertCourse("c-db", "数据库原理", "李老师", "周助教", 72);
        insertCourse("c-writing", "学术写作", "王老师", "刘助教", 54);
    }

    private void seedProjects() {
        if (projects.selectById("p-web-final") != null) return;
        insertProject("p-web-final", "c-web", "Web 期末项目",
                "完成一个具备项目管理、材料流转和操作审计能力的课程项目平台。",
                "张同学", "[\"张同学\",\"李同学\",\"王同学\"]", "2026-06-25", "材料完善",
                72, 68, "medium", 76, "说明书正文处于需修改状态，源码压缩包尚未提交。",
                "说明书正文需要补充数据库设计说明。");
        insertProject("p-db-er", "c-db", "数据库实验报告",
                "提交 ER 图、表结构说明和实验报告 PDF。", "张同学", "[\"张同学\"]",
                "2026-06-24", "待补交", 46, 46, "high", 58,
                "ER 图未提交，距离截止不足 24 小时。", "ER 图缺少关系说明，请今天补齐。");
        insertProject("p-writing", "c-writing", "课程论文阶段稿",
                "完成二稿修改、引用检查和最终提交记录。", "李同学", "[\"李同学\",\"张同学\"]",
                "2026-06-28", "教师反馈处理", 83, 79, "low", 88,
                "当前无明显风险。", "引用格式已基本通过，等待最终确认。");
    }

    private void seedMembers() {
        if (members.selectById("tm-1") != null) return;
        insertMember("tm-1", "p-web-final", "张同学", "leader", "Z", "前端与说明书正文", 74, false);
        insertMember("tm-2", "p-web-final", "李同学", "member", "L", "系统截图与测试记录", 90, false);
        insertMember("tm-3", "p-web-final", "王同学", "member", "W", "数据库设计图", 42, true);
        insertMember("tm-4", "p-db-er", "张同学", "leader", "Z", "ER 图与实验报告", 46, true);
        insertMember("tm-5", "p-writing", "李同学", "leader", "L", "论文正文", 86, false);
    }

    private void seedMaterials() {
        if (materials.selectById("m-report") != null) return;
        insertMaterial("m-report", "p-web-final", "说明书正文",
                "包含系统简介、功能说明、截图、数据库设计和技术架构。", "DOCX",
                "2026-06-25", "revision_required", 70, "张同学", 2,
                "courseops-manual-v2.docx", "教师反馈未处理");
        insertMaterial("m-screenshots", "p-web-final", "系统截图",
                "登录页、工作台、项目中心、材料看板、数据洞察截图。", "ZIP",
                "2026-06-25", "submitted", 90, "李同学", 0,
                "courseops-screenshots.zip", "等待教师确认");
        insertMaterial("m-source", "p-web-final", "源代码压缩包",
                "包含前端、后端、数据库脚本和运行说明。", "ZIP",
                "2026-06-26", "preparing", 52, "张同学", 1, "", "尚未上传文件");
        insertMaterial("m-er", "p-db-er", "数据库 ER 图",
                "展示实体、关系、主键、外键和约束。", "PDF",
                "2026-06-24", "not_started", 20, "张同学", 1, "", "距离截止不足 24 小时");
        insertMaterial("m-paper", "p-writing", "论文二稿",
                "根据教师反馈修改正文、引用和摘要。", "DOCX",
                "2026-06-28", "approved", 100, "李同学", 0,
                "academic-writing-v3.docx", "已通过");
    }

    private void seedSubmissions() {
        if (submissions.selectById("s-report-v2") != null) return;
        insertSubmission("s-report-v2", "m-report", "courseops-manual-v2.docx", 2430000L,
                "张同学", "2026-06-23 10:12", "revision_required", "请补充项目空间与材料流转说明。");
        insertSubmission("s-screenshots", "m-screenshots", "courseops-screenshots.zip", 8210000L,
                "李同学", "2026-06-23 09:48", "submitted", "等待教师确认。");
    }

    private void seedFeedback() {
        if (feedback.selectById("fb-1") != null) return;
        insertFeedback("fb-1", "m-report", "p-web-final", "张老师",
                "说明书需要补充项目空间与材料流转的操作截图。", "unresolved", "2026-06-23 10:20");
        insertFeedback("fb-2", "m-source", "p-web-final", "张老师",
                "源码压缩包需要包含运行说明和数据库脚本。", "unresolved", "2026-06-23 09:36");
        insertFeedback("fb-3", "m-paper", "p-writing", "王老师",
                "引用格式已通过，可以准备最终提交。", "resolved", "2026-06-22 16:12");
    }

    private void seedHistories() {
        if (histories.selectById("mh-1") != null) return;
        insertHistory("mh-1", "m-report", "张同学", "上传文件", "preparing", "submitted",
                "courseops-manual-v2.docx", "2026-06-23 10:12");
        insertHistory("mh-2", "m-report", "张老师", "标记需修改", "submitted", "revision_required",
                null, "2026-06-23 10:20");
        insertHistory("mh-3", "m-screenshots", "李同学", "上传文件", "preparing", "submitted",
                "courseops-screenshots.zip", "2026-06-23 09:48");
    }

    private void seedNotifications() {
        if (notifications.selectById("n-1") != null) return;
        insertNotification("n-1", "u-student-001", "说明书正文需要修改",
                "张老师反馈：请补充项目空间与材料流转说明。", "feedback", "m-report",
                false, "2026-06-23 10:20");
        insertNotification("n-2", "u-student-001", "数据库 ER 图即将截止",
                "距离截止不足 24 小时，建议今天完成上传。", "deadline", "m-er",
                false, "2026-06-23 09:30");
        insertNotification("n-3", "u-teacher-001", "有 2 份材料待审核",
                "Web 期末项目新增材料提交，请及时检查。", "submission", "p-web-final",
                false, "2026-06-23 10:30");
    }

    private void seedOperationLogs() {
        if (logs.count() > 0) return;
        insertLog("log-001", "张同学", "student", "上传文件", "submission",
                "说明书正文", "preparing", "submitted", "2026-06-23 10:12:00", "材料流转中心");
        insertLog("log-002", "张老师", "teacher", "标记需修改", "material",
                "说明书正文", "submitted", "revision_required", "2026-06-23 10:20:00", "项目空间");
        insertLog("log-003", "李同学", "student", "上传文件", "submission",
                "系统截图", "preparing", "submitted", "2026-06-23 09:48:00", "材料流转中心");
        insertLog("log-004", "系统", "admin", "审计提醒", "notification",
                "数据库 ER 图", "normal", "high_risk", "2026-06-23 09:30:00", "系统通知");
        insertLog("log-005", "张老师", "teacher", "提交反馈", "feedback",
                "源码压缩包", "pending", "needs_runbook", "2026-06-23 09:36:00", "教师反馈");
        insertLog("log-006", "王老师", "teacher", "标记通过", "feedback",
                "论文二稿", "pending", "approved", "2026-06-22 16:12:00", "项目空间");
    }

    private void insertUser(String id, String name, String role, String department, String avatar) {
        var item = new UserEntity();
        item.id = id;
        item.name = name;
        item.role = role;
        item.department = department;
        item.avatar = avatar;
        item.password = "{noop}123456";
        users.insert(item);
    }

    private void insertCourse(String id, String name, String teacher, String assistant, Integer count) {
        var item = new CourseEntity();
        item.id = id;
        item.name = name;
        item.teacher = teacher;
        item.assistant = assistant;
        item.semester = "2026 春季学期";
        item.studentsCount = count;
        courses.insert(item);
    }

    private void insertProject(String id, String courseId, String title, String description, String owner,
                               String membersJson, String deadline, String phase, Integer progress,
                               Integer teamProgress, String riskLevel, Integer healthScore, String riskReason,
                               String recentFeedback) {
        var item = new ProjectEntity();
        item.id = id;
        item.courseId = courseId;
        item.title = title;
        item.description = description;
        item.owner = owner;
        item.membersJson = membersJson;
        item.deadline = deadline;
        item.phase = phase;
        item.progress = progress;
        item.teamProgress = teamProgress;
        item.riskLevel = riskLevel;
        item.healthScore = healthScore;
        item.riskReason = riskReason;
        item.recentFeedback = recentFeedback;
        projects.insert(item);
    }

    private void insertMember(String id, String projectId, String name, String role, String avatar,
                              String responsibility, Integer progress, Boolean risk) {
        var item = new TeamMemberEntity();
        item.id = id;
        item.projectId = projectId;
        item.name = name;
        item.role = role;
        item.avatar = avatar;
        item.responsibility = responsibility;
        item.progress = progress;
        item.hasOverdueRisk = risk;
        members.insert(item);
    }

    private void insertMaterial(String id, String projectId, String title, String description, String format,
                                String deadline, String status, Integer progress, String assignee,
                                Integer feedbackCount, String latestFile, String riskHint) {
        var item = new MaterialEntity();
        item.id = id;
        item.projectId = projectId;
        item.title = title;
        item.description = description;
        item.required = true;
        item.format = format;
        item.deadline = deadline;
        item.status = status;
        item.progress = progress;
        item.assignee = assignee;
        item.feedbackCount = feedbackCount;
        item.latestFile = latestFile;
        item.riskHint = riskHint;
        materials.insert(item);
    }

    private void insertSubmission(String id, String materialId, String fileName, Long fileSize,
                                  String submittedBy, String submittedAt, String status, String note) {
        var item = new SubmissionEntity();
        item.id = id;
        item.materialId = materialId;
        item.fileName = fileName;
        item.fileUrl = "#";
        item.fileSize = fileSize;
        item.submittedBy = submittedBy;
        item.submittedAt = submittedAt;
        item.status = status;
        item.feedback = note;
        submissions.insert(item);
    }

    private void insertFeedback(String id, String materialId, String projectId, String teacherName,
                                String content, String status, String createdAt) {
        var item = new TeacherFeedbackEntity();
        item.id = id;
        item.materialId = materialId;
        item.projectId = projectId;
        item.teacherName = teacherName;
        item.content = content;
        item.status = status;
        item.createdAt = createdAt;
        feedback.insert(item);
    }

    private void insertHistory(String id, String materialId, String actor, String action, String fromStatus,
                               String toStatus, String fileName, String createdAt) {
        var item = new MaterialHistoryEntity();
        item.id = id;
        item.materialId = materialId;
        item.actor = actor;
        item.action = action;
        item.fromStatus = fromStatus;
        item.toStatus = toStatus;
        item.fileName = fileName;
        item.createdAt = createdAt;
        histories.insert(item);
    }

    private void insertNotification(String id, String userId, String title, String description, String type,
                                    String targetId, Boolean read, String createdAt) {
        var item = new NotificationEntity();
        item.id = id;
        item.userId = userId;
        item.title = title;
        item.description = description;
        item.type = type;
        item.relatedTargetId = targetId;
        item.readFlag = read;
        item.createdAt = createdAt;
        notifications.insert(item);
    }

    private void insertLog(String id, String actor, String actorRole, String action, String targetType,
                           String targetName, String before, String after, String createdAt, String source) {
        var item = new OperationLogDocument();
        item.id = id;
        item.actor = actor;
        item.actorRole = actorRole;
        item.action = action;
        item.targetType = targetType;
        item.targetName = targetName;
        item.before = before;
        item.after = after;
        item.createdAt = createdAt;
        item.source = source;
        logs.save(item);
    }
}
