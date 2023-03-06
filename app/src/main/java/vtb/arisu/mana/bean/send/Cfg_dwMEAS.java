package vtb.arisu.mana.bean.send;

import vtb.arisu.mana.annotation.Info;
import vtb.arisu.mana.annotation.Nya;
import vtb.arisu.mana.annotation.RspType;
import vtb.arisu.mana.annotation.VtbBean;
import vtb.arisu.mana.bean.Define;
import vtb.arisu.mana.bean.OFLType;
import vtb.mashiro.kanon.base.Bean;

/**
 * @by: origami
 * @date: {2022/5/25}
 * @info: 设置基站测量UE配置（LMT -> eNB）
 * DW相关
 * 是否立即生效：是
 * 重启是否保留配置：否
 * 应答消息（eNB ->LMT）：O_FL_ENB_TO_LMT_MEAS_UE_ACK(0xF007)
 *
 * 此接口用于配置UE的测量模式，
 * 版本默认发布u8WorkMode为0（持续侦码模式）,
 * 各个模式的功能说明以及相关配置流程
 * @see Cfg_wlMEAS
 **/
@Nya(l = 15 + Define.C_MAX_IMSI_LEN)
@Info("设置基站测量UE配置")
@VtbBean(OFLType.O_FL_LMT_TO_ENB_MEAS_UE_CFG)
@RspType(OFLType.O_FL_ENB_TO_LMT_MEAS_UE_ACK)
public class Cfg_dwMEAS extends Bean {

    /**
     * 测试UE模式配置
     * 0: 持续侦码模式
     * 1: 周期侦码模式
     * 2: 定位模式
     * 3: 管控模式
     * 4: 重定向模式
     */
    @Nya(l = 1)
    public int u8_u8WorkMode;

    /**
     * 0: 名单中的用户执行重定向；名单外的全部踢回公网
     * 1: 名单中的用户踢回公网；名单外的全部重定向
     * 2: 名单中的用户执行重定向；名单外的全部吸附在本站
     * 3: 名单中的用户吸附在本站;名单外的全部重定向
     * 4: 所有目标重定向
     * 5： 名单中的踢回公网，名单内的重定向，基站自动更新名单。1分钟周期对半清除名单。
     * [0…4]
     * u8WorkMode仅当取4（重定向模式）有效。
     */
    @Nya(f = 1, l = 1)
    public int u8_u8RedirectSubMode;

    /**
     * 周期模式参数，指示针对同一个IMSI的抓号周期
     * 5~65535分钟
     */
    @Nya(f = 2, l = 2)
    public int u16_u16CapturePeriod;

    @Nya(f = 4, l = Define.C_MAX_IMSI_LEN)
    public String u8$IMSI_IMSI;

    /**
     * 定位模式，终端测量值的上报周期，建议设置为1024ms
     * 0：120ms
     * 1：240ms
     * 2：480ms
     * 3：640ms
     * 4：1024ms
     * 5：2048ms
     */
    @Nya(f = 4 + Define.C_MAX_IMSI_LEN, l = 1)
    public int u8_u8MeasReportPeriod;

    /**
     * 定位模式，调度定位终端最大功率发射开关，需要设置为0
     * 0：enable
     * 1：disable
     */
    @Nya(f = 5 + Define.C_MAX_IMSI_LEN, l = 1)
    public int u8_SchdUeMaxPowerTxFlag;

    /**
     * 定位模式，UE最大发射功率，
     * 最大值不超过wrFLLmtToEnbSysArfcnCfg配置的UePMax，
     * 建议设置为23dBm
     * 0~23dBm
     */
    @Nya(f = 6 + Define.C_MAX_IMSI_LEN, l = 1)
    public int u8_SchdUeMaxPowerValue;

    /**
     * 定位模式，由于目前都采用SRS方案配合单兵，
     * 因此此值需要设置为disable，
     * 否则大用户量时有定位终端掉线概率。
     * 0: disable
     * 1: enable
     */
    @Nya(f = 7 + Define.C_MAX_IMSI_LEN, l = 1)
    public int u8_SchdUeUlFixedPrbSwitch;

    /**
     * 定位模式，是否把非定位终端踢回公网的配置，建议设置为0。
     * 设置为1的方式是为了发布版本时保留用户反复接入基站测试使用。
     * 1：非定位终端继续被本小区吸附,
     * 0：把非定位终端踢回公网
     */
    @Nya(f = 8 + Define.C_MAX_IMSI_LEN, l = 1)
    public int u8_CampOnAllowedFlag;

    /**
     * 定位模式，是否对定位终端建立SRS配置。
     * 0: disable
     * 1: enable
     */
    @Nya(f = 9 + Define.C_MAX_IMSI_LEN, l = 1)
    public int u8_SrsSwitch;

    /**
     * 保留字节
     */
    @Nya(f = 10 + Define.C_MAX_IMSI_LEN, l = 1)
    public int u8_Res;

    /**
     * 管控模式的子模式
     * 0：黑名单子模式；1：白名单子模式
     */
    @Nya(f = 11 + Define.C_MAX_IMSI_LEN, l = 1)
    public int u8_u8ControlSubMode;

    /**
     * 保留字节
     */
    @Nya(f = 12 + Define.C_MAX_IMSI_LEN, l = 3)
    public int u8$3_Res;

}
