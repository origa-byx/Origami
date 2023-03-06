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
 * @info: NTP服务器IP配置
 * 是否立即生效：否
 * 重启是否保留配置：是
 * 应答消息（eNB ->LMT）：O_FL_ENB_TO_LMT_NTP_SERVER_IP_CFG_ACK (0xF087)
 *
 * 此接口用于设置基站NTP时间同步的NTP服务器IP，
 * 系统启动时会自动进行NTP时间同步
 **/
@Nya(l = 20)
@Info("NTP服务器IP配置")
@VtbBean(OFLType.O_FL_LMT_TO_ENB_NTP_SERVER_IP_CFG)
@RspType(OFLType.O_FL_ENB_TO_LMT_NTP_SERVER_IP_CFG_ACK)
public class Cfg_ntpServerIp extends Bean {

    /**
     * 字符数组，以结束符结束
     * Ntp服务器ip地址：
     * 例如：“192.168.8.86”
     */
    @Nya(l = 20)
    public String u8$20_ntpServerIp;

}
