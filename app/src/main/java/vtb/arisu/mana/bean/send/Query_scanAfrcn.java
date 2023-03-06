package vtb.arisu.mana.bean.send;

import vtb.arisu.mana.annotation.Info;
import vtb.arisu.mana.annotation.RspType;
import vtb.arisu.mana.annotation.VtbBean;
import vtb.arisu.mana.bean.OFLType;
import vtb.mashiro.kanon.base.Bean;

/**
 * @by: origami
 * @date: {2022/6/1}
 * @info: 扫频频点查询
 * 是否立即生效：是
 * 重启是否保留配置：否
 * 查询消息（LMT ->eNB）：O_FL_LMT_TO_ENB_REM_CFG_QUERY(0xF037)
 * 应答消息（eNB-> LMT）：O_FL_LMT_TO_ENB_REM_CFG_QUERY_ACK(0xF038)
 *
 * O_FL_LMT_TO_ENB_REM_CFG_QUERY
 * @see vtb.arisu.mana.bean.report.Ack_QueryScanAfrcn
 **/
@Info("扫频频点查询")
@VtbBean(OFLType.O_FL_LMT_TO_ENB_REM_CFG_QUERY)
@RspType(OFLType.O_FL_LMT_TO_ENB_REM_CFG_QUERY_ACK)
public class Query_scanAfrcn extends Bean {
}
