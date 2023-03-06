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
 * @info: 扫频频点查询-ACK
 * @see vtb.arisu.mana.bean.send.Query_scanAfrcn
 **/
@Info("扫频频点查询-ACK")
@VtbBean(OFLType.O_FL_LMT_TO_ENB_REM_CFG_QUERY_ACK)
public class Ack_QueryScanAfrcn extends Bean {

    /**
     * 0：不开全频段扫频；
     * 1：开启全频段扫频
     */
    public int u32_wholeBandRem;

    /**
     * 频点号数组
     */
    public final List<Integer> sysEarfcns = new ArrayList<>();

    /**
     * 预留的4个字节，不需要解析
     */
    public int u32_Res;

    @Override
    public void fromBytes(byte[] body) {
        u32_wholeBandRem = ByteUtil.toInt_s(body, 0, 4);
        int num = ByteUtil.toInt_s(body, 4, 4);
        int f = 8;
        for (int i = 0; i < num; i++) {
            sysEarfcns.add(ByteUtil.toInt_s(body, f, 4));
            f += 4;
        }
        u32_Res = ByteUtil.toInt_s(body, f, 4);
    }

    @Override
    public int getSize() {
        int num = Math.min(Define.C_MAX_REM_ARFCN_NUM, sysEarfcns.size());
        return 12 + num * 4;
    }

    @Override
    public byte[] toBytes() {
        byte[] body = new byte[getSize()];
        int num = Math.min(Define.C_MAX_REM_ARFCN_NUM, sysEarfcns.size());
        ByteUtil.setNum_s(body, 0, 4, u32_wholeBandRem);
        ByteUtil.setNum_s(body, 4, 4, num);
        int f = 8;
        for (int i = 0; i < num; i++) {
            ByteUtil.setNum_s(body, f, 4, sysEarfcns.get(i));
            f += 4;
        }
        ByteUtil.setNum_s(body, f, 4, u32_Res);
        return body;
    }

}
