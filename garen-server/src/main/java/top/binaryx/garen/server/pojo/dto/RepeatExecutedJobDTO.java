package top.binaryx.garen.server.pojo.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * 重复执行任务DTO
 */
@Data
public class RepeatExecutedJobDTO {
    private Long jobId;
    private String jobName;
    private String cron;
    private String contacts;
    private LocalDateTime startTime;
    private LocalDateTime nextTime;
}
