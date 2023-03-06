package vtb.arisu.mana.bean.report;

import vtb.arisu.mana.annotation.Info;
import vtb.arisu.mana.annotation.Nya;
import vtb.arisu.mana.annotation.VtbBean;
import vtb.arisu.mana.bean.Define;
import vtb.arisu.mana.bean.OFLType;
import vtb.mashiro.kanon.base.Bean;
import vtb.mashiro.kanon.util.ByteUtil;

/**
 * @by: origami
 * @date: {2022/5/31}
 * @info: 采集用户信息上报（eNB -> LMT）
 * 小区激活以后，基站采集接入用户信息并立即上报给客户端，只有开启IMEI捕获功能的时，才会上报IMEI。
 * 非主控板用户接口：
 * 说明：对于{@link vtb.arisu.mana.bean.send.GetIMSI_withFtp}中的配置，
 * 按照UploadImsiType取1要求并且非主控模式才进行序列号分配，才进行ACK回复。
 * 此处只对的非主控板上报，主控板上报需自己再去对接口
 **/
@Nya(l = 26 + Define.C_MAX_IMEI_LEN + Define.C_MAX_IMSI_LEN)
@Info("采集用户信息上报")
@VtbBean(OFLType.O_FL_ENB_TO_LMT_UE_INFO_RPT)
public class UeInfoRpt extends Bean {

    /**
     * UE ID 类型
     * 0:IMSI
     * 1:IMEI
     * 2:BOTH
     */
    @Nya
    public int u32_UeIdType;

    /**
     * IMSI
     */
    @Nya(f = 4, l = Define.C_MAX_IMSI_LEN)
    public String u8$IMSI_IMSI;

    /**
     * IMEI
     */
    @Nya(f = 4 + Define.C_MAX_IMSI_LEN, l = Define.C_MAX_IMEI_LEN)
    public String u8$IMEI_IMEI;

    /**
     * 采集用户的RSSI
     */
    @Nya(f = 4 + Define.C_MAX_IMEI_LEN + Define.C_MAX_IMSI_LEN, l = 1)
    public int u8_RSSI;

    /**
     * S-TMSI是否有效
     */
    @Nya(f = 5 + Define.C_MAX_IMEI_LEN + Define.C_MAX_IMSI_LEN, l = 1)
    public int u8_STMSIPresent;

    /**
     * 采集用户的S-TMSI
     */
    public int[] u8$5_S_TMSI;

    /**
     * 保留字节
     */
    @Nya(f = 11 + Define.C_MAX_IMEI_LEN + Define.C_MAX_IMSI_LEN, l = 3)
    public int u8$3_Res;

    /**
     * ねん
     */
    @Nya(f = 14 + Define.C_MAX_IMEI_LEN + Define.C_MAX_IMSI_LEN, l = 2)
    public int u16_year;

    /**
     * がつ
     */
    @Nya(f = 16 + Define.C_MAX_IMEI_LEN + Define.C_MAX_IMSI_LEN, l = 1)
    public int u8_month;

    /**
     * にち
     */
    @Nya(f = 17 + Define.C_MAX_IMEI_LEN + Define.C_MAX_IMSI_LEN, l = 1)
    public int u8_day;

    /**
     * じ
     */
    @Nya(f = 18 + Define.C_MAX_IMEI_LEN + Define.C_MAX_IMSI_LEN, l = 1)
    public int u8_hour;

    /**
     * ぷん
     */
    @Nya(f = 19 + Define.C_MAX_IMEI_LEN + Define.C_MAX_IMSI_LEN, l = 1)
    public int u8_min;

    /**
     * びょう
     */
    @Nya(f = 20 + Define.C_MAX_IMEI_LEN + Define.C_MAX_IMSI_LEN, l = 1)
    public int u8_sec;

    /**
     * 保留字节
     */
    @Nya(f = 21 + Define.C_MAX_IMEI_LEN + Define.C_MAX_IMSI_LEN, l = 1)
    public int u8_Res;

    /**
     * 序列号（针对该条消息的编号）
     */
    @Nya(f = 22 + Define.C_MAX_IMEI_LEN + Define.C_MAX_IMSI_LEN)
    public int u32_seqNum;

    @Override
    public void fromBytes(byte[] body) {
        super.fromBytes(body);
        int f = 6 + Define.C_MAX_IMEI_LEN + Define.C_MAX_IMSI_LEN;
        u8$5_S_TMSI = new int[5];
        for (int i = 0; i < 5; i++) {
            u8$5_S_TMSI[0] = ByteUtil.toInt_s(body, f, 1);
            f += 1;
        }
    }

    @Override
    public byte[] toBytes() {
        byte[] body = super.toBytes();
        if(u8$5_S_TMSI != null) {
            int size = Math.min(5, u8$5_S_TMSI.length);
            int f = 6 + Define.C_MAX_IMEI_LEN + Define.C_MAX_IMSI_LEN;
            for (int i = 0; i < size; i++) {
                ByteUtil.setNum_s(body, f, 1, u8$5_S_TMSI[i]);
                f += 1;
            }
        }
        return body;
    }

}
