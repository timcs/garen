package top.binaryx.garen.server.pojo.dto;

import lombok.Data;

import java.time.LocalDateTime;


@Data
public class JobConfigRequest {

    public Byte deleted;
    private Long id;
    private String jobName;
    private Long jobNo;
    private String jobParam;
    private String jobDesc;
    private Integer protocolType;
    private Integer jobType;
    private String cron;
    private String targetAddress;
    private String executorIp;
    private Integer status;
    private String creator;
    private String modifier;

    private LocalDateTime createTime;
    private LocalDateTime modifiedTime;

    private Long pageNum = 0L;
    private Long pageSize = 10L;
}
