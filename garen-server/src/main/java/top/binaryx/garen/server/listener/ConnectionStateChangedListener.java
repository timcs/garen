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
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.quartz.Scheduler;
import top.binaryx.garen.server.component.SpringContextHolder;
import top.binaryx.garen.server.service.ScheduleService;
import top.binaryx.garen.server.service.ZookeeperService;

/**
 * TODO
 *
 * @author weihongtian
 * @version v0.1 2019-09-20 21:45 weihongtian Exp $
 */
@Slf4j
public class ConnectionStateChangedListener implements ConnectionStateListener {

    protected final ZookeeperService zookeeperService = SpringContextHolder.getBean(ZookeeperService.class);
    protected final ScheduleService scheduleService = SpringContextHolder.getBean(ScheduleService.class);

    @Override
    public void stateChanged(CuratorFramework client, ConnectionState newState) {
        log.warn("newState.{}.isLeader:{}", newState, zookeeperService.getLeaderLatch().hasLeadership());
        try {
            Scheduler scheduler = scheduleService.getScheduler();
            if (ConnectionState.LOST == newState) {
                log.info("LOST");
                //删除调度,让leader去重新分配ip
//                scheduler.clear();
//                scheduler.standby();
            }

            if (ConnectionState.RECONNECTED == newState) {
                log.info("RECONNECTED");
//                //注册ip
//                if (scheduler.isInStandbyMode() || !scheduler.isStarted()) {
//                    log.info("start scheduler");
//                    scheduler.start();
//                }
//
//                zookeeperService.registryIp();
            }

            if (ConnectionState.SUSPENDED == newState) {
                log.info("SUSPENDED");
            }

            if (ConnectionState.READ_ONLY == newState) {
                log.info("READ_ONLY");
            }

            if (ConnectionState.CONNECTED == newState) {
                log.info("CONNECTED");
            }
        } catch (Exception e) {
            log.error("error.", e);
        }

    }
}
