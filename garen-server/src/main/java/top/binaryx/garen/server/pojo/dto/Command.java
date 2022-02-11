package top.binaryx.garen.server.pojo.dto;

import lombok.Data;
import top.binaryx.garen.common.enums.CommandEnum;

/**
 * Command
 *
 * @author xiongjie001
 * @version v0.1 2017-1-5 11:59 xiongjie001 Exp $
 */
@Data
public class Command<T> {

    CommandEnum command;

    T value;

    public Command(CommandEnum command, T value) {
        this.command = command;
        this.value = value;
    }
}
