package vtb.arisu.mana.bean.send;

import vtb.arisu.mana.annotation.Info;
import vtb.arisu.mana.annotation.RspType;
import vtb.arisu.mana.annotation.VtbBean;
import vtb.arisu.mana.bean.OFLType;
import vtb.arisu.mana.bean.report.Ack_JwAddress;
import vtb.mashiro.kanon.base.Bean;

/**
 * @by: origami
 * @date: {2022/5/23}
 * @info: GPS经纬高度查询
 * resp: {@link Ack_JwAddress}
 **/
@Info("GPS经纬高度查询")
@VtbBean(OFLType.O_FL_LMT_TO_ENB_GPS_LOCATION_QUERY)
@RspType(OFLType.O_FL_ENB_TO_LMT_GPS_LOCATION_QUERY_ACK)
public class Query_jwAddress extends Bean {
}
