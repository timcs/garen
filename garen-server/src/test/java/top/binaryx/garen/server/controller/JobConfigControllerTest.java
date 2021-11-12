package top.binaryx.garen.server.controller;

import cn.hutool.core.util.RandomUtil;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import top.binaryx.garen.server.mapper.JobConfigMapper;
import top.binaryx.garen.server.pojo.dto.JobConfigRequest;
import top.binaryx.garen.server.service.JobConfigService;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;


@WebMvcTest(JobConfigController.class)
public class JobConfigControllerTest {

    @MockBean
    JobConfigMapper mapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private JobConfigService userService;

    @Test
    public void signup() throws Exception {
        JobConfigRequest request = new JobConfigRequest();
        request.setJobName(RandomUtil.randomString(10));

        String content = new Gson().toJson(request);

        MvcResult mvcResult = this.mockMvc.perform(
                        post("/job/config").contentType("application/json").content(content))
                .andReturn();
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(content().string(equalTo("1")));
        String contentAsString = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        System.out.println(contentAsString);
    }
}
