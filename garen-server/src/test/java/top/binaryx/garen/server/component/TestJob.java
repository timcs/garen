package top.binaryx.garen.server.component;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;

/**
 * <>
 *
 * @author tim
 * @date 2022-01-21 17:57
 * @since
 */
@Slf4j
public class TestJob implements Job {

    @SneakyThrows
    @Override
    public void execute(JobExecutionContext context) {
        log.info("start job");
        int i = 0;
        do {
            log.info("executing...{}" , i);
            Thread.sleep(1000);
//            if (i == 8) {
//                throw new RuntimeException("errrrrrrrrrrrrrrror");
//            }
        } while (i++ < 10);
        log.info("end job");
    }
}