package vtb.arisu.mana.bean.send;

import vtb.arisu.mana.annotation.Info;
import vtb.arisu.mana.annotation.RspType;
import vtb.arisu.mana.annotation.VtbBean;
import vtb.arisu.mana.bean.OFLType;
import vtb.mashiro.kanon.base.Bean;

/**
 * @by: origami
 * @date: {2022/6/1}
 * @info: GPS同步模式下的pps1s偏移量查询
 * 查询消息（LMT->eNB）：O_FL_LMT_TO_ENB_GPS1PPS_QUERY (0xF073)
 * 应答消息（eNB->LMT）：O_FL_ENB_TO_LMT_GSP1PPS_QUERY_ACK (0xF074)
 **/
@Info("GPS同步模式下的pps1s偏移量查询")
@VtbBean(OFLType.O_FL_LMT_TO_ENB_GPS1PPS_QUERY)
@RspType(OFLType.O_FL_ENB_TO_LMT_GSP1PPS_QUERY_ACK)
public class Query_gspPpsls extends Bean {

}
