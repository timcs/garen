package top.binaryx.garen.server.executor;

import top.binaryx.garen.common.enums.ProtocolTypeEnum;
import top.binaryx.garen.server.component.SpringContextHolder;

import java.util.Map;
import java.util.Objects;


public class ExecutorContext {

    private final Map<ProtocolTypeEnum, Class> executorMap;

    public ExecutorContext(Map<ProtocolTypeEnum, Class> executorMap) {
        this.executorMap = executorMap;
    }

    public JobExecutor getExecutor(ProtocolTypeEnum type) {
        Class clazz = executorMap.get(type);
        if (Objects.isNull(clazz)) {
            throw new IllegalArgumentException("not found handler for type: " + type);
        }
        return (JobExecutor) SpringContextHolder.getBean(clazz);
    }
}
