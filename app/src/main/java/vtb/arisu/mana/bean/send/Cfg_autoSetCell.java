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
 * @info: 启动小区自配置请求
 * 是否立即生效：是
 * 重启是否保留配置：否
 * 应答消息（eNB ->LMT）：
 * O_FL_LMT_TO_ENB_SELFCFG_CELLPARA_REQ_ACK (0xF050)
 *
 * 此接口用于指示基站在IDLE态开始小区自配置流程，小区自配置流程图参考:
 *
 *  [
 *      小区自配置激活流程
 *      基站支持根据扫频结果，自配置小区的参数，主动激活小区。由于目前仅TDD支持扫频功能，
 *      因此仅TDD支持自配置，等FDD支持扫频功能，会同时支持功能。
 *      自配置效果依赖于自配置后台频点的覆盖度。调用小区自配置请求前请确
 *      保自配置后台频点至少覆盖当前运营商所有BAND的常用频点，例如：
 *      37900,38098,38400,38544,38950, 39148,39292,40936。
 *  ]
 *
 * 基站收到此消息，根据自配置频点列表搜索公网频点和小区信息，
 * 基站会根据扫频结果，选择本小区参数自动建立小区。
 *
 * 如果整机设备支持全频段，可以配置SelfBand为0xFF，
 * 基站将会对自配置频点列表中所有频点以及公网广播消息SIB5中的频点进行全频段扫频
 *
 * --Rsp:
 * (0xF050)
 * CfgResult
 * U32
 * 0:成功
 * 1:配置失败
 * 2:自配置后台频点列表中未含指定频段的频点	配置结果
 * */
@Nya
@Info("启动小区自配置请求")
@VtbBean(OFLType.O_FL_LMT_TO_ENB_SELFCFG_CELLPARA_REQ)
@RspType(OFLType.O_FL_LMT_TO_ENB_SELFCFG_CELLPARA_REQ_ACK)
public class Cfg_autoSetCell extends Bean {

    /**
     * 指定自配置的频段
     * 38,39,40,41（TDD频段）
     * 255
     */
    @Nya(l = 1)
    public int u8_SelfBand;

    /**
     * 保留字节
     */
    @Nya(f = 1, l = 3)
    public int u8$3_Res;

}
