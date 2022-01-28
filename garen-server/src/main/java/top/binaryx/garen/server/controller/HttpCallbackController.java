package top.binaryx.garen.server.controller;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.binaryx.garen.common.enums.ExecuteStatusEnum;
import top.binaryx.garen.common.enums.MessageEnum;
import top.binaryx.garen.server.pojo.dto.HttpCallbackRequest;
import top.binaryx.garen.server.pojo.dto.HttpCallbackResponse;
import top.binaryx.garen.server.pojo.dto.JobExecLog;
import top.binaryx.garen.server.pojo.dto.JobExecuteLogDTO;
import top.binaryx.garen.server.service.JobLogService;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * <>
 *
 * @author hongtian.wei
 * @date 2022-01-14 18:45
 * @since
 */
@Api(tags = "callback管理")
@Slf4j
@RestController
@RequestMapping("/http")
public class HttpCallbackController {

    @Autowired
    JobLogService jobLogService;

    @PostMapping("/callback")
    public ResponseEntity<HttpCallbackResponse> callback(@RequestBody HttpCallbackRequest request)
            throws Exception {
        HttpCallbackResponse response = new HttpCallbackResponse();
        response.setCode(MessageEnum.SUCCESS.getCode());
        response.setDesc(MessageEnum.SUCCESS.getDesc());

        Long executeId = request.getExecuteId();
        JobExecLog jobExecLog = jobLogService.findById(executeId);

        if (Objects.isNull(jobExecLog)) {
            return ResponseEntity.ok(response);
        }

        if (!ExecuteStatusEnum.isRunning(jobExecLog.getStatus())) {
            return ResponseEntity.ok(response);
        }

        if (MessageEnum.isPending(response.getCode())) {
            return ResponseEntity.ok(response);
        }

        JobExecuteLogDTO update = new JobExecuteLogDTO();
        update.setId(jobExecLog.getId());
        update.setEndTime(LocalDateTime.now());
        update.setResponseBody(request.getData());
        update.setMemo(request.getDesc());

        if (MessageEnum.isSuccess(response.getCode())) {
            update.setStatus(ExecuteStatusEnum.SUCCESS.getValue());
        }
        if (MessageEnum.isFail(response.getCode())) {
            update.setStatus(ExecuteStatusEnum.FAILED.getValue());
        }

        jobLogService.update(update);

        return ResponseEntity.ok(response);
    }
}
