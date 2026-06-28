package cn.edu.uibe.courseops.mongo;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OperationLogRepository extends MongoRepository<OperationLogDocument, String> {
    List<OperationLogDocument> findTop100ByOrderByCreatedAtDesc();
}
