package vtb.arisu.mana.bean.send;

import vtb.arisu.mana.annotation.Info;
import vtb.arisu.mana.annotation.Nya;
import vtb.arisu.mana.annotation.RspType;
import vtb.arisu.mana.annotation.VtbBean;
import vtb.arisu.mana.bean.OFLType;
import vtb.mashiro.kanon.base.Bean;
import vtb.mashiro.kanon.util.ByteUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @by: origami
 * @date: {2022/5/25}
 * @info: Band功率衰减关系表配置
 * 是否立即生效：是
 * 重启是否保留配置：是
 * 应答消息（eNB ->LMT）：
 * O_FL_ENB_TO_LMT_MULTI_BAND_POWERDEREASE_ACK (0xF0A8)
 *
 * 该接口用于配置不同band的小区时，基站根据band，查询关系表，配置衰减值。
 * 不支持增量配置。若建小区的band无法在表中查找到对应衰减值，默认输出衰减值0xFF。
 * 出厂默认关闭该功能
 **/
@Info("Band功率衰减关系表配置")
@VtbBean(OFLType.O_FL_LMT_TO_ENB_MULTI_BAND_POWERDEREASE_CFG)
@RspType(OFLType.O_FL_ENB_TO_LMT_MULTI_BAND_POWERDEREASE_ACK)
public class Cfg_bandPowerDEREASE extends Bean {

    /**
     * Band和衰减值对应关系
     * 长度0 or null: 关闭功能
     * >0:配置相关关系
     * 最多 32 个
     */
    public List<BandPwrdereaseMap> type32_BandPwrdereaseMaps;

    @Nya
    public static class BandPwrdereaseMap extends Bean{

        /**
         * 频带值
         * 1..255
         */
        @Nya(l = 1)
        public int u8_band;

        /**
         * 衰减值
         * 0..255
         */
        @Nya(f = 1, l = 1)
        public int u8_Pwrderease;

        /**
         * 预留
         */
        @Nya(f = 2, l = 2)
        public int U8$2_Reserved;

        public BandPwrdereaseMap initByBytes(byte[] body){
            super.fromBytes(body);
            return this;
        }

        public byte[] tBytes(){
            return super.toBytes();
        }

    }

    @Override
    public void fromBytes(byte[] body) {
        int u8_NumElem = ByteUtil.toInt_s(body, 0, 1);
        type32_BandPwrdereaseMaps = new ArrayList<>();
        Nya nya = BandPwrdereaseMap.class.getDeclaredAnnotation(Nya.class);
        if(nya == null)
            return;
        int f = 1;
        for (int i = 0; i < u8_NumElem; i++) {
            byte[] item = new byte[nya.l()];
            System.arraycopy(body, f, item, 0, nya.l());
            f += nya.l();
            type32_BandPwrdereaseMaps.add(new BandPwrdereaseMap().initByBytes(item));
        }
    }

    @Override
    public byte[] toBytes() {
        int u8_NumElem;
        if (type32_BandPwrdereaseMaps != null)
            u8_NumElem = Math.min(32, type32_BandPwrdereaseMaps.size());
        else
            u8_NumElem = 0;
        Nya nya = BandPwrdereaseMap.class.getDeclaredAnnotation(Nya.class);
        if(nya == null)
            return new byte[0];
        byte[] body = new byte[u8_NumElem * nya.l() + 1];
        ByteUtil.setNum_s(body, 0, 1, u8_NumElem);
        int f = 1;
        for (int i = 0; i < u8_NumElem; i++) {
            ByteUtil.setByteArray(body, f, nya.l(), type32_BandPwrdereaseMaps.get(i).tBytes());
            f += nya.l();
        }
        return body;
    }
}
