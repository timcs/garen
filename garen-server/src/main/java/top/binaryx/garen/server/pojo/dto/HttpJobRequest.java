package top.binaryx.garen.server.pojo.dto;

import lombok.Data;


@Data
public class HttpJobRequest {
    private Long executeId;
    private Long jobId;
    private String jobParam;
    private String callBackUrl;
}
