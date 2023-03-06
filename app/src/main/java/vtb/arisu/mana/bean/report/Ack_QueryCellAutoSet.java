package vtb.arisu.mana.bean.report;

import vtb.arisu.mana.annotation.Info;
import vtb.arisu.mana.annotation.Nya;
import vtb.arisu.mana.annotation.VtbBean;
import vtb.arisu.mana.bean.OFLType;
import vtb.mashiro.kanon.base.Bean;

/**
 * @by: origami
 * @date: {2022/5/31}
 * @info: 电小区自激活配置查询-ACK
 * @see vtb.arisu.mana.bean.send.Query_cellAutoSet
 **/
@Nya
@Info("电小区自激活配置查询-ACK")
@VtbBean(OFLType.O_FL_ENB_TO_LMT_SELF_ACTIVE_CFG_PWR_ON_QUERY_ACK)
public class Ack_QueryCellAutoSet extends Bean {

    /**
     * 基站上电是否采用当前配置自动激活小区
     * 0：上电自激活
     * 1：上电不自激活
     */
    @Nya
    public int u32_SelfActiveCfg;

}
