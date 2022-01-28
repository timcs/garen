package top.binaryx.garen.server.service;

import io.swagger.models.auth.In;
import top.binaryx.garen.server.pojo.dto.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;


public interface JobLogService {

    int save(JobExecuteLogDTO dto);

    int update(JobExecuteLogDTO dto);

    Integer findCount(JobExecLog dto);

    List<JobExecLog> findByExecuteNo(Long executeNo);

    JobExecLog findById(Long id);

    List<JobExecLog> findPage(JobExecLog dto, Integer pageSize, Integer offset);

    //    void executeHttpCallBack(HttpCallBackRequest request);
    JobExecuteLogDTO queryLog(Long id);

    JobExecuteLogDTO queryDetailLog(Long id);

    List<NotExecutedJobDTO> selectNotExecutedJobs(LocalDateTime startTime, LocalDateTime endTime);

    List<RepeatExecutedJobDTO> selectRepeatExecutedJobs(LocalDateTime startTime, LocalDateTime endTime);
}
