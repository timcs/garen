package top.binaryx.garen.server.pojo.dto;

import lombok.Data;


@Data
public class HttpCallbackRequest extends BaseRequest<String> {
    private String code;
    private String desc;
}
