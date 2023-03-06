package vtb.arisu.mana.bean.send;

import vtb.arisu.mana.annotation.Info;
import vtb.arisu.mana.annotation.RspType;
import vtb.arisu.mana.annotation.VtbBean;
import vtb.arisu.mana.bean.OFLType;
import vtb.mashiro.kanon.base.Bean;

/**
 * @by: origami
 * @date: {2022/6/1}
 * @info: Band功率衰减关系表查询
 * 是否立即生效：是
 * 重启是否保留配置：是
 * 应答消息（eNB ->LMT）：
 * O_FL_ENB_TO_LMT_MULTI_BAND_POWERDEREASE_QUERY_ACK (0xF0AA)
 *
 * 该接口用于查询band和衰减对应关系表。返回的结构体和配置的结构体一致。
 * O_FL_LMT_TO_ENB_MULTI_BAND_POWERDEREASE_QUERY
 * @see vtb.arisu.mana.bean.report.Ack_QueryBandPowerDerease
 **/
@Info("Band功率衰减关系表查询")
@VtbBean(OFLType.O_FL_LMT_TO_ENB_MULTI_BAND_POWERDEREASE_QUERY)
@RspType(OFLType.O_FL_ENB_TO_LMT_MULTI_BAND_POWERDEREASE_QUERY_ACK)
public class Query_bandPowerDerease extends Bean {
}
