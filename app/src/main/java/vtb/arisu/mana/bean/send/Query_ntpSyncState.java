package vtb.arisu.mana.bean.send;

import vtb.arisu.mana.annotation.Info;
import vtb.arisu.mana.annotation.RspType;
import vtb.arisu.mana.annotation.VtbBean;
import vtb.arisu.mana.bean.OFLType;
import vtb.mashiro.kanon.base.Bean;

/**
 * @by: origami
 * @date: {2022/6/1}
 * @info: NTP同步状态查询
 * 查询消息（LMT->eNB）：
 * O_FL_LMT_TO_ENB_NTP_SYNC_STATE_QUERY（0xF09B）
 * 应答消息（eNB->LMT）：
 * O_FL_ENB_TO_LMT_NTP_SYNC_STATE_QUERY_ACK（0xF09C）
 * @see vtb.arisu.mana.bean.report.Ack_QueryNtpSyncState
 **/
@Info("NTP同步状态查询")
@VtbBean(OFLType.O_FL_LMT_TO_ENB_NTP_SYNC_STATE_QUERY)
@RspType(OFLType.O_FL_ENB_TO_LMT_NTP_SYNC_STATE_QUERY_ACK)
public class Query_ntpSyncState extends Bean {
}
