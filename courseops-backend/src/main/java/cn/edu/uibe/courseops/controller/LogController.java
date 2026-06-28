package cn.edu.uibe.courseops.controller;

import cn.edu.uibe.courseops.common.Result;
import cn.edu.uibe.courseops.mongo.OperationLogDocument;
import cn.edu.uibe.courseops.service.OperationLogService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/logs")
public class LogController {
    private final OperationLogService logService;

    public LogController(OperationLogService logService) {
        this.logService = logService;
    }

    @GetMapping
    public Result<Object> list() {
        return Result.ok(logService.list());
    }

    @PostMapping
    public Result<Object> append(@RequestBody OperationLogDocument log) {
        return Result.ok(logService.append(log));
    }
}
