package cn.edu.uibe.courseops.dto;

public record UploadResponse(
        String materialId,
        String fileName,
        long fileSize,
        String fileType,
        String submittedAt,
        String submittedBy,
        String status,
        String message
) {
}
