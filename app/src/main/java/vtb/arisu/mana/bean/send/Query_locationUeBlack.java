package vtb.arisu.mana.bean.send;

import vtb.arisu.mana.annotation.Info;
import vtb.arisu.mana.annotation.RspType;
import vtb.arisu.mana.annotation.VtbBean;
import vtb.arisu.mana.bean.OFLType;
import vtb.mashiro.kanon.base.Bean;

/**
 * @by: origami
 * @date: {2022/6/1}
 * @info: 定位模式黑名单查询
 * 查询消息（LMT->eNB）：
 * O_FL_LMT_TO_ENB_LOCATION_UE_BLACKLIST_QUERY (0xF055)
 * 应答消息（eNB->LMT）：
 * O_FL_ENB_TO_LMT_LOCATION_UE_BLACKLIST_QUERY_ACK (0xF056)
 *     该接口最多支持100UE，基站根据实际BlacklUENum个数发送字节数
 * @see vtb.arisu.mana.bean.report.Ack_QueryLocationUeBlack
 **/
@Info("定位模式黑名单查询")
@VtbBean(OFLType.O_FL_LMT_TO_ENB_LOCATION_UE_BLACKLIST_QUERY)
@RspType(OFLType.O_FL_ENB_TO_LMT_LOCATION_UE_BLACKLIST_QUERY_ACK)
public class Query_locationUeBlack extends Bean {
}
