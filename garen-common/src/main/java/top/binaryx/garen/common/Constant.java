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

package top.binaryx.garen.common;

/**
 * TODO
 *
 * @author weihongtian
 * @version v0.1 2019-09-17 13:02 weihongtian Exp $
 */
public interface Constant {

    String SLANT = "/";

    String GROUP_NAME = "groupName";

    String JOB_ID = "jobId";

    String JOB_PROTOCOL_TYPE = "jobProtocolType";

    String CLIENT_ID = "clientId";

    String JOB_NAME = "jobName";

    String SERVER_URL = "http://%s:%d/server";

    String REMOVE_GROUP_URL = SERVER_URL + "/migrate/group/remove";

    String TAKE_GROUP_URL = SERVER_URL + "/migrate/group/take";

    String DISCARD_GROUP_URL = SERVER_URL + "/migrate/discard";

    String TAKE_JOB_URL = SERVER_URL + "/migrate/job/take";

    String REMOVE_JOB_URL = SERVER_URL + "/migrate/job/remove";

    String PROPERTY = "${%s}";

    String SYSTEM = "system";

    String ZK_NAMESPACE = "garen";

}
