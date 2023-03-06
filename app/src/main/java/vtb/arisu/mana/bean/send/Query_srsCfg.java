package vtb.arisu.mana.bean.send;

import vtb.arisu.mana.annotation.Info;
import vtb.arisu.mana.annotation.RspType;
import vtb.arisu.mana.annotation.VtbBean;
import vtb.arisu.mana.bean.OFLType;

/**
 * @by: origami
 * @date: {2022/6/1}
 * @info: SRS配置查询
 * 查询消息（LMT->eNB）：
 * O_FL_LMT_TO_ENB_SRS_CFG_QUERY（0xF09D）
 * 应答消息（eNB->LMT）：
 * O_FL_ENB_TO_LMT_SRS_CFG_QUERY_ACK（0xF09E）
 * @see vtb.arisu.mana.bean.report.Ack_QuerySrsCfg
 **/
@Info("SRS配置查询")
@VtbBean(OFLType.O_FL_LMT_TO_ENB_SRS_CFG_QUERY)
@RspType(OFLType.O_FL_ENB_TO_LMT_SRS_CFG_QUERY_ACK)
public class Query_srsCfg {
}
