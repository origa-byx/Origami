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
 * @info: Fdd共建站重定向信息配置
 * 是否立即生效：是
 * 重启是否保留配置：是
 * 应答消息（eNB ->LMT）：O_FL_ENB_TO_LMT_FDD_REDIRECTION_CFG_ACK(0xF0E7)
 * 此接口用于联通和电信共建站时，
 * 用来配置联通和电信终端重定向频点。
 * 参数上下电后参数保存，
 *
 * 该接口不能和 O_FL_LMT_TO_ENB_REDIRECT_INFO_CFG 接口同时使用
 * @see Cfg_ueRedirectInfo
 **/
@Nya(l = 12)
@Info("Fdd共建站重定向信息配置")
@VtbBean(OFLType.O_FL_LMT_TO_ENB_FDD_REDIRECTION_CFG)
@RspType(OFLType.O_FL_ENB_TO_LMT_FDD_REDIRECTION_CFG_ACK)
public class Cfg_reDirectionFDD extends Bean {

    /**
     * 重定向开关
     * 0：打开
     * 1：关闭
     */
    @Nya
    public int u32_OnOff;

    /**
     * 联通重定向频点
     * 0~65535
     *
     */
    @Nya(f = 4)
    public int u32_UnicomEarfcn;

    /**
     * 电信重定向频点
     * 0~65535
     */
    @Nya(f = 8)
    public int u32_TelecomEarfcn;

}
