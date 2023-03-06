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
 * @info: 服务小区参数配置
 * 是否立即生效：是
 * 重启是否保留配置：是。
 * 应答消息（eNB ->LMT）：O_FL_ENB_TO_LMT_SYS_ARFCN_ACK (0xF004)
 * 此接口用于配置建立小区相关参数配置，在小区激活态配置此消息，基站会执行先去激活再激活的流程；
 * 在小区IDLE态下配置此消息，基站会直接执行激活小区的流程
 **/
@Nya(l = 32)
@Info("服务小区参数配置")
@VtbBean(OFLType.O_FL_LMT_TO_ENB_SYS_ARFCN_CFG)
@RspType(OFLType.O_FL_ENB_TO_LMT_SYS_ARFCN_ACK)
public class Cfg_servingCellArgs extends Bean {

    /**
     * 上行频点
     * TDD: 255
     * FDD:实际频点，同sysBand对应
     */
    @Nya
    public int u32_ulEarfcn;

    /**
     * 下行频点
     * 实际频点，同Band对应
     */
    @Nya(f = 4)
    public int u32_dlEarfcn;

    /**
     * 字符数组，以结束符结束eg:“46000”
     */
    @Nya(f = 8, l = 7)
    public String u8$7_PLMN;

    /**
     *系统带宽
     *25,   *5M, tdd+fdd*
     *50,   *10M, tdd+fdd *
     *75,   *15M. only fdd*
     *100   *20M, tdd+fdd*
     */
    @Nya(f = 15, l = 1)
    public int u8_Bandwidth;

    /**
     * 指示建立小区的BAND，基站可以支持频率范围是
     * 50MHz~4GHz，其他BAND可定制开发。
     * FDD:Band 1/3/7
     * TDD:Band 38/39/40/41
     */
    @Nya(f = 16)
    public int u32_Band;

    /**
     * PhysCellId is used to indicate the physical layer identity of the cell
     * 0~503
     */
    @Nya(f = 20, l = 2)
    public int u16_PCI;

    /**
     * TrackingAreaCode is used to identify a tracking area within the scope of a PLMN
     * 0~65535
     */
    @Nya(f = 22, l = 2)
    public int u16_TAC;

    /**
     * CellIdentity is used to unambiguously identify a cell within a PLMN
     * 0x00000000~0x0FFFFFFF
     */
    @Nya(f = 24)
    public int u32_CellId;

    /**
     * 终端最大发射功率
     * 终端最大发射功率对应系统消息SIB1中P-Max，
     * 表示小区允许UE的最大发射功率，一般设置为23，表示23dBm。
     * 0 ~ 23dBm，默认值为23
     */
    @Nya(f = 28, l = 2)
    public int u16_UePMax;

    /**
     * 基站广播参考信号功率
     * 基站最大发射功率对应系统广播消息SIB2中的referenceSignalPower。
     * 此值的设置从加功放之后的总输出功率计算而来，用于终端计算路损，
     * 不会影响单板的输出功率。一般设置为20dBm（20W），此值相对于其他
     * 功率会比较大，但是经过测试，对基站性能影响不大，可以不用修改。
     * 0 ~ 20 dBm
     */
    @Nya(f = 30, l = 2)
    public int u16_EnodeBPMax;

}
