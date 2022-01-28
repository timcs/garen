package top.binaryx.garen.server.pojo.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class NotExecutedJobDTO {

    private Long id;

    private String sarName;

    private String jobName;

    private Long executeId;

    private LocalDateTime nextExecuteTime;

    private String cron;

    private String contacts;

    private Byte disabled;

    private Integer protocolType;

    //非数据库字段
    /**
     * zk状态
     */
    private String zkStatus;
    /**
     * 补跑状态 true:已补跑,false:未补跑
     */
    private Boolean retry;

    private Boolean monitorExecution;


}
