package top.binaryx.garen.server.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.binaryx.garen.common.enums.MessageEnum;
import top.binaryx.garen.common.exception.GarenException;
import top.binaryx.garen.server.pojo.dto.*;
import top.binaryx.garen.server.thread.NotExecutedJobThread;
import top.binaryx.garen.server.thread.RepeatExecutedJobThread;
import top.binaryx.garen.server.util.ThreadUtil;

import java.util.Objects;

/**
 * <>
 *
 * @author hongtian.wei
 * @date 2022-01-24 18:10
 * @since
 */
@Slf4j
@RestController
@RequestMapping("/monitor")
public class MonitorController {

    @PostMapping("/not_executed")
    public ResponseEntity<BaseResponse<NotExecutedJobResponse>> checkNotExecuted(@RequestBody HttpJobRequest<NotExecutedJobRequest> request) {
        NotExecutedJobRequest data = request.getData();
        if (Objects.isNull(data)) {
            throw new GarenException("参数不能为空");
        }
        if (!Objects.isNull(data.getStartTime()) && !Objects.isNull(data.getEndTime())) {
            if (data.getEndTime().isBefore(data.getStartTime())) {
                throw new GarenException("开始时间和结束时间不合法");
            }
        }

        BaseResponse<NotExecutedJobResponse> response = new BaseResponse<>();
        response.setCode(MessageEnum.SUCCESS.getCode());
        response.setDesc(MessageEnum.SUCCESS.getDesc());

        ThreadUtil.newSingleThreadExecutor().submit(() -> {
            new NotExecutedJobThread(request);
        });

        return ResponseEntity.ok(response);
    }


    @PostMapping("/repeat_executed")
    public ResponseEntity<BaseResponse<NotExecutedJobResponse>> checkRepeatExecuted(@RequestBody HttpJobRequest<RepeatExecutedJobRequest> request) {
        RepeatExecutedJobRequest data = request.getData();
        if (Objects.isNull(data)) {
            throw new GarenException("参数不能为空");
        }
        if (!Objects.isNull(data.getStartTime()) && !Objects.isNull(data.getEndTime())) {
            if (data.getEndTime().isBefore(data.getStartTime())) {
                throw new GarenException("开始时间和结束时间不合法");
            }
        }

        BaseResponse<NotExecutedJobResponse> response = new BaseResponse<>();
        response.setCode(MessageEnum.SUCCESS.getCode());
        response.setDesc(MessageEnum.SUCCESS.getDesc());

        ThreadUtil.newSingleThreadExecutor().submit(() -> {
            new RepeatExecutedJobThread(request);
        });

        return ResponseEntity.ok(response);
    }
}
