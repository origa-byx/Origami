package vtb.arisu.mana.bean.report;

import vtb.arisu.mana.annotation.Info;
import vtb.arisu.mana.annotation.Nya;
import vtb.arisu.mana.annotation.VtbBean;
import vtb.arisu.mana.bean.OFLType;
import vtb.mashiro.kanon.base.Bean;

/**
 * @by: origami
 * @date: {2022/5/31}
 * @info: 小区状态信息查询-ACK
 *
 * @see vtb.arisu.mana.bean.send.Query_cellStateInfo
 **/
@Nya
@Info("小区状态信息查询-ACK")
@VtbBean(OFLType.O_FL_ENB_TO_LMT_CELL_STATE_INFO_QUERY_ACK)
public class Ack_QueryCellStateInfo extends Bean {

    /**
     * 小区状态
     * 0：小区空闲态
     * 1：同步或扫频中
     * 2：小区激活中
     * 3：小区已激活
     * 4：小区去激活中
     * 7:  频偏校准进行中
     */
    @Nya
    public int u32_u32CellState;

}
