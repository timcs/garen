package top.binaryx.garen.server.util;

import javax.annotation.Generated;
import top.binaryx.garen.server.pojo.dto.JobConfigDTO;
import top.binaryx.garen.server.pojo.entity.JobConfigDO;
import top.binaryx.garen.server.pojo.vo.JobConfigVO;
import top.binaryx.garen.server.util.MapperUtil.JobConfigPojoMapper;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2021-12-21T16:55:22+0800",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 1.8.0_301 (Oracle Corporation)"
)
public class MapperUtil$JobConfigPojoMapperImpl implements JobConfigPojoMapper {

    @Override
    public JobConfigDO dto2do(JobConfigDTO dto) {
        if ( dto == null ) {
            return null;
        }

        JobConfigDO jobConfigDO = new JobConfigDO();

        jobConfigDO.setDeleted( dto.getDeleted() );
        jobConfigDO.setId( dto.getId() );
        jobConfigDO.setCreator( dto.getCreator() );
        jobConfigDO.setModifier( dto.getModifier() );
        jobConfigDO.setCreateTime( dto.getCreateTime() );
        jobConfigDO.setModifiedTime( dto.getModifiedTime() );
        jobConfigDO.setJobName( dto.getJobName() );
        jobConfigDO.setJobNo( dto.getJobNo() );
        jobConfigDO.setJobParam( dto.getJobParam() );
        jobConfigDO.setJobDesc( dto.getJobDesc() );
        jobConfigDO.setProtocolType( dto.getProtocolType() );
        jobConfigDO.setJobType( dto.getJobType() );
        jobConfigDO.setCron( dto.getCron() );
        jobConfigDO.setTargetAddress( dto.getTargetAddress() );
        jobConfigDO.setExecutorIp( dto.getExecutorIp() );
        jobConfigDO.setStatus( dto.getStatus() );

        return jobConfigDO;
    }

    @Override
    public JobConfigDTO do2dto(JobConfigDO d) {
        if ( d == null ) {
            return null;
        }

        JobConfigDTO jobConfigDTO = new JobConfigDTO();

        jobConfigDTO.setDeleted( d.getDeleted() );
        jobConfigDTO.setId( d.getId() );
        jobConfigDTO.setCreator( d.getCreator() );
        jobConfigDTO.setModifier( d.getModifier() );
        jobConfigDTO.setCreateTime( d.getCreateTime() );
        jobConfigDTO.setModifiedTime( d.getModifiedTime() );
        jobConfigDTO.setJobName( d.getJobName() );
        jobConfigDTO.setJobNo( d.getJobNo() );
        jobConfigDTO.setJobParam( d.getJobParam() );
        jobConfigDTO.setJobDesc( d.getJobDesc() );
        jobConfigDTO.setProtocolType( d.getProtocolType() );
        jobConfigDTO.setJobType( d.getJobType() );
        jobConfigDTO.setCron( d.getCron() );
        jobConfigDTO.setTargetAddress( d.getTargetAddress() );
        jobConfigDTO.setExecutorIp( d.getExecutorIp() );
        jobConfigDTO.setStatus( d.getStatus() );

        return jobConfigDTO;
    }

    @Override
    public JobConfigVO dto2vo(JobConfigDTO dto) {
        if ( dto == null ) {
            return null;
        }

        JobConfigVO jobConfigVO = new JobConfigVO();

        jobConfigVO.setDeleted( dto.getDeleted() );
        jobConfigVO.setId( dto.getId() );
        jobConfigVO.setCreator( dto.getCreator() );
        jobConfigVO.setModifier( dto.getModifier() );
        jobConfigVO.setCreateTime( dto.getCreateTime() );
        jobConfigVO.setModifiedTime( dto.getModifiedTime() );
        jobConfigVO.setJobName( dto.getJobName() );
        jobConfigVO.setJobNo( dto.getJobNo() );
        jobConfigVO.setJobParam( dto.getJobParam() );
        jobConfigVO.setJobDesc( dto.getJobDesc() );
        jobConfigVO.setProtocolType( dto.getProtocolType() );
        jobConfigVO.setJobType( dto.getJobType() );
        jobConfigVO.setCron( dto.getCron() );
        jobConfigVO.setTargetAddress( dto.getTargetAddress() );
        jobConfigVO.setExecutorIp( dto.getExecutorIp() );
        jobConfigVO.setStatus( dto.getStatus() );

        return jobConfigVO;
    }
}
