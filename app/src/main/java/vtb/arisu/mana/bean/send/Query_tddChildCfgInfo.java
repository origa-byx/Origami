package vtb.arisu.mana.bean.send;

import vtb.arisu.mana.annotation.Info;
import vtb.arisu.mana.annotation.RspType;
import vtb.arisu.mana.annotation.VtbBean;
import vtb.arisu.mana.bean.OFLType;
import vtb.mashiro.kanon.base.Bean;

/**
 * @by: origami
 * @date: {2022/5/31}
 * @info: TDD子帧配置和上行功控系数查询
 * 查询消息（LMT->eNB）：O_FL_LMT_TO_ENB_TDD_SUBFRAME_ASSIGNMENT_QUERY(0xF04B)
 * 应答消息（eNB->LMT）：
 * O_FL_ENB_TO_LMT_TDD_SUBFRAME_ASSIGNMENT_QUERY_ACK(0xF04C)
 * @see vtb.arisu.mana.bean.report.Ack_QueryTddChildCfgInfo
 **/
@Info("TDD子帧配置和上行功控系数查询")
@VtbBean(OFLType.O_FL_LMT_TO_ENB_TDD_SUBFRAME_ASSIGNMENT_QUERY)
@RspType(OFLType.O_FL_ENB_TO_LMT_TDD_SUBFRAME_ASSIGNMENT_QUERY_ACK)
public class Query_tddChildCfgInfo extends Bean {
}
