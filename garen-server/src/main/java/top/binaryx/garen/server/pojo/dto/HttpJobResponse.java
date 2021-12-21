package top.binaryx.garen.server.pojo.dto;

import lombok.Data;


@Data
public class HttpJobResponse<T> {
    private String code;
    private String message;
    private T data;
}
