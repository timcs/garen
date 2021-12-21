package top.binaryx.garen.server.executor;

import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import top.binaryx.garen.common.Constant;
import top.binaryx.garen.common.enums.ProtocolTypeEnum;
import top.binaryx.garen.server.component.SpringContextHolder;
import top.binaryx.garen.server.pojo.dto.JobConfigDTO;
import top.binaryx.garen.server.service.JobConfigService;

import java.util.Objects;

@Slf4j
public class SpringJob implements Job {

    @Override
    public void execute(JobExecutionContext context) {
        JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
        Long jobId = jobDataMap.getLong(Constant.JOB_ID);
        try {
            JobConfigDTO jobConfig = SpringContextHolder.getBean(JobConfigService.class).findById(jobId);
            if (Objects.isNull(jobConfig)) {
                return;
            }

            ProtocolTypeEnum protocolType = ProtocolTypeEnum.findByValue(jobConfig.getProtocolType());
            JobExecutor executor = SpringContextHolder.getBean(ExecutorContext.class).getExecutor(protocolType);
            executor.execute(jobConfig);
        } catch (Exception e) {
            log.error("Job execute failed. jobId:{}, ", jobId, e);
        }
    }
}
