package cn.edu.uibe.courseops.dto;

import cn.edu.uibe.courseops.entity.UserEntity;

public record LoginResponse(String token, UserEntity user) {
}
