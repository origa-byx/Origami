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
 * @info: 扫频端口配置
 * 是否立即生效：是
 * 重启是否保留配置：是
 * 应答消息（eNB ->LMT）：O_FL_ENB_TO_LMT_REM_ANT_CFG_ACK(0xF07E)
 * 此接口仅用于配置TDD扫频端口，目前支持RX和SINNIFER 2个端口模式
 **/
@Nya
@Info("扫频端口配置")
@VtbBean(OFLType.O_FL_LMT_TO_ENB_REM_ANT_CFG)
@RspType(OFLType.O_FL_ENB_TO_LMT_REM_ANT_CFG_ACK)
public class Cfg_scanPort extends Bean {

    /**
     * 端口类型
     * 0：Rx
     * 1：SNF
     */
    @Nya
    public int u32_RxorSnf;

}
