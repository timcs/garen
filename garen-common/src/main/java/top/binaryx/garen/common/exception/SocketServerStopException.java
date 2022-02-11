package top.binaryx.garen.common.exception;


public class SocketServerStopException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "socket server stop failed.";

    public SocketServerStopException() {
        super(DEFAULT_MESSAGE);
    }

}