package cn.edu.uibe.courseops.entity;

import com.baomidou.mybatisplus.annotation.TableName;

@TableName("users")
public class UserEntity {
    public String id;
    public String name;
    public String role;
    public String department;
    public String avatar;
    public String password;
}
