package top.binaryx.garen.server.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import top.binaryx.garen.server.pojo.dto.JobConfigDTO;

import java.util.List;


public interface JobConfigService {

    void create(JobConfigDTO dto) throws Exception;

    void delete(Long id);

    void update(JobConfigDTO dto);

    JobConfigDTO findById(Long id);

    int updateIp(Long id, String ip);


    Page<JobConfigDTO> find(Long pageNum, Long pageSize, JobConfigDTO dto);

    List<JobConfigDTO> findAll();

    List<JobConfigDTO> findByIp(String ip);
}
