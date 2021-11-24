package top.binaryx.garen.server.service;


import top.binaryx.garen.server.pojo.dto.JobExecuteDetailLogDTO;

import java.util.List;

public interface JobExecuteDetailLogService {

    List<JobExecuteDetailLogDTO> findByJobExecuteLogId(Long jobExecuteLogId);
}
