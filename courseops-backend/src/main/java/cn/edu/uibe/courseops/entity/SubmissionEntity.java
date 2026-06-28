package cn.edu.uibe.courseops.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName("submissions")
public class SubmissionEntity {
    public String id;
    @TableField("material_id")
    public String materialId;
    @TableField("file_name")
    public String fileName;
    @TableField("file_url")
    public String fileUrl;
    @TableField("file_size")
    public Long fileSize;
    @TableField("submitted_by")
    public String submittedBy;
    @TableField("submitted_at")
    public String submittedAt;
    public String status;
    public String feedback;
}
