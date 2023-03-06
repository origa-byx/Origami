package vtb.arisu.mana.bean.send;

import vtb.arisu.mana.annotation.Info;
import vtb.arisu.mana.annotation.Nya;
import vtb.arisu.mana.annotation.RspType;
import vtb.arisu.mana.annotation.VtbBean;
import vtb.arisu.mana.bean.OFLType;
import vtb.mashiro.kanon.base.Bean;

/**
 * @by: origami
 * @date: {2022/5/25}
 * @info: UE NAS REJECT CAUSE配置
 * 是否立即生效：是
 * 重启是否保留配置：是
 * 应答消息（eNB ->LMT）：
 * O_FL_ENB_TO_LMT_TAU_ATTACH_REJECT_CAUSE_CFG_ACK(0xF058)
 *
 * 此接口用于配置基站把接入UE踢回公网时，
 * 回复UE的TAU REJECT或者ATTACH REJECT消息中的reject cause值，
 * 基站默认使用cause#15，一般此值不需要修改
 **/
@Nya
@Info("UE NAS REJECT CAUSE配置")
@VtbBean(OFLType.O_FL_LMT_TO_ENB_TAU_ATTACH_REJECT_CAUSE_CFG)
@RspType(OFLType.O_FL_ENB_TO_LMT_TAU_ATTACH_REJECT_CAUSE_CFG_ACK)
public class Cfg_ueNasRejectCause extends Bean {

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
