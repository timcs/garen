package top.binaryx.garen.common.exception;

import lombok.Getter;
import lombok.Setter;
import top.binaryx.garen.common.enums.MessageEnum;


public class GarenException extends RuntimeException {

    @Getter
    @Setter
    MessageEnum messageEnum;

    public GarenException(MessageEnum messageEnum) {
        super();
        this.messageEnum = messageEnum;
    }

    public GarenException(MessageEnum messageEnum, Throwable cause) {
        super(cause);
        this.messageEnum = messageEnum;
    }

    public GarenException(MessageEnum messageEnum, String message) {
        super(messageEnum.toString() + message);
        this.messageEnum = messageEnum;
    }

    public GarenException(String message, Throwable cause) {
        super(message, cause);
    }

    public GarenException(String message) {
        super(message);
    }

    public GarenException(Throwable cause) {
        super(cause);
    }

    public GarenException() {
        super();
    }
}
