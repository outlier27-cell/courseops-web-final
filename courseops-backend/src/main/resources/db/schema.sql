SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;

CREATE TABLE IF NOT EXISTS users (
  id VARCHAR(64) PRIMARY KEY,
  name VARCHAR(64) NOT NULL,
  role VARCHAR(32) NOT NULL,
  department VARCHAR(128) NOT NULL,
  avatar VARCHAR(16) NOT NULL,
  password VARCHAR(128) NOT NULL DEFAULT '{noop}123456'
) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS courses (
  id VARCHAR(64) PRIMARY KEY,
  name VARCHAR(128) NOT NULL,
  teacher VARCHAR(64) NOT NULL,
  assistant VARCHAR(64) NOT NULL,
  semester VARCHAR(64) NOT NULL,
  students_count INT NOT NULL
) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS projects (
  id VARCHAR(64) PRIMARY KEY,
  course_id VARCHAR(64) NOT NULL,
  title VARCHAR(128) NOT NULL,
  description TEXT NOT NULL,
  owner VARCHAR(64) NOT NULL,
  members_json TEXT NOT NULL,
  deadline VARCHAR(32) NOT NULL,
  phase VARCHAR(64) NOT NULL,
  progress INT NOT NULL,
  team_progress INT NOT NULL,
  risk_level VARCHAR(32) NOT NULL,
  health_score INT NOT NULL,
  risk_reason TEXT NOT NULL,
  recent_feedback TEXT NOT NULL
) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS project_members (
  id VARCHAR(64) PRIMARY KEY,
  project_id VARCHAR(64) NOT NULL,
  name VARCHAR(64) NOT NULL,
  role VARCHAR(32) NOT NULL,
  avatar VARCHAR(16) NOT NULL,
  responsibility VARCHAR(255) NOT NULL,
  progress INT NOT NULL,
  has_overdue_risk TINYINT(1) NOT NULL
) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS materials (
  id VARCHAR(64) PRIMARY KEY,
  project_id VARCHAR(64) NOT NULL,
  title VARCHAR(128) NOT NULL,
  description TEXT NOT NULL,
  required TINYINT(1) NOT NULL,
  format VARCHAR(16) NOT NULL,
  deadline VARCHAR(32) NOT NULL,
  status VARCHAR(32) NOT NULL,
  progress INT NOT NULL,
  assignee VARCHAR(64) NOT NULL,
  feedback_count INT NOT NULL,
  latest_file VARCHAR(255) NOT NULL DEFAULT '',
  risk_hint VARCHAR(255) NOT NULL
) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS submissions (
  id VARCHAR(64) PRIMARY KEY,
  material_id VARCHAR(64) NOT NULL,
  file_name VARCHAR(255) NOT NULL,
  file_url VARCHAR(255) NOT NULL,
  file_size BIGINT NOT NULL,
  submitted_by VARCHAR(64) NOT NULL,
  submitted_at VARCHAR(64) NOT NULL,
  status VARCHAR(32) NOT NULL,
  feedback TEXT NOT NULL
) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS teacher_feedback (
  id VARCHAR(64) PRIMARY KEY,
  material_id VARCHAR(64) NOT NULL,
  project_id VARCHAR(64) NOT NULL,
  teacher_name VARCHAR(64) NOT NULL,
  content TEXT NOT NULL,
  status VARCHAR(32) NOT NULL,
  created_at VARCHAR(64) NOT NULL
) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS material_histories (
  id VARCHAR(64) PRIMARY KEY,
  material_id VARCHAR(64) NOT NULL,
  actor VARCHAR(64) NOT NULL,
  action VARCHAR(64) NOT NULL,
  from_status VARCHAR(32),
  to_status VARCHAR(32),
  file_name VARCHAR(255),
  created_at VARCHAR(64) NOT NULL
) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS notifications (
  id VARCHAR(64) PRIMARY KEY,
  user_id VARCHAR(64) NOT NULL,
  title VARCHAR(128) NOT NULL,
  description TEXT NOT NULL,
  type VARCHAR(32) NOT NULL,
  related_target_id VARCHAR(64),
  read_flag TINYINT(1) NOT NULL,
  created_at VARCHAR(64) NOT NULL
) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

INSERT IGNORE INTO users (id, name, role, department, avatar, password) VALUES
('u-student-001', '张同学', 'student', '信息学院', 'Z', '{noop}123456'),
('u-teacher-001', '张老师', 'teacher', '信息学院', 'T', '{noop}123456'),
('u-admin-001', '教务管理员', 'admin', '教务处', 'A', '{noop}123456');

INSERT IGNORE INTO courses VALUES
('c-web', 'Web 应用开发', '张老师', '陈助教', '2026 春季学期', 86),
('c-db', '数据库原理', '李老师', '周助教', '2026 春季学期', 72),
('c-writing', '学术写作', '王老师', '刘助教', '2026 春季学期', 54);

INSERT IGNORE INTO projects VALUES
('p-web-final', 'c-web', 'Web 期末项目', '完成一个具备项目管理、材料流转和操作审计能力的课程项目平台。', '张同学', '["张同学","李同学","王同学"]', '2026-06-25', '材料完善', 72, 68, 'medium', 76, '说明书正文处于需修改状态，源码压缩包尚未提交。', '说明书正文需要补充数据库设计说明。'),
('p-db-er', 'c-db', '数据库实验报告', '提交 ER 图、表结构说明和实验报告 PDF。', '张同学', '["张同学"]', '2026-06-24', '待补交', 46, 46, 'high', 58, 'ER 图未提交，距离截止不足 24 小时。', 'ER 图缺少关系说明，请今天补齐。'),
('p-writing', 'c-writing', '课程论文阶段稿', '完成二稿修改、引用检查和最终提交记录。', '李同学', '["李同学","张同学"]', '2026-06-28', '教师反馈处理', 83, 79, 'low', 88, '当前无明显风险。', '引用格式已基本通过，等待最终确认。');

INSERT IGNORE INTO project_members VALUES
('tm-1', 'p-web-final', '张同学', 'leader', 'Z', '前端与说明书正文', 74, 0),
('tm-2', 'p-web-final', '李同学', 'member', 'L', '系统截图与测试记录', 90, 0),
('tm-3', 'p-web-final', '王同学', 'member', 'W', '数据库设计图', 42, 1),
('tm-4', 'p-db-er', '张同学', 'leader', 'Z', 'ER 图与实验报告', 46, 1),
('tm-5', 'p-writing', '李同学', 'leader', 'L', '论文正文', 86, 0);

INSERT IGNORE INTO materials VALUES
('m-report', 'p-web-final', '说明书正文', '包含系统简介、功能说明、截图、数据库设计和技术架构。', 1, 'DOCX', '2026-06-25', 'revision_required', 70, '张同学', 2, 'courseops-manual-v2.docx', '教师反馈未处理'),
('m-screenshots', 'p-web-final', '系统截图', '登录页、工作台、项目中心、材料看板、数据洞察截图。', 1, 'ZIP', '2026-06-25', 'submitted', 90, '李同学', 0, 'courseops-screenshots.zip', '等待教师确认'),
('m-source', 'p-web-final', '源代码压缩包', '包含前端、后端、数据库脚本和运行说明。', 1, 'ZIP', '2026-06-26', 'preparing', 52, '张同学', 1, '', '尚未上传文件'),
('m-er', 'p-db-er', '数据库 ER 图', '展示实体、关系、主键、外键和约束。', 1, 'PDF', '2026-06-24', 'not_started', 20, '张同学', 1, '', '距离截止不足 24 小时'),
('m-paper', 'p-writing', '论文二稿', '根据教师反馈修改正文、引用和摘要。', 1, 'DOCX', '2026-06-28', 'approved', 100, '李同学', 0, 'academic-writing-v3.docx', '已通过');

INSERT IGNORE INTO submissions VALUES
('s-report-v2', 'm-report', 'courseops-manual-v2.docx', '#', 2430000, '张同学', '2026-06-23 10:12', 'revision_required', '请补充项目空间与材料流转说明。'),
('s-screenshots', 'm-screenshots', 'courseops-screenshots.zip', '#', 8210000, '李同学', '2026-06-23 09:48', 'submitted', '等待教师确认。');

INSERT IGNORE INTO teacher_feedback VALUES
('fb-1', 'm-report', 'p-web-final', '张老师', '说明书需要补充项目空间与材料流转的操作截图。', 'unresolved', '2026-06-23 10:20'),
('fb-2', 'm-source', 'p-web-final', '张老师', '源码压缩包需要包含运行说明和数据库脚本。', 'unresolved', '2026-06-23 09:36'),
('fb-3', 'm-paper', 'p-writing', '王老师', '引用格式已通过，可以准备最终提交。', 'resolved', '2026-06-22 16:12');

INSERT IGNORE INTO material_histories VALUES
('mh-1', 'm-report', '张同学', '上传文件', 'preparing', 'submitted', 'courseops-manual-v2.docx', '2026-06-23 10:12'),
('mh-2', 'm-report', '张老师', '标记需修改', 'submitted', 'revision_required', NULL, '2026-06-23 10:20'),
('mh-3', 'm-screenshots', '李同学', '上传文件', 'preparing', 'submitted', 'courseops-screenshots.zip', '2026-06-23 09:48');

INSERT IGNORE INTO notifications VALUES
('n-1', 'u-student-001', '说明书正文需要修改', '张老师反馈：请补充项目空间与材料流转说明。', 'feedback', 'm-report', 0, '2026-06-23 10:20'),
('n-2', 'u-student-001', '数据库 ER 图即将截止', '距离截止不足 24 小时，建议今天完成上传。', 'deadline', 'm-er', 0, '2026-06-23 09:30'),
('n-3', 'u-teacher-001', '有 2 份材料待审核', 'Web 期末项目新增材料提交，请及时检查。', 'submission', 'p-web-final', 0, '2026-06-23 10:30');
