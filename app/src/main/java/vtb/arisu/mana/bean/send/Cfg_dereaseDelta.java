package vtb.arisu.mana.bean.send;

import vtb.arisu.mana.annotation.Info;
import vtb.arisu.mana.annotation.Nya;
import vtb.arisu.mana.annotation.RspType;
import vtb.arisu.mana.annotation.VtbBean;
import vtb.arisu.mana.bean.OFLType;
import vtb.mashiro.kanon.base.Bean;

/**
 * @by: origami
 * @date: {2022/5/31}
 * @info: 衰减配置delta值设置
 * 是否立即生效：是
 * 重启是否保留配置：是
 * 应答消息（eNB ->LMT）：O_FL_LMT_TO_ENB_DEREASE_DELTA_CFG_ACK (0xF0D9)
 * 此接口用于配置每个单板的输出功率相对于0dbm的delta值，用于设置衰减，衰减寄存器生效值=配置衰减+4*delta。
 **/
@Nya
@Info("衰减配置delta值设置")
@VtbBean(OFLType.O_FL_LMT_TO_ENB_DEREASE_DELTA_CFG)
@RspType(OFLType.O_FL_LMT_TO_ENB_DEREASE_DELTA_CFG_ACK)
public class Cfg_dereaseDelta extends Bean {

    /**
     * 寄存器生效值=配置衰减+4*delta
     *  -10 ~ 10 db
     */
    @Nya(l = 1, signed = true)
    public int s8_pwrDecreDelta;

    /**
     * 保留字节
     */
    @Nya(f = 1, l = 3)
    public int u8$3_padding;

}
