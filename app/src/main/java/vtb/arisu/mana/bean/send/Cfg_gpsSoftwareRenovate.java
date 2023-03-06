package vtb.arisu.mana.bean.send;

import vtb.arisu.mana.annotation.Info;
import vtb.arisu.mana.annotation.RspType;
import vtb.arisu.mana.annotation.VtbBean;
import vtb.arisu.mana.bean.OFLType;
import vtb.mashiro.kanon.base.Bean;

/**
 * @by: origami
 * @date: {2022/5/25}
 * @info: GPS固件彻底复位配置
 * 	是否立即生效：否
 * 	重启是否保留配置：是
 * 	应答消息（eNB ->LMT）：O_FL_LMT_TO_ENB_GPS_SOFTWARE_RENOVATE_CFG_ACK(0xF0D3)
 * 此接口主要用于当gps信号很好的场景下反复无法同步成功，
 * 搜不到星的场景，可以尝试将GPS固件彻底复位，
 * 复位时间比较长，等收到响应后，重启生效
 **/
@Info("GPS固件彻底复位配置")
@VtbBean(OFLType.O_FL_LMT_TO_ENB_GPS_SOFTWARE_RENOVATE_CFG)
@RspType(OFLType.O_FL_LMT_TO_ENB_GPS_SOFTWARE_RENOVATE_CFG_ACK)
public class Cfg_gpsSoftwareRenovate extends Bean {
}
