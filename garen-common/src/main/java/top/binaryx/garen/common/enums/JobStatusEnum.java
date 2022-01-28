package top.binaryx.garen.common.enums;

import org.quartz.Trigger;

import java.util.Arrays;

/**
 * 任务调度状态，对应Trigger.TriggerState NONE, NORMAL, PAUSED
 *
 * @author xiongjie001
 * @version v0.1 2018-1-3 15:47 xiongjie001 Exp $
 */
public enum JobStatusEnum {

    /**
     * 停止状态
     */
    NONE(0, "NONE"),
    /**
     * 正常状态
     */
    NORMAL(1, "NORMAL"),
    /**
     * 暂停状态
     */
    PAUSED(2, "PAUSED"),
    ;

    private int code;

    private String memo;

    JobStatusEnum(int code, String memo) {
        this.code = code;
        this.memo = memo;
    }

    public int getCode() {
        return code;
    }

    public String getMemo() {
        return memo;
    }

    public static boolean isNormal(int status) {
        return JobStatusEnum.NORMAL.getCode() == status;
    }

    public static boolean isPaused(int status) {
        return JobStatusEnum.PAUSED.getCode() == status;
    }

    public static boolean isNone(int status) {
        return JobStatusEnum.NONE.getCode() == status;
    }

    public static JobStatusEnum getByCode(int status) {
        return Arrays.stream(JobStatusEnum.values()).filter(e -> e.getCode() == status).findFirst().orElse(JobStatusEnum.NONE);
    }

    public static JobStatusEnum getByMemo(String memo) {
        return Arrays.stream(JobStatusEnum.values()).filter(e -> e.getMemo().equalsIgnoreCase(memo)).findFirst().orElse(JobStatusEnum.NONE);
    }

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
