package top.binaryx.garen.server.service;

import top.binaryx.garen.server.pojo.dto.JobExecLog;
import top.binaryx.garen.server.pojo.dto.JobExecuteLogDTO;

import java.util.List;


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

}
