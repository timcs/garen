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
package top.binaryx.garen.server.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.EmbeddedValueResolverAware;
import org.springframework.stereotype.Service;
import org.springframework.util.StringValueResolver;
import top.binaryx.garen.common.Constant;
import top.binaryx.garen.server.service.PropertiesService;

@Slf4j
@Service
public class PropertiesServiceImpl implements EmbeddedValueResolverAware, PropertiesService {

    private StringValueResolver resolver;

    @Override
    public void setEmbeddedValueResolver(StringValueResolver resolver) {
        this.resolver = resolver;
    }

    @Override
    public String getPropertiesValue(String name) {
        try {
            return resolver.resolveStringValue(String.format(Constant.PROPERTY, name));
        } catch (Exception e) {
            log.error("getPropertiesValue error.", e);
            return null;
        }
    }

    @Override
    public int getInt(String name, int defaultValue) {
        String value = getPropertiesValue(name);
        if (StringUtils.isBlank(value)) {
            return defaultValue;
        }
        return Integer.parseInt(value);
    }

    @Override
    public String getString(String name, String defaultValue) {
        String value = getPropertiesValue(name);
        if (StringUtils.isBlank(value)) {
            return defaultValue;
        }
        return value;
    }

    @Override
    public boolean getBoolean(String name, boolean defaultValue) {
        String value = getPropertiesValue(name);
        if (StringUtils.isBlank(value)) {
            return defaultValue;
        }
        return Boolean.parseBoolean(value);
    }
}
