package vtb.arisu.mana.bean.report;

import vtb.arisu.mana.annotation.Info;
import vtb.arisu.mana.annotation.Nya;
import vtb.arisu.mana.annotation.VtbBean;
import vtb.arisu.mana.bean.OFLType;
import vtb.mashiro.kanon.base.Bean;

/**
 * @by: origami
 * @date: {2022/6/1}
 * @info: 扫频/同步端口查询-ACK
 * @see vtb.arisu.mana.bean.send.Query_scanSyncPort
 **/
@Nya
@Info("扫频/同步端口查询-ACK")
@VtbBean(OFLType.O_FL_ENB_TO_LMT_REM_PORT_QUERY_ACK)
public class Ack_QueryScanSyncPort extends Bean {

    /**
     * 扫频端口
     * 1：Snf口
     * 0：Rx口
     * 0,1
     */
    @Nya(l = 1)
    public int u8_ScanPort;

    /**
     * 同步端口
     * 1：Snf口
     * 0：Rx口
     * 0,1
     */
    @Nya(f = 1, l = 1)
    public int u8_SyncPort;

    /**
     * 保留字节
     */
    @Nya(f = 2, l = 2)
    public int u16_Spare;

}
