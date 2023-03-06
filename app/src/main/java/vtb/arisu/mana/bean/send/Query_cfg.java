package vtb.arisu.mana.bean.send;

import vtb.arisu.mana.annotation.Info;
import vtb.arisu.mana.annotation.Nya;
import vtb.arisu.mana.annotation.RspType;
import vtb.arisu.mana.annotation.VtbBean;
import vtb.arisu.mana.bean.OFLType;
import vtb.mashiro.kanon.base.Bean;

/**
 * @by: origami
 * @date: {2022/5/31}
 * @info: 基站基本信息查询
 * 查询消息（LMT->eNB）：O_FL_LMT_TO_ENB_BASE_INFO_QUERY(0xF02B)
 * 应答消息（eNB -> LMT）：O_FL_ENB_TO_LMT_BASE_INFO_QUERY_ACK(0xF02C)
 *
 * 此消息用于客户端查询一些基站的基本信息，比如版本号，MAC地址，SN等。
 * 7.非当前工作分区版本号查询
 * 比如查询结果为M:BaiStation128W_TDD_FDD_R004C0000G01B008SPC006
 * M表示非工作分区为master,对应版本号为BaiStation128W_TDD_FDD_R004C0000G01B008SPC006,
 * 所以当前工作的分区是slave分区
 *
 * @see vtb.arisu.mana.bean.report.Ack_QueryCfg
 **/
@Nya
@Info("基站基本信息查询")
@VtbBean(OFLType.O_FL_LMT_TO_ENB_BASE_INFO_QUERY)
@RspType(OFLType.O_FL_ENB_TO_LMT_BASE_INFO_QUERY_ACK)
public class Query_cfg extends Bean {

    /**
     * 0: 设备型号
     * 1：硬件版本
     * 2：软件版本
     * 3：SN号
     * 4：MAC地址
     * 5：uboot版本号
     * 6：板卡温度
     * 7: 非工作分区软件版本
     */
    @Nya
    public int u32_u32EnbBaseInfoType;

}
