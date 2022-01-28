package top.binaryx.garen.server.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import top.binaryx.garen.server.mapper.JobConfigMapper;
import top.binaryx.garen.server.mapper.JobExecuteLogMapper;
import top.binaryx.garen.server.pojo.dto.RepeatExecutedJobDTO;
import top.binaryx.garen.server.pojo.entity.JobConfigDO;

import java.time.LocalDateTime;
import java.util.List;


@SpringBootTest
public class JobLogServiceTest {

    @Autowired
    JobLogService service;

    @Autowired
    JobExecuteLogMapper mapper;

    @Test
    public void testSelectRepeatExecutedJobs() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime endTime = now;
        LocalDateTime startTime = now.plusDays(-100);
        Assertions.assertTrue(startTime.isBefore(endTime));
        List<RepeatExecutedJobDTO> repeatExecutedJobDTOS = service.selectRepeatExecutedJobs(startTime, endTime);
        System.out.println(repeatExecutedJobDTOS);
    }



}
