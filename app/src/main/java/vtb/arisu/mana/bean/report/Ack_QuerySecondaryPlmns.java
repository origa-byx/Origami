package vtb.arisu.mana.bean.report;

import vtb.arisu.mana.annotation.Info;
import vtb.arisu.mana.annotation.Nya;
import vtb.arisu.mana.annotation.VtbBean;
import vtb.arisu.mana.bean.OFLType;
import vtb.mashiro.kanon.base.Bean;
import vtb.mashiro.kanon.util.ByteUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @by: origami
 * @date: {2022/6/1}
 * @info: 辅PLMN列表查询-ACK
 * @see vtb.arisu.mana.bean.send.Query_secondaryPlmns
 **/
@Nya
@Info("辅PLMN列表查询-ACK")
@VtbBean(OFLType.O_FL_ENB_TO_LMT_SECONDARY_PLMNS_QUERY_ACK)
public class Ack_QuerySecondaryPlmns extends Bean {

    public final List<String> u8SecPLMNList = new ArrayList<>();

    @Override
    public void fromBytes(byte[] body) {
        int u8SecPLMNNum = ByteUtil.toInt_s(body, 0, 1);
        int f = 1;
        for (int i = 0; i < u8SecPLMNNum; i++) {
            u8SecPLMNList.add(ByteUtil.toString(body, f, 7));
            f += 7;
        }
    }

    @Override
    public int getSize() {
        int mun = Math.min(5, u8SecPLMNList.size());
        return 1 + 7 * mun;
    }

    @Override
    public byte[] toBytes() {
        byte[] body = new byte[getSize()];
        int mun = Math.min(5, u8SecPLMNList.size());
        ByteUtil.setNum_s(body, 0, 1, mun);
        int f = 1;
        for (int i = 0; i < mun; i++) {
            ByteUtil.setString(body, f, 7, u8SecPLMNList.get(i));
            f += 7;
        }
        return super.toBytes();
    }
}
