package vtb.arisu.mana.bean.report;

import vtb.arisu.mana.annotation.Info;
import vtb.arisu.mana.annotation.Nya;
import vtb.arisu.mana.annotation.VtbBean;
import vtb.arisu.mana.bean.OFLType;

/**
 * @by: origami
 * @date: {2022/5/31}
 * @info: 接收增益和发射功率查询-ACK
 * @see vtb.arisu.mana.bean.send.Query_rxGainPower
 **/
@Nya(l = 12)
@Info("接收增益和发射功率查询-ACK")
@VtbBean(OFLType.O_FL_ENB_TO_LMT_RXGAIN_POWER_DEREASE_QUERY_ACK)
public class Ack_QueryRxGainPower {

    /**
     * 寄存器中的值，
     * 实际生效的值（FDD模式下仅在建立完小区查询，该值有效）
     * 0~127
     */
    @Nya(l = 1)
    public int u8_u8RxGainValueFromReg;

    /**
     * 数据库中的保存值，重启保留生效的值
     * 0~127
     */
    @Nya(f = 1, l = 1)
    public int u8_u8RxGainValueFromMib;

    /**
     * 寄存器中的值，实际生效的值（FDD模式下仅在建立完小区查询，该值有效）
     * 0~255
     */
    @Nya(f = 2, l = 1)
    public int u8_u8PowerDereaseValueFromReg;

    /**
     * 数据库中的保存值，重启保留生效的值
     * 0~255
     */
    @Nya(f = 3, l = 1)
    public int u8_u8PowerDereaseValueFromMib;

    /**
     * FDD AGC开关
     * 0：关闭
     * 1：打开
     */
    @Nya(f = 4, l = 1)
    public int u8_u8AgcFlag;

    /**
     * 只在FDD模式下有效，寄存器中的值，实际生效的值,
     * 该值只有在扫频完成后，建立小区前查询有效
     * 0~127
     */
    @Nya(f = 5, l = 1)
    public int u8_u8SnfRxGainValueFromReg;

    /**
     * eeprom中的保存值，重启保留生效的值
     * 0~127
     */
    @Nya(f = 6, l = 1)
    public int u8_u8SnfRxGainValueFromMib;

    /**
     * 保留字段
     */
    @Nya(f = 7, l = 1)
    public int u8$1_Res;

    /**
     * 寄存器生效值=u8RxGainValueFromMib+4*delta
     * -10~10
     */
    @Nya(f = 8)
    public int s32_pwrDecreDelta;
}
