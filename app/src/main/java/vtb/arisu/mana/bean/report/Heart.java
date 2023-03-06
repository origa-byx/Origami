package vtb.arisu.mana.bean.report;

import vtb.arisu.mana.annotation.Info;
import vtb.arisu.mana.annotation.Nya;
import vtb.arisu.mana.annotation.VtbBean;
import vtb.arisu.mana.bean.OFLType;
import vtb.mashiro.kanon.base.Bean;
import vtb.mashiro.kanon.util.ByteUtil;

/**
 * @by: origami
 * @date: {2022/5/16}
 * @info: 基站软件启动完成后，基站开始周期的(5S)向客户端发送此消息，
 * 客户端收到此消息后，即可开始与基站的通信操作。心跳指示中携带小区状态，
 * 小区配置等参数信息，小区状态为已激活态时，后面的频点等小区配置信息才有效。
 * 因为配置消息会触发基站执行一些流程，因此为了避免与当前流程冲突，
 * 只有小区状态为去激活和已激活的状态下，客户端才可以下发配置信息给基站。
 **/
@Nya(l = 24)
@Info("心跳指示")
@VtbBean(OFLType.O_FL_ENB_TO_LMT_SYS_INIT_SUCC_IND)
public class Heart extends Bean {

    /**
     * 代表小区状态
     * 0：小区IDLE态
     * 1：扫频/同步进行中
     * 2：小区激活中
     * 3：小区激活态
     * 4：小区去激活中
     */
    @Nya(l = 2)
    public int u16_CellState;

    /**
     * 指示当前激活小区的BAND
     * FDD:Band 1/3/7
     * TDD:Band 38/39/40/41
     */
    @Nya(f = 2, l = 2)
    public int u16_Band;

    /**
     * 上行频点
     * TDD: 255
     * FDD:实际频点，同sysBand对应
     */
    @Nya(f = 4)
    public int u32_ulEarfcn;

    /**
     * 下行频点
     * 实际频点，同sysBand对应
     */
    @Nya(f = 8)
    public int u32_dlEarfcn;

    /**
     * 字符数组，以结束符结束eg:“46000”
     */
    @Nya(f = 12, l = 7)
    public String u8$7_PLMN;

    /**
     * 系统带宽
     *25,   *5M, tdd+fdd*
     *50,   *10M, tdd+fdd*
     *75,   *15M. only fdd*
     *100   *20M, tdd+fdd*
     */
    @Nya(f = 19, l = 1)
    public int u8_Bandwidth;

    /**
     * PhysCellId is used to indicate the
     * physical layer identity of the cell
     * 0~503
     */
    @Nya(f = 20, l = 2)
    public int u16_PCI;

    /**
     * TrackingAreaCode is used to identify a
     * tracking area within the scope of a PLMN
     * 0~65535
     */
    @Nya(f = 22, l = 2)
    public int u16_TAC;

}
