package top.binaryx.garen.common.annotation;

import top.binaryx.garen.common.enums.ProtocolTypeEnum;

import java.lang.annotation.*;


@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface ExecutorType {

    ProtocolTypeEnum protocolType();
}
