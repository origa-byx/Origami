package vtb.arisu.mana.bean.send;

import vtb.arisu.mana.annotation.Info;
import vtb.arisu.mana.annotation.Nya;
import vtb.arisu.mana.annotation.RspType;
import vtb.arisu.mana.annotation.VtbBean;
import vtb.arisu.mana.bean.OFLType;
import vtb.mashiro.kanon.base.Bean;
import vtb.mashiro.kanon.base.Packet;
import vtb.mashiro.kanon.util.ByteUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @by: origami
 * @date: {2022/5/24}
 * @info: 选频配置
 * 是否立即生效：否
 * 重启是否保留配置：是
 * 应答消息（eNB ->LMT）：
 * O_FL_ENB_TO_LMT_SELECT_FREQ_CFG_ACK (0xF083)
 *
 * 基站根据当前激活小区的BAND输出不同的GPIO信号，
 * 用于客户一个单板切换不同的BAND建立小区时根据该GPIO信号匹配不同的功放。
 * 输出GPIO信号的管脚以及配置描述见下图。目前V2 Board提供2根GPIO管脚，
 * V3 Board以及后面型号的Board提供4根GPIO管脚，配置接口兼容支持，
 * 请根据不同的Board配置值范围
 **/
@Info("选频配置")
@VtbBean(OFLType.O_FL_LMT_TO_ENB_SELECT_FREQ_CFG)
@RspType(OFLType.O_FL_ENB_TO_LMT_SELECT_FREQ_CFG_ACK)
public class Cfg_SelectFreq extends Bean {

    /**
     * 管脚频带关系表
     */
    public List<PinBandRelaMap> u32$var_pinBandRelaMap;

    @Nya
    public static class PinBandRelaMap extends Bean{

        /**
         * 管脚取值范围，低位有效。
         * V2 Board：{0..3}
         * V3 Board：{1..15}
         * V5 Board：{1..7}
         */
        @Nya(l = 1)
        public int u8_pinValue;

        /**
         * 频带取值范围1..44;
         * 0:不配置。
         */
        @Nya(f = 1, l = 1)
        public int u8_BandVal1;

        /**
         * 频带取值范围1..44;
         * 0:不配置。
         */
        @Nya(f = 2, l = 1)
        public int u8_BandVal2;

        /**
         * 频带取值范围1..44;
         * 0:不配置。
         */
        @Nya(f = 3, l = 1)
        public int u8_BandVal3;

        PinBandRelaMap initByItem(byte[] body) {
            super.fromBytes(body);
            return this;
        }

        public byte[] toArrayBytes() {
            return super.toBytes();
        }

    }

    @Override
    public void fromBytes(byte[] body) {
        u32$var_pinBandRelaMap = new ArrayList<>();
        Nya nya = PinBandRelaMap.class.getDeclaredAnnotation(Nya.class);
        if(nya == null)
            throw new IllegalArgumentException("class PinBandRelaMap missed Nya");
        int size = ByteUtil.toInt_s(body, 0, 4);
        int f = 4;
        for (int i = 0; i < size; i++) {
            byte[] item = new byte[nya.l()];
            System.arraycopy(body, f, item, 0, nya.l());
            u32$var_pinBandRelaMap.add(new PinBandRelaMap().initByItem(item));
            f += nya.l();
        }
    }

    @Override
    public byte[] toBytes() {
        if(u32$var_pinBandRelaMap == null)
            return new byte[0];
        Nya nya = PinBandRelaMap.class.getDeclaredAnnotation(Nya.class);
        if(nya == null)
            throw new IllegalArgumentException("class PinBandRelaMap missed Nya");
        int size = Math.min(15, u32$var_pinBandRelaMap.size());
        byte[] body = new byte[4 + nya.l() * size];
        int f = 4;
        for (int i = 0; i < size; i++) {
            ByteUtil.setByteArray(body, f, nya.l(), u32$var_pinBandRelaMap.get(i).toArrayBytes());
            f += nya.l();
        }
        return body;
    }
}
