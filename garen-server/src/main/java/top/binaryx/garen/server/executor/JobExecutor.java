package top.binaryx.garen.server.executor;

import top.binaryx.garen.server.pojo.dto.JobConfigDTO;

public interface JobExecutor {

    void execute(JobConfigDTO jobConfigDTO);
}