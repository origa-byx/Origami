package vtb.arisu.mana.bean.report;

import vtb.arisu.mana.annotation.Info;
import vtb.arisu.mana.annotation.Nya;
import vtb.arisu.mana.annotation.VtbBean;
import vtb.arisu.mana.bean.OFLType;
import vtb.mashiro.kanon.base.Bean;

/**
 * @by: origami
 * @date: {2022/6/1}
 * @info: Fdd共建站重定向配置查询-ACK
 * @see vtb.arisu.mana.bean.send.Query_fddRedirectInfo
 **/
@Nya(l = 12)
@Info("Fdd共建站重定向配置查询-ACK")
@VtbBean(OFLType.O_FL_LMT_TO_ENB_FDD_REDIRECTION_CFG_QUERY_ACK)
public class Ack_QueryFddRedirectInfo extends Bean {

    /**
     * 重定向开关
     * 0：打开
     * 1：关闭
     */
    @Nya
    public int u32_OnOff;

    /**
     * 联通重定向频点
     * 0~65535
     */
    @Nya(f = 4)
    public int u32_UnicomEarfcn;

    /**
     * 电信重定向频点
     * 0~65535
     */
    @Nya(f = 8)
    public int u32_TelecomEarfcn;

}
