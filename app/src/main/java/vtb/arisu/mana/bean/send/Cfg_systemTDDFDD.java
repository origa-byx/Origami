package vtb.arisu.mana.bean.send;

import vtb.arisu.mana.annotation.Info;
import vtb.arisu.mana.annotation.Nya;
import vtb.arisu.mana.annotation.RspType;
import vtb.arisu.mana.annotation.VtbBean;
import vtb.arisu.mana.bean.OFLType;
import vtb.mashiro.kanon.base.Bean;

/**
 * @by: origami
 * @date: {2022/5/25}
 * @info: 系统模式（TDD/FDD）配置
 * 是否立即生效：否
 * 重启是否保留配置：是
 * 应答消息（eNB ->LMT）：O_FL_ENB_TO_LMT_SYS_MODE_ACK(0xF002)
 *
 * 此消息用于设置系统模式（TDD/FDD），基站收到此配置，记录启动模式，
 * 客户端下发reboot指令或者重新上下电给基站，基站会根据配置的模式启动系统。
 * 当sysMode=2时，系统根据硬件一个GPIO（PIN5）管脚的输入信号决定启动TDD还是FDD，PINI5悬空时启动TDD，PIN5接地时启动FDD，仅V3系列板卡支持此功能。
 * 仅TDD和FDD共版版本才支持此设置，非共版版本基站会回复失败
 **/
@Nya
@Info("系统模式（TDD/FDD）配置")
@VtbBean(OFLType.O_FL_LMT_TO_ENB_SYS_MODE_CFG)
@RspType(OFLType.O_FL_ENB_TO_LMT_SYS_MODE_ACK)
public class Cfg_systemTDDFDD extends Bean {

    /**
     * 协议栈模式
     * 0: TDD，
     * 1: FDD, .
     * 2: 硬件上区分启动是TDD还FDD
     */
    @Nya
    public int u32_sysMode;


}
