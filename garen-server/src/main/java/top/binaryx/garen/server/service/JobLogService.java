package top.binaryx.garen.server.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import top.binaryx.garen.server.pojo.dto.JobConfigDTO;
import top.binaryx.garen.server.pojo.dto.JobExecuteLogDTO;

/**
 * <>
 *
 * @author tim
 * @date 2021-11-15 10:53
 * @since
 */
public interface JobLogService {

    JobExecuteLogDTO queryLog(Long id);
    JobExecuteLogDTO queryDetailLog(Long id);

}
