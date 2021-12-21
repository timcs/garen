package top.binaryx.garen.server.util;

import javax.annotation.Generated;
import top.binaryx.garen.server.pojo.dto.JobExecuteLogDTO;
import top.binaryx.garen.server.pojo.entity.JobExecuteLogDO;
import top.binaryx.garen.server.pojo.vo.JobExecuteLogVO;
import top.binaryx.garen.server.util.MapperUtil.JobExecuteLogPojoMapper;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2021-12-21T16:55:22+0800",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 1.8.0_301 (Oracle Corporation)"
)
public class MapperUtil$JobExecuteLogPojoMapperImpl implements JobExecuteLogPojoMapper {

    @Override
    public JobExecuteLogDO dto2do(JobExecuteLogDTO dto) {
        if ( dto == null ) {
            return null;
        }

        JobExecuteLogDO jobExecuteLogDO = new JobExecuteLogDO();

        jobExecuteLogDO.setDeleted( dto.getDeleted() );
        jobExecuteLogDO.setId( dto.getId() );
        jobExecuteLogDO.setCreator( dto.getCreator() );
        jobExecuteLogDO.setModifier( dto.getModifier() );
        jobExecuteLogDO.setCreateTime( dto.getCreateTime() );
        jobExecuteLogDO.setModifiedTime( dto.getModifiedTime() );
        jobExecuteLogDO.setJobId( dto.getJobId() );
        jobExecuteLogDO.setExecuteNo( dto.getExecuteNo() );
        jobExecuteLogDO.setStartTime( dto.getStartTime() );
        jobExecuteLogDO.setEndTime( dto.getEndTime() );
        jobExecuteLogDO.setNextTime( dto.getNextTime() );
        jobExecuteLogDO.setExecutorIP( dto.getExecutorIP() );
        jobExecuteLogDO.setTargetAddress( dto.getTargetAddress() );
        jobExecuteLogDO.setRequestBody( dto.getRequestBody() );
        jobExecuteLogDO.setResponseBody( dto.getResponseBody() );
        jobExecuteLogDO.setStatus( dto.getStatus() );
        jobExecuteLogDO.setMemo( dto.getMemo() );

        return jobExecuteLogDO;
    }

    @Override
    public JobExecuteLogDTO do2dto(JobExecuteLogDO d) {
        if ( d == null ) {
            return null;
        }

        JobExecuteLogDTO jobExecuteLogDTO = new JobExecuteLogDTO();

        jobExecuteLogDTO.setDeleted( d.getDeleted() );
        jobExecuteLogDTO.setId( d.getId() );
        jobExecuteLogDTO.setCreator( d.getCreator() );
        jobExecuteLogDTO.setModifier( d.getModifier() );
        jobExecuteLogDTO.setCreateTime( d.getCreateTime() );
        jobExecuteLogDTO.setModifiedTime( d.getModifiedTime() );
        jobExecuteLogDTO.setJobId( d.getJobId() );
        jobExecuteLogDTO.setExecuteNo( d.getExecuteNo() );
        jobExecuteLogDTO.setStartTime( d.getStartTime() );
        jobExecuteLogDTO.setEndTime( d.getEndTime() );
        jobExecuteLogDTO.setNextTime( d.getNextTime() );
        jobExecuteLogDTO.setExecutorIP( d.getExecutorIP() );
        jobExecuteLogDTO.setTargetAddress( d.getTargetAddress() );
        jobExecuteLogDTO.setRequestBody( d.getRequestBody() );
        jobExecuteLogDTO.setResponseBody( d.getResponseBody() );
        jobExecuteLogDTO.setStatus( d.getStatus() );
        jobExecuteLogDTO.setMemo( d.getMemo() );

        return jobExecuteLogDTO;
    }

    @Override
    public JobExecuteLogVO dto2vo(JobExecuteLogDTO dto) {
        if ( dto == null ) {
            return null;
        }

        JobExecuteLogVO jobExecuteLogVO = new JobExecuteLogVO();

        jobExecuteLogVO.setDeleted( dto.getDeleted() );
        jobExecuteLogVO.setId( dto.getId() );
        jobExecuteLogVO.setCreator( dto.getCreator() );
        jobExecuteLogVO.setModifier( dto.getModifier() );
        jobExecuteLogVO.setCreateTime( dto.getCreateTime() );
        jobExecuteLogVO.setModifiedTime( dto.getModifiedTime() );
        jobExecuteLogVO.setJobId( dto.getJobId() );
        jobExecuteLogVO.setExecuteNo( dto.getExecuteNo() );
        jobExecuteLogVO.setStartTime( dto.getStartTime() );
        jobExecuteLogVO.setEndTime( dto.getEndTime() );
        jobExecuteLogVO.setNextTime( dto.getNextTime() );
        jobExecuteLogVO.setExecutorIP( dto.getExecutorIP() );
        jobExecuteLogVO.setTargetAddress( dto.getTargetAddress() );
        jobExecuteLogVO.setRequestBody( dto.getRequestBody() );
        jobExecuteLogVO.setResponseBody( dto.getResponseBody() );
        jobExecuteLogVO.setStatus( dto.getStatus() );
        jobExecuteLogVO.setMemo( dto.getMemo() );

        return jobExecuteLogVO;
    }
}
