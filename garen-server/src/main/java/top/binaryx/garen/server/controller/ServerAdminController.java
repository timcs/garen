package top.binaryx.garen.server.controller;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.binaryx.garen.common.enums.JobOpsEnum;
import top.binaryx.garen.common.enums.JobStatusEnum;
import top.binaryx.garen.server.component.LeaderHandler;
import top.binaryx.garen.server.pojo.dto.JobConfigDTO;
import top.binaryx.garen.server.pojo.dto.JobOptionRequest;
import top.binaryx.garen.server.pojo.dto.MigrateRequest;
import top.binaryx.garen.server.pojo.dto.MigrateResponse;
import top.binaryx.garen.server.service.JobConfigService;
import top.binaryx.garen.server.service.MigrateService;
import top.binaryx.garen.server.service.ScheduleService;

import java.util.List;


@Api(tags = "JOB管理")
@Slf4j
@RestController
@RequestMapping("/admin")
public class ServerAdminController {

    @Autowired
    MigrateService migrateService;
    @Autowired
    ScheduleService scheduleService;
    @Autowired
    JobConfigService jobConfigService;
    @Autowired
    LeaderHandler leaderHandler;

    @PostMapping("/migrate")
    public ResponseEntity<MigrateResponse> migrate(@RequestBody MigrateRequest request) throws Exception {
        MigrateResponse response = new MigrateResponse();
        List<Long> result = migrateService.migrate(request);
        response.setJobIds(result);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/scheduler/shutdown")
    public ResponseEntity shutDownScheduler() throws Exception {
        scheduleService.shutDownScheduler();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/job/ops")
    public ResponseEntity optionJob(@RequestBody JobOptionRequest request) throws Exception {
        if (request.getJobOpsEnum() == JobOpsEnum.START) {
            scheduleService.startJob(request.getJobConfigDTO());
        }
        if (request.getJobOpsEnum() == JobOpsEnum.STOP) {
            scheduleService.stopJob(request.getJobConfigDTO());
        }
        if (request.getJobOpsEnum() == JobOpsEnum.TRIGGER) {
            scheduleService.triggerJob(request.getJobConfigDTO());
        }
        if (request.getJobOpsEnum() == JobOpsEnum.PAUSE) {
            scheduleService.pauseJob(request.getJobConfigDTO());
        }
        if (request.getJobOpsEnum() == JobOpsEnum.RESUME) {
            scheduleService.resumeJob(request.getJobConfigDTO());
        }

        Trigger.TriggerState schedulerStatus = scheduleService.getSchedulerStatus(request.getJobConfigDTO());

        JobConfigDTO update = new JobConfigDTO();
        update.setId(request.getJobConfigDTO().getId());
        update.setStatus(JobStatusEnum.fromTriggerState(schedulerStatus));
        jobConfigService.update(update);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/load/ip")
    public ResponseEntity<String> getLoadIp() {
        String lowerIp = leaderHandler.getLowerIp();
        return ResponseEntity.ok(lowerIp);
    }
}
