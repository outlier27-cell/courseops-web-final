package cn.edu.uibe.courseops.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("cn.edu.uibe.courseops.mapper")
public class MyBatisPlusConfig {
}
