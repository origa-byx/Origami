package vtb.arisu.mana.connect;

import vtb.arisu.mana.annotation.Info;
import vtb.mashiro.kanon.base.Bean;
import vtb.mashiro.kanon.base.Packet;
import vtb.mashiro.kanon.os.Handler;
import vtb.mashiro.kanon.os.Looper;
import vtb.mashiro.kanon.os.Message;
import vtb.mashiro.kanon.util.DefMap;
import vtb.mashiro.kanon.util.Funichi;
import vtb.mashiro.kanon.util.Log;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * @by: origami
 * @date: {2022/5/16}
 * @info:
 **/
public class PacketHandler implements Runnable{

    static final DefMap<Integer, Constructor<? extends Bean>> HandlerMap = new DefMap<>();

    public static void addMap(int vtbBeanVal, Constructor<? extends Bean> constructor){
        HandlerMap.put(vtbBeanVal, constructor);
    }

    private final Funichi<Bean> beanPost;
    private volatile Handler mHandler;

    public PacketHandler(Funichi<Bean> beanPost) {
        this.beanPost = beanPost;
        new Thread(this).start();
    }

    synchronized void post(Packet packet){
        while (mHandler == null) {
            try {
                TimeUnit.MILLISECONDS.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Message msg = new Message();
        msg.obj = packet;
        mHandler.post(msg);
    }

    void close(){
        if(mHandler != null)
            mHandler.quit();
    }

    @Override
    public void run() {
        Looper.prepare();
        mHandler = new Handler(Looper.myLooper()) {
            @Override
            public void handleMessage(Message message) {
                if(message.obj instanceof Packet){
                    Packet obj = (Packet) message.obj;
                    Constructor<? extends Bean> constructor = HandlerMap.get(obj.u16MessageId);
                    if(constructor == null) {
                        return;
                    }
                    try {
                        Bean bean = constructor.newInstance();
                        bean.fromPacket(obj);
                        MANAListener.Lazy.mana.invokeListener(bean, obj.socketThreadId);
                        if(beanPost != null)
                            beanPost.invoke(bean);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        Looper.loop();
    }

}
