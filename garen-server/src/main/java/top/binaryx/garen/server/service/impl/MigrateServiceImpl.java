package top.binaryx.garen.server.service.impl;

import cn.hutool.core.text.CharSequenceUtil;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.binaryx.garen.server.common.GarenContext;
import top.binaryx.garen.server.pojo.dto.JobConfigDTO;
import top.binaryx.garen.server.pojo.dto.MigrateRequest;
import top.binaryx.garen.server.service.JobConfigService;
import top.binaryx.garen.server.service.MigrateService;
import top.binaryx.garen.server.service.ScheduleService;
import top.binaryx.garen.server.util.CronUtil;
import top.binaryx.garen.server.util.DateTimeUtil;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Slf4j
@Service
public class MigrateServiceImpl implements MigrateService {

    @Autowired
    ScheduleService scheduleService;

    @Autowired
    JobConfigService jobConfigService;

    @Override
    public List<Long> migrate(MigrateRequest request) throws Exception {
        Integer count = request.getCount();
        if (count > 0) {
            return discard(count);
        } else {
            return take(-count, request.getJobIds());
        }
    }

    private List<Long> discard(Integer count) throws Exception {
        //时间窗口 3分钟
        List<JobConfigDTO> jobConfigs = jobConfigService.findByIp(GarenContext.getInstance().getServer());
        List<JobConfigDTO> list = getMigrateJobs(jobConfigs, 3);

        int[] scheduleCount = {count};
        list.parallelStream().forEach(jobConfig -> {
            try {
                scheduleService.stopJob(jobConfig);
                jobConfigService.updateIp(jobConfig.getId(), CharSequenceUtil.EMPTY);
                if (scheduleCount[0]-- == 0) {
                    return;
                }
            } catch (SchedulerException e) {
                log.error("stop job fail.jobConfig:{}.", jobConfig, e);
            }

        });

        return list.stream().map(JobConfigDTO::getId).collect(Collectors.toList());
    }

    private List<Long> take(Integer count, List<Long> jobIds) throws Exception {
        int[] scheduleCount = {count};
        List<Long> result = Lists.newArrayList();
        jobIds.parallelStream().forEach(jobId -> {
            try {
                JobConfigDTO jobConfig = jobConfigService.findById(jobId);
                scheduleService.startJob(jobConfig);
                jobConfigService.updateIp(jobId, GarenContext.getInstance().getServer());

                result.add(jobId);

                if (scheduleCount[0]-- == 0) {
                    return;
                }
            } catch (Exception e) {
                log.error("scheduler job fail.jobId:{}.", jobId, e);
            }
        });
        return result;
    }


    private List<JobConfigDTO> getMigrateJobs(List<JobConfigDTO> jobConfigs, int amount) {
        Calendar now = Calendar.getInstance();
        now.add(Calendar.MINUTE, amount);
        return jobConfigs.parallelStream().filter(jobConfig -> {
            try {
                //触发任务不迁移
                if (Objects.isNull(jobConfig.getCron()) || jobConfig.getCron().isEmpty()) {
                    return false;
                }

                LocalDateTime nextFireTime = CronUtil.getNextFireTime(jobConfig.getCron());
                if (null == nextFireTime) {
                    return false;
                }

                int compareTo = DateUtils.truncatedCompareTo(now.getTime(), DateTimeUtil.toDate(nextFireTime), Calendar.SECOND);
                if (compareTo < 0) {
                    return true;
                }
            } catch (Exception e) {
                log.error("check match date error jobConfig{}:", jobConfig, e);
            }
            return false;
        }).collect(Collectors.toList());
    }
}
