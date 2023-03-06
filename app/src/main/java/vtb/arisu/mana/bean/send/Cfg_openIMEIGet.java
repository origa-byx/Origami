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
 * @info: 开启IMEI捕获功能配置
 * 是否立即生效：是
 * 重启是否保留配置：是
 * 应答消息（eNB ->LMT）：
 * O_FL_ENB_TO_LMT_IMEI_REQUEST_CFG_ACK (0xF08B)
 *
 * 此接口用于配置基站是否开启获取UE的IMEI功能，由于IMEI的获取，
 * 基站会对接入的UE先释放让其重新接入，会影响高速抓号的成功率，
 * 因此版本默认是关闭功能，客户可根据自己的需要决定是否开启此功能。
 * 根据测试，获取UE IMEI相对于IMSI的比例大概是10~15%
 **/
@Nya
@Info("开启IMEI捕获功能配置")
@VtbBean(OFLType.O_FL_LMT_TO_ENB_IMEI_REQUEST_CFG)
@RspType(OFLType.O_FL_ENB_TO_LMT_IMEI_REQUEST_CFG_ACK)
public class Cfg_openIMEIGet extends Bean {

    /**
     * 是否开启IMEI获取功能
     * 0：关闭
     * 1：开启
     */
    @Nya(l = 1)
    public int u8_ImeiEnable;

    /**
     * 保留字节
     */
    @Nya(f = 1, l = 3)
    public int u8$3_Res;

}
