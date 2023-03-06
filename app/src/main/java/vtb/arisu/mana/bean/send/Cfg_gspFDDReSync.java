package vtb.arisu.mana.bean.send;

import vtb.arisu.mana.annotation.Info;
import vtb.arisu.mana.annotation.RspType;
import vtb.arisu.mana.annotation.VtbBean;
import vtb.arisu.mana.bean.OFLType;
import vtb.mashiro.kanon.base.Bean;

/**
 * @by: origami
 * @date: {2022/5/25}
 * @info: FDD GPS同步不成功重新同步配置
 * 是否立即生效：是
 * 重启是否保留配置：否
 * 应答消息（eNB ->LMT）：
 * O_FL_ENB_TO_LMT_FDD_GPS_RESYNC_CFG _ACK (0xF091)
 *
 * 该接口用于FDD 模式下：
 * 当通过O_FL_LMT_TO_ENB_REM_MODE_CFG（0xF023）开启FDD gps同步功能时，
 * 此消息接口用于当gps同步不成功时，重新配置gps去做同步
 **/
@Info("FDD GPS同步不成功重新同步配置")
@VtbBean(OFLType.O_FL_LMT_TO_ENB_FDD_GPS_RESYNC_CFG)
@RspType(OFLType.O_FL_ENB_TO_LMT_FDD_GPS_RESYNC_CFG_ACK)
public class Cfg_gspFDDReSync extends Bean {

}
