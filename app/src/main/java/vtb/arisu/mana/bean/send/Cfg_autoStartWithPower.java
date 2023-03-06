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
 * @info: 上电自动激活小区配置
 * 是否立即生效：否
 * 重启是否保留配置：是
 * 应答消息（eNB ->LMT）：
 * O_FL_ENB_TO_LMT_SELF_ACTIVE_CFG_PWR_ON_ACK (0xF03C)
 *
 * 上电自激活配置：
 * 用于配置基站上电启动时是否执行自动激活小区的流程，
 * 默认版本中上电不自动激活小区，进入IDLE状态。
 *
 * Reboot配置：
 * Wl模式下，用于配置除带宽改变时，reboot是否执行自动激活小区的流程，
 * 默认版本中reboot自动激活小区，进入active状态
 **/
@Nya(l = 8)
@Info("上电自动激活小区配置")
@VtbBean(OFLType.O_FL_LMT_TO_ENB_SELF_ACTIVE_CFG_PWR_ON)
@RspType(OFLType.O_FL_ENB_TO_LMT_SELF_ACTIVE_CFG_PWR_ON_ACK)
public class Cfg_autoStartWithPower extends Bean {

    /**
     * 0：上电自动激活小区
     * 1：上电不自动激活小区
     */
    @Nya
    public int u32_SelfActiveCfg;

    /**
     * 仅WL版本有效
     * 0：reboot自动激活小区
     * 1：reboot不自动激活小区
     */
    @Nya(f = 4)
    public int u32_rebootSelfActiveCfg;

}
