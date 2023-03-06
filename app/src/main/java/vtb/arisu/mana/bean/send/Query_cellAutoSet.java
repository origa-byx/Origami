package vtb.arisu.mana.bean.send;

import vtb.arisu.mana.annotation.Info;
import vtb.arisu.mana.annotation.RspType;
import vtb.arisu.mana.annotation.VtbBean;
import vtb.arisu.mana.bean.OFLType;
import vtb.mashiro.kanon.base.Bean;

/**
 * @by: origami
 * @date: {2022/5/31}
 * @info: 电小区自激活配置查询
 * 查询消息（LMT->eNB）：O_FL_LMT_TO_ENB_SELF_ACTIVE_CFG_PWR_ON_QUERY (0xF041)
 * 应答消息（eNB->LMT）：
 * O_FL_ENB_TO_LMT_SELF_ACTIVE_CFG_PWR_ON_QUERY_ACK (0xF042)
 * @see vtb.arisu.mana.bean.report.Ack_QueryCellAutoSet
 **/
@Info("电小区自激活配置查询")
@VtbBean(OFLType.O_FL_LMT_TO_ENB_SELF_ACTIVE_CFG_PWR_ON_QUERY)
@RspType(OFLType.O_FL_ENB_TO_LMT_SELF_ACTIVE_CFG_PWR_ON_QUERY_ACK)
public class Query_cellAutoSet extends Bean {
}
