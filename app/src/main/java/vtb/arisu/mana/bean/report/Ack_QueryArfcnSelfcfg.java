package vtb.arisu.mana.bean.report;

import vtb.arisu.mana.annotation.Info;
import vtb.arisu.mana.annotation.Nya;
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
 * @info: 频点自配置后台频点列表查询-ACK
 * @see vtb.arisu.mana.bean.send.Query_arfcnSelfcfg
 **/
@Info("频点自配置后台频点列表查询-ACK")
@VtbBean(OFLType.O_FL_ENB_TO_LMT_SELFCFG_ARFCN_QUERY_ACK)
public class Ack_QueryArfcnSelfcfg extends Bean {

    public final List<Integer> arfcnValues = new ArrayList<>();

    @Override
    public void fromBytes(byte[] body) {
        int u32_DefaultArfcnNum = ByteUtil.toInt_s(body, 0, 4);
        int f = 4;
        for (int i = 0; i < u32_DefaultArfcnNum; i++) {
            arfcnValues.add(ByteUtil.toInt_s(body, f, 4));
            f += 4;
        }
    }

    @Override
    public int getSize() {
        return 4 * arfcnValues.size() + 4;
    }

    @Override
    public byte[] toBytes() {
        byte[] body = new byte[getSize()];
        int u32_DefaultArfcnNum = Math.min(Define.C_MAX_DEFAULT_ARFCN_NUM, arfcnValues.size());
        ByteUtil.setNum_s(body, 0, 4, u32_DefaultArfcnNum);
        int f = 4;
        for (int i = 0; i < u32_DefaultArfcnNum; i++) {
            ByteUtil.setNum_s(body, f, 4, arfcnValues.get(i));
            f += 4;
        }
        return body;
    }
}
