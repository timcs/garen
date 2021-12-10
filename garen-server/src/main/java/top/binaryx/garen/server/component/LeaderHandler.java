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

package top.binaryx.garen.server.component;

import com.google.common.cache.Cache;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.binaryx.garen.server.helper.NodePathHelper;
import top.binaryx.garen.server.service.ZookeeperService;

import java.util.*;
import java.util.stream.Collectors;

/**
 * LeaderHandler
 *
 * @author weihongtian
 * @version v0.1 2019-09-17 19:04 weihongtian Exp $
 */
@Slf4j
@Component
public class LeaderHandler {

//    @Autowired
//    MigrateService migrateService;

    @Autowired
    ZookeeperService zookeeperService;


    public void serverOffline() {
        migrateGroups();
    }

    public void serverOnline() {
//        //获取groups
//        Map<String, List<Integer>> groupsMap = getGroups();
//
//        //构建缓存
//        Cache<String, Integer> serverCache = CacheUtil.getServerCache();
//        serverCache.invalidateAll();
//
//        List<String> servers = zookeeperService.getChildrenKeys(NodePathHelper.getServerIpNode());
//        servers.forEach(server -> {
//            serverCache.put(server, groupsMap.getOrDefault(server, Lists.newArrayList()).size());
//        });
//
//        //计算需要释放的group数
//        int totalNamespaceCount = groupsMap.values().stream().mapToInt(List::size).sum();
//        int avg = Math.floorDiv(totalNamespaceCount, servers.size());
//        for (Map.Entry<String, List<Integer>> entry : groupsMap.entrySet()) {
//            String ip = entry.getKey();
//            List<Integer> groups = entry.getValue();
//            if (CollectionUtils.isEmpty(groups) || groups.size() <= avg) {
//                break;
//            }
//
//            int count = groups.size() - avg;
//            String url = String.format(Constant.DISCARD_GROUP_URL, ip, GarenContext.getInstance().getServerPort());
//            MigrateHttpRequest request = new MigrateHttpRequest();
//            request.setCount(count);
//            request.setGroups(groups);
//            try {
//                //调用接口转移,从list里面释放count个
//                String httpResp = HttpClientUtil.patchJson(url, request);
//                MigrateHttpResponse response = new Gson().fromJson(httpResp, MigrateHttpResponse.class);
//                if (!MessageEnum.isSuccess(response.getCode())) {
//                    throw new GarenException(String.format("discard groups:%s from:%s fail.msg:%s", request, ip, response));
//                }
//            } catch (Exception e) {
//                log.error("post:{},param:{}. error.", url, request, e);
//            }
//        }
    }

    public void migrateGroups() {
//        List<Integer> groups = zookeeperService.getChildrenKeys(NodePathHelper.getGroupsNode())
//                .parallelStream().map(str -> Integer.parseInt(str)).collect(Collectors.toList());
//
//        //获取需要迁移的group列表.构建机器负载
//        List<Integer> migrateList = Lists.newArrayList();
//        Map<String, Integer> groupLoad = Maps.newHashMap();
//        groups.parallelStream().forEach(group -> {
//            String jobsNode = NodePathHelper.getJobsNode(group);
//            List<String> jobs = zookeeperService.getChildrenKeys(jobsNode);
//            if (CollectionUtils.isEmpty(jobs)) {
//                //改group下没有任务.直接删除应用 todo
//                zookeeperService.remove(NodePathHelper.getGroupNode(group));
//                return;
//            }
//            String ip = zookeeperService.get(NodePathHelper.getOwnerNode(group));
//            if (Objects.isNull(ip) || ip.isEmpty()) {
//                //没有机器接管.加入迁移列表
//                migrateList.add(group);
//            } else {
//                //构建应用负载
//                Math.addExact(groupLoad.putIfAbsent(ip, 0), 1);
//            }
//        });
//
//        Cache<String, Integer> serverCache = CacheUtil.getServerCache();
//        serverCache.putAll(groupLoad);
//
//        //给需要迁移的group分配机器
//        Map<String, List<Integer>> migrateMap = Maps.newHashMap();
//        migrateList.parallelStream().forEach(group -> {
//            try {
//                String ip = getLowerIp(serverCache);
//                if (ip.equals(AddressUtil.getLocalHost())) {
//                    //本机直接接管
//                    migrateService.takeGroup(group);
//                } else {
//                    migrateMap.putIfAbsent(ip, Lists.newArrayList()).add(group);
//                }
//            } catch (Exception e) {
//                log.info("prepare migrateMap error.", e);
//            }
//        });
//
//        //开始迁移
//        migrateMap.forEach((ip, list) -> {
//            try {
//                String url = String.format(Constant.TAKE_GROUP_URL, ip, GarenContext.getInstance().getServerPort());
//
//                MigrateHttpRequest request = new MigrateHttpRequest();
//                request.setGroups(list);
//
//                String httpResp = HttpClientUtil.patchJson(url, request);
//                MigrateHttpResponse response = new Gson().fromJson(httpResp, MigrateHttpResponse.class);
//
//                if (!MessageEnum.isSuccess(response.getCode())) {
//                    throw new GarenException(String.format("discard groups:%s from:%s fail.msg:%s", request, ip, response));
//                }
//            } catch (Exception e) {
//                log.error("migrate group:{} fail.", list, e);
//            }
//        });
    }

    public void addGroup(Integer groupId) throws Exception {
//        try {
//            Cache<String, Integer> serverCache = CacheUtil.getServerCache();
//            String ip = getLowerIp(serverCache);
//
//            MigrateHttpRequest request = new MigrateHttpRequest();
//            request.setGroups(Lists.newArrayList(groupId));
//
//            String url = String.format(Constant.TAKE_GROUP_URL, ip, GarenContext.getInstance().getServerPort());
//            String httpResp = HttpClientUtil.patchJson(url, request);
//
//            MigrateHttpResponse response = new Gson().fromJson(httpResp, MigrateHttpResponse.class);
//            if (MessageEnum.isSuccess(response.getCode())) {
//                serverCache.put(ip, serverCache.getIfPresent(ip) + 1);
//            } else {
//                throw new GarenException(String.format("add group:%s to:%s fail.error msg:%s", groupId, ip, response));
//            }
//        } catch (Exception e) {
//            throw e;
//        }
    }

    public void removeGroup(String ip, Integer groupId) throws Exception {
//        try {
//            MigrateHttpRequest request = new MigrateHttpRequest();
//            request.setGroups(Lists.newArrayList(groupId));
//
//            String url = String.format(Constant.REMOVE_GROUP_URL, ip, GarenContext.getInstance().getServerPort());
//            String httpResp = HttpClientUtil.patchJson(url, request);
//
//            MigrateHttpResponse response = new Gson().fromJson(httpResp, MigrateHttpResponse.class);
//            if (MessageEnum.isSuccess(response.getCode())) {
//                Cache<String, Integer> serverCache = CacheUtil.getServerCache();
//                serverCache.put(ip, serverCache.getIfPresent(ip) - 1);
//                return;
//            }
//            throw new GarenException(String.format("remove group:%s from:%s fail.error msg:%s", groupId, ip, response));
//        } catch (Exception e) {
//            throw e;
//        }
    }

    public String getLowerIp(Cache<String, Integer> serverCache) {
        if (Objects.isNull(serverCache) || 0L == serverCache.size()) {
            serverCache = buildServerCache();
        }

        //排序
        List<Map.Entry<String, Integer>> entries = new ArrayList(serverCache.asMap().entrySet());
        Collections.sort(entries, Comparator.comparingInt(Map.Entry::getValue));
        return entries.get(0).getKey();
    }

    /**
     * 创建机器负载缓存
     *
     * @return
     */
    private Cache<String, Integer> buildServerCache() {
//        Cache<String, Integer> serverCache = CacheUtil.getServerCache();
//        serverCache.invalidateAll();
//
//        Map<String, List<Integer>> groups = getGroups();
//
//        List<String> servers = zookeeperService.getChildrenKeys(NodePathHelper.getServerIpNode());
//
//        servers.forEach(server -> {
//            serverCache.put(server, groups.getOrDefault(server, Lists.newArrayList()).size());
//        });
//
//        return serverCache;
        return null;
    }

    /**
     * 获取所有机器接管的group列表.忽略没有接管的group
     * key:ip,value:group列表
     *
     * @return
     */
    private Map<String, List<Integer>> getGroups() {
        Map<String, List<Integer>> map = Maps.newHashMap();

        List<String> groups = zookeeperService.getChildrenKeys(NodePathHelper.GROUPS_NODE);

        List<Integer> groupIds = groups.stream().map(str -> Integer.parseInt(str)).collect(Collectors.toList());
        for (Integer group : groupIds) {
            String ownerNode = NodePathHelper.getOwnerNode(group);
            String ip = zookeeperService.get(ownerNode);

            if (Objects.isNull(ip) || ip.isEmpty()) {
                log.warn("serverIp is null.group :{}", group);
                continue;
            }

            map.putIfAbsent(ip, Lists.newArrayList()).add(group);
        }
        return map;
    }
}
