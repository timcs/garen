package top.binaryx.garen.common.enums;

import lombok.Getter;


public enum ProtocolTypeEnum {

    BEAN(1, "BEAN模式"),

    HTTP_SYNC(2, "HTTP同步"),

    HTTP_ASYNC(3, "HTTP异步"),
    ;
    @Getter
    private final int value;
    @Getter
    private final String desc;

    ProtocolTypeEnum(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static ProtocolTypeEnum findByValue(int value) {
        for (ProtocolTypeEnum typeEnum : ProtocolTypeEnum.values()) {
            if (typeEnum.value == value) {
                return typeEnum;
            }
        }
        return null;
    }
}
