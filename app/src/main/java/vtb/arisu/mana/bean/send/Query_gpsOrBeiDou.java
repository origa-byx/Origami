package vtb.arisu.mana.bean.send;

import vtb.arisu.mana.annotation.Info;
import vtb.arisu.mana.annotation.RspType;
import vtb.arisu.mana.annotation.VtbBean;
import vtb.arisu.mana.bean.OFLType;
import vtb.mashiro.kanon.base.Bean;

/**
 * @by: origami
 * @date: {2022/6/1}
 * @info: GPS芯片选择gps或北斗配置查询
 * 查询消息（LMT->eNB）：
 * O_FL_LMT_TO_ENB_GPS_OR_BEIDOU_CFG_QUERY（0xF099）
 * 应答消息（eNB->LMT）：
 * O_FL_ENB_TO_LMT_GPS_OR_BEIDOU_CFG_QUERY_ACK (0xF09A)
 * @see vtb.arisu.mana.bean.report.Ack_QueryGpsOrBeiDou
 **/
@Info("GPS芯片选择gps或北斗配置查询")
@VtbBean(OFLType.O_FL_LMT_TO_ENB_GPS_OR_BEIDOU_CFG_QUERY)
@RspType(OFLType.O_FL_ENB_TO_LMT_GPS_OR_BEIDOU_CFG_QUERY_ACK)
public class Query_gpsOrBeiDou extends Bean {
}
