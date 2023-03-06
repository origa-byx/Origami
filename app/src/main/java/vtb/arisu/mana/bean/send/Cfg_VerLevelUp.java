package vtb.arisu.mana.bean.send;

import vtb.arisu.mana.annotation.Info;
import vtb.arisu.mana.annotation.Nya;
import vtb.arisu.mana.annotation.RspType;
import vtb.arisu.mana.annotation.VtbBean;
import vtb.arisu.mana.bean.OFLType;
import vtb.mashiro.kanon.base.Bean;

/**
 * @by: origami
 * @date: {2022/5/25}
 * @info: 基站版本升级配置
 * 是否立即生效：是
 * 重启是否保留配置：否
 * 应答消息（eNB ->LMT）：
 * O_FL_ENB_TO_LMT_UPDATE_SOFT_VERSION_CFG_ACK (0x070)
 *
 * 此接口用于客户端配置升级基站版本使用，需要客户端建立FTP服务器
 *
 * rsp:
 * @see vtb.arisu.mana.bean.report.Ack_VerLevelUp
 **/
@Nya(l = 336)
@Info("基站版本升级配置")
@VtbBean(OFLType.O_FL_LMT_TO_ENB_UPDATE_SOFT_VERSION_CFG)
@RspType(OFLType.O_FL_ENB_TO_LMT_UPDATE_SOFT_VERSION_CFG_ACK)
public class Cfg_VerLevelUp extends Bean {

    /**
     * 升级版本类型
     * 0：基站软件版本;
     * 1：uboot版本;
     * 2: 同时升级基站软件版本和uboot版本
     */
    @Nya(l = 1)
    public int u8_updateType;

    /**
     * 字符串，基站软件版本名字：如
     * “BaiStation128D_ FDD_R002C0000G01B005.IMG”；
     */
    @Nya(f = 1, l = 102)
    public String u8$102_enbSoftFileName;

    /**
     * 是否保留配置（仅对基站软件系统有效）
     * 0：不保留
     * 1：保留
     */
    @Nya(f = 103, l = 1)
    public int u8_isReservedCfg;

    /**
     * 针对升级基站软件计算的md5值,32字节长度。
     */
    @Nya(f = 104, l = 36)
    public String u8$36_enbSoftMD5;

    /**
     * 该字段为boot文件名，如：“u-boot-t2200-nand-1.0.15.img”
     */
    @Nya(f = 140, l = 40)
    public String u8$40_uBootFileName;

    /**
     * 针对升级文件计算的md5值,32字节长度。
     */
    @Nya(f = 180, l = 36)
    public String ubootMD5;

    /**
     * 是否重新配置FTP服务器地址
     * 0：不配置
     * 1：配置
     */
    @Nya(f = 216, l = 1)
    public int u8_isCfgFtpServer;

    /**
     * FTP服务器IP, eg: “192.168.2.11”，
     */
    @Nya(f = 217, l = 16)
    public String u8$16_FtpServerIp;

    /**
     * 保留字节
     */
    @Nya(f = 233, l = 3)
    public int u8$3_Reserved;

    /**
     * FTP服务器端口号，eg: 21
     */
    @Nya(f = 236)
    public int u32_FtpServerPort;

    /**
     * Ftp用户名，eg:“kkk ”
     */
    @Nya(f = 240, l = 20)
    public String u8$20_FtpLoginNam;

    /**
     * Ftp登录密码, eg: “123456”
     */
    @Nya(f = 260, l = 10)
    public String u8$10_FtpPassword;

    /**
     * 待升级文件所在FTP服务器路径，默认根目录。路径以/结尾。
     * eg：待升级文件位于FTP服务器根目录下的filePath文件夹，完整的路径为：“/filePath/”
     */
    @Nya(f = 270, l = 66)
    public String u8$66_FtpServerFilePath;

}
