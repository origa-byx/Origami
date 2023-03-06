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
 * @date: {2022/5/24}
 * @info:
 * @see vtb.arisu.mana.bean.send.Cfg_WBList
 **/
@Info("IMSI黑白名单配置-ACK")
@VtbBean(OFLType.O_FL_ENB_TO_LMT_CONTROL_UE_LIST_CFG_ACK)
public class Ack_WBlist extends Bean {

    /**
     * 配置结果
     * 4:管控名单清除成功；
     * 3：管控名单配置中含有无效值；
     * 2：存在用户未添加成功；
     * 1：存在用户未删除成功；
     * 0：配置成功
     */
    public int u8_CfgResult;

    /**
     * 未操作成功UE Imsi集合
     * UE IMSI数组
     * 字符串格式，以'\0'结束，如：
     * "460011111111111"
     */
    public List<String> u8$10$var_IgnoreUEList;

    @Override
    public void fromBytes(byte[] body) {
        u8$10$var_IgnoreUEList = new ArrayList<>();
        u8_CfgResult = ByteUtil.toInt_s(body, 0, 1);
        int size = ByteUtil.toInt_s(body, 1, 1);
        int f = 2;
        for (int i = 0; i < size; i++) {
            String ue_imsi = ByteUtil.toString(body, f, Define.C_MAX_IMEI_LEN);
            u8$10$var_IgnoreUEList.add(ue_imsi);
            f += Define.C_MAX_IMEI_LEN;
        }
    }

    @Override
    public byte[] toBytes() {
        if(u8$10$var_IgnoreUEList == null)
            throw new IllegalArgumentException("u8$10$var_IgnoreUEList is null");
        int size = Math.min(10, u8$10$var_IgnoreUEList.size());
        byte[] body = new byte[Define.C_MAX_IMEI_LEN * size + 2];
        ByteUtil.setNum_s(body, 0, 1, u8_CfgResult);
        ByteUtil.setNum_s(body, 1, 1, size);
        int f = 2;
        for (int i = 0; i < size; i++) {
            ByteUtil.setString(body, f, Define.C_MAX_IMEI_LEN, u8$10$var_IgnoreUEList.get(i));
            f += Define.C_MAX_IMEI_LEN;
        }
        return body;
    }
}
