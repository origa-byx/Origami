package vtb.arisu.mana.bean.report;

import vtb.arisu.mana.annotation.Info;
import vtb.arisu.mana.annotation.Nya;
import vtb.arisu.mana.annotation.VtbBean;
import vtb.arisu.mana.bean.OFLType;
import vtb.mashiro.kanon.base.Bean;

/**
 * @by: origami
 * @date: {2022/6/1}
 * @info: GPS芯片选择gps或北斗配置查询
 * @see vtb.arisu.mana.bean.send.Query_gpsOrBeiDou
 **/
@Nya
@Info("GPS芯片选择gps或北斗配置查询-ACK")
@VtbBean(OFLType.O_FL_ENB_TO_LMT_GPS_OR_BEIDOU_CFG_QUERY_ACK)
public class Ack_QueryGpsOrBeiDou extends Bean {

    /**
     * 0：GPS
     * 1：北斗
     */
    @Nya
    public int u32_u32FlagInUse;

}
