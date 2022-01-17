package top.binaryx.garen.common.enums;

import lombok.Getter;


public enum ExecuteStatusEnum {

    RUNNING(0, "运行中"),

    SUCCESS(1, "成功"),

    FAILED(2, "失败");

    @Getter
    private final int value;

    @Getter
    private final String desc;

    ExecuteStatusEnum(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static ExecuteStatusEnum findByValue(int value) {
        for (ExecuteStatusEnum item : values()) {
            if (value == item.getValue()) {
                return item;
            }
        }
        return null;
    }

    public static boolean isRunning(Integer status) {
        return RUNNING.getValue() == status;
    }

}
