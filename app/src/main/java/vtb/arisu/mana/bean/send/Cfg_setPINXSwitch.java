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
 * @info: 单板管脚电平输出控制
 * 是否立即生效：是
 * 重启是否保留配置：否
 * 应答消息（eNB ->LMT）：O_FL_ENB_TO_LMT_PINX_SWITCH_CFG_ACK (0xF0A0)
 *     此接口用于控制V3单板PIN1管脚的电平输出，以及V5板IO6管脚输出控制。
 * 单板自主判断当前板卡类型，若是V3则该接口控制PIN1输出，若是V5板卡，控制IO6电平输出。
 * V3 对应板卡上扩展IO1里的IO1（J7-IO1）；V5对应板卡上IO6.
 **/
@Nya
@Info("单板管脚电平输出控制")
@VtbBean(OFLType.O_FL_LMT_TO_ENB_PINX_SWITCH_CFG)
@RspType(OFLType.O_FL_ENB_TO_LMT_PINX_SWITCH_CFG_ACK)
public class Cfg_setPINXSwitch extends Bean {

    /**
     * 0: 输出低电平 1：输出高电平
     */
    @Nya(l = 1)
    public int u8_PinxSwitch;

    /**
     * 预留
     */
    @Nya(f = 1, l = 3)
    public int u8$3_Res;

}
