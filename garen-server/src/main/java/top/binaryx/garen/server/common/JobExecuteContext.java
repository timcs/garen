package top.binaryx.garen.server.common;

import lombok.Data;
import top.binaryx.garen.server.pojo.dto.JobConfigDTO;
import top.binaryx.garen.server.pojo.dto.JobExecuteLogDTO;


@Data
public class JobExecuteContext {

    private JobConfigDTO jobConfig;

    private JobExecuteLogDTO executeLog;

}
