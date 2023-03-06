package vtb.arisu.mana.bean.send;

import vtb.arisu.mana.annotation.Info;
import vtb.arisu.mana.annotation.RspType;
import vtb.arisu.mana.annotation.VtbBean;
import vtb.arisu.mana.bean.Define;
import vtb.arisu.mana.bean.OFLType;
import vtb.mashiro.kanon.base.Bean;
import vtb.mashiro.kanon.util.ByteUtil;

/**
 * @by: origami
 * @date: {2022/5/23}
 * @info: 扫频频点配置
 * 是否立即生效：是
 * 重启是否保留配置：是。
 * 应答消息（eNB ->LMT）：O_FL_ENB_TO_LMT_SYS_ARFCN_ACK (0xF004)
 * TODO 确定不是回复的 O_FL_ENB_TO_LMT_REM_INFO_RPT ??? （文档错误吗 ???）
 * TDD模式：
 * 此接口用于客户端在基站IDLE态时开始SCAN公网LTE小区参数的流程。无需配置Band ID，
 * 基站会根据频点自动计算。基站支持RX和SINNIFER 2个端口扫频，基站默认版本是RX口，
 * 通过O_FL_LMT_TO_ENB_REM_ANT_CFG消息可配置扫频端口。
 * wholeBandRem字段指示是否开启全频段扫频，若开启全频段扫频(wholeBandRem=1)，
 * 板卡在扫完配置的频点后，继续搜索公网小区SIB5中配置的其他邻区频点。
 * 基站单板本身支持的扫频范围是（(50MHZ~4GHz)，
 * 但若整机系统中RX或者SINNIFFER口外接了限制接收频率的硬件（比如滤波器），
 * 则配置频点时应仅限于属于该频段的频点，且配置wholeBandRem为0。
 *
 * FDD模式：
 * 此接口用于客户端在基站IDLE态时开始SCAN公网LTE小区参数的流程。无需配置Band ID，
 * 基站会根据频点自动计算。FDD模式只支持用SNF端口扫频，基站默认版本是SNF口。
 * wholeBandRem字段指示是否开启全频段扫频，若开启全频段扫频(wholeBandRem=1)，
 * 板卡在扫完配置的频点后，继续搜索公网小区SIB5中配置的其他邻区频点。
 * 更新预设扫频频点列表后，单板判断基站当前小区激活状态并保存。如果小区激活，
 * 则先进行去激活小区操作，小区去激活完毕后，进行扫频操作。
 * 如果扫频失败则重新按照之前的小区配置激活小区，如果扫频成功，
 * 则上报扫频结果后，按照之前的小区配置激活小区
 **/
@Info("扫频频点配置")
@VtbBean(OFLType.O_FL_LMT_TO_ENB_REM_CFG)
@RspType(OFLType.O_FL_ENB_TO_LMT_SYS_ARFCN_ACK)
public class Cfg_scanArfcn extends Bean {

    /**
     * 是否开启全频段扫频
     * 0：不开启；
     * 1：开启
     */
    public int u32_wholeBandRem;

    /**
     * 需要修改的扫频频点数目；如果是0，
     * 则不操作基带板预设的扫频频点列表，用预设的频点进行扫频。
     * 0~10
     */
    public int u32_sysEarfcnNum;

    /**
     * 最大配置10个，系统最多支持10个扫频频点
     * 频点数组
     */
    public int[] u32$max10_sysEarfcn;

    /**
     * 修改扫频频点操作
     * 0:增加扫频频点；
     * 1:删除扫频频点
     */
    public int u8_addSwitch;

    /**
     * 保留字节
     */
    public int[] u8$3_Res;

    @Override
    public void fromBytes(byte[] body) {
        u32_wholeBandRem = ByteUtil.toInt_s(body, 0, 4);
        u32_sysEarfcnNum = ByteUtil.toInt_s(body, 4, 4);
        u32$max10_sysEarfcn = new int[u32_sysEarfcnNum];
        int f = 8;
        for (int i = 0; i < u32_sysEarfcnNum; i++) {
            u32$max10_sysEarfcn[i] = ByteUtil.toInt_s(body, f, 4);
            f += 4;
        }
        u8_addSwitch = ByteUtil.toInt_s(body, f, 1);
        f += 1;
        u8$3_Res = new int[3];
        for (int i = 0; i < 3; i++) {
            u8$3_Res[i] = ByteUtil.toInt_s(body, f, 1);
            f += 1;
        }
    }

    @Override
    public byte[] toBytes() {
        u32_sysEarfcnNum = Math.min(Define.C_MAX_REM_ARFCN_NUM, u32$max10_sysEarfcn.length);
        byte[] body = new byte[12 + u32_sysEarfcnNum * 4];
        ByteUtil.setNum_s(body, 0, 4, u32_wholeBandRem);
        ByteUtil.setNum_s(body, 0, 4, u32_sysEarfcnNum);
        int f = 8;
        for (int i = 0; i < u32_sysEarfcnNum; i++) {
            ByteUtil.setNum_s(body, f, 4, u32$max10_sysEarfcn[i]);
            f += 4;
        }
        ByteUtil.setNum_s(body, f, 1, u8_addSwitch);
        f += 1;
        for (int i = 0; i < 3; i++) {
            ByteUtil.setNum_s(body, f, 1, u8$3_Res[i]);
            f += 1;
        }
        return body;
    }

}
