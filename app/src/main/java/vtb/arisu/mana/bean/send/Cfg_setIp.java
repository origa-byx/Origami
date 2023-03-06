package vtb.arisu.mana.bean.send;

import vtb.arisu.mana.annotation.Info;
import vtb.arisu.mana.annotation.Nya;
import vtb.arisu.mana.annotation.RspType;
import vtb.arisu.mana.annotation.VtbBean;
import vtb.arisu.mana.bean.OFLType;
import vtb.mashiro.kanon.base.Bean;

/**
 * @by: origami
 * @date: {2022/5/23}
 * @info: 基站IP配置
 * 是否立即生效：否
 * 重启是否保留配置：是
 * 应答消息（eNB ->LMT）：O_FL_ENB_TO_LMT_IP_CFG_ACK (0xF01C)
 *
 * 该接口用于修改基站的IP配置。
 * 版本默认基站地址是“192.168.2.53#255.255.255.0#192.168.2.1#”。
 **/
@Nya(l = 52)
@Info("基站IP配置")
@VtbBean(OFLType.O_FL_LMT_TO_ENB_IP_CFG)
@RspType(OFLType.O_FL_ENB_TO_LMT_IP_CFG_ACK)
public class Cfg_setIp extends Bean {

    /**
     * 设置基站的IP
     * IP配置字符串，以’\0’结束。
     * 例如：“192.168.1.51#255.255.255.0#192.168.1.1#”
     */
    @Nya(l = 52)
    public String ip;

}
