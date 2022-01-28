package top.binaryx.garen.server.pojo.dto;

import lombok.Data;

import java.time.LocalDateTime;


@Data
public class RepeatExecutedJobRequest {
    private String recipients;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
