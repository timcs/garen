package top.binaryx.garen.server.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.binaryx.garen.common.Constant;
import top.binaryx.garen.common.enums.DeletedEnum;
import top.binaryx.garen.common.enums.JobOpsEnum;
import top.binaryx.garen.common.enums.MessageEnum;
import top.binaryx.garen.common.exception.GarenException;
import top.binaryx.garen.server.common.GarenContext;
import top.binaryx.garen.server.component.LeaderHandler;
import top.binaryx.garen.server.helper.NodePathHelper;
import top.binaryx.garen.server.mapper.JobConfigMapper;
import top.binaryx.garen.server.pojo.dto.JobConfigDTO;
import top.binaryx.garen.server.pojo.dto.JobOptionRequest;
import top.binaryx.garen.server.pojo.entity.JobConfigDO;
import top.binaryx.garen.server.service.JobConfigService;
import top.binaryx.garen.server.service.ScheduleService;
import top.binaryx.garen.server.service.ZookeeperService;
import top.binaryx.garen.server.util.MapperUtil;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class JobConfigServiceImpl implements JobConfigService {

    @Autowired
    ZookeeperService zookeeperService;

    @Autowired
    ScheduleService scheduleService;

    @Autowired
    JobConfigMapper mapper;
    @Autowired
    LeaderHandler leaderHandler;

    @Override
//    @Transactional(rollbackFor = )
    public void create(JobConfigDTO dto) throws Exception {
        QueryWrapper<JobConfigDO> wrapper = new QueryWrapper<>();
        wrapper.eq("job_name", dto.getJobName());
        JobConfigDO jobConfigDO = mapper.selectOne(wrapper);
        if (!Objects.isNull(jobConfigDO)) {
            throw new GarenException(MessageEnum.JOB_NAME_REPEAT);
        }
        JobConfigDO entity = MapperUtil.JobConfigPojoMapper.INSTANCE.dto2do(dto);

        mapper.insert(entity);

        dto.setId(entity.getId());

        boolean isLeader = zookeeperService.getLeaderLatch().hasLeadership();
        if (isLeader) {
            String lowerIp = leaderHandler.getLowerIp();
            if (lowerIp.equals(GarenContext.getInstance().getServer())) {
                scheduleService.startJob(dto);
            } else {
                notifyJob(lowerIp, dto);
            }
        } else {
            String leader = zookeeperService.get(NodePathHelper.getServerLeaderNode());
            String url = String.format(Constant.LOAD_IP_URL, leader, GarenContext.getInstance().getServerPort());
            String lowerIp = HttpUtil.createPost(url).execute().body();
            notifyJob(lowerIp, dto);
        }
    }

    private void notifyJob(String ip, JobConfigDTO dto) {
        String url = String.format(Constant.OPTION_URL, ip, GarenContext.getInstance().getServerPort());
        JobOptionRequest request = new JobOptionRequest();
        request.setJobConfigDTO(dto);
        request.setJobOpsEnum(JobOpsEnum.START);
        String body = HttpUtil.createPost(url).body(new Gson().toJson(request)).execute().body();
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
        JobConfigDO entity = MapperUtil.JobConfigPojoMapper.INSTANCE.dto2do(dto);
        mapper.updateById(entity);
    }

    @Override
    public int updateIp(Long id, String ip) {
        JobConfigDO entity = new JobConfigDO();
        entity.setId(id);
        entity.setExecutorIp(ip);
        return mapper.updateById(entity);
    }

    @Override
    public JobConfigDTO findById(Long id) {
        JobConfigDO jobConfigDO = mapper.selectById(id);
        return MapperUtil.JobConfigPojoMapper.INSTANCE.do2dto(jobConfigDO);
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
            JobConfigDTO d = MapperUtil.JobConfigPojoMapper.INSTANCE.do2dto((JobConfigDO) e);
            dtoList.add(d);
        });

        results.setRecords(dtoList);

        return results;
    }

    @Override
    public List<JobConfigDTO> findAll() {
        QueryWrapper<JobConfigDO> wrapper = new QueryWrapper<>();
        return findWithPage(wrapper);
    }

    @Override
    public List<JobConfigDTO> findByIp(String ip) {
        QueryWrapper<JobConfigDO> wrapper = new QueryWrapper<>();
        wrapper.eq("executorIp", ip);
        return findWithPage(wrapper);
    }

    private List<JobConfigDTO> findWithPage(QueryWrapper<JobConfigDO> wrapper) {
        List<JobConfigDO> configs = Lists.newArrayList();
        List<JobConfigDTO> result = Lists.newArrayList();
        int pageSize = 200;
        int pageNum = 1;
        Page<JobConfigDO> pages;
        do {
            Page<JobConfigDO> page = Page.of(pageNum, pageSize, false);
            pages = mapper.selectPage(page, wrapper);
            configs.addAll(pages.getRecords());
            pageNum++;
        } while (pages.getRecords().size() == pageSize);

        configs.forEach(e -> {
            JobConfigDTO d = MapperUtil.JobConfigPojoMapper.INSTANCE.do2dto(e);
            result.add(d);
        });

        return result;
    }
}
