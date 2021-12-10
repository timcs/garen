/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package top.binaryx.garen.server.listener;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.leader.LeaderLatchListener;
import top.binaryx.garen.server.component.LeaderHandler;
import top.binaryx.garen.server.component.SpringContextHolder;
import top.binaryx.garen.server.service.ZookeeperService;

/**
 * @author weihongtian
 * @version v0.1 2019-07-04 15:40 weihongtian Exp $
 */
@Slf4j
public class LeaderChangedLatchListener implements LeaderLatchListener {

    private final LeaderHandler leaderHandler = SpringContextHolder.getBean(LeaderHandler.class);
    private final ZookeeperService zookeeperService = SpringContextHolder.getBean(ZookeeperService.class);

    @Override
    public void isLeader() {
        log.info("become leader server.");
        //成为leader 监听ip节点,给任务分配调度器
        //监听 /server/ip节点
        PathChildrenCache childrenCache = zookeeperService.getServerIpPathCache();
        childrenCache.getListenable().addListener(new ServerChangedListener());

        try {
            leaderHandler.migrateGroups();
        } catch (Exception e) {
            log.error("leader migrate group error.");
        }
    }

    @Override
    public void notLeader() {
        log.info("become follower server.");
        zookeeperService.getServerIpPathCache().getListenable().clear();
    }
}