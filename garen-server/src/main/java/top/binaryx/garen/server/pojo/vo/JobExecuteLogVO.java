package top.binaryx.garen.server.pojo.vo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.binaryx.garen.server.common.BaseTableAttr;

import java.time.LocalDateTime;


@Data
@EqualsAndHashCode(callSuper = true)
public class JobExecuteLogVO extends BaseTableAttr {


    private Long jobId;

    private Long executeNo;
    private String executorIP;


    private LocalDateTime startTime;


    private LocalDateTime endTime;


    private LocalDateTime nextTime;


    private Integer status;


    private String memo;

}
