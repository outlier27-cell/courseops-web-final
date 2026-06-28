package cn.edu.uibe.courseops.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName("teacher_feedback")
public class TeacherFeedbackEntity {
    public String id;
    @TableField("material_id")
    public String materialId;
    @TableField("project_id")
    public String projectId;
    @TableField("teacher_name")
    public String teacherName;
    public String content;
    public String status;
    @TableField("created_at")
    public String createdAt;
}
