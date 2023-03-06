package vtb.arisu.mana.bean.report;

import vtb.arisu.mana.annotation.Info;
import vtb.arisu.mana.annotation.Nya;
import vtb.arisu.mana.annotation.VtbBean;
import vtb.arisu.mana.bean.OFLType;
import vtb.mashiro.kanon.base.Bean;

/**
 * @by: origami
 * @date: {2022/5/31}
 * @info: 重定向配置查询-ACK
 *
 * @see vtb.arisu.mana.bean.send.Query_redirectInfo
 **/
@Nya(l = 16)
@Info("重定向配置查询-ACK")
@VtbBean(OFLType.O_FL_ENB_TO_LMT_REDIRECT_INFO_CFG_QUERY_ACK)
public class Ack_QueryRedirectInfo extends Bean {

    /**
     * 重定向开关
     * 0：打开
     * 1：关闭
     */
    @Nya
    public int u32_OnOff;

    /**
     * 重定向频点
     * 0~65535
     */
    @Nya(f = 4)
    public int u32_Earfcn;

    /**
     * 重定向类型
     * 0：4G
     * 1：3G
     * 2：2G
     */
    @Nya(f = 8)
    public int u32_RedirectType;

    /**
     * 重定向次数
     * 0~999
     */
    @Nya(f = 12)
    public int u32_RedirectNum;

}
