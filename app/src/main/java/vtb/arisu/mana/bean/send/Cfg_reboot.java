package vtb.arisu.mana.bean.send;

import vtb.arisu.mana.annotation.Info;
import vtb.arisu.mana.annotation.Nya;
import vtb.arisu.mana.annotation.RspType;
import vtb.arisu.mana.annotation.VtbBean;
import vtb.arisu.mana.bean.OFLType;
import vtb.mashiro.kanon.base.Bean;

/**
 * @by: origami
 * @date: {2022/5/23}
 * @info: 基站重启配置
 * 是否立即生效：是
 * 重启是否保留配置：否
 * 应答消息（eNB ->LMT）：O_FL_ENB_TO_LMT_REBOOT_ACK (0xF00C)
 * 此接口用于客户端指示基站执行reboot操作。基站收到此消息，先回复ACK，再执行reboot。基站处于任何状态都会处理该消息
 **/
@Nya
@Info("基站重启配置")
@VtbBean(OFLType.O_FL_LMT_TO_ENB_REBOOT_CFG)
@RspType(OFLType.O_FL_ENB_TO_LMT_REBOOT_ACK)
public class Cfg_reboot extends Bean {

    /**
     * 指示基站重启后是否采用现有参数配置自动激活小区
     * 0：reboot后自动激活小区
     * 1：reboot后不自激活小区
     */
    @Nya
    public int u32_SelfActiveCfg;

}
