package vtb.arisu.mana.bean.report;

import vtb.arisu.mana.annotation.Info;
import vtb.arisu.mana.annotation.Nya;
import vtb.arisu.mana.annotation.VtbBean;
import vtb.arisu.mana.bean.OFLType;
import vtb.mashiro.kanon.base.Bean;

/**
 * @by: origami
 * @date: {2022/5/31}
 * @info: TDD子帧配置和上行功控系数查询-ACK
 * @see vtb.arisu.mana.bean.send.Query_tddChildCfgInfo
 **/
@Nya
@Info("TDD子帧配置和上行功控系数查询-ACK")
@VtbBean(OFLType.O_FL_ENB_TO_LMT_TDD_SUBFRAME_ASSIGNMENT_QUERY_ACK)
public class Ack_QueryTddChildCfgInfo extends Bean {

    /**
     * TDD子帧配比（fdd该值为255）
     * 1：sa1
     * 2：sa2
     */
    @Nya(l = 1)
    public int u8_u8TddSfAssignment;

    /**
     * TDD特殊子帧配比（fdd该值为255）
     * 5：ssp5
     * 7：ssp7
     */
    @Nya(f = 1, l = 1)
    public int u8_u8TddSpecialSfPatterns;

    /**
     * Sib2中上行功控系数
     * （0，40，50，60，70，80，90，100）
     */
    @Nya(f = 2, l = 1)
    public int u8_u8UlAlpha;

    /**
     * 保留字节
     */
    @Nya(f = 3, l = 1)
    public int u8$1_Res;

}
