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
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;

/**
 * 应用上线下线监听器
 * /groups/{groupId}
 *
 * @author weihongtian
 * @version v0.1 2019-09-17 18:07 weihongtian Exp $
 */
@Slf4j
public class ServerChangedListener extends AbstractListener implements PathChildrenCacheListener {

    @Override
    public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
        if (PathChildrenCacheEvent.Type.CHILD_ADDED != event.getType()
                || PathChildrenCacheEvent.Type.CHILD_REMOVED != event.getType()) {
            return;
        }

        //应用上线
        if (PathChildrenCacheEvent.Type.CHILD_ADDED == event.getType()) {
            leaderHandler.serverOnline();
            return;
        }

        //应用下线
        if (PathChildrenCacheEvent.Type.CHILD_REMOVED == event.getType()) {
            leaderHandler.serverOffline();
            return;
        }
    }
}
