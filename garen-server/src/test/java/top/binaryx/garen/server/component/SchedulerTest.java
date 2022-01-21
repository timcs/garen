package top.binaryx.garen.server.component;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import top.binaryx.garen.common.Constant;

/**
 * <>
 *
 * @author tim
 * @date 2022-01-21 17:30
 * @since
 */
@Slf4j
public class SchedulerTest {

    static Scheduler scheduler;
    String id = "12345678";
    TriggerKey triggerKey = TriggerKey.triggerKey(id);
    JobKey jobKey = JobKey.jobKey(id);

    @BeforeAll
    public static void before() {
        try {
            StdSchedulerFactory stdSchedulerFactory = new StdSchedulerFactory();
            stdSchedulerFactory.initialize();
            scheduler = stdSchedulerFactory.getScheduler();
            scheduler.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testTriggerJob() throws Exception {
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put(Constant.JOB_ID, id);

        JobDetail jobDetail = JobBuilder.newJob(TestJob.class)
                .withIdentity(jobKey)
                .usingJobData(jobDataMap)
                .build();

        SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule();
        SimpleTrigger simpleTrigger = TriggerBuilder.newTrigger()
                .withIdentity(triggerKey)
                .withSchedule(scheduleBuilder)
                .startNow()
                .build();

        log.info("scheduleJob");
        scheduler.scheduleJob(jobDetail, simpleTrigger);
        printStatus();
    }

    private void printStatus() throws Exception {
        while (true) {
            Trigger.TriggerState triggerState = scheduler.getTriggerState(triggerKey);
            log.info("triggerState:{}", triggerState);
            Thread.sleep(500);
        }
    }

    @Test
    public void testCronJob() throws Exception {
        CronTrigger cronTrigger = TriggerBuilder.newTrigger()
                .withIdentity(triggerKey)
                .withSchedule(CronScheduleBuilder.cronSchedule("0 */2 * ? * *"))
                .build();

        JobDetail jobDetail = JobBuilder.newJob(TestJob.class)
                .withIdentity(jobKey)
                .build();

        // schedule job
        scheduler.scheduleJob(jobDetail, cronTrigger);
        printStatus();
    }

}

