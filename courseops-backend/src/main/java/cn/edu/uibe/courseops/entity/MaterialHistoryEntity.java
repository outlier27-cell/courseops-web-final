package cn.edu.uibe.courseops.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName("material_histories")
public class MaterialHistoryEntity {
    public String id;
    @TableField("material_id")
    public String materialId;
    public String actor;
    public String action;
    @TableField("from_status")
    public String fromStatus;
    @TableField("to_status")
    public String toStatus;
    @TableField("file_name")
    public String fileName;
    @TableField("created_at")
    public String createdAt;
}
