package top.binaryx.garen.server.thread;

import cn.hutool.core.util.StrUtil;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import top.binaryx.garen.common.enums.JobStatusEnum;
import top.binaryx.garen.common.enums.MessageEnum;
import top.binaryx.garen.server.component.RetryQueen;
import top.binaryx.garen.server.component.SpringContextHolder;
import top.binaryx.garen.server.helper.NodePathHelper;
import top.binaryx.garen.server.pojo.dto.*;
import top.binaryx.garen.server.service.JobLogService;
import top.binaryx.garen.server.service.ZookeeperService;
import top.binaryx.garen.server.util.CronUtil;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <>
 *
 * @author hongtian.wei
 * @date 2022-01-24 18:44
 * @since
 */
@Slf4j
public class NotExecutedJobThread extends BaseThread implements Runnable {

    JobLogService jobLogService = SpringContextHolder.getBean(JobLogService.class);
    ZookeeperService zookeeperService = SpringContextHolder.getBean(ZookeeperService.class);
    HttpJobRequest<NotExecutedJobRequest> request;

    public NotExecutedJobThread(HttpJobRequest<NotExecutedJobRequest> request) {
        this.request = request;
    }

    @Override
    public void run() {
        NotExecutedJobResponse response = new NotExecutedJobResponse();

        HttpCallbackRequest callbackRequest = new HttpCallbackRequest();
        callbackRequest.setJobId(request.getJobId());
        callbackRequest.setExecuteId(request.getExecuteId());
        callbackRequest.setCode(MessageEnum.SUCCESS.getCode());
        callbackRequest.setCode(MessageEnum.SUCCESS.getDesc());

        try {
            Integer count = checkNotExecuteJob(request.getData());
            response.setCount(count);
            callbackRequest.setData(new Gson().toJson(response));
        } catch (Exception e) {
            callbackRequest.setCode(MessageEnum.FAIL.getCode());
            callbackRequest.setCode(e.getMessage());
            log.error("[checkNotExecute] failed.", e);
        }

        callback(request.getCallbackUrl(), callbackRequest);
    }

    public Integer checkNotExecuteJob(NotExecutedJobRequest request) {
        String path = NodePathHelper.getAdminConfigPath("not_execute_job/start_time");
        setTime(request, path);

        LocalDateTime startTime = request.getStartTime();
        LocalDateTime endTime = request.getEndTime();

        //获取数据
        List<NotExecutedJobDTO> notExecuteJobs = jobLogService.selectNotExecutedJobs(startTime, endTime);

        log.info("[checkNotExecuteJob] startTime:{}, endTime:{}, count:{}", startTime, endTime, notExecuteJobs.size());

        if (CollectionUtils.isEmpty(notExecuteJobs)) {
            //没有未执行的任务
            zookeeperService.persistOrUpdate(path, endTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            return 0;
        }

        //分组
        Map<String, NotExecutedJobDTO> notExecuteJobMap = group(notExecuteJobs);

        //重试
        sendRetryQueen(notExecuteJobMap);

        //更新zk节点
        zookeeperService.persistOrUpdate(path, endTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

        return notExecuteJobMap.size();
    }

    private void setTime(NotExecutedJobRequest request, String path) {
        LocalDateTime startTime, endTime;

        //结束时间.当前系统时间往前推一个小时
        LocalDateTime now = LocalDateTime.now();
        now.plusHours(-1);
        endTime = now;
        request.setEndTime(endTime);

        //开始时间
        String startTimeStr = zookeeperService.get(path);
        if (StrUtil.isBlank(startTimeStr)) {
            now.plusHours(-1);
            startTime = now;
        } else {
            startTime = LocalDateTime.parse(startTimeStr);
        }
        request.setStartTime(startTime);
    }

    private Map<String, NotExecutedJobDTO> group(List<NotExecutedJobDTO> notExecuteJobs) {
        //根据nextTime过滤同一个任务的数据
        return notExecuteJobs.stream()
                //根据项目名和任务名分组
                .collect(Collectors.groupingBy(o -> o.getSarName() + "_" + o.getJobName(),
                        //根据下次执行时间筛选
                        Collectors.collectingAndThen(Collectors.maxBy(Comparator.comparing(NotExecutedJobDTO::getNextExecuteTime)), Optional::get))
                );
    }

    private boolean checkNextFireTime(NotExecutedJobDTO notExecuteJob) {
        LocalDateTime now = LocalDateTime.now();
        now.plusSeconds(-1);

        LocalDateTime nextFireTime;
        try {
            nextFireTime = CronUtil.getNextFireTime(notExecuteJob.getCron(), now);
        } catch (Exception e) {
            log.error("get next fire time error.corn:{}", notExecuteJob.getCron(), e);
            return true;
        }
        if (Objects.isNull(nextFireTime)) {
            //下次执行时间为空,不补跑
            return true;
        }
        return nextFireTime.compareTo(notExecuteJob.getNextExecuteTime()) != 0;
    }

    private void sendRetryQueen(Map<String, NotExecutedJobDTO> notExecuteJobMap) {
        Iterator<Map.Entry<String, NotExecutedJobDTO>> iterator = notExecuteJobMap.entrySet().iterator();
        while (iterator.hasNext()) {
            NotExecutedJobDTO notExecuteJob = iterator.next().getValue();
            //非normal任务不补跑
            if (!JobStatusEnum.isNormal(notExecuteJob.getDisabled().intValue())) {
                iterator.remove();
                continue;
            }
            //监控关闭不补跑
            if (!notExecuteJob.getMonitorExecution()) {
                iterator.remove();
                continue;
            }

            //过滤下次执行时间不一致的情况
            // todo 如果修改了cron表达式把下次执行时间往前推,此情况无法检测
            if (checkNextFireTime(notExecuteJob)) {
                iterator.remove();
                continue;
            }
        }
        RetryQueen.put(iterator);
    }
}
