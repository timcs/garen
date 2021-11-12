package top.binaryx.garen.server.common;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BaseTableAttr {


    public Byte deleted;
    @TableId(type = IdType.AUTO)
    private Long id;
    private String creator;


    private String modifier;


    private LocalDateTime createTime;


    private LocalDateTime modifiedTime;
}
