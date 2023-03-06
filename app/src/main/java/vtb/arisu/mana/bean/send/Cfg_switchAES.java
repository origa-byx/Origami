package vtb.arisu.mana.bean.send;

import vtb.arisu.mana.annotation.Nya;
import vtb.arisu.mana.annotation.RspType;
import vtb.arisu.mana.annotation.VtbBean;
import vtb.arisu.mana.bean.OFLType;
import vtb.mashiro.kanon.base.Bean;

/**
 * @by: origami
 * @date: {2022/5/31}
 * @info: AES加解密开关配置
 * 是否立即生效：是
 * 重启是否保留配置：是
 * 应答消息（eNB ->LMT）：O_FL_ENB_TO_LMT_AES_ALGORITHM_SWITCH_CFG_ACK(0xF0DE)
 * 该消息以及ACK消息不加密，仅对其他和上位机的消息加密。
 * 仅加密消息体。采用AES算法，补零。消息长度填写加密后的总长度。
 * 加密后消息头中填写ox5555BBBB
 **/
@Nya
@VtbBean(OFLType.O_FL_LMT_TO_ENB_AES_ALGORITHM_SWITCH_CFG)
@RspType(OFLType.O_FL_ENB_TO_LMT_AES_ALGORITHM_SWITCH_CFG_ACK)
public class Cfg_switchAES extends Bean {

    /**
     * 0:关闭算法
     * 1：打开算法
     */
    @Nya(l = 1)
    public int u8_aesSwitch;

    @Nya(f = 1, l = 3)
    public int u8$3_Res;

}
