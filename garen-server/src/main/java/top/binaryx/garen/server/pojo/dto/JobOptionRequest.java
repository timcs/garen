package top.binaryx.garen.server.pojo.dto;

import lombok.Data;
import top.binaryx.garen.common.enums.JobOpsEnum;

import java.util.List;


@Data
public class JobOptionRequest {
    private JobConfigDTO jobConfigDTO;
    private JobOpsEnum jobOpsEnum;
}
