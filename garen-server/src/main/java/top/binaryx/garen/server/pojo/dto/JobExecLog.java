package top.binaryx.garen.server.pojo.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import top.binaryx.garen.server.common.BaseTableAttr;

import java.time.LocalDateTime;


@Data
@EqualsAndHashCode(callSuper = true)
public class JobExecLog extends BaseTableAttr {
    /**
     * 主键
     */
    private Long id;

    /**
     * 执行编号
     */
    private Long executeNo;

    /**
     * 任务id
     */
    private Long jobId;

    /**
     * 执行路径
     */
    private String requestUrl;

    /**
     * 开始执行时间
     */
    private LocalDateTime startTime;

    /**
     * 结束执行时间
     */
    private LocalDateTime endTime;

    /**
     * 下次执行时间
     */
    private LocalDateTime nextTime;

    /**
     * 响应码
     */
    private String respCode;

    /**
     * 响应消息
     */
    private String respMsg;

    /**
     * 执行状态
     */
    private Integer status;

    /**
     * 备注,描述
     */
    private String memo;
}
