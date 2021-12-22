package top.binaryx.garen.server.common;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class IdWorker {

    private final static int MAX_INT = 1000000000;

    private final static int MIN_INT = 100000000;

    private final static long MIN_LONG = 1000000000000000000L;

    public static int getGroupId() {
        return RandomUtils.nextInt(MIN_INT, MAX_INT);
    }

    public static long getJobId(int groupId) {
        String jobId = String.format("%s%s", groupId, getGroupId());
        return Long.parseLong(jobId);
    }

    public static long getLogId() {
        return RandomUtils.nextLong(MIN_LONG, Long.MAX_VALUE);
    }

    public static long getExecuteNo() {
        return RandomUtils.nextLong(MIN_LONG, Long.MAX_VALUE);
    }

}
