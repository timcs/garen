package top.binaryx.garen.server.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.binaryx.garen.server.common.BaseTableAttr;

import java.time.LocalDateTime;


@Data
@TableName("job_execute_log")
@EqualsAndHashCode(callSuper = true)
public class JobExecuteLogDO extends BaseTableAttr {

    private Long jobId;
    private Long executeNo;

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LocalDateTime nextTime;

    private String executorIP;
    private String targetAddress;
    private String requestBody;
    private String responseBody;

    private Integer status;
    private String memo;
}
