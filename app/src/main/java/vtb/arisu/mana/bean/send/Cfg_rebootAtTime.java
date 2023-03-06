package vtb.arisu.mana.bean.send;

import vtb.arisu.mana.annotation.Info;
import vtb.arisu.mana.annotation.Nya;
import vtb.arisu.mana.annotation.RspType;
import vtb.arisu.mana.annotation.VtbBean;
import vtb.arisu.mana.bean.OFLType;
import vtb.mashiro.kanon.base.Bean;

/**
 * @by: origami
 * @date: {2022/5/24}
 * @info: 定点重启配置
 * 是否立即生效：否
 * 重启是否保留配置：是
 * 应答消息（eNB ->LMT）：
 * O_FL_ENB_TO_LMT_TIME_TO_RESET_CFG_ACK (0xF087)
 *
 * 此接口用于配置基站是否开启定点重启功能，基站系统采用NTP同步方式获取系统格林威治时间，如果开启此功能，请设置正确的NTP服务器IP。
 * 版本发布默认此功能关闭
 **/
@Nya(l = 16)
@Info("定点重启配置")
@VtbBean(OFLType.O_FL_LMT_TO_ENB_TIME_TO_RESET_CFG)
@RspType(OFLType.O_FL_ENB_TO_LMT_TIME_TO_RESET_CFG_ACK)
public class Cfg_rebootAtTime extends Bean {

    /**
     * 定点重启开关
     * 0：关闭
     * 1：打开
     */
    @Nya(l = 1)
    public int u8_ResetSwitch;

    /**
     * 保留字节
     */
    @Nya(f = 1, l = 3)
    public int u8$3_Res;

    /**
     * 重启时间配置
     * 例如：“23：15：15”
     * 格林威治时间
     */
    @Nya(f = 4, l = 12)
    public String u8$12_ResetTime;
}
