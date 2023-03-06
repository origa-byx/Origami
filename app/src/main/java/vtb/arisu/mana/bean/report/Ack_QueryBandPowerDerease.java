package vtb.arisu.mana.bean.report;

import vtb.arisu.mana.annotation.Info;
import vtb.arisu.mana.annotation.VtbBean;
import vtb.arisu.mana.bean.OFLType;
import vtb.arisu.mana.bean.send.Cfg_bandPowerDEREASE;
import vtb.mashiro.kanon.base.Bean;

/**
 * @by: origami
 * @date: {2022/6/1}
 * @info: Band功率衰减关系表查询-ACK
 * @see vtb.arisu.mana.bean.send.Query_bandPowerDerease
 **/
@Info("Band功率衰减关系表查询-ACK")
@VtbBean(OFLType.O_FL_ENB_TO_LMT_MULTI_BAND_POWERDEREASE_QUERY_ACK)
public class Ack_QueryBandPowerDerease extends Bean {

    public final Cfg_bandPowerDEREASE cfg_bandPowerDEREASE = new Cfg_bandPowerDEREASE();

    @Override
    public void fromBytes(byte[] body) {
        cfg_bandPowerDEREASE.fromBytes(body);
    }

    @Override
    public int getSize() {
        return cfg_bandPowerDEREASE.getSize();
    }

    @Override
    public byte[] toBytes() {
        return cfg_bandPowerDEREASE.toBytes();
    }
}
