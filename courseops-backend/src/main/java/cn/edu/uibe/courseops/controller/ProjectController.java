package cn.edu.uibe.courseops.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import cn.edu.uibe.courseops.common.Result;
import cn.edu.uibe.courseops.entity.TeamMemberEntity;
import cn.edu.uibe.courseops.entity.ProjectEntity;
import cn.edu.uibe.courseops.mapper.ProjectMapper;
import cn.edu.uibe.courseops.mapper.TeamMemberMapper;
import cn.edu.uibe.courseops.service.ProjectMapperService;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {
    private final ProjectMapper projectMapper;
    private final TeamMemberMapper memberMapper;
    private final ProjectMapperService projectMapperService;

    public ProjectController(ProjectMapper projectMapper, TeamMemberMapper memberMapper, ProjectMapperService projectMapperService) {
        this.projectMapper = projectMapper;
        this.memberMapper = memberMapper;
        this.projectMapperService = projectMapperService;
    }

    @GetMapping
    public Result<Object> list() {
        return Result.ok(projectMapper.selectList(null).stream().map(projectMapperService::toDto).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public Result<Object> detail(@PathVariable String id) {
        var project = projectMapper.selectById(id);
        return Result.ok(project == null ? null : projectMapperService.toDto(project));
    }

    @PostMapping
    public Result<Object> create(@RequestBody ProjectEntity project) {
        projectMapper.insert(project);
        return Result.ok(project);
    }

    @PutMapping("/{id}")
    public Result<Object> update(@PathVariable String id, @RequestBody ProjectEntity project) {
        project.id = id;
        projectMapper.updateById(project);
        return Result.ok(project);
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable String id) {
        return Result.ok(projectMapper.deleteById(id) > 0);
    }

    @GetMapping("/{id}/members")
    public Result<Object> members(@PathVariable String id) {
        return Result.ok(memberMapper.selectList(new QueryWrapper<TeamMemberEntity>().eq("project_id", id)));
    }
}
