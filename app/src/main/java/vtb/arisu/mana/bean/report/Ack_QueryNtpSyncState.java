package vtb.arisu.mana.bean.report;

import vtb.arisu.mana.annotation.Info;
import vtb.arisu.mana.annotation.Nya;
import vtb.arisu.mana.annotation.VtbBean;
import vtb.arisu.mana.bean.OFLType;
import vtb.mashiro.kanon.base.Bean;

/**
 * @by: origami
 * @date: {2022/6/1}
 * @info: NTP同步状态查询-ACK
 * @see vtb.arisu.mana.bean.send.Query_ntpSyncState
 **/
@Nya
@Info("NTP同步状态查询-ACK")
@VtbBean(OFLType.O_FL_ENB_TO_LMT_NTP_SYNC_STATE_QUERY_ACK)
public class Ack_QueryNtpSyncState extends Bean {

    /**
     * 0：Not Sync
     * 1：Sync Succ
     */
    @Nya(l = 1)
    public int u8_u8NtpSyncState;

    /**
     * 空余字节
     */
    @Nya(f = 1, l = 3)
    public int u8$3_Res;

}
