package top.binaryx.garen.server.pojo.vo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import top.binaryx.garen.server.common.BaseTableAttr;


@Data
@TableName("job_config")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class JobConfigVO extends BaseTableAttr {


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
}
