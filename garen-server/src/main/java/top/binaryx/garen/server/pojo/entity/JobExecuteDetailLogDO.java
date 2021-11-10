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

package top.binaryx.garen.server.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.binaryx.garen.server.common.BaseTableAttr;

import java.time.LocalDateTime;

/**
 * TODO
 *
 * @author weihongtian
 * @version v0.1 2019-09-18 21:45 weihongtian Exp $
 */
@Data
@TableName( "job_execute_detail_log")
@EqualsAndHashCode(callSuper = true)
public class JobExecuteDetailLogDO extends BaseTableAttr {


    private Long jobExecuteLogId;


    private String targetAddress;

    /**
     * 开始执行时间
     */
    private LocalDateTime startTime;

    /**
     * 结束执行时间
     */
    private LocalDateTime endTime;

    /**
     * 响应码
     */
    private String requestBody;

    /**
     * 响应描述
     */
    private String responseBody;

    /**
     * 执行状态
     */
    private Integer status;

    /**
     * 备注,描述
     */
    private String memo;


}
