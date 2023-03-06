package vtb.arisu.mana.bean.report;

import vtb.arisu.mana.annotation.Info;
import vtb.arisu.mana.annotation.Nya;
import vtb.arisu.mana.annotation.VtbBean;
import vtb.arisu.mana.bean.Define;
import vtb.arisu.mana.bean.OFLType;
import vtb.mashiro.kanon.base.Bean;

/**
 * @by: origami
 * @date: {2022/5/25}
 * @info: 定位UE测量值上报（eNB -> LMT）
 * 此接口用于基站测量定位的终端测量值，
 * 用于衡量距离的远近，值越大，代表UE距基站距离越近
 **/
@Nya(l = 3 + Define.C_MAX_IMSI_LEN)
@Info("定位UE测量值上报")
@VtbBean(OFLType.O_FL_ENB_TO_LMT_MEAS_INFO_RPT)
public class MeasInfo extends Bean {

    /**
     * 定位UE的测量值
     * 0~255
     */
    @Nya(l = 1)
    public int u8_UeMeasValue;

    /**
     * 定位模式，定位的终端IMSI
     */
    @Nya(f = 1, l = Define.C_MAX_IMSI_LEN)
    public int u8$IMSI_IMSI;

    /**
     * 保留字节
     */
    @Nya(f = 1 + Define.C_MAX_IMSI_LEN, l = 1)
    public int u8_Res;

    /**
     * 终端类型
     * 0：R8
     * 1：R10
     */
    @Nya(f = 2 + Define.C_MAX_IMSI_LEN, l = 1)
    public int u8_ProtocolVer;

}
