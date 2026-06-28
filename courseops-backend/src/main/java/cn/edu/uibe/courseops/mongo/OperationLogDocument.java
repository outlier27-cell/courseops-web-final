package cn.edu.uibe.courseops.mongo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("operation_logs")
public class OperationLogDocument {
    @Id
    public String id;
    public String actor;
    public String actorRole;
    public String action;
    public String targetType;
    public String targetName;
    public String before;
    public String after;
    public String createdAt;
    public String source;
}
