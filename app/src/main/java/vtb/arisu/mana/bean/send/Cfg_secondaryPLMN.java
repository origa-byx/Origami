package vtb.arisu.mana.bean.send;

import vtb.arisu.mana.annotation.Info;
import vtb.arisu.mana.annotation.Nya;
import vtb.arisu.mana.annotation.RspType;
import vtb.arisu.mana.annotation.VtbBean;
import vtb.arisu.mana.bean.OFLType;
import vtb.mashiro.kanon.base.Bean;
import vtb.mashiro.kanon.util.ByteUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @by: origami
 * @date: {2022/5/24}
 * @info: 辅PLMN列表配置
 * 是否立即生效：否
 * 重启是否保留配置：是
 * 应答消息（eNB ->LMT）：
 * O_FL_ENB_TO_LMT_SECONDARY_PLMNS_SET_ACK (0xF061)
 *
 * 此接口用于配置基站广播SIB1 中PLMN LIST字段中的非主PLMN
 *
 * 版本默认发布BAND与其PIN脚的关系如下：
 *
 * V2单板默认关系：
 * Band	            PIN1	PIN2	说明
 * B8、B39	        0	    0	    十进制：0
 * B7、B38、B41	    0	    1	    十进制：1
 * B3、B9	        1	    0	    十进制：2
 * B1、B40	        1	    1	    十进制：3
 *
 * V3单板默认关系：
 * 	        PIN1	PIN2	PIN3	PIN4	说明
 * Band	    0	    0	    0	    0	    全0的输出代表小区处于未激活状态，此时可以关闭功放
 * 38,41	0	    0	    0	    1	    十进制：1
 * 39	    0	    0	    1	    0	    十进制：2
 * 40	    0	    0	    1	    1	    十进制：3
 * 8	    1	    0	    0	    0	    十进制：8
 * 1	    1	    0	    0	    1	    十进制：9
 * 3,9	    1	    0	    1	    1	    十进制：11
 * 20	    1	    1	    0	    0	    十进制：12
 * 5	    1	    1	    0	    1	    十进制：13
 * 7	    1	    1	    1	    1	    十进制：15
 *
 * V5-C单板默认关系：
 * 	       IO1 IO2 IO3 IO5	说明
 * Band	    0	0	0	0	全0的输出代表小区处于未激活状态，此时可以关闭功放
 * 38,41	0	0	0	1	十进制：1
 * 39	    0	0	1	0	十进制：2
 * 40	    0	0	1	1	十进制：3
 * 3	    0	1	0	0	十进制：4
 * 1, 9	    0	1	0	1	十进制：5
 * 7	    0	1	1	0	十进制：6
 * 5	    0	1	1	1	十进制：7
 * 8	    1	0	0	0	十进制：8
 *
 * 硬件管脚示意图：4.7.22
 **/
@Info("辅PLMN列表配置")
@VtbBean(OFLType.O_FL_LMT_TO_ENB_SECONDARY_PLMNS_SET)
@RspType(OFLType.O_FL_ENB_TO_LMT_SECONDARY_PLMNS_SET_ACK)
public class Cfg_secondaryPLMN extends Bean {

    /**
     * 1~5个
     * 辅PLMN列表
     * 字符数组，以结束符结束eg:
     * “46000”
     * “46001”
     */
    public List<String> u8$5$7_u8SecPLMNList;


    @Override
    public void fromBytes(byte[] body) {
        u8$5$7_u8SecPLMNList = new ArrayList<>();
        int size = ByteUtil.toInt_s(body, 0, 1);
        int f = 1;
        for (int i = 0; i < size; i++) {
            u8$5$7_u8SecPLMNList.add(ByteUtil.toString(body, f, 7));
            f += 7;
        }
    }

    @Override
    public byte[] toBytes() {
        int size = Math.min(5, u8$5$7_u8SecPLMNList.size());
        byte[] body = new byte[size * 7 + 1];
        ByteUtil.setNum_s(body, 0, 1, size);
        int f = 1;
        for (int i = 0; i < size; i++) {
            ByteUtil.setString(body, f, 7, u8$5$7_u8SecPLMNList.get(i));
            f += 7;
        }
        return body;
    }
}
