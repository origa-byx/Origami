package vtb.arisu.mana.bean.report;

import vtb.arisu.mana.annotation.Info;
import vtb.arisu.mana.annotation.Nya;
import vtb.arisu.mana.annotation.VtbBean;
import vtb.arisu.mana.bean.OFLType;
import vtb.mashiro.kanon.base.Bean;

/**
 * @by: origami
 * @date: {2022/5/25}
 * @info: 基站版本升级配置 - ACK
 * @see vtb.arisu.mana.bean.send.Cfg_VerLevelUp
 **/
@Nya
@Info("基站版本升级配置-ACK")
@VtbBean(OFLType.O_FL_ENB_TO_LMT_UPDATE_SOFT_VERSION_CFG_ACK)
public class Ack_VerLevelUp extends Bean {

    /**
     * 指示升级的版本类型
     * 0: 基站软件版本
     * 1: uboot
     */
    @Nya(l = 1)
    public int u8_VerType;

    /**
     * 指示执行状态(UpgradeStatusId)
     * 0 ~ 255
     * 0	参数校验错误
     * 1	升级冲突，有升级执行中
     * ……	预留
     * 21	开始传输升级文件
     * 22	传输成功
     * 23	传输失败
     * 24	校验md5成功
     * 25	校验md5失败
     * 26	开始执行升级
     * 27	升级成功
     * 28	升级失败
     * 29	内部错误，升级失败
     * 30	升级成功，开始重启基站
     */
    @Nya(f = 1, l = 1)
    public int u8_StatusInd;

    /**
     * 预留
     */
    @Nya(f = 2, l = 2)
    public int u8$2_Reserved;
}
