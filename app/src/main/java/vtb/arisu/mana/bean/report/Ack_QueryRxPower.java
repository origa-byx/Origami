package vtb.arisu.mana.bean.report;

import vtb.arisu.mana.annotation.Info;
import vtb.arisu.mana.annotation.Nya;
import vtb.arisu.mana.annotation.VtbBean;
import vtb.arisu.mana.bean.OFLType;
import vtb.mashiro.kanon.base.Bean;

/**
 * @by: origami
 * @date: {2022/6/1}
 * @info: RX口功率值查询-ACK
 * @see vtb.arisu.mana.bean.send.Query_rxPower
 **/
@Nya
@Info("RX口功率值查询-ACK")
@VtbBean(OFLType.O_FL_ENB_TO_LMT_GET_RX_PARAMS_ACK)
public class Ack_QueryRxPower extends Bean {

    /**
     * RX口功率值，单位:dbm
     */
    @Nya
    public float f32_RxPwrVal;

}
