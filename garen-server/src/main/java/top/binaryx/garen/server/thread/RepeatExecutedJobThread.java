package top.binaryx.garen.server.thread;

import lombok.extern.slf4j.Slf4j;
import top.binaryx.garen.common.enums.MessageEnum;
import top.binaryx.garen.server.component.SpringContextHolder;
import top.binaryx.garen.server.pojo.dto.HttpCallbackRequest;
import top.binaryx.garen.server.pojo.dto.HttpJobRequest;
import top.binaryx.garen.server.pojo.dto.RepeatExecutedJobDTO;
import top.binaryx.garen.server.pojo.dto.RepeatExecutedJobRequest;
import top.binaryx.garen.server.service.JobLogService;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <>
 *
 * @author hongtian.wei
 * @date 2022-01-25 11:39
 * @since
 */
@Slf4j
public class RepeatExecutedJobThread extends BaseThread implements Runnable {

    JobLogService jobLogService = SpringContextHolder.getBean(JobLogService.class);

    HttpJobRequest<RepeatExecutedJobRequest> request;

    public RepeatExecutedJobThread(HttpJobRequest<RepeatExecutedJobRequest> request) {
        this.request = request;
    }

    @Override
    public void run() {
        LocalDateTime startTime = request.getData().getStartTime();
        LocalDateTime endTime = request.getData().getEndTime();
        List<RepeatExecutedJobDTO> repeatScheduleJobs = jobLogService.selectRepeatExecutedJobs(startTime, endTime);

        HttpCallbackRequest callbackRequest = new HttpCallbackRequest();
        callbackRequest.setJobId(request.getJobId());
        callbackRequest.setExecuteId(request.getExecuteId());
        callbackRequest.setCode(MessageEnum.SUCCESS.getCode());
        callbackRequest.setCode("详见告警邮件");
        callbackRequest.setData(String.valueOf(repeatScheduleJobs.size()));

        callback(request.getCallbackUrl(), callbackRequest);

        //todo sendNotify
    }
}
