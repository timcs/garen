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
@ExecutorType(protocolType = ProtocolTypeEnum.HTTP_ASYNC)
@Component
public class HttpAsyncJobExecutor extends AbstractJobExecutor implements JobExecutor {

    @Autowired
    JobLogService jobLogService;

    @Value("${garen.server.callbackurl}")
    String callBackUrl;

    @Override
    public void execute(JobConfigDTO jobConfigDTO) {
        log.info("[HttpAsyncJobExecutor.execute] jobConfigDTO:{}", jobConfigDTO);
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
            if (!Objects.isNull(context.getExecuteLog())) {
                JobExecuteLogDTO logDTO = context.getExecuteLog();
                logDTO.setMemo(StringUtils.abbreviate(e.getMessage(), 512));
                logDTO.setStatus(ExecuteStatusEnum.FAILED.getValue());
            }
        } finally {
            if (!Objects.isNull(context.getExecuteLog())
                    && context.getExecuteLog().getStatus().intValue() != ExecuteStatusEnum.RUNNING.getValue()) {
                jobLogService.update(context.getExecuteLog());
            }
        }
    }

    private void execute(JobExecuteContext context) {
        HttpJobRequest<String> request = new HttpJobRequest();
        request.setExecuteId(context.getExecuteLog().getId());
        request.setJobId(context.getJobConfig().getId());
        request.setData(context.getJobConfig().getJobParam());
        request.setCallbackUrl(callBackUrl);

        String url = context.getJobConfig().getTargetAddress();
        String requestBody = new Gson().toJson(request);

        JobExecuteLogDTO updateLog = new JobExecuteLogDTO();
        updateLog.setId(context.getExecuteLog().getId());
        updateLog.setRequestBody(requestBody);
        try {
            HttpResponse httpResponse = HttpUtil.createPost(url).body(requestBody).executeAsync();
            if (!httpResponse.isOk()) {
                updateLog.setStatus(ExecuteStatusEnum.FAILED.getValue());
                updateLog.setResponseBody(httpResponse.body());
            }
        } catch (Exception e) {
            updateLog.setStatus(ExecuteStatusEnum.FAILED.getValue());
            updateLog.setResponseBody(StringUtils.abbreviate(e.getMessage(), 512));
        }
        context.setExecuteLog(updateLog);
    }
}
