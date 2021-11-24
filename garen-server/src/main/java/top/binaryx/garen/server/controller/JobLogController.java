package top.binaryx.garen.server.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.binaryx.garen.server.common.ResponseEnvelope;
import top.binaryx.garen.server.pojo.dto.JobConfigRequest;
import top.binaryx.garen.server.pojo.dto.JobExecuteDetailLogDTO;
import top.binaryx.garen.server.pojo.dto.JobExecuteLogDTO;
import top.binaryx.garen.server.pojo.dto.JobLogQueryRequest;
import top.binaryx.garen.server.pojo.vo.JobExecuteDetailLogVO;
import top.binaryx.garen.server.pojo.vo.JobExecuteLogVO;
import top.binaryx.garen.server.pojo.vo.JobLogVO;
import top.binaryx.garen.server.service.JobExecuteDetailLogService;
import top.binaryx.garen.server.service.JobExecuteLogService;
import top.binaryx.garen.server.service.JobLogService;

import java.util.List;
import java.util.Objects;


/**
 * @author tim
 */
@Slf4j
@RestController
@RequestMapping("/job/log")
public class JobLogController {

    @Autowired
    private JobExecuteLogService executeLogService;

    @Autowired
    private JobExecuteDetailLogService detailLogService;

    @PostMapping("/query")
    public ResponseEntity<ResponseEnvelope<Page<JobLogVO>>> pageList(@RequestBody @Validated JobLogQueryRequest request) {
        Page<JobLogVO> page = Page.of(request.getPageNum(), request.getPageSize(), true);
        if (!Objects.isNull(request.getJobLogId())) {
            JobExecuteLogDTO jobExecuteLogDTO = executeLogService.find(request.getJobLogId());

            Assert.notNull(jobExecuteLogDTO,"查不到指定的日志");

            List<JobExecuteDetailLogDTO> byJobExecuteLogId = detailLogService.findByJobExecuteLogId(jobExecuteLogDTO.getJobId());


        }

        return null;
    }


}
