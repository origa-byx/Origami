package vtb.arisu.mana.bean.send;

import vtb.arisu.mana.annotation.Info;
import vtb.arisu.mana.annotation.Nya;
import vtb.arisu.mana.annotation.RspType;
import vtb.arisu.mana.annotation.VtbBean;
import vtb.arisu.mana.bean.OFLType;
import vtb.mashiro.kanon.base.Bean;

/**
 * @by: origami
 * @date: {2022/6/1}
 * @info: IMSI黑白名单查询
 * 查询消息（LMT->eNB）：O_FL_LMT_TO_ENB_CONTROL_LIST_QUERY (0xF043)
 * 应答消息（eNB->LMT）：
 * O_FL_ENB_TO_LMT_CONTROL_LIST _QUERY_ACK (0xF044)
 * @see vtb.arisu.mana.bean.report.Ack_QueryImsiBlackList
 **/
@Nya
@Info("IMSI黑白名单查询")
@VtbBean(OFLType.O_FL_LMT_TO_ENB_CONTROL_LIST_QUERY)
@RspType(OFLType.O_FL_ENB_TO_LMT_CONTROL_LIST)
public class Query_imsiBlackList extends Bean {

    /**
     * 名单类型
     * 0：查询黑名单
     * 1：查询白名单
     */
    @Nya(l = 1)
    public int u8_ControlListType;

    /**
     * 保留字节
     */
    @Nya(f = 1, l = 3)
    public int u8$3_Res;

}
