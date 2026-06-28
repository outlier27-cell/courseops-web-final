package cn.edu.uibe.courseops.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;

@TableName("notifications")
public class NotificationEntity {
    public String id;
    @TableField("user_id")
    public String userId;
    public String title;
    public String description;
    public String type;
    @TableField("related_target_id")
    public String relatedTargetId;
    @TableField("read_flag")
    @JsonProperty("read")
    public Boolean readFlag;
    @TableField("created_at")
    public String createdAt;
}
