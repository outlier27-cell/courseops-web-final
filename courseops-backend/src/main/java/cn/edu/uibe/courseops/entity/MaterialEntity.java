package cn.edu.uibe.courseops.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName("materials")
public class MaterialEntity {
    public String id;
    @TableField("project_id")
    public String projectId;
    public String title;
    public String description;
    public Boolean required;
    public String format;
    public String deadline;
    public String status;
    public Integer progress;
    public String assignee;
    @TableField("feedback_count")
    public Integer feedbackCount;
    @TableField("latest_file")
    public String latestFile;
    @TableField("risk_hint")
    public String riskHint;
}
