package vtb.arisu.mana.bean.send;

import vtb.arisu.mana.annotation.Info;
import vtb.arisu.mana.annotation.RspType;
import vtb.arisu.mana.annotation.VtbBean;
import vtb.arisu.mana.bean.OFLType;
import vtb.mashiro.kanon.base.Bean;

/**
 * @by: origami
 * @date: {2022/6/1}
 * @info: IMSI文件上传配置查询
 * 查询消息（LMT->eNB）：
 * O_FL_LMT_TO_ENB_UPLOAD_IMSI_FILE_CFG_QUERY（0xF094）
 * 应答消息（eNB->LMT）：
 * O_FL_ENB_TO_LMT_UPLOAD_IMSI_FILE_CFG_QUERY_ACK（0xF095）
 * @see vtb.arisu.mana.bean.report.Ack_QueryImsiFileCfg
 **/
@Info("IMSI文件上传配置查询")
@VtbBean(OFLType.O_FL_LMT_TO_ENB_UPLOAD_IMSI_FILE_CFG_QUERY)
@RspType(OFLType.O_FL_ENB_TO_LMT_UPLOAD_IMSI_FILE_CFG_QUERY_ACK)
public class Query_imsiFileCfg extends Bean {
}
