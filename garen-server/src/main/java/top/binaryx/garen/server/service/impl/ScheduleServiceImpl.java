package top.binaryx.garen.server.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import top.binaryx.garen.common.Constant;
import top.binaryx.garen.server.executor.SpringJob;
import top.binaryx.garen.server.pojo.dto.JobConfigDTO;
import top.binaryx.garen.server.service.ScheduleService;

import java.util.Objects;

@Slf4j
@Service
public class ScheduleServiceImpl implements InitializingBean, ScheduleService {

    private Scheduler scheduler;

    @Override
    public void afterPropertiesSet() throws Exception {
        StdSchedulerFactory stdSchedulerFactory = new StdSchedulerFactory();
        stdSchedulerFactory.initialize();
        scheduler = stdSchedulerFactory.getScheduler();
        scheduler.start();
    }

    @Override
    public Scheduler getScheduler() {
        return scheduler;
    }

    @Override
    public boolean isScheduled(String groupName, String jobName) throws SchedulerException {
        Scheduler scheduler = getScheduler();
        if (!Objects.isNull(scheduler)) {
            return scheduler.checkExists(JobKey.jobKey(jobName, groupName));
        }
        return false;
    }

    @Override
    public void startJob(JobConfigDTO dto) throws SchedulerException {
        String id = dto.getId().toString();
        String cron = dto.getCron();

        Scheduler scheduler = getScheduler();
        JobKey jobKey = JobKey.jobKey(id);
        TriggerKey triggerKey = TriggerKey.triggerKey(id);

        JobDetail jobDetail;
        if (scheduler.checkExists(jobKey) && scheduler.checkExists(triggerKey)) {
            jobDetail = scheduler.getJobDetail(jobKey);
            scheduler.pauseTrigger(triggerKey);
            scheduler.unscheduleJob(triggerKey);
            scheduler.deleteJob(jobKey);
        } else {
            JobDataMap jobDataMap = new JobDataMap();
            jobDataMap.put(Constant.JOB_ID, dto.getId());
            jobDetail = JobBuilder.newJob(SpringJob.class)
                    .withIdentity(jobKey)
                    .usingJobData(jobDataMap)
                    .build();
        }

        CronTrigger cronTrigger = TriggerBuilder.newTrigger()
                .withIdentity(triggerKey)
                .withSchedule(CronScheduleBuilder.cronSchedule(cron))
                .build();

        // schedule job
        scheduler.scheduleJob(jobDetail, cronTrigger);
        log.info("Schedule job success: JobConfigDTO:{}", dto);
    }

    @Override
    public void stopJob(JobConfigDTO dto) throws SchedulerException {
        Long jobId = dto.getId();
        Scheduler scheduler = getScheduler();
        TriggerKey triggerKey = TriggerKey.triggerKey(jobId.toString());
        JobKey jobKey = JobKey.jobKey(jobId.toString());
        scheduler.unscheduleJob(triggerKey);
        scheduler.deleteJob(jobKey);
        log.info("Stop job success, JobConfigDTO:{}", dto);
    }

    @Override
    public void pauseJob(JobConfigDTO jobConfigDTO) throws SchedulerException {
        Scheduler scheduler = getScheduler();
        String id = jobConfigDTO.getId().toString();

        JobKey jobKey = JobKey.jobKey(id);
        TriggerKey triggerKey = TriggerKey.triggerKey(id);

        if (scheduler.checkExists(jobKey) || scheduler.checkExists(triggerKey)) {
            return;
        }

        scheduler.pauseTrigger(triggerKey);
        scheduler.pauseJob(jobKey);
        log.info("Pause job success, jobConfigDTO{}", jobConfigDTO);
    }

    @Override
    public void resumeJob(JobConfigDTO jobConfigDTO) throws SchedulerException {
        Scheduler scheduler = getScheduler();
        String id = jobConfigDTO.getId().toString();

        JobKey jobKey = JobKey.jobKey(id);
        TriggerKey triggerKey = TriggerKey.triggerKey(id);

        if (scheduler.checkExists(jobKey) || scheduler.checkExists(triggerKey)) {
            return;
        }

        if (scheduler.getTriggerState(triggerKey) == Trigger.TriggerState.PAUSED) {
            // 任务处于暂停状态
            scheduler.resumeJob(jobKey);
            scheduler.resumeTrigger(triggerKey);
            log.info("Resume job success, jobConfigDTO:{}", jobConfigDTO);
        }
    }

    @Override
    public void triggerJob(JobConfigDTO jobConfigDTO) throws SchedulerException {
        Scheduler scheduler = getScheduler();
        String id = jobConfigDTO.getId().toString();

        JobKey jobKey = JobKey.jobKey(id);
        TriggerKey triggerKey = TriggerKey.triggerKey(id);

        if (scheduler.checkExists(jobKey) || scheduler.checkExists(triggerKey)) {
            return;
        }

        if (scheduler.getTriggerState(triggerKey) == Trigger.TriggerState.NORMAL) {
            scheduler.triggerJob(jobKey);
            log.info("Trigger job success, jobConfigDTO:{}", jobConfigDTO);
        }
    }

    @Override
    public void shutDownScheduler(Scheduler scheduler) {
        try {
            if (!scheduler.isShutdown() || scheduler.isStarted()) {
                scheduler.shutdown(true);
            }
        } catch (SchedulerException e) {
            log.error("shutDownScheduler error", e);
        }
    }

    @Override
    public void shutDownScheduler() {
        Scheduler scheduler = getScheduler();
        shutDownScheduler(scheduler);
    }
}
