package top.binaryx.garen.server.component;

import com.google.common.collect.Maps;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;
import top.binaryx.garen.common.ClassScanner;
import top.binaryx.garen.common.annotation.ExecutorType;
import top.binaryx.garen.common.enums.ProtocolTypeEnum;
import top.binaryx.garen.server.executor.ExecutorContext;

import java.util.Map;


@Component
public class ExecutorTypeProcessor implements BeanFactoryPostProcessor {

    private static final String HANDLER_PACKAGE = "top.binaryx.garen.server.executor";

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        Map<ProtocolTypeEnum, Class> executorMap = Maps.newHashMapWithExpectedSize(3);
        ClassScanner.scan(HANDLER_PACKAGE, ExecutorType.class).forEach(clazz -> {
            // 获取注解中的类型值
            ProtocolTypeEnum type = clazz.getAnnotation(ExecutorType.class).protocolType();
            // 将注解中的类型值作为key，对应的类作为value，保存在Map中
            executorMap.put(type, clazz);
        });
        // 初始化HandlerContext，将其注册到spring容器中
        ExecutorContext context = new ExecutorContext(executorMap);
        beanFactory.registerSingleton(ExecutorContext.class.getName(), context);
    }
}
