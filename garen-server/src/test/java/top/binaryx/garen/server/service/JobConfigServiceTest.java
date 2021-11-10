package top.binaryx.garen.server.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import top.binaryx.garen.server.mapper.JobConfigMapper;
import top.binaryx.garen.server.pojo.entity.JobConfigDO;

/**
 * <>
 *
 * @author tim
 * @date 2021-11-10 16:31
 * @since
 */
@SpringBootTest
public class JobConfigServiceTest {
    @Autowired
    JobConfigService service;

    @Autowired
    JobConfigMapper mapper;

    @Test
    public void testCreate() {
        JobConfigDO jobConfigDO = new JobConfigDO();
        jobConfigDO.setJobName("124");
        jobConfigDO.setJobNo(1L);
        int insert = mapper.insert(jobConfigDO);
        System.err.println(jobConfigDO);
    }

}
