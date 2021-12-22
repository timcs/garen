package top.binaryx.garen.server.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.binaryx.garen.server.common.ResponseEnvelope;
import top.binaryx.garen.server.pojo.dto.JobLogQueryRequest;
import top.binaryx.garen.server.pojo.vo.JobLogVO;
import top.binaryx.garen.server.service.JobExecuteLogService;


@Api(tags = "JOB日志")
@Slf4j
@RestController
@RequestMapping("/job/log")
public class JobLogController {

    @Autowired
    private JobExecuteLogService executeLogService;


    @PostMapping("/query")
    public ResponseEntity<ResponseEnvelope<Page<JobLogVO>>> pageList(@RequestBody @Validated JobLogQueryRequest request) {

        return null;
    }


}
