package top.binaryx.garen.common.enums;

import lombok.Getter;

public enum MessageEnum {
    SUCCESS("0000", "成功"),
    PENDING("1000", "处理中"),
    FAIL("9999", "失败"),

    JOB_NAME_REPEAT("6000", "任务名重复"),
    GROUP_NAME_REPEAT("6001", "应用名重复"),
    PARAM_EXCEPTION("5000", "参数校验不通过"),
    SERVER_ERROR("9998", "服务器异常"),
    GET_IP_FAIL("9001", "获取本机ip失败"),
    PERSIST_IP_FAIL("9002", "写入zk节点失败"),


    ;

    private final String code;

    @Getter
    private final String desc;

    MessageEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static boolean isSuccess(String code) {
        return SUCCESS.getCode().equals(code);
    }

    public static boolean isFail(String code) {
        return FAIL.getCode().equals(code);
    }

    public static boolean isPending(String code) {
        return PENDING.getCode().equals(code);
    }

    public String getCode() {
        return this.code;
    }

}
