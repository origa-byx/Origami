package vtb.arisu.mana.bean.report;

import vtb.arisu.mana.annotation.Info;
import vtb.arisu.mana.annotation.VtbBean;
import vtb.arisu.mana.bean.Define;
import vtb.arisu.mana.bean.OFLType;
import vtb.mashiro.kanon.base.Bean;
import vtb.mashiro.kanon.util.ByteUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @by: origami
 * @date: {2022/6/1}
 * @info: 定位模式黑名单查询-ACK
 * @see vtb.arisu.mana.bean.send.Query_locationUeBlack
 **/
@Info("定位模式黑名单查询-ACK")
@VtbBean(OFLType.O_FL_ENB_TO_LMT_LOCATION_UE_BLACKLIST_QUERY_ACK)
public class Ack_QueryLocationUeBlack extends Bean {

    /**
     * IMSI 数组
     */
    public final List<String> blackUEIdentity = new ArrayList<>();

    /**
     * 预留
     */
    public int u8$2_Res;

    @Override
    public void fromBytes(byte[] body) {
        int num = ByteUtil.toInt_s(body, 0, 2);
        int f = 2;
        for (int i = 0; i < num; i++) {
            blackUEIdentity.add(ByteUtil.toString(body, f, Define.C_MAX_IMSI_LEN));
            f += Define.C_MAX_IMSI_LEN;
        }
        u8$2_Res = ByteUtil.toInt_s(body, f, 2);
    }

    @Override
    public int getSize() {
        int mun = Math.min(Define.C_MAX_LOCATION_BLACKLIST_UE_QUERY_NUM, blackUEIdentity.size());
        return 4 + mun * Define.C_MAX_IMSI_LEN;
    }

    @Override
    public byte[] toBytes() {
        byte[] body = new byte[getSize()];
        int mun = Math.min(Define.C_MAX_LOCATION_BLACKLIST_UE_QUERY_NUM, blackUEIdentity.size());
        ByteUtil.setNum_s(body, 0, 2, mun);
        int f = 2;
        for (int i = 0; i < mun; i++) {
            ByteUtil.setString(body, f, Define.C_MAX_IMSI_LEN, blackUEIdentity.get(i));
            f += Define.C_MAX_IMSI_LEN;
        }
        ByteUtil.setNum_s(body, 0, 2, u8$2_Res);
        return body;
    }
}
