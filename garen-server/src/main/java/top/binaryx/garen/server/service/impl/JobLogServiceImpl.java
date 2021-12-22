package top.binaryx.garen.server.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.binaryx.garen.server.mapper.JobExecuteLogMapper;
import top.binaryx.garen.server.pojo.dto.JobExecLog;
import top.binaryx.garen.server.pojo.dto.JobExecuteLogDTO;
import top.binaryx.garen.server.pojo.entity.JobExecuteLogDO;
import top.binaryx.garen.server.service.JobLogService;
import top.binaryx.garen.server.util.MapperUtil;

import java.util.List;


@Slf4j
@Service
public class JobLogServiceImpl implements JobLogService {

    @Autowired
    JobExecuteLogMapper jobExecLogMapper;

    @Override
    public int save(JobExecuteLogDTO dto) {
        JobExecuteLogDO entity = MapperUtil.JobExecuteLogPojoMapper.INSTANCE.dto2do(dto);
        int count = jobExecLogMapper.insert(entity);
        dto.setId(entity.getId());
        return count;
    }

    @Override
    public int update(JobExecuteLogDTO dto) {
        JobExecuteLogDO entity = MapperUtil.JobExecuteLogPojoMapper.INSTANCE.dto2do(dto);
        int count = jobExecLogMapper.updateById(entity);
        return count;
    }

    @Override
    public Integer findCount(JobExecLog dto) {
        return null;
    }

    @Override
    public List<JobExecLog> findByExecuteNo(Long executeNo) {
        return null;
    }

    @Override
    public JobExecLog findById(Long id) {
        return null;
    }

    @Override
    public List<JobExecLog> findPage(JobExecLog dto, Integer pageSize, Integer offset) {
        return null;
    }

    @Override
    public JobExecuteLogDTO queryLog(Long id) {
        return null;
    }

    @Override
    public JobExecuteLogDTO queryDetailLog(Long id) {
        return null;
    }

//    @Override
//    public void executeHttpCallBack(HttpCallBackRequest request) {
//        JobExecLog update = new JobExecLog();
//        update.setId(request.getExecuteId());
//        update.setRespCode(request.getCode());
//        update.setRespMsg(request.getMessage());
//        update.setEndTime(LocalDateTime.now());
//
//        if (!Objects.isNull(request.getData())) {
//            update.setMemo(request.getData() instanceof Object ? new Gson().toJson(request.getData()) : request.getData().toString());
//        }
//
//        if (MessageEnum.isSuccess(request.getCode())) {
//            update.setStatus(ExecuteStatusEnum.SUCCESS.getValue());
//        } else {
//            update.setStatus(ExecuteStatusEnum.FAILED.getValue());
//        }
//
//        update(update);
//    }
}
