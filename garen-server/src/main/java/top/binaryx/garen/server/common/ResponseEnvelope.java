package top.binaryx.garen.server.common;

import lombok.Data;
import top.binaryx.garen.common.enums.MessageEnum;

@Data
public class ResponseEnvelope<T> {
    private T data;
    private String code;
    private String message;

    public ResponseEnvelope() {
    }

    public ResponseEnvelope(MessageEnum messageEnum) {
        this.code = messageEnum.getCode();
        this.message = messageEnum.getDesc();
    }

    public ResponseEnvelope(T data, MessageEnum messageEnum) {
        this.data = data;
        this.code = messageEnum.getCode();
        this.message = messageEnum.getDesc();
    }

    public static <T> ResponseEnvelope success(T t) {
        return new ResponseEnvelope(t, MessageEnum.SUCCESS);
    }

    public static ResponseEnvelope success() {
        return new ResponseEnvelope(MessageEnum.SUCCESS);
    }

    public static <T> ResponseEnvelope fail(T t) {
        return new ResponseEnvelope(t, MessageEnum.FAIL);
    }

    public static ResponseEnvelope fail() {
        return new ResponseEnvelope(MessageEnum.FAIL);
    }
}
