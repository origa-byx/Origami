package vtb.arisu.mana.bean.report;

import vtb.arisu.mana.annotation.Info;
import vtb.arisu.mana.annotation.Nya;
import vtb.arisu.mana.annotation.VtbBean;
import vtb.arisu.mana.bean.OFLType;
import vtb.mashiro.kanon.base.Bean;

/**
 * @by: origami
 * @date: {2022/5/31}
 * @info: 告警指示上报
 * 此消息用于基站上报一些运行异常的告警信息，目前支持的告警包括失步、高低温（基带板温度）告警。
 **/
@Nya(l = 8)
@Info("告警指示上报")
@VtbBean(OFLType.O_FL_ENB_TO_LMT_ALARMING_TYPE_IND)
public class WarnRpt extends Bean {

    /**
     * 0：基带板高温告警>=70度
     * 1：失步告警
     * 5：基带板低温告警<=-20
     * 7: 分区损坏告警
     */
    @Nya
    public int u32_AlarmingType;

    /**
     * 0：产生告警指示
     * 1：取消告警指示
     */
    @Nya(f = 4)
    public int u32_AlarmingFlag;

}
