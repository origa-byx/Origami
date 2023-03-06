package vtb.arisu.mana.bean.send;

import vtb.arisu.mana.annotation.Info;
import vtb.arisu.mana.annotation.Nya;
import vtb.arisu.mana.annotation.VtbBean;
import vtb.arisu.mana.bean.OFLType;
import vtb.mashiro.kanon.base.Bean;

/**
 * @by: origami
 * @date: {2022/5/31}
 * @info: 非主控板用户ACK接口
 * @see vtb.arisu.mana.bean.report.UeInfoRpt
 **/
@Nya
@Info("非主控板用户ACK")
@VtbBean(OFLType.O_FL_LMT_TO_ENB_UE_INFO_RPT_ACK)
public class UeInfoRptAck extends Bean {

    /**
     * 序列号：等于
     * @see vtb.arisu.mana.bean.report.UeInfoRpt
     * 消息中seqNum字段
     */
    @Nya
    public int u32_seqNum;

}
