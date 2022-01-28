package top.binaryx.garen.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.binaryx.garen.common.Constant;
import top.binaryx.garen.server.mapper.JobConfigMapper;
import top.binaryx.garen.server.mapper.JobExecuteLogMapper;
import top.binaryx.garen.server.pojo.dto.JobExecLog;
import top.binaryx.garen.server.pojo.dto.JobExecuteLogDTO;
import top.binaryx.garen.server.pojo.dto.NotExecutedJobDTO;
import top.binaryx.garen.server.pojo.dto.RepeatExecutedJobDTO;
import top.binaryx.garen.server.pojo.entity.JobConfigDO;
import top.binaryx.garen.server.pojo.entity.JobExecuteLogDO;
import top.binaryx.garen.server.service.JobConfigService;
import top.binaryx.garen.server.service.JobLogService;
import top.binaryx.garen.server.service.ZookeeperService;
import top.binaryx.garen.server.util.MapperUtil;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


@Slf4j
@Service
public class JobLogServiceImpl implements JobLogService {

    @Autowired
    JobExecuteLogMapper jobExecLogMapper;

    @Autowired
    JobConfigMapper jobConfigMapper;

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

    @Override
    public List<NotExecutedJobDTO> selectNotExecutedJobs(LocalDateTime startTime, LocalDateTime endTime) {
        return null;
    }

    private static final String IN_SQL = "SELECT next_time FROM job_execute_log WHERE " +
            "start_time BETWEEN '#{startTime}' AND '#{endTime}' GROUP BY next_time HAVING COUNT(*) > 1";

    @Override
    public List<RepeatExecutedJobDTO> selectRepeatExecutedJobs(LocalDateTime startTime, LocalDateTime endTime) {
        List<RepeatExecutedJobDTO> result = Lists.newArrayList();
        //        IPage<RepeatExecutedJobDTO> page = multiple(startTime, endTime);

        //分开查
//        Select * From job_execute_log Where next_time In (Select next_time From job_execute_log Group By next_time Having Count(*)>1);
        Page<JobExecuteLogDO> page = new Page<>(1, Constant.PAGE_SIZE, false);

        String inSql = IN_SQL.replace("#{startTime}", startTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .replace("#{endTime}", endTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        QueryWrapper<JobExecuteLogDO> wrapper = new QueryWrapper<>();
        wrapper.inSql("next_time", inSql);

        List<JobExecuteLogDO> logDOS = Lists.newArrayList();
        do {
            jobExecLogMapper.selectPage(page, wrapper);
            page.setCurrent(page.getCurrent() + 1);
            logDOS.addAll(page.getRecords());
        } while (page.getRecords().size() == page.getSize());

        //查询任务配置
        Set<Long> jobIds = logDOS.stream().map(JobExecuteLogDO::getJobId).collect(Collectors.toSet());
        List<JobConfigDO> jobConfigDOS = jobConfigMapper.selectBatchIds(jobIds);
        //组装返回结果
        logDOS.stream().forEach(e -> {
            Optional<JobConfigDO> jobConfigDO = jobConfigDOS.stream().filter(c -> c.getId().equals(e.getJobId())).findAny();
            RepeatExecutedJobDTO dto = new RepeatExecutedJobDTO();
            dto.setJobId(jobConfigDO.get().getId());
            dto.setJobName(jobConfigDO.get().getJobName());
            dto.setCron(jobConfigDO.get().getCron());
            dto.setStartTime(e.getStartTime());
            dto.setNextTime(e.getNextTime());
            result.add(dto);
        });

        return result;
    }

    //多表查 如果要显示完整的重复记录,需要再次通过next_time查询
    private IPage<RepeatExecutedJobDTO> multiple(LocalDateTime startTime, LocalDateTime endTime) {
        QueryWrapper<RepeatExecutedJobDTO> wrapper = new QueryWrapper<>();
        wrapper.between("start_time", startTime, endTime);

        Page<Object> objectPage = new Page<>(1, 100);
        return jobExecLogMapper.selectRepeatExecutedJobs(objectPage, wrapper);
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
