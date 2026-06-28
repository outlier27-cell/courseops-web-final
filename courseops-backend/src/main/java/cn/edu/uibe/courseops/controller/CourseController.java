package cn.edu.uibe.courseops.controller;

import cn.edu.uibe.courseops.common.Result;
import cn.edu.uibe.courseops.mapper.CourseMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/courses")
public class CourseController {
    private final CourseMapper courseMapper;

    public CourseController(CourseMapper courseMapper) {
        this.courseMapper = courseMapper;
    }

    @GetMapping
    public Result<Object> list() {
        return Result.ok(courseMapper.selectList(null));
    }

    @GetMapping("/{id}")
    public Result<Object> detail(@PathVariable String id) {
        return Result.ok(courseMapper.selectById(id));
    }
}
