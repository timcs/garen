package top.binaryx.garen.server.listener;

import top.binaryx.garen.server.component.LeaderHandler;
import top.binaryx.garen.server.component.SpringContextHolder;
import top.binaryx.garen.server.service.ZookeeperService;


public abstract class AbstractListener {

    protected final LeaderHandler leaderHandler = SpringContextHolder.getBean(LeaderHandler.class);

    protected final ZookeeperService zookeeperService = SpringContextHolder.getBean(ZookeeperService.class);

}
