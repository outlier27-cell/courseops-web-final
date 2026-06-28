package cn.edu.uibe.courseops.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName("project_members")
public class TeamMemberEntity {
    public String id;
    @TableField("project_id")
    public String projectId;
    public String name;
    public String role;
    public String avatar;
    public String responsibility;
    public Integer progress;
    @TableField("has_overdue_risk")
    public Boolean hasOverdueRisk;
}
