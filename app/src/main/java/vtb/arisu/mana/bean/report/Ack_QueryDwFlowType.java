package vtb.arisu.mana.bean.report;

import vtb.arisu.mana.annotation.Info;
import vtb.arisu.mana.annotation.Nya;
import vtb.arisu.mana.annotation.VtbBean;
import vtb.arisu.mana.bean.OFLType;
import vtb.mashiro.kanon.base.Bean;

/**
 * @by: origami
 * @date: {2022/6/1}
 * @info: 定位UE处理配置查询-ACK
 * @see vtb.arisu.mana.bean.send.Query_dwFlowType
 **/
@Nya
@Info("定位UE处理配置查询-ACK")
@VtbBean(OFLType.O_FL_ENB_TO_LMT_DW_FLOW_TYPE_QUERY_ACK)
public class Ack_QueryDwFlowType extends Bean {

    /**
     * 0:定位终端在定位过程中仍显示4G在线，因此对于定位具有隐藏效果。但是在个别运营商宏站覆盖范围内会出现定位终端掉线后难以再接入的问题（出现概率较低）。
     * 1:不存在上述定位终端掉线后难以接入问题，但定位终端在定位过程中会显示无服务。
     * 0,1
     */
    @Nya
    public int u32_DwFlowType;

}
