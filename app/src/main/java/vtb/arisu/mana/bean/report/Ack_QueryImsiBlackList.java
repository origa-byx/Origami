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
 * @info: IMSI黑白名单查询-ACK
 * @see vtb.arisu.mana.bean.send.Query_imsiBlackList
 **/
@Info("IMSI黑白名单查询-ACK")
@VtbBean(OFLType.O_FL_ENB_TO_LMT_CONTROL_LIST)
public class Ack_QueryImsiBlackList extends Bean {

    /**
     * 名单类型
     * 0:黑名单
     * 1:白名单
     */
    public int u8_ControlListProperty;

    /**
     * IMSI字符串，如：
     * "460011111111111"
     * 非有效UE ID为'\0'。
     * (该字段为固定长度)
     */
    public List<String> controlListUEId = new ArrayList<>();

    @Override
    public void fromBytes(byte[] body) {
        u8_ControlListProperty = ByteUtil.toInt_s(body, 0, 1);
        int mun = ByteUtil.toInt_s(body, 1, 1);
        int f = 2;
        for (int i = 0; i < mun; i++) {
            controlListUEId.add(ByteUtil.toString(body, f, Define.C_MAX_IMSI_LEN));
            f += Define.C_MAX_IMSI_LEN;
        }
    }

    @Override
    public int getSize() {
        int mun = Math.min(Define.C_MAX_CONTROL_LIST_UE_NUM, controlListUEId.size());
        return 2 + Define.C_MAX_IMSI_LEN * mun;
    }

    @Override
    public byte[] toBytes() {
        byte[] body = new byte[getSize()];
        int mun = Math.min(Define.C_MAX_CONTROL_LIST_UE_NUM, controlListUEId.size());
        ByteUtil.setNum_s(body, 0, 1, u8_ControlListProperty);
        ByteUtil.setNum_s(body, 1, 1, mun);
        int f = 2;
        for (int i = 0; i < mun; i++) {
            ByteUtil.setString(body, f, Define.C_MAX_IMSI_LEN, controlListUEId.get(i));
            f += Define.C_MAX_IMSI_LEN;
        }
        return body;
    }
}
