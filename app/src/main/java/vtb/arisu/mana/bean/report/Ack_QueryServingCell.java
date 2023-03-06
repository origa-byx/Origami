package vtb.arisu.mana.bean.report;

import vtb.arisu.mana.annotation.Info;
import vtb.arisu.mana.annotation.Nya;
import vtb.arisu.mana.annotation.VtbBean;
import vtb.arisu.mana.bean.OFLType;
import vtb.arisu.mana.bean.send.Cfg_servingCellArgs;
import vtb.mashiro.kanon.base.Bean;

/**
 * @by: origami
 * @date: {2022/5/31}
 * @info: 服务小区配置参数查询-ACK
 * @see vtb.arisu.mana.bean.send.Query_servingCell
 **/
@Info("服务小区配置参数查询-ACK")
@VtbBean(OFLType.O_FL_ENB_TO_LMT_ARFCN_IND)
public class Ack_QueryServingCell extends Bean {

    public final Cfg_servingCellArgs servingCellArgs = new Cfg_servingCellArgs();

    @Override
    public void fromBytes(byte[] body) {
        servingCellArgs.fromBytes(body);
    }

    @Override
    public int getSize() {
        return servingCellArgs.getSize();
    }

    @Override
    public byte[] toBytes() {
        return servingCellArgs.toBytes();
    }

}
