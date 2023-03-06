package vtb.arisu.mana.bean.send;

import vtb.arisu.mana.annotation.Info;
import vtb.arisu.mana.annotation.Nya;
import vtb.arisu.mana.annotation.RspType;
import vtb.arisu.mana.annotation.VtbBean;
import vtb.arisu.mana.bean.OFLType;
import vtb.mashiro.kanon.base.Bean;

/**
 * @by: origami
 * @date: {2022/5/23}
 * @info: GPS同步模式下的pps1s偏移量配置
 * 是否立即生效：否
 * 重启是否保留配置：是
 * 应答消息（eNB ->LMT）：O_FL_ENB_TO_LMT_GPS_PP1S_ACK(0xF02A)
 *
 * 在TDD选择GPS同步模式下，此接口用于设置同步偏移量，在中国，一般band39,band40需要进行GPS同步偏移量调节,一般-700微秒（OffsetTime）左右数据帧头偏移（正值说明时域相对原始值向后移动，负值说明是时域对应原始值向前移动），具体各个BAND的偏移量以实际测量为准。
 * 接口中设置的Gpspps1s = OffsetTime * （Gpspps1sToBW/微秒）
 * OffsetTime：运营商网络此BAND相对于GPS的偏移量，单位微秒；
 * Gpspps1sToBW/微秒：相关带宽下每微秒的偏移值，带宽是指本基带板的带宽；
 * 1微秒的偏移情况下，Gpspps1s与带宽对应关系如下：
 * Bandwidth	       （5M）	（10M）	   （20M）
 * Gpspps1sToBW/微秒	7.68	 15.36	    30.72
 * 例如：基站配置20M带宽，BAND40偏移-700微妙，则接口中配置的Gpspps1s=-700*30.72。
 **/
@Nya
@Info("GPS同步模式下的pps1s偏移量配置")
@VtbBean(OFLType.O_FL_LMT_TO_ENB_GPS_PP1S_CFG)
@RspType(OFLType.O_FL_ENB_TO_LMT_GPS_PP1S_ACK)
public class Cfg_gpsPps1s extends Bean {

    /**
     * Gps pps1s偏移量
     */
    @Nya
    public int s32_Gpspps1s;

}
