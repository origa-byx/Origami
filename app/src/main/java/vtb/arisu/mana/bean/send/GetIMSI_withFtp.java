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
 * @info: 上传IMSI文件配置
 * 是否立即生效：是
 * 重启是否保留配置：是
 * 应答消息（eNB ->LMT）：
 * O_FL_ENB_TO_LMT_UPLOAD_IMSI_FILE_CFG_ACK (0xF078)
 *
 * 此接口用于开启基站支持以IMSI文件上传的方式上报到客户端，使用FTP方式
 **/
@Nya(l = 140)
@Info("上传IMSI文件配置")
@VtbBean(OFLType.O_FL_LMT_TO_ENB_UPLOAD_IMSI_FILE_CFG)
@RspType(OFLType.O_FL_ENB_TO_LMT_UPLOAD_IMSI_FILE_CFG_ACK)
public class GetIMSI_withFtp extends Bean {

    /**
     * 上传类型
     * 0：实时逐条上报IMSI ，不考虑IMSI是否传输成功，版本默认是此种方式
     * 1：实时逐条上报IMSI ，适用于TCP模式，传输失败的IMSI文件保存到本地，FTP上传，FTP配置如下。
     * 2: IMSI都以文件形式存储，FTP上传，FTP配置如下。
     */
    @Nya(l = 1)
    public int u8_UploadImsiType;

    /**
     * FTP配置仅在UploadImsiType为1和2的时候有效，如果FTP已经配置过，
     * 基站会保存配置，不需要重复配置。
     * 0：不配置
     * 1：配置
     */
    @Nya(f = 1, l = 1)
    public int u8_isCfgFtpServer;

    /**
     * 预留
     */
    @Nya(f = 2, l = 2)
    public int u8$2_Res;

    /**
     * 上报文件时间间隔，单位：分钟
     * 1..0xFFFFFFFF
     */
    @Nya(f = 4)
    public int u32_reportInterval;

    /**
     * FTP服务器IP, eg: “192.168.2.11”，
     */
    @Nya(f = 8, l = 16)
    public String u8$16_UploadFileFtpServerIp;

    /**
     * FTP服务器端口号，eg: 21
     */
    @Nya(f = 24)
    public int u32_FtpServerPort;

    /**
     * Ftp用户名，eg:“kkk”
     */
    @Nya(f = 28, l = 20)
    public String u8$20_FtpLoginNam;

    /**
     * Ftp登录密码, eg: “123456”
     */
    @Nya(f = 48, l = 10)
    public String u8$10_FtpPassword;

    /**
     * 上传文件放置目录，字符串，以’\0’结尾。
     * 例如：
     * 欲放置文件于FTP服务器根目录下的filePath文件夹，完整的路径为：“/filePath/”
     */
    @Nya(f = 58, l = 66)
    public String u8$66_FtpServerFilePath;

    /**
     * 文件名中IP字段，字符串，以’\0’结尾
     */
    @Nya(f = 124, l = 16)
    public String u8$16_FileNameIpDomain;

}
