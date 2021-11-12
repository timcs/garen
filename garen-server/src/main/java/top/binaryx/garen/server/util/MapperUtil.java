package top.binaryx.garen.server.util;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import top.binaryx.garen.server.pojo.dto.JobConfigDTO;
import top.binaryx.garen.server.pojo.entity.JobConfigDO;
import top.binaryx.garen.server.pojo.vo.JobConfigVO;


public class MapperUtil {

    @Mapper
    public interface JobConfigMapper {

        JobConfigMapper INSTANCE = Mappers.getMapper(JobConfigMapper.class);

        JobConfigDO dto2do(JobConfigDTO dto);

        JobConfigDTO do2dto(JobConfigDO d);

        JobConfigVO dto2vo(JobConfigDTO dto);
    }
}
