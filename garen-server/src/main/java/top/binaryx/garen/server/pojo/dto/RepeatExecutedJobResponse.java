package top.binaryx.garen.server.pojo.dto;

import lombok.Data;

import java.util.List;


@Data
public class RepeatExecutedJobResponse {
    List<RepeatExecutedJobDTO> list;
}
