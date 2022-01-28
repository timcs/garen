package top.binaryx.garen.server.thread;

import cn.hutool.http.HttpUtil;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import top.binaryx.garen.server.pojo.dto.HttpCallbackRequest;

/**
 * <>
 *
 * @author hongtian.wei
 * @date 2022-01-25 15:42
 * @since
 */
@Slf4j
public class BaseThread {
    protected void callback(String url, HttpCallbackRequest request) {
        try {
            log.info("start call back:{},params:{}", url, request);
            String response = HttpUtil.post(url, new Gson().toJson(request));
            log.info("call back response:{}", response);
        } catch (Exception e) {
            log.error("[checkNotExecute] call back error.", e);
        }
    }
}
