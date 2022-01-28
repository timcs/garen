package top.binaryx.garen.server.pojo.dto;

import lombok.Data;


@Data
public class HttpJobRequest<T> extends BaseRequest<T> {
    private String callbackUrl;
}
