package top.binaryx.garen.server.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import top.binaryx.garen.server.pojo.dto.JobConfigDTO;


public interface JobConfigService {


    void create(JobConfigDTO dto);

    void delete(Long id);

    void update(JobConfigDTO dto);

    JobConfigDTO find(Long id);

    Page<JobConfigDTO> find(Long pageNum, Long pageSize, JobConfigDTO dto);
}
