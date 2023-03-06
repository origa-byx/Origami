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
 * @date: {2022/5/26}
 * @info: 定位模式黑名单配置（LMT -> eNB）
 * 是否立即生效：是
 * 重启是否保留配置：否
 * 应答消息（eNB ->LMT）：
 * O_FL_ ENB_TO_LMT_LOCATION_UE_BLACKLIST_CFG_ACK(0xF054)
 * 该接口用于配置定位模式下的黑名单IMSI列表，该列表中的IMSI，不会被踢回公网，用于实现轮流定位功能
 **/
@Info("定位模式黑名单配置")
@VtbBean(OFLType.O_FL_LMT_TO_ENB_LOCATION_UE_BLACKLIST_CFG)
@RspType(OFLType.O_FL_ENB_TO_LMT_LOCATION_UE_BLACKLIST_CFG_ACK)
public class Cfg_locBlackList extends Bean {

    /**
     * 黑名单UE
     * IMSI数组
     * 字符串，以结束符结束，(该字段为固定长度)
     */
    public List<String> u8$17$maxLoc_BlackUEIdentity;

    /**
     * 操作标志
     * 0:保留当前所有配置
     * 1:清空之前配置的IMSI配置列表
     */
    public int u8_clearListFlag;

    /**
     * 预留
     */
    public int u8_Res;

    @Override
    public void fromBytes(byte[] body) {
        u8$17$maxLoc_BlackUEIdentity = new ArrayList<>();
        int u16_BlacklUENum = ByteUtil.toInt_s(body, 0, 2);
        int f = 2;
        for (int i = 0; i < u16_BlacklUENum; i++) {
            u8$17$maxLoc_BlackUEIdentity.add(ByteUtil.toString(body, f, Define.C_MAX_IMSI_LEN));
            f += Define.C_MAX_IMSI_LEN;
        }
        u8_clearListFlag = ByteUtil.toInt_s(body, f, 1);
        u8_Res = ByteUtil.toInt_s(body, f + 1, 1);
    }

    @Override
    public byte[] toBytes() {
        int u16_BlacklUENum;
        if(u8$17$maxLoc_BlackUEIdentity == null)
            u16_BlacklUENum = 0;
        else
            u16_BlacklUENum = Math.min(Define.C_MAX_LOCATION_BLACKLIST_UE_CFG_NUM, u8$17$maxLoc_BlackUEIdentity.size());
        byte[] body = new byte[u16_BlacklUENum * Define.C_MAX_IMSI_LEN + 4];
        ByteUtil.setNum_s(body, 0, 2, u16_BlacklUENum);
        int f = 2;
        for (int i = 0; i < u16_BlacklUENum; i++) {
            ByteUtil.setString(body, f, Define.C_MAX_IMSI_LEN, u8$17$maxLoc_BlackUEIdentity.get(i));
            f += Define.C_MAX_IMSI_LEN;
        }
        ByteUtil.setNum_s(body, f, 1, u8_clearListFlag);
        ByteUtil.setNum_s(body, f + 1, 1, u8_Res);
        return body;
    }
}
