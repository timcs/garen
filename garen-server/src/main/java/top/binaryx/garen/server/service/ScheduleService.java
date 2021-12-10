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
package top.binaryx.garen.server.service;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import top.binaryx.garen.server.pojo.dto.JobConfigDTO;

import java.util.List;
import java.util.Map;

/**
 * @author weihongtian
 */
public interface ScheduleService {

    /**
     * 判断任务是否已加载到调度引擎
     *
     * @param groupName 任务群组
     * @param jobName   任务名称
     * @return 是否已加载到调度引擎
     */
    boolean isScheduled(String groupName, String jobName) throws SchedulerException;

    /**
     * 安排任务执行
     *
     * @param jobConfigDTO 任务配置
     */
    void scheduleJob(JobConfigDTO jobConfigDTO) throws SchedulerException;

    /**
     * 停止任务，从Scheduler中移除任务
     *
     * @param groupId 任务群组
     * @param jobId   任务名称
     */
    void stopJob(JobConfigDTO jobConfigDTO) throws SchedulerException;

    void stopJob(Integer groupId, Long jobId) throws SchedulerException;

    void stopJob(Map<Integer, List<JobConfigDTO>> groupList) throws SchedulerException;

    /**
     * 暂停任务
     *
     * @param groupName 任务群组
     * @param jobName   任务名称
     */
    void pauseJob(String groupName, String jobName) throws SchedulerException;

    /**
     * 唤醒暂停的任务
     *
     * @param groupName 任务群组
     * @param jobName   任务名称
     */
    void resumeJob(String groupName, String jobName) throws SchedulerException;

    /**
     * 触发任务执行一次
     *
     * @param groupName 任务群组
     * @param jobName   任务名称
     */
    void triggerJob(String groupName, String jobName) throws SchedulerException;


    Scheduler getScheduler();

    void shutDownScheduler(Scheduler scheduler);
}
