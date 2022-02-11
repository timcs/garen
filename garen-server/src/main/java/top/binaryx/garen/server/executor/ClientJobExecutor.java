package top.binaryx.garen.server.executor;

import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import top.binaryx.garen.common.annotation.ExecutorType;
import top.binaryx.garen.common.enums.ExecuteStatusEnum;
import top.binaryx.garen.common.enums.ProtocolTypeEnum;
import top.binaryx.garen.server.common.JobExecuteContext;
import top.binaryx.garen.server.pojo.dto.HttpJobRequest;
import top.binaryx.garen.server.pojo.dto.JobConfigDTO;
import top.binaryx.garen.server.pojo.dto.JobExecuteLogDTO;
import top.binaryx.garen.server.service.JobLogService;

import java.util.Objects;


@Slf4j
@ExecutorType(protocolType = ProtocolTypeEnum.BEAN)
@Component
public class ClientJobExecutor extends AbstractJobExecutor implements JobExecutor {

    @Autowired
    JobLogService jobLogService;


    @Override
    public void execute(JobConfigDTO jobConfigDTO) {
        log.info("[ClientJobExecutor.execute] jobConfigDTO:{}", jobConfigDTO);
        JobExecuteContext context = new JobExecuteContext();

        try {
            //1.保存数据
            JobExecuteLogDTO logDTO = buildLog(jobConfigDTO);
            jobLogService.save(logDTO);

            //设置上下文
            context.setExecuteLog(logDTO);
            context.setJobConfig(jobConfigDTO);

            execute(context);
        } catch (Exception e) {
            log.info("execute....", e);

        } finally {

        }
    }

    private void execute(JobExecuteContext context) {

    }
}
