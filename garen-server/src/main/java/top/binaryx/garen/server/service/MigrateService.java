package top.binaryx.garen.server.service;

import top.binaryx.garen.server.pojo.dto.MigrateRequest;

import java.util.List;


public interface MigrateService {

    List<Long> migrate(MigrateRequest request) throws Exception;

}
