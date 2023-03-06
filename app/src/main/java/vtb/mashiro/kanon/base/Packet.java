package vtb.mashiro.kanon.base;

import vtb.arisu.mana.annotation.VtbBean;
import vtb.mashiro.kanon.util.ByteUtil;

import java.util.Arrays;
import java.util.Random;

/**
 * @by: origami
 * @date: {2022/5/12}
 * @info: 32
 **/
public class Packet {

    /**
     * 消息帧头
     */
    public int u32FrameHeader = 0x5555AAAA;

    /**
     * 消息ID
     */
    public int u16MessageId;

    /**
     * 长度，包括消息头
     */
    public int u16MsgLength;

    /**
     * 0xFF00:FDD
     * 0x00FF:TDD
     */
    public int u16Frame;

    /**
     * 16
     * u16SubSysCode
     * （1） 最高1bit用于指示基站发送给客户端的数据是否传输完成 0：传输完成；1代表传输未完成。
     * （2）低15bit：消息传输的TransId，0是无效值，1~0x8FFFF。
     * 这里直接将在TCP流处进行相应处理，所以此参数使用时直接当成 TransId 使用
     */
    public int u16SubSysCode;

    /**
     * 20
     * 基站单板的SN号，每块基站单板都不同。客户端发送给基站的消息可以不填写此信息，基站不做检查。
     */
    public String sn_20u8;

    public byte[] body;


    public long socketThreadId = 0;

    public Packet() { }

    public Packet(Bean bean) {
        this.u16MessageId = bean.getClass().getAnnotation(VtbBean.class).value();
        this.u16SubSysCode = new Random().nextInt(0x3fff);
        this.body = bean.toBytes();
        this.u16MsgLength = 32 + body.length;
    }

    public byte[] toBytes(){
        byte[] content = new byte[32 + body.length];
        ByteUtil.setNum_s(content, 0, 4, u32FrameHeader);
        ByteUtil.setNum_s(content, 4, 2, u16MessageId);
        ByteUtil.setNum_s(content, 6, 2, body.length);
        ByteUtil.setNum_s(content, 8, 2, u16Frame);
        ByteUtil.setNum_s(content, 10, 2, u16SubSysCode & 0x7fff);
        ByteUtil.setByteArray(content, 12, 20, ByteUtil.getBytes(sn_20u8));
        ByteUtil.setByteArray(content, 32, body.length, body);
        return content;
    }

    @Override
    public String toString() {
        return "Packet{" +
                "u32FrameHeader=" + u32FrameHeader +
                ", u16MessageId=" + u16MessageId +
                ", u16MsgLength=" + u16MsgLength +
                ", u16Frame=" + u16Frame +
                ", u16SubSysCode=" + u16SubSysCode +
                ", sn_20u8='" + sn_20u8 + '\'' +
                ", body=" + Arrays.toString(body) +
                '}';
    }

}
