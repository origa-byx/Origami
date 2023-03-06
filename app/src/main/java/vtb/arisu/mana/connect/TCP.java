package vtb.arisu.mana.connect;

import vtb.arisu.mana.bean.send.HeartResp;
import vtb.mashiro.kanon.base.Bean;
import vtb.mashiro.kanon.base.Packet;
import vtb.mashiro.kanon.util.ByteUtil;
import vtb.mashiro.kanon.util.Funichi;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import static vtb.mashiro.kanon.util.Log.Lazy.log;

import android.util.Log;

import com.origami.utils.Ori;

/**
 * @by: origami
 * @date: {2022/5/12}
 * @info:
 * 由于基站能力限制，以及需要兼容串口这样传输速率较低的传输模式，
 * 如果基站发送消息的长度大于512字节（包括消息头大小），基站则执行分段发送数据给客户端
 *
 * 消息传输的TransId主要用于客户端发送重复消息类型给基站时，基站回复信息的匹配检查，
 * 对于客户端发送的消息，基站未做检查。TransId值的管理原则是：
 * 基站和客户端各自维护一个TransId值，对于请求类消息，回复的ACK需要填写请求中携带的TRANSID,
 * 同一个消息如果分段发送，TRANSID不变
 **/
public class TCP {

    public static final String TAG = "TCP";

    private ServerSocket serverSocket;
    private final Set<Connect> connects = new HashSet<>();

    private final Funichi<Bean> beanPost;

    public TCP(Funichi<Bean> beanPost) {
        this.beanPost = beanPost;
    }

    public void initServer(int port){
        if(serverSocket != null && !serverSocket.isClosed())
            throw new RuntimeException("only one Server can be created, or close the old one and then try to init");
        MANAListener.Lazy.mana.start();
        new Thread(()->{
            try {
                serverSocket = new ServerSocket(port);
            } catch (IOException e) {
                e.printStackTrace();
                log.pr(TAG, e.getMessage());
            }
            log.pr(TAG, "TCP Server init ok");
            while (serverSocket != null && !serverSocket.isClosed()){
                try {
                    Socket accept = serverSocket.accept();
                    Connect connect = new Connect(accept);
                    connects.add(connect);
                    connect.runLoop();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void close(){
        Iterator<Connect> iterator = connects.iterator();
        while (iterator.hasNext()){
            Connect next = iterator.next();
            next.stopAndClose();
            iterator.remove();
        }
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        MANAListener.Lazy.mana.exit();
        serverSocket = null;
    }

    private final class Connect implements Runnable {

        private long socketThreadId = 0;
        Socket socket;
        InputStream data_in;
        OutputStream data_out;

        PacketHandler packetHandler;

        boolean connect = true;
        int nullPacket_num = 0;//收到空包计数
        byte[] buffer = new byte[1024];//28个字节的头部
        int offset = 0;//写入时的偏移，处理tcp粘包

        Packet cachePacket;

        Connect(Socket socket) throws IOException {
            this.socket = socket;
            data_in = socket.getInputStream();
            data_out = socket.getOutputStream();
            log.pr("TCP", "连接已建立" + socket.getInetAddress().getHostAddress() + " : " + socket.getPort());
        }

        void runLoop(){
            packetHandler = new PacketHandler(beanPost);
            Thread thread = new Thread(this);
            socketThreadId = thread.getId();
            thread.start();
        }

        @Override
        public void run() {
            //监听TCP
            try {
                while (socket.isConnected() && connect && data_in != null) {
                    moreBytesIfNeed();
                    int read = data_in.read(buffer, offset, buffer.length - offset);
                    if (read == -1) {//空包     连续三次以上空包则认为socket已经关闭
                        if (nullPacket_num >= 3) {
                            break;
                        }
                        nullPacket_num++;
                        continue;
                    }
                    nullPacket_num = 0;
//                    Log.e("ORI-BUFFER", Ori.ByteArrayToHexString(buffer));
                    fillPacketAndPost(offset + read);
                }
            } catch (IOException e) {
                log.pr(TAG, e);
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    log.pr(TAG, e);
                } finally {
                    log.pr(TAG, "TCP socket 已关闭");
                    connects.remove(Connect.this);
                    if (packetHandler != null)
                        packetHandler.close();
                }
            }
        }

        public void stopAndClose(){
            log.pr(TAG, "stop TCP");
            connect = false;
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(packetHandler != null)
                packetHandler.close();
        }

        /**
         * 拆包报文
         * 非主控板方案消息头格式
         * @param length 缓冲区有效报文长度
         */
        private void fillPacketAndPost(int length) {
            offset = 0;
            //报文缓冲区数据不足头部28字节，偏移length读取
            if (length < 32) {
                Log.e("TAG", "报文缓冲区数据不足头部32字节，偏移length读取");
                offset = length;
                return;
            }
            //u16MsgLength是该消息的全部数据长度(包含消息头本身)，数据发送按小端模式
            int msgLength = ByteUtil.toInt_s(buffer, 6, 2);
            //报文缓冲区数据不足此单个数据包长度msgLength，偏移length读取
            if (length < msgLength) {
                Log.e("TAG", "报文缓冲区数据不足此单个数据包长度msgLength，偏移length读取-》 " + msgLength);
                offset = length;
                return;
            }
            Packet bean = new Packet();
            bean.u16MsgLength = msgLength;
            bean.socketThreadId = socketThreadId;
            bean.u32FrameHeader = ByteUtil.toInt_s(buffer, 0, 4);
            bean.u16MessageId = ByteUtil.toInt_s(buffer, 4, 2);
            bean.u16Frame = ByteUtil.toInt_s(buffer, 8, 2);
            bean.u16SubSysCode = ByteUtil.toInt_s(buffer, 10, 2);
            bean.sn_20u8 = ByteUtil.toString(buffer, 12, 20);
            bean.body = new byte[msgLength];
            if(msgLength != 0)
                System.arraycopy(buffer, 32, bean.body, 0, Math.min(msgLength, buffer.length - 32));
            //非阻塞分发
            checkPacketAndPostIfFinish(bean);
            //报文缓冲区数据存在粘包，将继续拆包
            if (msgLength < length) {
                int read = length - msgLength;
                System.arraycopy(buffer, msgLength, buffer, 0, read);
                fillPacketAndPost(read);
            }
        }

        /**
         * 是否需要扩容缓冲区，如需要则二倍扩容
         */
        private void moreBytesIfNeed() {
            if(buffer.length < offset + 28){
                byte[] newBuffer = new byte[buffer.length * 2];
                ByteUtil.setByteArray(newBuffer, 0, buffer.length, buffer);
                buffer = newBuffer;
                log.pr("TCP", "缓冲区产生扩容，当前大小： " + buffer.length  + "字节");
                moreBytesIfNeed();
            }
        }

        /**
         * 校验 u16SubSysCode
         * 最高1bit用于指示基站发送给客户端的数据是否传输完成，
         * 0：传输完成；1代表传输未完成。
         * @param bean bean
         */
        private void checkPacketAndPostIfFinish(Packet bean) {
            int flag = (bean.u16SubSysCode & 0x8000) >>> 15;
            if(cachePacket != null){
                byte[] body = new byte[cachePacket.body.length + bean.body.length];
                ByteUtil.setByteArray(body, 0, cachePacket.body.length, cachePacket.body);
                ByteUtil.setByteArray(body, cachePacket.body.length, bean.body.length, bean.body);
                cachePacket.u16MsgLength += bean.body.length;
                cachePacket.body = body;
                if(flag == 0){
                    Packet realPacket = cachePacket;
                    cachePacket = null;
                    //心跳
                    if(realPacket.u16MessageId == 0xF010){
                        Packet heartResp = new Packet(new HeartResp());
                        heartResp.u16Frame = bean.u16Frame;
                        sendPacket(heartResp);
                    }
                    packetHandler.post(realPacket);
                }
            }else if(flag == 0)
                packetHandler.post(bean);
            else
                cachePacket = bean;
        }

        /**
         * 发送一段数据报文
         *
         * @param bean -> 报文
         * @return true -> 发送成功  flase -> 发送失败
         */
        private boolean sendPacket(Packet bean) {
            if (socket.isConnected() && data_out != null) {
                try {
                    data_out.write(bean.toBytes());
                    data_out.flush();
                    if(bean.u16MessageId != 0xF011)
                        log.pr(TAG, String.format("TCP 指令下发成功<%s>:%s",
                                socket.getInetAddress().getHostAddress(),
                                bean.toString()));
                    return true;
                } catch (IOException e) {
                    log.pr(TAG, String.format("TCP 指令下发失败: %s", e.getMessage()));
                }
            } else {
                log.pr(TAG, String.format("TCP 错误的对一个已关闭的socket 发送报文:%s", bean.toString()));
            }
            return false;
        }

    }

}
