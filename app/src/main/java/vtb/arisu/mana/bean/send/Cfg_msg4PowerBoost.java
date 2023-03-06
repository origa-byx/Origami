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
 * @info: MSG4功率抬升配置
 * 是否立即生效：否
 * 重启是否保留配置：是
 * 应答消息（eNB ->LMT）：O_FL_ENB_TO_LMB_MSG4_POWER_BOOST_CFG_ACK (0xF0B0)
 * 此接口用于配置MSG4功率是否抬升。默认出厂设置为不抬升，在不同环境下测试时，如出现接入成功率不理想，可以配置抬升MSG4功率
 **/
@Nya
@Info("MSG4功率抬升配置")
@VtbBean(OFLType.O_FL_LMT_TO_ENB_MSG4_POWER_BOOST_CFG)
@RspType(OFLType.O_FL_ENB_TO_LMB_MSG4_POWER_BOOST_CFG_ACK)
public class Cfg_msg4PowerBoost extends Bean {

    /**
     * 0：不抬升（出厂默认值）
     * 1：抬升
     */
    @Nya
    public int u32_Msg4PowerBoost;

}
