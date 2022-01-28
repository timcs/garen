package top.binaryx.garen.server.pojo.dto;

import lombok.Data;


@Data
public class BaseResponse<T> {
    private String code;
    private String desc;
    private T data;
}
