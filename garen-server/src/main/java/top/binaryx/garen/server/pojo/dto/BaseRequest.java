package top.binaryx.garen.server.pojo.dto;

import lombok.Data;

/**
 * <>
 *
 * @author hongtian.wei
 * @date 2022-01-24 18:54
 * @since
 */
@Data
public class BaseRequest<T> {
    private Long executeId;
    private Long jobId;
    private T data;
}
