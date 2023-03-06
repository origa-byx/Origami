package vtb.arisu.mana.bean.send;

import vtb.arisu.mana.annotation.Info;
import vtb.arisu.mana.annotation.RspType;
import vtb.arisu.mana.annotation.VtbBean;
import vtb.arisu.mana.bean.OFLType;
import vtb.mashiro.kanon.base.Bean;

/**
 * @by: origami
 * @date: {2022/6/1}
 * @info: RX口功率值查询
 * 是否立即生效：是
 * 重启是否保留配置：否
 * 应答消息（eNB ->LMT）：
 * O_FL_ENB_TO_LMT_GET_RX_PARAMS_ACK(0xF0AC)
 *
 * 该接口用于获取RX口的功率值，仅在小区去激活状态有效。
 * 该查询结果仅作参考。
 * O_FL_LMT_TO_ENB_GET_RX_PARAMS
 * @see vtb.arisu.mana.bean.report.Ack_QueryRxPower
 **/
@Info("RX口功率值查询")
@VtbBean(OFLType.O_FL_LMT_TO_ENB_GET_RX_PARAMS)
@RspType(OFLType.O_FL_ENB_TO_LMT_GET_RX_PARAMS_ACK)
public class Query_rxPower extends Bean {
}
