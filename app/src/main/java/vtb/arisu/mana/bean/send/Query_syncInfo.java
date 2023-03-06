package vtb.arisu.mana.bean.send;

import vtb.arisu.mana.annotation.Info;
import vtb.arisu.mana.annotation.RspType;
import vtb.arisu.mana.annotation.VtbBean;
import vtb.arisu.mana.bean.OFLType;
import vtb.mashiro.kanon.base.Bean;

/**
 * @by: origami
 * @date: {2022/5/31}
 * @info: 基站同步信息查询
 * 查询消息（LMT->eNB）：O_FL_LMT_TO_ENB_SYNC_INFO_QUERY(0xF02D)
 * 应答消息（eNB->LMT）：O_FL_ENB_TO_LMT_SYNC_INFO_QUERY_ACK(0xF02E)
 *
 * 此消息用于客户端查询基站当前的同步方式和同步状态
 * @see vtb.arisu.mana.bean.report.Ack_QuerySyncInfo
 **/
@Info("基站同步信息查询")
@VtbBean(OFLType.O_FL_LMT_TO_ENB_SYNC_INFO_QUERY)
@RspType(OFLType.O_FL_ENB_TO_LMT_SYNC_INFO_QUERY_ACK)
public class Query_syncInfo extends Bean {
}
