package vtb.arisu.mana.bean.send;

import vtb.arisu.mana.annotation.Info;
import vtb.arisu.mana.annotation.RspType;
import vtb.arisu.mana.annotation.VtbBean;
import vtb.arisu.mana.bean.OFLType;
import vtb.mashiro.kanon.base.Bean;

/**
 * @by: origami
 * @date: {2022/5/31}
 * @info: 服务小区配置参数查询
 * 查询消息（LMT->eNB）：O_FL_LMT_TO_ENB_GET_ARFCN(0xF027)
 * 应答消息（eNB->LMT）：O_FL_ENB_TO_LMT_ARFCN_IND(0xF028)
 **/
@Info("服务小区配置参数查询")
@VtbBean(OFLType.O_FL_LMT_TO_ENB_GET_ARFCN)
@RspType(OFLType.O_FL_ENB_TO_LMT_ARFCN_IND)
public class Query_servingCell extends Bean {
}
