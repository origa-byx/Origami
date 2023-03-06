package vtb.arisu.mana.bean.send;

import vtb.arisu.mana.annotation.Info;
import vtb.arisu.mana.annotation.RspType;
import vtb.arisu.mana.annotation.VtbBean;
import vtb.arisu.mana.bean.OFLType;
import vtb.mashiro.kanon.base.Bean;

/**
 * @by: origami
 * @date: {2022/5/31}
 * @info: 重定向配置查询
 * 查询消息（LMT->eNB）：O_FL_LMT_TO_ENB_REDIRECT_INFO_CFG_QUERY(0xF03F)
 * 应答消息（eNB->LMT）：
 * O_FL_ENB_TO_LMT_REDIRECT_INFO_CFG_QUERY_ACK (0xF040)
 *
 * @see vtb.arisu.mana.bean.report.Ack_QueryRedirectInfo
 **/
@Info("重定向配置查询")
@VtbBean(OFLType.O_FL_LMT_TO_ENB_REDIRECT_INFO_CFG_QUERY)
@RspType(OFLType.O_FL_ENB_TO_LMT_REDIRECT_INFO_CFG_QUERY_ACK)
public class Query_redirectInfo extends Bean {
}
