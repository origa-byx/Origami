package vtb.arisu.mana.bean.report;

import vtb.arisu.mana.annotation.Info;
import vtb.arisu.mana.annotation.Nya;
import vtb.arisu.mana.annotation.VtbBean;
import vtb.arisu.mana.bean.OFLType;
import vtb.mashiro.kanon.base.Bean;
import vtb.mashiro.kanon.util.ByteUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @by: origami
 * @date: {2022/6/1}
 * @info: 选频配置查询-ACK
 * @see vtb.arisu.mana.bean.send.Query_selectFreqCfg
 **/
@Nya
@Info("选频配置查询-ACK")
@VtbBean(OFLType.O_FL_ENB_TO_LMT_SELECT_FREQ_CFG_QUERY_ACK)
public class Ack_QuerySelectFreqCfg extends Bean {

    public final List<PinBandRelation> pinBandRelaList = new ArrayList<>();

    @Override
    public void fromBytes(byte[] body) {
        int u32_PinBandRelaNum = ByteUtil.toInt_s(body, 0, 4);
        int f = 4;
        Nya nya = PinBandRelation.class.getDeclaredAnnotation(Nya.class);
        if(nya == null) return;
        byte[] items = new byte[nya.l()];
        for (int i = 0; i < u32_PinBandRelaNum; i++) {
            System.arraycopy(body, f, items, 0, nya.l());
            PinBandRelation pinBandRelation = new PinBandRelation();
            pinBandRelation.fromBytes(items);
            pinBandRelaList.add(pinBandRelation);
            f += nya.l();
        }
    }

    @Override
    public int getSize() {
        int size = 4;
        int mun = Math.min(15, pinBandRelaList.size());
        Nya nya = PinBandRelation.class.getDeclaredAnnotation(Nya.class);
        if(nya != null)
            size += nya.l() * mun;
        return size;
    }

    @Override
    public byte[] toBytes() {
        byte[] body = new byte[getSize()];
        int mun = Math.min(15, pinBandRelaList.size());
        ByteUtil.setNum_s(body, 0, 4, mun);
        int f = 4;
        for (int i = 0; i < mun; i++) {
            byte[] bytes = pinBandRelaList.get(i).toBytes();
            ByteUtil.setByteArray(body, f, bytes.length, bytes);
            f += bytes.length;
        }
        return body;
    }

    @Nya
    public static class PinBandRelation extends Bean{

        /**
         * 管脚取值范围，低位有效。
         * V2 Board：{0..3}
         * V3 Board：{1..15}
         */
        @Nya(l = 1)
        public int u8_pinValue;

        /**
         * 频带取值范围1..44;
         * 0:不配置。
         * V2 Board：{0..3}
         * V3 Board：{1..15}
         */
        @Nya(f = 1, l = 1)
        public int u8_BandVal1;

        /**
         * 频带取值范围1..44;
         * 0:不配置。
         * V2 Board：{0..3}
         * V3 Board：{1..15}
         */
        @Nya(f = 2, l = 1)
        public int u8_BandVal2;

        /**
         * 频带取值范围1..44;
         * 0:不配置。
         * V2 Board：{0..3}
         * V3 Board：{1..15}
         */
        @Nya(f = 3, l = 1)
        public int u8_BandVal3;

    }

}
