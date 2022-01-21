package top.binaryx.garen.server.service;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import top.binaryx.garen.server.pojo.dto.JobConfigDTO;

import java.util.List;
import java.util.Map;


public interface ScheduleService {

    boolean isScheduled(String groupName, String jobName) throws SchedulerException;

    void startJob(JobConfigDTO jobConfigDTO) throws SchedulerException;

    void stopJob(JobConfigDTO jobConfigDTO) throws SchedulerException;

    void pauseJob(JobConfigDTO jobConfigDTO) throws SchedulerException;

    void resumeJob(JobConfigDTO jobConfigDTO) throws SchedulerException;

    void triggerJob(JobConfigDTO jobConfigDTO) throws SchedulerException;

    Trigger.TriggerState getSchedulerStatus(JobConfigDTO jobConfigDTO) throws SchedulerException;

    Scheduler getScheduler();

    void shutDownScheduler(Scheduler scheduler);

    void shutDownScheduler();
}
