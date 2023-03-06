package vtb.arisu.mana.bean.send;

import vtb.arisu.mana.annotation.Info;
import vtb.arisu.mana.annotation.Nya;
import vtb.arisu.mana.annotation.RspType;
import vtb.arisu.mana.annotation.VtbBean;
import vtb.arisu.mana.bean.OFLType;
import vtb.mashiro.kanon.base.Bean;

/**
 * @by: origami
 * @date: {2022/5/26}
 * @info: SRS参数配置（LMT -> eNB）
 * 是否立即生效：是
 * 重启是否保留配置：是
 * 应答消息（eNB ->LMT）：O_FL_ENB_TO_LMT_SRS_CFG_ACK (0xF085)
 *
 * 此消息接口用于SRS相关配置，基站默认SRS配置关闭。对于DW版本，
 * 打开SRS功能，可以达到让定位终端固定周期(毫秒级)且在固定4个PRB上发送上行消息，
 * 可以配合单兵设备精准的定位终端位置。
 * 该接口支持立即生效，但是如果DW在线的话，会导致DW终端掉线再重新连接
 **/
@Nya(l = 12)
@Info("SRS参数配置")
@VtbBean(OFLType.O_FL_LMT_TO_ENB_SRS_CFG)
@RspType(OFLType.O_FL_ENB_TO_LMT_SRS_CFG_ACK)
public class Cfg_srsArgs extends Bean {

    /**
     * 是否开启SRS配置
     * 0：关闭
     * 1：开启
     */
    @Nya(l = 1)
    public int u8_SrsEnable;

    /**
     * Cell级SRS配置中
     * srs-BandwidthConfig 字段值，取值范围
     * (0~7)
     */
    @Nya(f = 1, l = 1)
    public int u7_C_SRS;

    /**
     * Cell级SRS配置中
     * srs-SubframeConfig字段值
     * (0~15)
     */
    @Nya(f = 2, l = 1)
    public int u8_SubframeConfig;

    /**
     * Cell级SRS配置中
     * ackNackSRS-SimultaneousTransmission字段
     * (0~1)
     */
    @Nya(f = 3, l = 1)
    public int u8_ackNackSRS_SimTrans;

    /**
     * 保留字节
     */
    @Nya(f = 4, l = 1)
    public int u8_Res;


    /**
     * UE级SRS配置中
     * srs-Bandwidth字段
     * 0~3
     */
    @Nya(f = 5, l = 1)
    public int u8_B_SRS;

    /**
     * UE级SRS配置中
     * srs-HoppingBandwidth
     * 字段
     * 0~3
     */
    @Nya(f = 6, l = 1)
    public int u8_b_hop;

    /**
     * UE级SRS配置中
     * freqDomainPosition字段
     * 0~23
     */
    @Nya(f = 7, l = 1)
    public int u8_n_RRC;

    /**
     * UE级SRS配置中duration字段
     * 0~1
     */
    @Nya(f = 8, l = 1)
    public int u8_duration;

    /**
     * UE级SRS配置中
     * Srs-ConfigIndex字段
     * 0~1023
     */
    @Nya(f = 9, l = 1)
    public int u8_I_SRS;

    /**
     * UE级SRS配置中
     * transmissionComb字段
     * 0~1
     */
    @Nya(f = 10, l = 1)
    public int u8_Comb;

    /**
     * UE级SRS配置中
     * cyclicShift字段
     * 0~7
     */
    @Nya(f = 11, l = 1)
    public int u8_n_cs_SRS;

}
