package vtb.arisu.mana.bean.send;

import vtb.arisu.mana.annotation.Info;
import vtb.arisu.mana.annotation.Nya;
import vtb.arisu.mana.annotation.RspType;
import vtb.arisu.mana.annotation.VtbBean;
import vtb.arisu.mana.bean.OFLType;
import vtb.mashiro.kanon.base.Bean;

/**
 * @by: origami
 * @date: {2022/5/26}
 * @info: 定位UE处理配置（LMT -> eNB）
 * 是否立即生效：是
 * 重启是否保留配置：是
 * 应答消息（eNB ->LMT）：O_FL_ENB_TO_LMT_DW_FLOW_TYPE_CFG_ACK (0xF0A2)
 * 此接口用于区别对于定位UE的处理流程。共有两种处理方案，可根据需求进行选择。
 * 默认出厂配置为1
 **/
@Nya
@Info("定位UE处理配置")
@VtbBean(OFLType.O_FL_LMT_TO_ENB_DW_FLOW_TYPE_CFG)
@RspType(OFLType.O_FL_ENB_TO_LMT_DW_FLOW_TYPE_CFG_ACK)
public class Cfg_dwFlowType extends Bean {

    /**
     * 0,1
     * 0:定位终端在定位过程中仍显示4G在线，因此对于定位具有隐藏效果。
     *      但是在个别运营商宏站覆盖范围内会出现定位终端掉线后难以再接入的问题（出现概率较低）。
     * 1:不存在上述定位终端掉线后难以接入问题，但定位终端在定位过程中会显示无服务。
     */
    @Nya
    public int u32_DwFlowType;

}
