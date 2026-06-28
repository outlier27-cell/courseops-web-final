package cn.edu.uibe.courseops.service;

import cn.edu.uibe.courseops.mongo.OperationLogDocument;
import cn.edu.uibe.courseops.mongo.OperationLogRepository;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class OperationLogService {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final OperationLogRepository repository;

    public OperationLogService(OperationLogRepository repository) {
        this.repository = repository;
    }

    public OperationLogDocument write(String actor, String actorRole, String action, String targetType, String targetName, String before, String after, String source) {
        var log = new OperationLogDocument();
        log.id = "mongo-" + System.currentTimeMillis();
        log.actor = actor;
        log.actorRole = actorRole;
        log.action = action;
        log.targetType = targetType;
        log.targetName = targetName;
        log.before = before;
        log.after = after;
        log.createdAt = LocalDateTime.now().format(FORMATTER);
        log.source = source;
        return repository.save(log);
    }

    public List<OperationLogDocument> list() {
        return repository.findTop100ByOrderByCreatedAtDesc();
    }

    public OperationLogDocument append(OperationLogDocument log) {
        if (log.id == null || log.id.isBlank()) {
            log.id = "mongo-" + System.currentTimeMillis();
        }
        return repository.save(log);
    }
}
