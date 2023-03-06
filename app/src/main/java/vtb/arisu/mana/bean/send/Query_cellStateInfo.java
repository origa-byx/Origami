package vtb.arisu.mana.bean.send;

import vtb.arisu.mana.annotation.Info;
import vtb.arisu.mana.annotation.RspType;
import vtb.arisu.mana.annotation.VtbBean;
import vtb.arisu.mana.bean.OFLType;
import vtb.mashiro.kanon.base.Bean;

/**
 * @by: origami
 * @date: {2022/5/31}
 * @info: 小区状态信息查询
 * 查询消息（LMT->eNB）：O_FL_LMT_TO_ENB_CELL_STATE_INFO_QUERY(0xF02F)
 * 应答消息（eNB->LMT）：
 * O_FL_ENB_TO_LMT_CELL_STATE_INFO_QUERY_ACK (0xF030)
 *
 * 此消息用于查询基站的小区状态信息
 * @see vtb.arisu.mana.bean.report.Ack_QueryCellStateInfo
 **/
@Info("小区状态信息查询")
@VtbBean(OFLType.O_FL_LMT_TO_ENB_CELL_STATE_INFO_QUERY)
@RspType(OFLType.O_FL_ENB_TO_LMT_CELL_STATE_INFO_QUERY_ACK)
public class Query_cellStateInfo extends Bean {
}
