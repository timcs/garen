package top.binaryx.garen.server.common;

import lombok.Getter;
import lombok.Setter;
import top.binaryx.garen.common.Constant;

@Getter
@Setter
public final class GarenContext {

    private Integer serverPort = 10323;
    private String zkServer;
    private String zkNameSpace = Constant.ZK_NAMESPACE;

    public static GarenContext getInstance() {
        return GarenContextHolder.getContext();
    }

    private static class GarenContextHolder {
        private static GarenContext context = new GarenContext();

        public static GarenContext getContext() {
            return context;
        }
    }
}
