package top.binaryx.garen.server.executor;

import lombok.extern.slf4j.Slf4j;
import top.binaryx.garen.common.Constant;
import top.binaryx.garen.common.enums.ExecuteStatusEnum;
import top.binaryx.garen.server.common.IdWorker;
import top.binaryx.garen.server.pojo.dto.JobConfigDTO;
import top.binaryx.garen.server.pojo.dto.JobExecuteLogDTO;
import top.binaryx.garen.server.util.CronUtil;

import java.time.LocalDateTime;


@Slf4j
public abstract class AbstractJobExecutor implements JobExecutor {

    protected JobExecuteLogDTO buildLog(JobConfigDTO configDTO) throws Exception {
        JobExecuteLogDTO dto = new JobExecuteLogDTO();
        dto.setExecuteNo(IdWorker.getExecuteNo());
        dto.setJobId(configDTO.getId());
        dto.setTargetAddress(configDTO.getTargetAddress());
        dto.setStartTime(LocalDateTime.now());
        dto.setNextTime(CronUtil.getNextFireTime(configDTO.getCron()));
        dto.setStatus(ExecuteStatusEnum.RUNNING.getValue());
        dto.setCreator(Constant.SYSTEM);
        dto.setModifier(Constant.SYSTEM);

        return dto;
    }
}
