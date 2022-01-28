package top.binaryx.garen.server.pojo.dto;

import lombok.Data;

import java.time.LocalDateTime;


@Data
public class NotExecutedJobRequest {
    private String recipients;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
