package vtb.arisu.mana.bean.report;

import vtb.arisu.mana.annotation.Info;
import vtb.arisu.mana.annotation.VtbBean;
import vtb.arisu.mana.bean.OFLType;
import vtb.arisu.mana.bean.send.Cfg_wlMEAS;
import vtb.mashiro.kanon.base.Bean;

/**
 * @by: origami
 * @date: {2022/6/1}
 * @info: UE测量配置查询-ACK
 * @see vtb.arisu.mana.bean.send.Query_ueMeasCfg
 **/
@Info("UE测量配置查询-ACK")
@VtbBean(OFLType.O_FL_ENB_TO_LMT_MEAS_UE_CFG_QUERY_ACK)
public class Ack_QueryUeMeasCfg extends Bean {

    public final Cfg_wlMEAS meas = new Cfg_wlMEAS();

    @Override
    public void fromBytes(byte[] body) {
        meas.fromBytes(body);
    }

    @Override
    public int getSize() {
        return meas.getSize();
    }

    @Override
    public byte[] toBytes() {
        return meas.toBytes();
    }
}
