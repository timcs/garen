package top.binaryx.garen.server.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.binaryx.garen.server.common.ResponseEnvelope;
import top.binaryx.garen.server.pojo.dto.JobConfigDTO;
import top.binaryx.garen.server.pojo.dto.JobConfigRequest;
import top.binaryx.garen.server.pojo.vo.JobConfigVO;
import top.binaryx.garen.server.service.JobConfigService;
import top.binaryx.garen.server.util.MapperUtil;

import java.util.List;


@Api(tags = "JOB配置")
@Slf4j
@RestController
@RequestMapping("/job/config")
public class JobConfigController {

    @Autowired
    private JobConfigService jobConfigService;

    @ApiOperation(value = "新增任务", notes = "入库并加入分配调度引擎")
    @PutMapping("/add")
    public ResponseEntity<ResponseEnvelope> add(@RequestBody @Validated JobConfigRequest request) throws Exception {
        JobConfigDTO dto = generateModifyDTO(request);
        jobConfigService.create(dto);
        return ResponseEntity.ok(ResponseEnvelope.success());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseEnvelope> delete(@PathVariable Long id) {
        jobConfigService.delete(id);
        return ResponseEntity.ok(ResponseEnvelope.success());
    }

    @PatchMapping("/update")
    public ResponseEntity<ResponseEnvelope> update(@RequestBody @Validated JobConfigRequest request) {
        JobConfigDTO dto = generateModifyDTO(request);
        dto.setId(request.getId());
        jobConfigService.update(dto);
        return ResponseEntity.ok(ResponseEnvelope.success());
    }

    @GetMapping("/load/{id}")
    public ResponseEntity<ResponseEnvelope<JobConfigVO>> load(@PathVariable Long id) {
        JobConfigDTO dto = jobConfigService.findById(id);
        JobConfigVO vo = dto2Vo(dto);
        return ResponseEntity.ok(ResponseEnvelope.success(vo));
    }

    @PostMapping("/query")
    public ResponseEntity<ResponseEnvelope<Page<JobConfigVO>>> pageList(@RequestBody @Validated JobConfigRequest request) {
        JobConfigDTO dto = generateQueryDTO(request);

        Page page = jobConfigService.find(request.getPageNum(), request.getPageSize(), dto);

        List<JobConfigVO> voList = Lists.newArrayList();

        page.getRecords().forEach(e -> voList.add(dto2Vo((JobConfigDTO) e)));
        page.setRecords(voList);

        return ResponseEntity.ok(ResponseEnvelope.success(page));
    }

    private JobConfigDTO generateQueryDTO(JobConfigRequest request) {
        JobConfigDTO dto = new JobConfigDTO();
        dto.setJobName(StrUtil.trimToNull(request.getJobName()));
        dto.setStatus(request.getStatus());
        return dto;
    }

    private JobConfigDTO generateModifyDTO(JobConfigRequest request) {
        JobConfigDTO dto = new JobConfigDTO();
        dto.setId(request.getId());
        dto.setJobNo(request.getJobNo());
        dto.setJobName(StrUtil.trimToNull(request.getJobName()));
        dto.setJobDesc(request.getJobDesc());
        dto.setCron(request.getCron());
        dto.setJobParam(request.getJobParam());
        dto.setJobType(request.getJobType());
        dto.setProtocolType(request.getProtocolType().getValue());
        dto.setTargetAddress(request.getTargetAddress());
        dto.setDeleted(request.getDeleted());
        dto.setStatus(request.getStatus());
        return dto;
    }

    private JobConfigVO dto2Vo(JobConfigDTO dto) {
        return MapperUtil.JobConfigPojoMapper.INSTANCE.dto2vo(dto);
    }
}
