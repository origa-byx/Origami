package vtb.arisu.mana.bean.report;

import vtb.arisu.mana.annotation.Info;
import vtb.arisu.mana.annotation.Nya;
import vtb.arisu.mana.annotation.VtbBean;
import vtb.arisu.mana.bean.OFLType;
import vtb.mashiro.kanon.base.Bean;

/**
 * @by: origami
 * @date: {2022/5/31}
 * @info: 频偏校准结果上报
 * 频偏校准结果上报。
 **/
@Nya(l = 8)
@Info("频偏校准结果上报")
@VtbBean(OFLType.O_FL_LMT_TO_ENB_FRQ_OFFSET_ADJ_RESULT_IND)
public class FrqOffsetAdjRpt extends Bean {

    /**
     * 0: 成功
     * 1：失败
     */
    @Nya(l = 1)
    public int u8_FreqOffsetAdjResult;

    /**
     * 保留字节
     */
    @Nya(f = 1, l = 3)
    public int u8$3_Res;

    /**
     *  仅FreqOffsetAdjResult为0时有效，
     *  代表当前单板的频偏值，
     *  频偏校准精度为绝对值200以内。
     *  -200~200Hz
     */
    @Nya(f = 4)
    public int s32_FreqOffsetValue;

}
