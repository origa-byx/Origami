package vtb.arisu.mana.bean.report;

import vtb.arisu.mana.annotation.Info;
import vtb.arisu.mana.annotation.Nya;
import vtb.arisu.mana.annotation.VtbBean;
import vtb.arisu.mana.bean.OFLType;
import vtb.mashiro.kanon.base.Bean;

/**
 * @by: origami
 * @date: {2022/6/1}
 * @info: GPS同步模式下的pps1s偏移量查询-ACK
 * @see vtb.arisu.mana.bean.send.Query_gspPpsls
 **/
@Nya
@Info("GPS同步模式下的pps1s偏移量查询-ACK")
@VtbBean(OFLType.O_FL_ENB_TO_LMT_GSP1PPS_QUERY_ACK)
public class Ack_QueryGpsPpsls extends Bean {

    @Nya
    public int s32_Gpspps1s;

}
