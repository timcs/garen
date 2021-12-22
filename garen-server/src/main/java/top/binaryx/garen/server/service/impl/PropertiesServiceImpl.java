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
//            log.error("getPropertiesValue error.", e);
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
