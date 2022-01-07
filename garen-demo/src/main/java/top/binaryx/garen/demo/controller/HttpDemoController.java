package top.binaryx.garen.demo.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * <>
 *
 * @author hongtian.wei
 * @date 2022-01-07 16:59
 * @since
 */
@Slf4j
@RestController
@RequestMapping("/http")
public class HttpDemoController {

    @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, path = "/sync")
    public String sync(HttpServletRequest request) throws Exception {
        log.info("receive request;{}", request.getServletPath());
        printRequest(request);
        Thread.sleep(1000);
        return "ok";
    }

    private void printRequest(HttpServletRequest request) {
        Enumeration<String> headerNames = request.getHeaderNames();

        while (headerNames.hasMoreElements()) {
            String header = headerNames.nextElement();
            String value = request.getHeader(header);
            log.info("{}\t-->\t{}", header, value);
        }

        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            log.info("{}", cookie);
        }

    }

}
