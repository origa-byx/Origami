package vtb.arisu.mana.bean.send;

import vtb.arisu.mana.annotation.Info;
import vtb.arisu.mana.annotation.Nya;
import vtb.arisu.mana.annotation.RspType;
import vtb.arisu.mana.annotation.VtbBean;
import vtb.arisu.mana.bean.OFLType;
import vtb.mashiro.kanon.base.Bean;

/**
 * @by: origami
 * @date: {2022/5/23}
 * @info: TDD子帧配置
 * 是否立即生效：否
 * 重启是否保留配置：是
 * 应答消息（eNB ->LMT）：
 * O_FL_ENB_TO_LMT_TDD_SUBFRAME_ASSIGNMENT_SET_ACK (0xF04A)
 *
 * 此消息接口用于配置TDD小区的子帧配比，小区去激活状态下配置立即生效
 **/
@Nya
@Info("TDD子帧配置")
@VtbBean(OFLType.O_FL_LMT_TO_ENB_TDD_SUBFRAME_ASSIGNMENT_SET)
@RspType(OFLType.O_FL_ENB_TO_LMT_TDD_SUBFRAME_ASSIGNMENT_SET_ACK)
public class Cfg_tddChildrenFrame extends Bean {

    /**
     * TDD子帧配比
     * 1：sa1
     * 2：sa2
     */
    @Nya(l = 1)
    public int u8_u8TddSfAssignment;

    /**
     * TDD特殊子帧配比
     * 5：ssp5
     * 7：ssp7
     */
    @Nya(f = 1, l = 1)
    public int u8_u8TddSpecialSfPatterns;

    /**
     * 保留字节
     */
    @Nya(f = 2, l = 2)
    public int u8$2_Res;

}
