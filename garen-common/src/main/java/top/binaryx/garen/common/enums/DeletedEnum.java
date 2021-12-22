package top.binaryx.garen.common.enums;

import lombok.Getter;

public enum DeletedEnum {
    NOT_DELETED(Byte.valueOf("0"), "未删除"),
    DELETED(Byte.valueOf("1"), "已删除"),
    ;

    @Getter
    private final Byte value;

    @Getter
    private final String desc;

    DeletedEnum(Byte value, String desc) {
        this.value = value;
        this.desc = desc;
    }
}
