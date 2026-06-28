package cn.edu.uibe.courseops.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName("courses")
public class CourseEntity {
    public String id;
    public String name;
    public String teacher;
    public String assistant;
    public String semester;
    @TableField("students_count")
    public Integer studentsCount;
}
