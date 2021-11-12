package top.binaryx.garen.server.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.binaryx.garen.common.enums.DeletedEnum;
import top.binaryx.garen.common.enums.MessageEnum;
import top.binaryx.garen.common.exception.GarenException;
import top.binaryx.garen.server.mapper.JobConfigMapper;
import top.binaryx.garen.server.pojo.dto.JobConfigDTO;
import top.binaryx.garen.server.pojo.entity.JobConfigDO;
import top.binaryx.garen.server.service.JobConfigService;
import top.binaryx.garen.server.util.MapperUtil;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class JobConfigServiceImpl implements JobConfigService {

    @Autowired
    JobConfigMapper mapper;

    @Override
    public void create(JobConfigDTO dto) {
        System.out.println(dto);
        QueryWrapper<JobConfigDO> wrapper = new QueryWrapper<>();
        wrapper.eq("job_name", dto.getJobName());
        JobConfigDO jobConfigDO = mapper.selectOne(wrapper);
        if (!Objects.isNull(jobConfigDO)) {
            throw new GarenException(MessageEnum.JOB_NAME_REPEAT);
        }
        JobConfigDO entity = MapperUtil.JobConfigMapper.INSTANCE.dto2do(dto);

        mapper.insert(entity);
    }

    @Override
    public void delete(Long id) {
        JobConfigDO entity = new JobConfigDO();
        entity.setId(id);
        entity.setDeleted(DeletedEnum.DELETED.getValue());
        //todo
        entity.setModifier("");
        mapper.updateById(entity);
    }

    @Override
    public void update(JobConfigDTO dto) {
        JobConfigDO entity = MapperUtil.JobConfigMapper.INSTANCE.dto2do(dto);
        mapper.updateById(entity);
    }

    @Override
    public JobConfigDTO find(Long id) {
        JobConfigDO jobConfigDO = mapper.selectById(id);
        return MapperUtil.JobConfigMapper.INSTANCE.do2dto(jobConfigDO);
    }

    @Override
    public Page<JobConfigDTO> find(Long pageNum, Long pageSize, JobConfigDTO dto) {
        Page<JobConfigDO> page = Page.of(pageNum, pageSize, true);

        QueryWrapper<JobConfigDO> wrapper = new QueryWrapper<>();
        if (!StrUtil.isEmpty(dto.getJobName())) {
            wrapper.eq("job_name", dto.getJobName());
        }

        if (!Objects.isNull(dto.getStatus())) {
            wrapper.eq("status", dto.getStatus());
        }

        Page results = mapper.selectPage(page, wrapper);

        List<JobConfigDTO> dtoList = Lists.newArrayList();

        results.getRecords().forEach(e -> {
            JobConfigDTO d = MapperUtil.JobConfigMapper.INSTANCE.do2dto((JobConfigDO) e);
            dtoList.add(d);
        });

        results.setRecords(dtoList);

        return results;
    }
}
