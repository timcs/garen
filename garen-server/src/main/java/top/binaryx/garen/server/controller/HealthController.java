package top.binaryx.garen.server.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Api(tags = "健康检查")
@RestController
@RequestMapping
public class HealthController {

    @ApiOperation("健康检查")
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("success");
    }
}
