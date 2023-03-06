package vtb.arisu.mana.bean.report;

import vtb.arisu.mana.annotation.Info;
import vtb.arisu.mana.annotation.Nya;
import vtb.arisu.mana.annotation.VtbBean;
import vtb.arisu.mana.bean.OFLType;

/**
 * @by: origami
 * @date: {2022/5/31}
 * @info: 基站基本信息查询-ACK
 * @see vtb.arisu.mana.bean.send.Query_cfg
 **/
@Nya(l = 104)
@Info("基站基本信息查询-ACK")
@VtbBean(OFLType.O_FL_ENB_TO_LMT_BASE_INFO_QUERY_ACK)
public class Ack_QueryCfg {

    /**
     * 查询信息的类型，
     * 0: 设备型号
     * 1：硬件版本
     * 2：软件版本
     * 3：序列号
     * 4：MAC地址
     * 5：uboot版本号
     * 6：板卡温度
     * 7: 备区软件版本
     */
    @Nya
    public int u32_u32EnbBaseInfoType;

    /**
     * 信息上报
     */
    @Nya(f = 4, l = 100)
    public String u8$100_u8EnbbaseInfo;

}
