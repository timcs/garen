package top.binaryx.garen.server.util;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import top.binaryx.garen.server.pojo.dto.JobConfigDTO;
import top.binaryx.garen.server.pojo.dto.JobExecuteLogDTO;
import top.binaryx.garen.server.pojo.entity.JobConfigDO;
import top.binaryx.garen.server.pojo.entity.JobExecuteLogDO;
import top.binaryx.garen.server.pojo.vo.JobConfigVO;
import top.binaryx.garen.server.pojo.vo.JobExecuteLogVO;


public class MapperUtil {

    @Mapper
    public interface JobConfigPojoMapper {

        JobConfigPojoMapper INSTANCE = Mappers.getMapper(JobConfigPojoMapper.class);

        JobConfigDO dto2do(JobConfigDTO dto);

        JobConfigDTO do2dto(JobConfigDO d);

        JobConfigVO dto2vo(JobConfigDTO dto);
    }

    @Mapper
    public interface JobExecuteLogPojoMapper {

        JobExecuteLogPojoMapper INSTANCE = Mappers.getMapper(JobExecuteLogPojoMapper.class);

        JobExecuteLogDO dto2do(JobExecuteLogDTO dto);

        JobExecuteLogDTO do2dto(JobExecuteLogDO d);

        JobExecuteLogVO dto2vo(JobExecuteLogDTO dto);
    }
}
