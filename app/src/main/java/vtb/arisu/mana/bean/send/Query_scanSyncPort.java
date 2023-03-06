package vtb.arisu.mana.bean.send;

import vtb.arisu.mana.annotation.Info;
import vtb.arisu.mana.annotation.RspType;
import vtb.arisu.mana.annotation.VtbBean;
import vtb.arisu.mana.bean.OFLType;
import vtb.mashiro.kanon.base.Bean;

/**
 * @by: origami
 * @date: {2022/6/1}
 * @info: 扫频/同步端口查询
 * 是否立即生效：是
 * 重启是否保留配置：否
 * 应答消息（eNB ->LMT）：
 * O_FL_ENB_TO_LMT_REM_PORT_QUERY_ACK (0xF0AE)
 * 查询扫频和同步流程使用的端口，扫频端口由F07d接口配置，同步端口由F05e接口配置，两接口互不影响。
 * O_FL_LMT_TO_ENB_REM_PORT_QUERY
 * @see vtb.arisu.mana.bean.report.Ack_QueryScanSyncPort
 **/
@Info("扫频/同步端口查询")
@VtbBean(OFLType.O_FL_LMT_TO_ENB_REM_PORT_QUERY)
@RspType(OFLType.O_FL_ENB_TO_LMT_REM_PORT_QUERY_ACK)
public class Query_scanSyncPort extends Bean {
}
