package cn.edu.uibe.courseops.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import cn.edu.uibe.courseops.common.Result;
import cn.edu.uibe.courseops.entity.SubmissionEntity;
import cn.edu.uibe.courseops.mapper.SubmissionMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/submissions")
public class SubmissionController {
    private final SubmissionMapper submissionMapper;

    public SubmissionController(SubmissionMapper submissionMapper) {
        this.submissionMapper = submissionMapper;
    }

    @GetMapping
    public Result<Object> list() {
        return Result.ok(submissionMapper.selectList(null));
    }

    @PostMapping
    public Result<Object> create(@RequestBody SubmissionEntity submission) {
        submissionMapper.insert(submission);
        return Result.ok(submission);
    }

    @GetMapping("/material/{materialId}")
    public Result<Object> byMaterial(@PathVariable String materialId) {
        return Result.ok(submissionMapper.selectList(new QueryWrapper<SubmissionEntity>().eq("material_id", materialId)));
    }
}
