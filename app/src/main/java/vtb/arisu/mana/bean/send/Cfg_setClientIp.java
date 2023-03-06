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
 * @info: 客户端IP配置
 * 是否立即生效：否
 * 重启是否保留配置：是
 * 应答消息（eNB ->LMT）：O_FL_ENB_TO_LMT_LMTIP_CFG_ACK (0xF026)
 *
 * 该接口用于配置客户端IP配置，版本默认客户端地址是“192.168.2.11#3345”。
 * 注意：该接口中的端口字段，单板在UDP模式下，也使用该端口值进行监听。
 * 端口默认3345，若更改，建议配置3350---3399.
 **/
@Nya(l = 32)
@Info("客户端IP配置")
@VtbBean(OFLType.O_FL_LMT_TO_ENB_LMTIP_CFG)
@RspType(OFLType.O_FL_ENB_TO_LMT_LMTIP_CFG_ACK)
public class Cfg_setClientIp extends Bean {

    /**
     * 设置主控板的IP和端口,字符串，以’\0’结束
     * “192.168.1.53#3345”
     */
    @Nya(l = 32)
    public String u8$32_LMTIPStr;

}
