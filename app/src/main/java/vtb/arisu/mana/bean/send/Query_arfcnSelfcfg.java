package vtb.arisu.mana.bean.send;

import vtb.arisu.mana.annotation.Info;
import vtb.arisu.mana.annotation.RspType;
import vtb.arisu.mana.annotation.VtbBean;
import vtb.arisu.mana.bean.OFLType;
import vtb.mashiro.kanon.base.Bean;

/**
 * @by: origami
 * @date: {2022/6/1}
 * @info: 频点自配置后台频点列表查询
 * 查询消息（LMT->eNB）：O_FL_LMT_TO_ENB_SELFCFG_ARFCN_QUERY (0xF04D)
 * 应答消息（eNB->LMT）：
 * O_FL_ENB_TO_LMT_SELFCFG_ARFCN_QUERY_ACK (0xF04E)
 * @see vtb.arisu.mana.bean.report.Ack_QueryArfcnSelfcfg
 **/
@Info("频点自配置后台频点列表查询")
@VtbBean(OFLType.O_FL_LMT_TO_ENB_SELFCFG_ARFCN_QUERY)
@RspType(OFLType.O_FL_ENB_TO_LMT_SELFCFG_ARFCN_QUERY_ACK)
public class Query_arfcnSelfcfg extends Bean {

}
