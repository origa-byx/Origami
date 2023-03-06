package vtb.arisu.mana.bean.send;

import vtb.arisu.mana.annotation.Info;
import vtb.arisu.mana.annotation.RspType;
import vtb.arisu.mana.annotation.VtbBean;
import vtb.arisu.mana.bean.OFLType;
import vtb.mashiro.kanon.base.Bean;

/**
 * @by: origami
 * @date: {2022/5/31}
 * @info: GPS经纬高度查询
 * 查询消息（LMT->eNB）：O_FL_LMT_TO_ENB_GPS_LOCATION_QUERY (0xF05C)
 * 应答消息（eNB->LMT）：O_FL_ENB_TO_LMT_GPS_LOCATION_QUERY_ACK(0xF05D)
 *
 * 基站启动时，会自动获取GPS经纬度信息，客户端可通过此查询接口获取的经纬度信息
 * @see vtb.arisu.mana.bean.report.Ack_QueryGps
 **/
@Info("GPS经纬高度查询")
@VtbBean(OFLType.O_FL_LMT_TO_ENB_GPS_LOCATION_QUERY)
@RspType(OFLType.O_FL_ENB_TO_LMT_GPS_LOCATION_QUERY_ACK)
public class Query_gps extends Bean {
}
