package top.binaryx.garen.server.component;

import com.google.common.collect.Queues;

import java.util.List;
import java.util.Queue;

/**
 * <>
 *
 * @author hongtian.wei
 * @date 2022-01-25 10:47
 * @since
 */
public class RetryQueen {
    private Queue list = Queues.newArrayBlockingQueue(10000);

    public static void put(Object e) {

    }
    public static void put(List<Object> e) {

    }
}
