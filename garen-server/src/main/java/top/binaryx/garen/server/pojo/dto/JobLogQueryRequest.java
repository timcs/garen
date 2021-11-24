package top.binaryx.garen.server.pojo.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDateTime;


@Data
public class JobLogQueryRequest {

    private String jobName;
    private String jobNo;

    private Long jobLogId;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private Integer status;

    private Long pageNum = 0L;
    private Long pageSize = 10L;
}
