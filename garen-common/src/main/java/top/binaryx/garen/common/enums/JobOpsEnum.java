package top.binaryx.garen.common.enums;

public enum JobOpsEnum {

    /**
     * 停止任务
     */
    STOP(0),

    /**
     * 启动任务
     */
    START(1),

    /**
     * 暂停任务单个分片
     */
    PAUSE(2),

    /**
     * 恢复任务单个分片
     */
    RESUME(3),

    /**
     * 任务执行一次单个分片
     */
    TRIGGER(4),

    /**
     * 暂停任务所有分片
     */
    PAUSE_ALL(5),

    /**
     * 恢复任务所有分片
     */
    RESUME_ALL(6),

    /**
     * 任务触发所有分片
     */
    TRIGGER_ALL(7);

    private int code;

    JobOpsEnum(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static JobOpsEnum getEnum(int code) {
        for (JobOpsEnum item : values()) {
            if (code == item.getCode()) {
                return item;
            }
        }
        return null;
    }
}