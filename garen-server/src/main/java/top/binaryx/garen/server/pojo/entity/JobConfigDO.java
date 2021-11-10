package top.binaryx.garen.server.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import top.binaryx.garen.server.common.BaseTableAttr;

/**
 * JobConfigEntity
 *
 * @author weihongtian
 * @version v0.1 2019-05-27 17:55 weihongtian Exp $
 */
@Data
@TableName("job_config")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class JobConfigDO extends BaseTableAttr {

    /**
     * 任务名称
     */
    private String jobName;

    private Long jobNo;

    /**
     * 任务参数
     */
    private String jobParam;

    /**
     * 任务描述
     */
    private String jobDesc;

    /**
     * 协议类型
     */
    private Integer protocolType;

    /**
     * 任务类型
     */
    private Integer jobType;

    /**
     * cron表达式
     */
    private String cron;

    /**
     * 执行路径
     */
    private String targetAddress;

    /**
     * 执行路径
     */
    private String executorIp;

    /**
     * 0禁用，1启用，3暂停
     */
    private Integer status;
}
