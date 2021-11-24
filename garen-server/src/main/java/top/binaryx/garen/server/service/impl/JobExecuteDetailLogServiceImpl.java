package top.binaryx.garen.server.service.impl;

import org.springframework.stereotype.Service;
import top.binaryx.garen.server.pojo.dto.JobExecuteDetailLogDTO;
import top.binaryx.garen.server.service.JobExecuteDetailLogService;

import java.util.List;

@Service
public class JobExecuteDetailLogServiceImpl implements JobExecuteDetailLogService {
    @Override
    public List<JobExecuteDetailLogDTO> findByJobExecuteLogId(Long jobExecuteLogId) {
        return null;
    }
}
