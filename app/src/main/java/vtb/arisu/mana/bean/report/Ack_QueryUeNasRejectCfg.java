package vtb.arisu.mana.bean.report;

import vtb.arisu.mana.annotation.Info;
import vtb.arisu.mana.annotation.Nya;
import vtb.arisu.mana.annotation.VtbBean;
import vtb.arisu.mana.bean.OFLType;
import vtb.mashiro.kanon.base.Bean;

/**
 * @by: origami
 * @date: {2022/6/1}
 * @info: UE NAS REJECT CAUSE配置查询-ACK
 * @see vtb.arisu.mana.bean.send.Query_ueNasRejectCfg
 **/
@Nya
@Info("UE NAS REJECT CAUSE配置查询-ACK")
@VtbBean(OFLType.O_FL_ENB_TO_LMT_TAU_ATTACH_REJECT_CAUSE_QUERY_ACK)
public class Ack_QueryUeNasRejectCfg extends Bean {

    /**
     * 回复UE的TAU REJECT或者ATTACH REJECT消息中的reject cause值
     * 0：#cause15 （追踪区不允许接入）
     * 1：#cause12  (追踪区无合适小区)
     * 2:  #cause3 （无效终端）
     * 3：#cause13
     * 4：#cause22
     */
    @Nya
    public int u32_RejectCause;

}
