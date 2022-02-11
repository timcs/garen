package top.binaryx.garen.server.service;

/**
 * Callback
 *
 */
public interface Callback<P, R> {

    /**
     * 执行回调
     *
     * @param param 回调参数
     * @return 结果
     */
    R call(P param);
}
