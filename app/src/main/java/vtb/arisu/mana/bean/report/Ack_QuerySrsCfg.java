package vtb.arisu.mana.bean.report;

import vtb.arisu.mana.annotation.Info;
import vtb.arisu.mana.annotation.VtbBean;
import vtb.arisu.mana.bean.OFLType;
import vtb.arisu.mana.bean.send.Cfg_srsArgs;
import vtb.mashiro.kanon.base.Bean;

/**
 * @by: origami
 * @date: {2022/6/1}
 * @info: SRS配置查询-ACK
 * @see vtb.arisu.mana.bean.send.Query_srsCfg
 **/
@Info("SRS配置查询-ACK")
@VtbBean(OFLType.O_FL_ENB_TO_LMT_SRS_CFG_QUERY_ACK)
public class Ack_QuerySrsCfg extends Bean {

    public final Cfg_srsArgs args = new Cfg_srsArgs();

    @Override
    public void fromBytes(byte[] body) {
        args.fromBytes(body);
    }

    @Override
    public int getSize() {
        return args.getSize();
    }

    @Override
    public byte[] toBytes() {
        return args.toBytes();
    }
}
