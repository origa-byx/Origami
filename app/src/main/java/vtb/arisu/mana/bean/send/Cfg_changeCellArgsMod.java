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
 * @info: 动态修改小区参数
 * 是否立即生效：是
 * 重启是否保留配置：否
 * 应答消息（eNB ->LMT）：O_FL_ENB_TO_LMT_SYS_ARFCN_MOD_ACK (0xF081)
 *
 * 此接口用于在小区激活态下，即时修改小区参数，
 * 但是此时修改的小区参数重启或者断电之后不会保存。
 * 如果当前小区没有激活，会返回配置失败
 *
 **/
@Nya(l = 24)
@Info("动态修改小区参数")
@VtbBean(OFLType.O_FL_LMT_TO_ENB_SYS_ARFCN_MOD)
@RspType(OFLType.O_FL_ENB_TO_LMT_SYS_ARFCN_MOD_ACK)
public class Cfg_changeCellArgsMod extends Bean {

    /**
     * 上行频点
     * TDD: 255
     * FDD:实际频点，同sysBand对应
     */
    @Nya
    public int u32_ulEarfcn;

    /**
     * 下行频点
     * 0~65535
     */
    @Nya(f = 4)
    public int u32_dlEarfcn;

    /**
     * plmn
     * 字符数组，以结束符结束eg:“46000”
     */
    @Nya(f = 8, l = 7)
    public String u8$7_PLMN;

    /**
     * 频段
     * 需要跟上面的频点参数匹配
     */
    @Nya(f = 15, l = 1)
    public int u8_Band;

    /**
     * 小区Id
     * 0~65535
     */
    @Nya(f = 16)
    public int u32_CellId;

    /**
     * 终端最大发射功率
     * 0~23dBm
     */
    @Nya(f = 20)
    public int u32_UePMax;

}
