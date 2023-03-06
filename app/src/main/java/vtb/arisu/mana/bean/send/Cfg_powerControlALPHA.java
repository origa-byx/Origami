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
 * @info: 上行功控Alpha系数配置
 * 是否立即生效：否
 * 重启是否保留配置：是
 * 应答消息（eNB ->LMT）：
 * O_FL_ENB_TO_LMT_UL_POWER_CONTROL_ALPHA_CFG_ACK(0xF093)
 *
 *     此消息接口用于当FDD开启配置TDD/FDD小区的SIB2中的上行功控系数，小区去激活状态下配置立即生效。
 * 本接口配置TDD出厂配置默认值为70，FDD默认出厂值为80。在空旷地带测试抓号，建议该值配置成80
 **/
@Nya
@Info("上行功控Alpha系数配置")
@VtbBean(OFLType.O_FL_LMT_TO_ENB_UL_POWER_CONTROL_ALPHA_CFG)
@RspType(OFLType.O_FL_ENB_TO_LMT_UL_POWER_CONTROL_ALPHA_CFG_ACK)
public class Cfg_powerControlALPHA extends Bean {

    /**
     * Sib2中上行功控系数
     * （ 0，40，50，60，70，80，90，100 ）
     * ps: 配置的（0，40，50，60，70，80，90，100）
     * 分别对应sib2信息alpha值的（0，1，2，3，4，5，6，7）
     */
    @Nya(l = 1)
    public int u8_UlPowerAlpha;

    /**
     * 预留
     */
    @Nya(f = 1, l = 3)
    public int u8$3_Res;

}
