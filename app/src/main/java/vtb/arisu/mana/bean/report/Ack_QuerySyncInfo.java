package vtb.arisu.mana.bean.report;

import vtb.arisu.mana.annotation.Info;
import vtb.arisu.mana.annotation.Nya;
import vtb.arisu.mana.annotation.VtbBean;
import vtb.arisu.mana.bean.OFLType;

/**
 * @by: origami
 * @date: {2022/5/31}
 * @info: 基站同步信息查询-ACK
 * @see vtb.arisu.mana.bean.send.Query_syncInfo
 **/
@Nya
@Info("基站同步信息查询-ACK")
@VtbBean(OFLType.O_FL_ENB_TO_LMT_SYNC_INFO_QUERY_ACK)
public class Ack_QuerySyncInfo {

    /**
     * 同步类型
     * 0：空口同步（FDD此值代表 GPS同步disable）
     * 1：GPS同步
     */
    @Nya(l = 2)
    public int u16_u16SyncMode;

    /**
     * 同步状态
     * 0：GPS同步成功；
     * 1：空口同步成功，
     * 2：未同步
     * 3: GPS失步
     * 4：空口失步
     */
    @Nya(f = 2, l = 2)
    public int u16_u16SyncState;

}
