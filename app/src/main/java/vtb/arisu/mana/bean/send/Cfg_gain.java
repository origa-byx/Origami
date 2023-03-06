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
 * @info: 接收增益配置
 * 是否立即生效：是
 * 重启是否保留配置：可配置
 * 应答消息（eNB ->LMT）：O_FL_ENB_TO_LMT_SYS_RxGAIN_ACK(0xF014)
 * 该接口用于配置基站9361寄存器的接收增益，表示将接收到的来自UE的信号放大多少倍。
 *
 * 接收增益:
 * 出厂默认值：FDD=40dB\TDD=52dB；此接收增益为9361寄存器的值。支持可配。
 * 客户设置接收增益的建议参考：
 * 基带板接收增益+功放底噪放增益-3dB线路衰减  = 63dB。
 **/
@Nya(l = 8)
@Info("接收增益配置")
@VtbBean(OFLType.O_FL_LMT_TO_ENB_SYS_RxGAIN_CFG)
@RspType(OFLType.O_FL_ENB_TO_LMT_SYS_RxGAIN_ACK)
public class Cfg_gain extends Bean {

    /**
     * 接收增益
     * 0~127(dB)
     */
    @Nya
    public int u32_Rxgain;

    /**
     * 配置值是否保存到配置，重启之后也保留
     * 0: not save,
     * 1: save
     */
    @Nya(f = 4, l = 1)
    public int u8_RxGainSaveFlag;

    /**
     * 配置该增益是修改rx口增益还是snf口增益
     * 注：仅FDD有效
     * 对于TDD，该字段无意义，基站不做判断。
     * 0：rx
     * 1：snf
     */
    @Nya(f = 5, l = 1)
    public int u8_RxOrSnfFlag;

    /**
     * 保留字节
     */
    @Nya(f = 6, l = 2)
    public int u8$2_Res;

}
