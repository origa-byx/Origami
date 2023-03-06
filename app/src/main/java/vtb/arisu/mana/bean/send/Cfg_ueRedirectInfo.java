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
 * @info: UE重定向信息配置
 * 是否立即生效：是
 * 重启是否保留配置：是
 * 应答消息（eNB ->LMT）：O_FL_ENB_TO_LMT_REDIRECT_INFO_ACK (0xF018)
 *
 * 此接口用于配置基站发送给UE的释放消息中是否携带重定向参数，默认不携带重定向参数。其中RedirectNum是最大重定向次数，超过该次数后，发拒绝消息，让终端不再接入本基站
 **/
@Nya(l = 24)
@Info("UE重定向信息配置")
@VtbBean(OFLType.O_FL_LMT_TO_ENB_REDIRECT_INFO_CFG)
@RspType(OFLType.O_FL_ENB_TO_LMT_REDIRECT_INFO_ACK)
public class Cfg_ueRedirectInfo extends Bean {

    /**
     * 重定向开关
     * 0：打开
     * 1：关闭
     */
    @Nya
    public int u32_OnOff;

    /**
     * 重定向频点
     * 0~65535
     */
    @Nya(f = 4)
    public int u32_Earfcn;

    /**
     * 重定向类型
     * 0：4G
     * 1：3G
     * 2：2G
     */
    @Nya(f = 8)
    public int u32_RedirectType;

    /**
     * 重定向次数
     * 0~999
     */
    @Nya(f = 12)
    public int u32_RedirectNum;

    /**
     * 最大保留IMSI数量
     * 0~65535
     */
    @Nya(f = 16)
    public int u32_RedirectNodeMax;

    /**
     * 最大保留IMSI时间,单位:分钟
     */
    @Nya(f = 20)
    public int u32_RedirectMinutesThreshold;

}
