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
 * @info: 设置基站测量UE配置（LMT -> eNB）
 * WL 相关
 * 是否立即生效：是
 * 重启是否保留配置：是
 * 应答消息（eNB ->LMT）：O_FL_ENB_TO_LMT_MEAS_UE_ACK(0xF007)
 * 此接口用于配置UE的测量模式，
 * 版本默认发布u8WorkMode为0（持续侦码模式）,
 * 各个模式的功能说明以及相关配置流程，
 * 详:
 *  在小区空闲或者激活态下，配置测量UE的模式，
 *  配置可立即生效，WL版本相关配置重启也保留。
 *  如果是空闲态下配置UE测量模式，配置完成之后，
 *  还需要激活小区，基站不会自己触发激活小区的流程。
 * 测量UE支持强干扰模式，周期采号模式，管控模式，
 * 重定向模式，定位模式（此模式仅DW版本支持）以及IMSI老化模式。
 * 各个工作模式配置方式以及处理说明，见 [3.4  基站测量UE ]章节
 **/
@Nya(l = 12)
@Info("设置基站测量UE配置")
@VtbBean(OFLType.O_FL_LMT_TO_ENB_MEAS_UE_CFG)
@RspType(OFLType.O_FL_ENB_TO_LMT_MEAS_UE_ACK)
public class Cfg_wlMEAS extends Bean {

    /**
     * 0: 持续侦码模式
     * 1: 周期侦码模式
     * 3: 管控模式
     * 4: 重定向模式
     * 6: 老化模式
     *
     * 取值范围:0,1,3
     * 默认0
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
     * Capture reperiod，只在periodic capture&重定向模式模式下使用
     * 1~65535
     */
    @Nya(f = 2, l = 2)
    public int u16_u16CapturePeriod;

    /**
     * 配置管控模式的子模式
     * 只在管控模式下有效
     * 0：黑名单子模式；1：白名单子模式
     */
    @Nya(f = 4, l = 1)
    public int u8_u8ControlSubMode;

    /**
     * 保留字节
     */
    @Nya(f = 5, l = 3)
    public int u8$3_Res;

    /**
     * 重定向限制时间,单位:秒
     * 1~180
     */
    @Nya(f = 8, l = 2)
    public int u16_imsiRedirectTime;

    /**
     * IMSI老化时间,单位:秒,按照老化模式设计,该值必须大于重定向限制时间
     * 1~300
     */
    @Nya(f = 10, l = 2)
    public int u16_imsiAgingTime;

}
