package top.binaryx.garen.server.util;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.apache.commons.lang3.StringUtils;
import org.quartz.CronExpression;
import org.quartz.impl.triggers.CronTriggerImpl;

import java.time.LocalDateTime;
import java.util.Objects;


public class CronUtil {

    private final static Cache<String, CronTriggerImpl> cache = CacheBuilder.newBuilder().initialCapacity(1000).build();

    private CronUtil() {
    }

    /**
     * 计算下一次执行时间
     *
     * @param cron
     * @return
     */
    public static LocalDateTime getNextFireTime(String cron) throws Exception {
        return getNextFireTime(cron, null);
    }

    /**
     * 计算下一次执行时间
     *
     * @param cron
     * @return
     */
    public static LocalDateTime getNextFireTime(String cron, LocalDateTime date) throws Exception {
        if (StringUtils.isBlank(cron)) {
            return null;
        }

        LocalDateTime afterTime = Objects.isNull(date) ? LocalDateTime.now() : date;

        if (cache.getIfPresent(cron) != null) {
            return DateTimeUtil.toLocalDateTime(cache.getIfPresent(cron).getFireTimeAfter(DateTimeUtil.toDate(afterTime)));
        }

        CronExpression expression = new CronExpression(cron);
        CronTriggerImpl trigger = new CronTriggerImpl();
        trigger.setCronExpression(expression);

        if (Objects.isNull(afterTime)) {
            cache.put(cron, trigger);
        } else {
            trigger.setStartTime(DateTimeUtil.toDate(afterTime));
        }

        cache.put(cron, trigger);
        return DateTimeUtil.toLocalDateTime(trigger.getFireTimeAfter(DateTimeUtil.toDate(afterTime)));
    }

}
