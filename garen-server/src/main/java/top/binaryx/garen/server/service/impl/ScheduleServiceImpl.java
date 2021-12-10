/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package top.binaryx.garen.server.service.impl;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import top.binaryx.garen.server.pojo.dto.JobConfigDTO;
import top.binaryx.garen.server.service.ScheduleService;

import java.util.List;
import java.util.Map;
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
    public void scheduleJob(JobConfigDTO jobConfigDTO) throws SchedulerException {
//        String groupId = jobConfigDTO.getGroupId().toString();
//        String jobName = jobConfigDTO.getJobName();
//        String cron = jobConfigDTO.getCron();
//
//        Scheduler scheduler = getScheduler();
//        JobKey jobKey = JobKey.jobKey(jobName, groupId);
//        TriggerKey triggerKey = TriggerKey.triggerKey(jobName, groupId);
//
//        JobDetail jobDetail;
//        if (scheduler.checkExists(jobKey) && scheduler.checkExists(triggerKey)) {
//            jobDetail = scheduler.getJobDetail(jobKey);
//            scheduler.pauseTrigger(triggerKey);
//            scheduler.unscheduleJob(triggerKey);
//            scheduler.deleteJob(jobKey);
//        } else {
//            JobDataMap jobDataMap = new JobDataMap();
//            jobDataMap.put(Constant.JOB_ID, jobConfigDTO.getId());
//            jobDetail = JobBuilder.newJob(SpringJob.class)
//                    .withIdentity(jobKey)
//                    .usingJobData(jobDataMap)
//                    .build();
//        }
//
//        CronTrigger cronTrigger = TriggerBuilder.newTrigger()
//                .withIdentity(triggerKey)
//                .withSchedule(CronScheduleBuilder.cronSchedule(cron))
//                .build();
//
//        // schedule job
//        scheduler.scheduleJob(jobDetail, cronTrigger);
//        log.info("Schedule job success: groupId:{},jobName:{},cron:{}", groupId, jobName, cron);
    }

    @Override
    public void stopJob(JobConfigDTO jobConfigDTO) throws SchedulerException {
//        stopJob(jobConfigDTO.getGroupId(), jobConfigDTO.getId());
    }

    @Override
    public void stopJob(Integer groupId, Long jobId) throws SchedulerException {
        Scheduler scheduler = getScheduler();
        TriggerKey triggerKey = TriggerKey.triggerKey(jobId.toString(), groupId.toString());
        JobKey jobKey = JobKey.jobKey(jobId.toString(), groupId.toString());
        scheduler.unscheduleJob(triggerKey);
        scheduler.deleteJob(jobKey);
        log.info("Stop job success, groupName:{}, jobName:{}", groupId, jobId);
    }

    @Override
    public void stopJob(Map<Integer, List<JobConfigDTO>> groupList) throws SchedulerException {
        List<TriggerKey> triggerKeys = Lists.newArrayList();
        List<JobKey> jobKeys = Lists.newArrayList();
        Scheduler scheduler = getScheduler();
        groupList.forEach((groupId, jobList) -> {
            try {
                jobList.forEach(job -> {
                    TriggerKey triggerKey = TriggerKey.triggerKey(job.getId().toString(), groupId.toString());
                    triggerKeys.add(triggerKey);

                    JobKey jobKey = JobKey.jobKey(job.getId().toString(), groupId.toString());
                    jobKeys.add(jobKey);

                });
                scheduler.unscheduleJobs(triggerKeys);
                scheduler.deleteJobs(jobKeys);
            } catch (SchedulerException e) {
                log.error("stop job batch error.param.{},errorMsg.", groupList, e);
            }
        });
    }

    @Override
    public void pauseJob(String groupName, String jobName) throws SchedulerException {
        Scheduler scheduler = getScheduler();
        if (scheduler.checkExists(JobKey.jobKey(jobName, groupName)) && scheduler.checkExists(TriggerKey.triggerKey(jobName, groupName))) {
            scheduler.pauseTrigger(TriggerKey.triggerKey(jobName, groupName));
            scheduler.pauseJob(JobKey.jobKey(jobName, groupName));
            log.info("Pause job success, groupName:{}, jobName:{}", groupName, jobName);
        }
    }

    @Override
    public void resumeJob(String groupName, String jobName) throws SchedulerException {
        Scheduler scheduler = getScheduler();
        if (scheduler.checkExists(JobKey.jobKey(jobName, groupName)) && scheduler.checkExists(TriggerKey.triggerKey(jobName, groupName))) {
            if (scheduler.getTriggerState(TriggerKey.triggerKey(jobName, groupName)) == Trigger.TriggerState.PAUSED) {
                // 任务处于暂停状态
                scheduler.resumeJob(JobKey.jobKey(jobName, groupName));
                scheduler.resumeTrigger(TriggerKey.triggerKey(jobName, groupName));
                log.info("Resume job success, groupName:{}, jobName:{}", groupName, jobName);
            }
        }
    }

    @Override
    public void triggerJob(String groupName, String jobName) throws SchedulerException {
        Scheduler scheduler = getScheduler();
        if (scheduler.checkExists(JobKey.jobKey(jobName, groupName)) && scheduler.checkExists(TriggerKey.triggerKey(jobName, groupName))) {
            if (scheduler.getTriggerState(TriggerKey.triggerKey(jobName, groupName)) == Trigger.TriggerState.NORMAL) {
                scheduler.triggerJob(JobKey.jobKey(jobName, groupName));
                log.info("Trigger job success, groupName:{}, jobName:{}", groupName, jobName);
            }
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
}
