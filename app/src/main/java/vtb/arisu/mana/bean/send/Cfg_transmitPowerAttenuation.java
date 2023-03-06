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
 * @info: 发射功率衰减配置
 * 是否立即生效：是
 * 重启是否保留配置：可配置
 * 应答消息（eNB ->LMT）：O_FL_ENB_TO_LMT_SYS_PWR1_DEREASE_ACK(0xF016)
 * 该接口用于配置基站发送通道的衰减值，用于客户校准整机输出功率。衰减值每加4，基站输出功率增加1dB衰减。无衰减时，即衰减值为0x00时，基站输出功率范围在-1dbm~-2dbm，每块单板会有差异。
 * 基站实际输出功率 = 零衰减功率 - 衰减值（Pwr1Derease*0.25）
 * 例如：基站输出功率为-1dB，当衰减值设置为0x28，输出功率为-11dBm
 **/
@Nya(l = 8)
@Info("发射功率衰减配置")
@VtbBean(OFLType.O_FL_LMT_TO_ENB_SYS_PWR1_DEREASE_CFG)
@RspType(OFLType.O_FL_ENB_TO_LMT_SYS_PWR1_DEREASE_ACK)
public class Cfg_transmitPowerAttenuation extends Bean {

    /**
     * 功率衰减，每步长代表0.25dB
     * 0x00~0xFF
     */
    @Nya
    public int u32_Pwr1Derease;

    /**
     * 配置值是否保存到配置，重启之后也保留
     * 1: 重启保留配置
     * 0：设备重启不保留配置
     */
    @Nya(f = 4, l = 1)
    public int u8_IsSave;

    /**
     * 保留字节
     */
    @Nya(f = 5, l = 3)
    public int u8$3_Res;

}
