package top.binaryx.garen.server.pojo.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;


@Data
@EqualsAndHashCode(callSuper = true)
public class MigrateResponse extends BaseResponse {
    private List<Long> jobIds;
}
