package top.binaryx.garen.server.executor;

import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.binaryx.garen.common.annotation.ExecutorType;
import top.binaryx.garen.common.enums.ExecuteStatusEnum;
import top.binaryx.garen.common.enums.MessageEnum;
import top.binaryx.garen.common.enums.ProtocolTypeEnum;
import top.binaryx.garen.server.common.JobExecuteContext;
import top.binaryx.garen.server.pojo.dto.HttpJobRequest;
import top.binaryx.garen.server.pojo.dto.HttpJobResponse;
import top.binaryx.garen.server.pojo.dto.JobConfigDTO;
import top.binaryx.garen.server.pojo.dto.JobExecuteLogDTO;
import top.binaryx.garen.server.service.JobLogService;

import java.time.LocalDateTime;
import java.util.Objects;


@Slf4j
@ExecutorType(protocolType = ProtocolTypeEnum.HTTP_SYNC)
@Component
public class HttpSyncJobExecutor extends AbstractJobExecutor implements JobExecutor {

    @Autowired
    JobLogService jobLogService;

    @Override
    public void execute(JobConfigDTO jobConfigDTO) {
        log.info("[HttpAsyncJobExecutor.execute] jobConfig:{}", jobConfigDTO);
        JobExecuteContext context = new JobExecuteContext();

        try {
            JobExecuteLogDTO logDTO = buildLog(jobConfigDTO);
            //1.保存数据
            jobLogService.save(logDTO);

            //设置上下文
            context.setExecuteLog(logDTO);
            context.setJobConfig(jobConfigDTO);

            execute(context);
        } catch (Exception e) {
            if (!Objects.isNull(context.getExecuteLog())) {
                JobExecuteLogDTO logDTO = context.getExecuteLog();
                logDTO.setMemo(StringUtils.abbreviate(e.getMessage(), 512));
                logDTO.setStatus(ExecuteStatusEnum.FAILED.getValue());
            }
        } finally {
            if (!Objects.isNull(context.getExecuteLog())) {
                jobLogService.update(context.getExecuteLog());
            }
        }
    }

    private void execute(JobExecuteContext context) {
        HttpJobRequest request = new HttpJobRequest();
        request.setExecuteId(context.getExecuteLog().getId());
        request.setJobId(context.getJobConfig().getId());
        request.setJobParam(context.getJobConfig().getJobParam());

        //请求服务
        String url = context.getJobConfig().getTargetAddress();
        String body = new Gson().toJson(request);
        HttpResponse httpResponse = HttpUtil.createPost(url).body(body).execute();

        //解析响应
        String responseBody = httpResponse.body();
        HttpJobResponse response = new Gson().fromJson(responseBody, HttpJobResponse.class);

        JobExecuteLogDTO updateLog = new JobExecuteLogDTO();
        updateLog.setId(context.getExecuteLog().getId());
        updateLog.setEndTime(LocalDateTime.now());
        updateLog.setRequestBody(body);
        updateLog.setResponseBody(responseBody);
        updateLog.setMemo(Objects.isNull(response.getData()) ? null : new Gson().toJson(response.getData()));

        if (MessageEnum.isSuccess(response.getCode())) {
            //响应成功
            updateLog.setStatus(ExecuteStatusEnum.SUCCESS.getValue());
        } else {
            updateLog.setStatus(ExecuteStatusEnum.FAILED.getValue());
        }
        context.setExecuteLog(updateLog);
    }
}
