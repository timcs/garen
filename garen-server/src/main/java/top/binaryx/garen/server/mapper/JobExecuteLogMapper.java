package top.binaryx.garen.server.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import top.binaryx.garen.server.pojo.dto.NotExecutedJobDTO;
import top.binaryx.garen.server.pojo.dto.RepeatExecutedJobDTO;
import top.binaryx.garen.server.pojo.entity.JobExecuteLogDO;

import java.time.LocalDateTime;
import java.util.List;


public interface JobExecuteLogMapper extends BaseMapper<JobExecuteLogDO> {
    String querySql = "select " +
            "c.id as jobId," +
            "c.job_name as jobName," +
            "c.cron," +
            "t.count as repeatCount," +
            "t.start_time as startTime," +
            "t.next_time as nextTime " +
            "from job_config c," +
            "(select job_id,count(*) as count, start_time, next_time " +
            "from job_execute_log " +
//            "where start_time between #{startTime} and #{endTime} " +
            "${ew.customSqlSegment} " +
//            "group by job_id,next_time" +
            "group by job_id,next_time having count > 1" +
            ") t where t.job_id = c.id and c.job_type=0 ";

    @Select(querySql)
    IPage<RepeatExecutedJobDTO> selectRepeatExecutedJobs(IPage page, @Param("ew") Wrapper wrapper);

}
