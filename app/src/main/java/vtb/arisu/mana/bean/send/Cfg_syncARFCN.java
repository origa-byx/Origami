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
 * @info: 异频同步接口配置
 * 是否立即生效：是
 * 重启是否保留配置：是
 * 应答消息（eNB ->LMT）：
 * O_FL_ENB_TO_LMT_SYNCARFCN_CFG_ACK(0xF05F)
 *
 * 本配置接口用于配置异频同步相关参数，其中TimeOffsetValue参数配置需要重启生效。
 * 仅支持5M和10M带宽下，最大1024微秒的时偏设置（对应TimeOffsetValue取值范围为-15729~15729）。
 *
 * 此接口中TimeOffsetValue用于设置同步偏移量，在中国，
 * 一般band39,band40需要进行GPS同步偏移量调节,
 * 一般-700微秒（OffsetTime）左右数据帧头偏移（正值说明时域相对原始值向后移动，负
 * 值说明是时域对应原始值向前移动），具体各个BAND的偏移量以实际测量为准。
 * 接口中设置的TimeOffsetValue = OffsetTime  * （Gpspps1sToBW/微秒）
 * OffsetTime：小区频点和空口同步频点之间的GPS pp1s偏移量，单位微秒；
 * Gpspps1sToBW/微秒：相关带宽下每微秒的偏移值，带宽是指本基带板的带宽；
 *
 * 1微秒的偏移情况下，Gpspps1s与带宽对应关系如下：
 *
 * Bandwidth	        （5M）	（10M）	   （20M）
 * Gpspps1sToBW/微秒	 7.68	 15.36	    30.72
 *
 * 例如：基站配置5带宽，宏网BAND40偏移-700微妙, B38偏移-965微秒，
 * 建立B38的小区，同步频点设置为B40的，这样TimeOffsetValue =  - 265*7.68
 **/
@Nya(l = 16)
@Info("异频同步接口配置")
@VtbBean(OFLType.O_FL_LMT_TO_ENB_SYNCARFCN_CFG)
@RspType(OFLType.O_FL_ENB_TO_LMT_SYNCARFCN_CFG_ACK)
public class Cfg_syncARFCN extends Bean {

    /**
     * 0：使用Rx口的异频同步
     * 1：使用SNF口的异频同步
     * 2: 取消异频同步（当配置为取消异频同步，其他参数值无效，
     *      基站内部相关参数会复位为同频同步模式，时偏也复位为0.）
     */
    @Nya(l = 1)
    public int u8_UsingSnfSwitch;

    /**
     * 保留字节
     */
    @Nya(f = 1, l = 3)
    public int u8$3_Reserved1;

    /**
     * 同步频点
     */
    @Nya(f = 4)
    public int u32_SyncArfcn;

    /**
     * 0：不需要配置（TimeOffsetValue保留之前值）
     * 1：需要配置
     */
    @Nya(f = 8, l = 1)
    public int u8_TimeOffsetPresent;

    /**
     * 保留字节
     */
    @Nya(f = 9, l = 3)
    public int u8$3_Reserved2;

    /**
     * 异频同步时偏值（不同band间）
     * -15729 ~ 15729
     */
    @Nya(f = 12)
    public int s32_TimeOffsetValue;

}
