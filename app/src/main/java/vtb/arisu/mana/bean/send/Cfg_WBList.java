package vtb.arisu.mana.bean.send;

import vtb.arisu.mana.annotation.Info;
import vtb.arisu.mana.annotation.RspType;
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
 * @info: IMSI黑白名单配置
 * 是否立即生效：是
 * 重启是否保留配置：是
 * 应答消息（eNB ->LMT）：
 * O_FL_ENB_TO_LMT_CONTROL_UE_LIST_CFG_ACK (0xF03A)
 *
 * 此接口用于配置IMSI的黑名单和白名单，
 * 每次最大可以配置10个IMSI，
 * 基站可同时保存维护黑名单和白名单两套名单，
 * 最大支持各100个名单配置。
 * 根据测量UE的配置模式决定采用哪个名单。
 *
 * rsp:
 * @see vtb.arisu.mana.bean.report.Ack_WBlist
 **/
@Info("IMSI黑白名单配置")
@VtbBean(OFLType.O_FL_LMT_TO_ENB_CONTROL_UE_LIST_CFG)
@RspType(OFLType.O_FL_ENB_TO_LMT_CONTROL_UE_LIST_CFG_ACK)
public class Cfg_WBList extends Bean {

    /**
     * 0: 在管控名单中删除用户
     * 1: 在管控名单中添加用户
     */
    public int u8_ControlMovement;

    /**
     * 0：添加/删除黑名单用户
     * 1：添加/删除白名单用户
     */
    public int u8_ControlUEProperty;

    /**
     * UE IMSI数组
     *字符串格式，以'\0'结束，如："460011111111111"
     */
    public List<String> u8$10$var_ControlUEIdentity;

    /**
     * 清除黑白名单配置
     * 0：该字段不起作用
     * 1：删除所有黑名单用户
     * 2: 删除所有白名单用户
     * 3：删除所有黑白名单用户
     */
    public int u8_ClearType;

    /**
     *保留字节
     */
    public int u8$2_Res;

    @Override
    public void fromBytes(byte[] body) {
       u8_ControlMovement = ByteUtil.toInt_s(body, 0, 1);
       int size = ByteUtil.toInt_s(body, 1, 1);
       u8_ControlUEProperty = ByteUtil.toInt_s(body, 2, 1);
       int f = 3;
       u8$10$var_ControlUEIdentity = new ArrayList<>();
       for (int i = 0; i < size; i++) {
           u8$10$var_ControlUEIdentity.add(ByteUtil.toString(body, f, Define.C_MAX_IMEI_LEN));
           f += Define.C_MAX_IMEI_LEN;
       }
       u8_ClearType = ByteUtil.toInt_s(body, f, 1);
       f += 1;
       u8$2_Res = ByteUtil.toInt_s(body, f, 2);
    }

    @Override
    public byte[] toBytes() {
        if(u8$10$var_ControlUEIdentity == null)
            throw new IllegalArgumentException("u8$10$var_ControlUEIdentity is null");
        int size = Math.min(10, u8$10$var_ControlUEIdentity.size());
        byte[] body = new byte[size * Define.C_MAX_IMEI_LEN + 6];
        ByteUtil.setNum_s(body, 0, 1, u8_ControlMovement);
        ByteUtil.setNum_s(body, 1, 1, size);
        ByteUtil.setNum_s(body, 2, 1, u8_ControlUEProperty);
        int f = 3;
        for (int i = 0; i < size; i++) {
            ByteUtil.setString(body, f, Define.C_MAX_IMEI_LEN, u8$10$var_ControlUEIdentity.get(i));
            f += Define.C_MAX_IMEI_LEN;
        }
        ByteUtil.setNum_s(body, f, 1, u8_ClearType);
        ByteUtil.setNum_s(body, f + 1, 2, u8$2_Res);
        return body;
    }
}
