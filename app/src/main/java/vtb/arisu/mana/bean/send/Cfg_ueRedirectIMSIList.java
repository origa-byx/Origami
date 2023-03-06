package vtb.arisu.mana.bean.send;

import vtb.arisu.mana.annotation.Info;
import vtb.arisu.mana.annotation.Nya;
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
 * @date: {2022/5/25}
 * @info: UE重定向模式动态黑名单配置
 * 是否立即生效：是
 * 重启是否保留配置：否
 * 应答消息（eNB ->LMT）：
 * O_FL_ENB_TO_LMT_UE_REDIRECT_IMSI_LIST_CFG_ACK (0xF08F)
 *
 * 本接口用于重定向模式下，配置IMSI黑名单，单次配置最多20，累次配置总数达到1000，删除时间节点配置靠前的IMSI。
 * ClearImsiListFlag取1并且AddImsiNum非零同时配置，先清空列表再添加
 **/
@Info("UE重定向模式动态黑名单配置")
@VtbBean(OFLType.O_FL_LMT_TO_ENB_UE_REDIRECT_IMSI_LIST_CFG)
@RspType(OFLType.O_FL_ENB_TO_LMT_UE_REDIRECT_IMSI_LIST_CFG_ACK)
public class Cfg_ueRedirectIMSIList extends Bean {

    /**
     * 0:保留当前所有配置
     * 1:清空所有IMSI配置列表
     */
    public int u8_ClearImsiListFlag;

    /**
     * 增加的IMSI
     * 字符串数组，如：
     * "460011111111111"
     * 非有效UE ID位注意置为'\0'。
     * (该字段为固定长度)
     */
    public List<String> u8$IMSI$var_ImsiStr;

    /**
     * 预留
     */
    public int u8$2_Res;


    @Override
    public void fromBytes(byte[] body) {
        u8$IMSI$var_ImsiStr = new ArrayList<>();
        u8_ClearImsiListFlag = ByteUtil.toInt_s(body, 0, 1);
        int u8_AddImsiNum = ByteUtil.toInt_s(body, 1, 1);
        int f = 2;
        for (int i = 0; i < u8_AddImsiNum; i++) {
            u8$IMSI$var_ImsiStr.add(ByteUtil.toString(body, f, Define.C_MAX_IMSI_LEN));
            f += Define.C_MAX_IMSI_LEN;
        }
        u8$2_Res = ByteUtil.toInt_s(body, f, 2);
    }

    @Override
    public byte[] toBytes() {
        if(u8$IMSI$var_ImsiStr == null)
            throw new IllegalArgumentException("u8$IMSI$var_ImsiStr is null");
        int u8_AddImsiNum = Math.min(Define.C_MAX_UE_REDIRECT_IMSI_ADD_NUM, u8$IMSI$var_ImsiStr.size());
        byte[] body = new byte[4 + u8_AddImsiNum * Define.C_MAX_IMEI_LEN];
        ByteUtil.setNum_s(body, 0, 1, u8_ClearImsiListFlag);
        ByteUtil.setNum_s(body, 1, 1, u8_AddImsiNum);
        int f = 2;
        for (int i = 0; i < u8_AddImsiNum; i++) {
            ByteUtil.setString(body, f, Define.C_MAX_IMSI_LEN, u8$IMSI$var_ImsiStr.get(i));
            f += Define.C_MAX_IMSI_LEN;
        }
        ByteUtil.setNum_s(body, f, 2, u8$2_Res);
        return body;
    }

}
