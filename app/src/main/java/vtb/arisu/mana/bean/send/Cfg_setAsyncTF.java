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
 * @info: 基站TDD/FDD同步方式配置
 * 是否立即生效：是
 * 重启是否保留配置：是
 * 应答消息（eNB ->LMT）：O_FL_ENB_TO_LMT_REM_MODE_CFG_ACK (0xF024)
 *
 * 此接口用于设置基站的同步方式，目前仅支持空口和GPS同步
 **/
@Nya
@Info("基站TDD/FDD同步方式配置")
@VtbBean(OFLType.O_FL_LMT_TO_ENB_REM_MODE_CFG)
@RspType(OFLType.O_FL_ENB_TO_LMT_REM_MODE_CFG_ACK)
public class Cfg_setAsyncTF extends Bean {

    /**
     * TDD模式支持空口和GPS同步，FDD仅支持GPS，用于频率同步。
     * 0：空口同步；
     * 1：GPS同步
     */
    @Nya
    public int u32_Remmode;

}
