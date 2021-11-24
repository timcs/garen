package top.binaryx.garen.server.pojo.vo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.binaryx.garen.server.common.BaseTableAttr;

import java.time.LocalDateTime;


@Data
@TableName("job_execute_detail_log")
@EqualsAndHashCode(callSuper = true)
public class JobExecuteDetailLogVO extends BaseTableAttr {


    private Long jobExecuteLogId;


    private String targetAddress;


    private LocalDateTime startTime;


    private LocalDateTime endTime;


    private String requestBody;


    private String responseBody;


    private Integer status;


    private String memo;


}
