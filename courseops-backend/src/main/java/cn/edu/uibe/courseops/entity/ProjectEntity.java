package cn.edu.uibe.courseops.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName("projects")
public class ProjectEntity {
    public String id;
    @TableField("course_id")
    public String courseId;
    public String title;
    public String description;
    public String owner;
    @TableField("members_json")
    public String membersJson;
    public String deadline;
    public String phase;
    public Integer progress;
    @TableField("team_progress")
    public Integer teamProgress;
    @TableField("risk_level")
    public String riskLevel;
    @TableField("health_score")
    public Integer healthScore;
    @TableField("risk_reason")
    public String riskReason;
    @TableField("recent_feedback")
    public String recentFeedback;
}
