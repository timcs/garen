package top.binaryx.garen.server.util;

import org.quartz.Trigger;

/**
 * <>
 *
 * @author tim
 * @date 2022-01-21 17:08
 * @since
 */
public class JobStatusUtil {

    public static Integer fromTriggerState(Trigger.TriggerState state) {
        if (Trigger.TriggerState.NORMAL == state) {
            return 1;
        }

        if (Trigger.TriggerState.PAUSED == state) {
            return 2;
        }

        //剩下都停止
        return 0;
    }

}
