package top.binaryx.garen.common.enums;

/**
 * CommandEnumm.
 *
 */
public enum CommandEnum {
    /**
     * 更新
     */
    UPDATE(1, "update"),

    /**
     * 移除
     */
    REMOVE(2, "remove");

    private int code;
    private String memo;

    CommandEnum(int code, String memo) {
        this.code = code;
        this.memo = memo;
    }

    public int getCode() {
        return code;
    }

    public String getMemo() {
        return memo;
    }

}