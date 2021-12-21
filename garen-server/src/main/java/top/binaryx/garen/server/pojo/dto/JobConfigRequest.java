package top.binaryx.garen.server.pojo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@ApiModel(value = "任务配置请求对象", description = "任务配置增改对象")
@Data
public class JobConfigRequest {

    @ApiModelProperty(value = "任务id", name = "id", dataType = "Long", example = "1L")
    private Long id;

    @ApiModelProperty(value = "任务名", name = "jobName", example = "user-info-sync")
    private String jobName;

    private Long jobNo;
    private String jobParam;
    private String jobDesc;
    private Integer protocolType;
    private Integer jobType;
    private String cron;
    private String targetAddress;
    private String executorIp;
    public Byte deleted;
    private Integer status;

    private Long pageNum = 0L;
    private Long pageSize = 10L;
}
