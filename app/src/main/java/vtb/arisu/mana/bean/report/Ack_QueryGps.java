package vtb.arisu.mana.bean.report;

import vtb.arisu.mana.annotation.Info;
import vtb.arisu.mana.annotation.Nya;
import vtb.arisu.mana.annotation.VtbBean;
import vtb.arisu.mana.bean.OFLType;
import vtb.mashiro.kanon.base.Bean;

/**
 * @by: origami
 * @date: {2022/5/31}
 * @info: GPS经纬高度查询-ACK
 * @see vtb.arisu.mana.bean.send.Query_gps
 **/
@Nya(l = 36)
@Info("GPS经纬高度查询-ACK")
@VtbBean(OFLType.O_FL_ENB_TO_LMT_GPS_LOCATION_QUERY_ACK)
public class Ack_QueryGps extends Bean {

    /**
     * 无效
     */
    @Nya
    public int u32_Paraoff1;

    /**
     * 经度（正值为东经，负值为西经）
     */
    @Nya(f = 4, l = 8)
    public double f64_Longitude;

    /**
     * 维度（正值为北纬，负值为南纬）
     */
    @Nya(f = 12, l = 8)
    public double f64_Latitude;

    /**
     * 高度
     */
    @Nya(f = 20, l = 8)
    public double f64_Altitude;

    /**
     * GPS经纬高度获取进度,百分比的值，例如：
     * 50对应50%
     */
    @Nya(f = 28)
    public int u32_RateOfPro;

    /**
     * 无效
     */
    @Nya(f = 32)
    public int u32_Paraoff2;

}
