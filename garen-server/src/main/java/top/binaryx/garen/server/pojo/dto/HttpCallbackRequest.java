package top.binaryx.garen.server.pojo.dto;

import lombok.Data;


@Data
public class HttpCallbackRequest {
    private Long executeId;
    private Long jobId;

    private Integer resultCode;
    private String resultMsg;
    private String resultData;
}
