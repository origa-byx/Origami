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
 * @info: 设置GPS失步后重启时间
 * 是否立即生效：是
 * 重启是否保留配置：是
 * 应答消息（eNB ->LMT）：O_FL_ENB_TO_LMT_GPS_LOSS_REBOOT_TMR_CFG_ACK (0xF0CF)
 **/
@Nya
@Info("设置GPS失步后重启时间")
@VtbBean(OFLType.O_FL_LMT_TO_ENB_GPS_LOSS_REBOOT_TMR_CFG)
@RspType(OFLType.O_FL_ENB_TO_LMT_GPS_LOSS_REBOOT_TMR_CFG_ACK)
public class Cfg_gpsLossRebootTime extends Bean {

    /**
     * 单位：分钟
     * 0-65535
     */
    @Nya
    public int u32_value;

}
