package vtb.arisu.mana.bean.send;

import vtb.arisu.mana.annotation.Info;
import vtb.arisu.mana.annotation.RspType;
import vtb.arisu.mana.annotation.VtbBean;
import vtb.arisu.mana.bean.OFLType;
import vtb.mashiro.kanon.base.Bean;

/**
 * @by: origami
 * @date: {2022/5/26}
 * @info: 动态修改定位UE处理配置（LMT -> eNB）
 * 是否立即生效：是
 * 重启是否保留配置：否
 * 应答消息（eNB ->LMT）：O_FL_ENB_TO_LMT_DW_FLOW_DYN_CFG_ACK (0xF0A6)
 * 当通过O_FL_LMT_TO_ENB_DW_FLOW_TYPE_CFG接口配置定位UE的处理流程为方式0时，
 * 如果遇到测量信号不稳定等情况，通过配置此接口可使定位UE掉线重新接入并按方式1处理。
 * 需注意此接口仅对当前定位UE生效一次，重启不保留，之后的定位UE再次接入也不保留
 **/
@Info("动态修改定位UE处理配置")
@VtbBean(OFLType.O_FL_LMT_TO_ENB_DW_FLOW_DYN_CFG)
@RspType(OFLType.O_FL_ENB_TO_LMT_DW_FLOW_DYN_CFG_ACK)
public class Cfg_dwFlowDyn extends Bean {
}
