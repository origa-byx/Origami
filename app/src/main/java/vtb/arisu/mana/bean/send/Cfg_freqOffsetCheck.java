package vtb.arisu.mana.bean.send;

import vtb.arisu.mana.annotation.Info;
import vtb.arisu.mana.annotation.Nya;
import vtb.arisu.mana.annotation.RspType;
import vtb.arisu.mana.annotation.VtbBean;
import vtb.arisu.mana.bean.OFLType;
import vtb.mashiro.kanon.base.Bean;

/**
 * @by: origami
 * @date: {2022/5/31}
 * @info: 频偏校准开关配置
 * 是否立即生效：是
 * 重启是否保留配置：是
 * 应答消息（eNB ->LMT）：O_FL_ENB_TO_LMT_FREQ_OFFSET_ADJ_CFG_ACK(0xF0DB)
 * 基站收到此条消息，设置校准开关打开，重启基站，基站重启以后会开始频偏校准过程。
 * 频偏校准过程相关流程如下：
 *
 *      1.使用O_FL_LMT_TO_ENB_SELFCFG_ARFCN_CFG_REQ（0xF051）预先配置基站扫频的基础频点
 * （此配置重启和上下电都会保留配置）。O_FL_LMT_TO_ENB_SELFCFG_ARFCN_QUERY (0xF04D)
 * 接口用于查询配置频点列表。
 *
 *      2.然后客户端下发频偏校准开关（0xF0DA）,打开频偏校准功能；
 *
 *      3.基站收到0xF0DA消息会上报状态：“频偏校准开始”（0xF019），然后重启基站。
 *
 *      4.基站重启以后则开始频偏校准流程，上报状态：“频偏校准进行中”（0xF019）。
 * 校准过程中会有扫频状态和扫频结果上报。
 *
 *      5.频偏校准结束，上报状态：“频偏校准结束”（0xF019），
 * 同时上报频偏校准结果（0xF0DC），复位校准开关。
 * 校准结束以后，需要客户端下发重启基站的指令，基站重启以后才能进行正常工作的状态
 **/
@Nya
@Info("频偏校准开关配置")
@VtbBean(OFLType.O_FL_LMT_TO_ENB_FRQ_OFFSET_ADJ_CFG)
@RspType(OFLType.O_FL_ENB_TO_LMT_FREQ_OFFSET_ADJ_CFG_ACK)
public class Cfg_freqOffsetCheck extends Bean {

    /**
     * 0:关闭
     * 1：打开
     */
    @Nya(l = 1)
    public int u8_FreqOffsetSwitch;

    @Nya(f = 1, l = 3)
    public int u8$3_Res;

}
