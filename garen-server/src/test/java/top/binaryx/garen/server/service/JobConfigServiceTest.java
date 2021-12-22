package top.binaryx.garen.server.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import top.binaryx.garen.server.mapper.JobConfigMapper;
import top.binaryx.garen.server.pojo.entity.JobConfigDO;


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

    @Test
    public void t() {
        Page<JobConfigDO> page = Page.of(1, 10, true);

        QueryWrapper<JobConfigDO> wrapper = new QueryWrapper<>();
        wrapper.eq("status", 0);

        Page results = mapper.selectPage(page, wrapper);
        System.out.println(results);

    }

}
