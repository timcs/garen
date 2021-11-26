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
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.recipes.leader.LeaderLatchListener;
import top.binaryx.garen.common.util.CacheUtil;
import top.binaryx.garen.server.component.LeaderHandler;
import top.binaryx.garen.server.component.SpringContextHolder;
import top.binaryx.garen.server.service.ZookeeperService;

/**
 * TODO 类实现描述
 *
 * @author weihongtian
 * @version v0.1 2019-07-04 15:40 weihongtian Exp $
 */
@Slf4j
public class LeaderChangedLatchListener implements LeaderLatchListener {

    private final LeaderHandler leaderHandler = SpringContextHolder.getBean(LeaderHandler.class);
    private final ZookeeperService zookeeperService = SpringContextHolder.getBean(ZookeeperService.class);

    GroupAddListener groupAddListener = new GroupAddListener();
    GroupRemoveListener groupRemoveListener = new GroupRemoveListener();
    JobConfigListener jobConfigListener = new JobConfigListener();
    OwnerLostListener ownerLostListener = new OwnerLostListener();

    @Override
    public void isLeader() {
        log.info("become leader server.");

        try {
            leaderHandler.migrateGroups();
        } catch (Exception e) {
            log.error("leader migrate group error.");
        }

        //监听
        PathChildrenCache childrenCache = zookeeperService.getServerIpPathCache();
        childrenCache.getListenable().addListener(new ServerChangeListener());

        TreeCache groupsTreeCache = zookeeperService.getGroupsTreeCache();

        groupsTreeCache.getListenable().addListener(groupAddListener);
        groupsTreeCache.getListenable().addListener(groupRemoveListener);
        groupsTreeCache.getListenable().addListener(jobConfigListener);
        groupsTreeCache.getListenable().addListener(ownerLostListener);
    }

    @Override
    public void notLeader() {
        log.info("become follower server.");
        CacheUtil.getServerCache().cleanUp();

        zookeeperService.getServerIpPathCache().getListenable().clear();
        zookeeperService.getGroupsTreeCache().getListenable().removeListener(groupAddListener);
        zookeeperService.getGroupsTreeCache().getListenable().removeListener(groupRemoveListener);
        zookeeperService.getGroupsTreeCache().getListenable().removeListener(jobConfigListener);
        zookeeperService.getGroupsTreeCache().getListenable().removeListener(ownerLostListener);
    }
}