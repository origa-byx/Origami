package vtb.arisu.mana.bean.send;

import vtb.arisu.mana.annotation.Info;
import vtb.arisu.mana.annotation.RspType;
import vtb.arisu.mana.annotation.VtbBean;
import vtb.arisu.mana.bean.OFLType;
import vtb.mashiro.kanon.base.Bean;

/**
 * @by: origami
 * @date: {2022/5/31}
 * @info: 接收增益和发射功率查询
 * 查询消息（LMT->eNB）：O_FL_LMT_TO_ENB_RXGAIN_POWER_DEREASE_QUERY(0xF031)
 * 应答消息（eNB->LMT）：
 * O_FL_ENB_TO_LMT_RXGAIN_POWER_DEREASE_QUERY_ACK (0xF032)
 * @see vtb.arisu.mana.bean.report.Ack_QueryRxGainPower
 **/
@Info("接收增益和发射功率查询")
@VtbBean(OFLType.O_FL_LMT_TO_ENB_RXGAIN_POWER_DEREASE_QUERY)
@RspType(OFLType.O_FL_ENB_TO_LMT_RXGAIN_POWER_DEREASE_QUERY_ACK)
public class Query_rxGainPower extends Bean {
}
