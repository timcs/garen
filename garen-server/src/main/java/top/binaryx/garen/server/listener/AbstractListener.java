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

import top.binaryx.garen.server.component.LeaderHandler;
import top.binaryx.garen.server.component.SpringContextHolder;
import top.binaryx.garen.server.service.ZookeeperService;

/**
 * TODO
 *
 * @author weihongtian
 * @version v0.1 2019-09-18 14:37 weihongtian Exp $
 */
public abstract class AbstractListener {

    protected final LeaderHandler leaderHandler = SpringContextHolder.getBean(LeaderHandler.class);

    protected final ZookeeperService zookeeperService = SpringContextHolder.getBean(ZookeeperService.class);

}
