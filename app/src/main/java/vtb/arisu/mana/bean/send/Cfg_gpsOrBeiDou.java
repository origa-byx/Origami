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
 * @info: GPS芯片选择gps或北斗配置
 * 是否立即生效：否
 * 重启是否保留配置：是
 * 应答消息（eNB ->LMT）：
 * O_FL_ENB_TO_LMT_GPS_OR_BEIDOU_CFG_ACK (0xF098)
 * 此消息接口用于配置gps芯片选择gps或者北斗，配置完重启生效
 **/
@Nya
@Info("GPS芯片选择gps或北斗配置")
@VtbBean(OFLType.O_FL_LMT_TO_ENB_GPS_OR_BEIDOU_CFG)
@RspType(OFLType.O_FL_ENB_TO_LMT_GPS_OR_BEIDOU_CFG_ACK)
public class Cfg_gpsOrBeiDou extends Bean {

    /**
     * 0：GPS
     * 1：北斗
     */
    @Nya(l = 1)
    public int u8_u8FlagInUse;

    /**
     * 保留字节
     */
    @Nya(f = 1, l = 3)
    public int u8$3_Res;

}
