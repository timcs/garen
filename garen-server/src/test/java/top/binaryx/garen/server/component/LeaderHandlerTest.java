package top.binaryx.garen.server.component;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import top.binaryx.garen.common.util.AddressUtil;
import top.binaryx.garen.server.helper.NodePathHelper;
import top.binaryx.garen.server.pojo.dto.JobConfigDTO;
import top.binaryx.garen.server.service.JobConfigService;
import top.binaryx.garen.server.service.ZookeeperService;

import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.when;

/**
 * <>
 *
 * @author hongtian.wei
 * @date 2021-12-22 14:27
 * @since
 */
@SpringBootTest
public class LeaderHandlerTest {

    @Autowired
    LeaderHandler handler;

    @MockBean
    JobConfigService jobConfigService;

    @MockBean
    ZookeeperService zookeeperService;

    List<JobConfigDTO> jobs = Lists.newArrayList();
    List<String> servers = Lists.newArrayList();

    @BeforeEach
    public void before() throws Exception {
        final Long[] id = {0L};
        Map<Integer, String> map = Maps.newHashMap();
        map.put(2, "");
        map.put(3, AddressUtil.getLocalHost());
        map.put(5, "127.0.0.2");
        map.put(6, "127.0.0.3");
        map.forEach((k, v) -> {
            while (k-- > 0) {
                JobConfigDTO dto = new JobConfigDTO();
                dto.setId(id[0]++);
                dto.setExecutorIp(v);
                jobs.add(dto);
            }
        });

        servers.add(AddressUtil.getLocalHost());
        servers.add("127.0.0.2");
        servers.add("127.0.0.3");
    }

    public static void main(String[] args) {
        Double i = Math.ceil(Math.floorDiv(16L, 3L));
        System.out.println(i);
        System.out.println(Math.floorDiv(16L, 3L));
        int ceil = (int) Math.ceil((double) 16 / 3);
        System.out.println(ceil);
    }

    @Test
    public void testBuildCache() {
//        when(jobConfigService.findAll()).thenReturn(jobs);
//        Cache<String, List<Long>> cache = handler.buildCache();
//        cache.asMap().forEach((k, v) -> System.out.println(k + ":" + v));
    }

    @Test
    public void testGetLowerIp() {
        when(jobConfigService.findAll()).thenReturn(jobs);
        String lowerIp = handler.getLowerIp();
        System.out.println(lowerIp);
    }

    @Test
    public void testMigrateJobs() throws Exception {
        when(jobConfigService.findAll()).thenReturn(jobs);
        when(zookeeperService.getChildrenKeys(NodePathHelper.getServerIpNode())).thenReturn(servers);
        handler.migrateJobs();
    }

    @Test
    public void testHandleDieAway() throws Exception {
        when(jobConfigService.findAll()).thenReturn(jobs);
        Map<String, List<Long>> jobLoad = handler.getJobLoad();
        jobLoad.put(AddressUtil.getLocalHost(), Lists.newArrayList(100L, 200L));

        handler.handleDieAway(jobLoad, servers);
    }

}
