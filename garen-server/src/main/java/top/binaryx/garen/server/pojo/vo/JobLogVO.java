package top.binaryx.garen.server.pojo.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;


@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class JobLogVO extends JobExecuteLogVO {

    List<JobExecuteDetailLogVO> detailLogs;

}
