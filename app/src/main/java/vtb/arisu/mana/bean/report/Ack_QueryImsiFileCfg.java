package vtb.arisu.mana.bean.report;

import vtb.arisu.mana.annotation.Info;
import vtb.arisu.mana.annotation.VtbBean;
import vtb.arisu.mana.bean.OFLType;
import vtb.arisu.mana.bean.send.GetIMSI_withFtp;
import vtb.mashiro.kanon.base.Bean;

/**
 * @by: origami
 * @date: {2022/6/1}
 * @info: IMSI文件上传配置查询-ACK
 * @see vtb.arisu.mana.bean.send.Query_imsiFileCfg
 **/
@Info("IMSI文件上传配置查询-ACK")
@VtbBean(OFLType.O_FL_ENB_TO_LMT_UPLOAD_IMSI_FILE_CFG_QUERY_ACK)
public class Ack_QueryImsiFileCfg extends Bean {

    /**
     * isCfgFtpServer 字段在查询接口中无意义
     * @see GetIMSI_withFtp#u8_isCfgFtpServer
     */
    public final GetIMSI_withFtp uploadImsiFileCfg = new GetIMSI_withFtp();

    @Override
    public void fromBytes(byte[] body) {
        uploadImsiFileCfg.fromBytes(body);
    }

    @Override
    public int getSize() {
        return uploadImsiFileCfg.getSize();
    }

    @Override
    public byte[] toBytes() {
       return uploadImsiFileCfg.toBytes();
    }
}
