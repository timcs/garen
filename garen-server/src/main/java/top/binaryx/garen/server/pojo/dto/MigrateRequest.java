package top.binaryx.garen.server.pojo.dto;

import lombok.Data;

import java.util.List;


@Data
public class MigrateRequest {
    private Integer count;
    private List<Long> jobIds;
}
