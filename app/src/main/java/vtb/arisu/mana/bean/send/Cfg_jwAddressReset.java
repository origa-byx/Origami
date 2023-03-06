package vtb.arisu.mana.bean.send;

import vtb.arisu.mana.annotation.Info;
import vtb.arisu.mana.annotation.RspType;
import vtb.arisu.mana.annotation.VtbBean;
import vtb.arisu.mana.bean.OFLType;
import vtb.mashiro.kanon.base.Bean;

/**
 * @by: origami
 * @date: {2022/5/23}
 * @info: GPS经纬度信息复位配置
 * 是否立即生效：是
 * 重启是否保留配置：否
 * 应答消息（eNB ->LMT）：O_FL_ENB_TO_LMT_GPS_INFO_RESET_ACK (0xF06E)
 *
 * 基站启动时，会自动获取GPS经纬度信息，
 * 获取的经纬度信息可通过 {@link Query_jwAddress} 接口查询，
 * 一旦获取到经纬度信息，基站会保存此次获取的值，下次重启将不再重复获取，
 * 因此如果基站移动了位置，请使用此接口清除上一次获取的经纬度信息
 **/
@Info("GPS经纬度信息复位配置")
@VtbBean(OFLType.O_FL_LMT_TO_ENB_GPS_INFO_RESET)
@RspType(OFLType.O_FL_ENB_TO_LMT_GPS_INFO_RESET_ACK)
public class Cfg_jwAddressReset extends Bean {
}
