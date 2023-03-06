package vtb.arisu.mana.connect;

import vtb.mashiro.kanon.base.Bean;
import vtb.mashiro.kanon.util.Fun;
import vtb.mashiro.kanon.util.WaitConcurrentQueue;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @by: origami
 * @date: {2022/6/1}
 * @info:
 **/
public class MANAListener implements Runnable{

    private volatile boolean run = false;
    private long threadId = -1;

    private final WaitConcurrentQueue<MANA> timeDeque = new WaitConcurrentQueue<>();
    private final Map<Long, Fun<Object, Integer>> manaMap = new ConcurrentHashMap<>();


    private MANAListener() { }

    public void invokeListener(Bean bean, long socketId){
        long manaKey = (socketId & 0xffL << 56) | (bean.u16MessageId & 0xffffffL << 32) | (bean.u16SubSysCode & 0xffffL);
        Fun<Object, Integer> fun = manaMap.remove(manaKey);
        if(fun != null)
            fun.invoke(fun);
    }

    public void addListener(long msgId, long transId, long socketId, long overTimeDelay, Fun<Object, Integer> callback){
        long overtime = System.currentTimeMillis() + overTimeDelay;
        long manaKey = (socketId & 0xffL << 56) | (msgId & 0xffffffL << 32) | (transId & 0xffffL);
        manaMap.put(manaKey, callback);
        timeDeque.addNotify(new MANA(overtime, manaKey));
    }

    @Override
    public void run() {
        while (run && Thread.currentThread().getId() == threadId){
            MANA poll = timeDeque.getWait();
            if(poll.overtime <= System.currentTimeMillis()) {
                Fun<Object, Integer> removeFun = manaMap.remove(poll.manakey);
                removeFun.invoke(null);
            }
        }
        if(!run) {
            timeDeque.clear();
            manaMap.clear();
        }
    }

    public void start(){
        if(run) return;
        run = true;
        Thread thread = new Thread(this);
        thread.start();
        threadId = thread.getId();
    }

    public void exit(){
        run = false;
    }

    public final static class MANA{

        private final long overtime;
        private final long manakey;

        public MANA(long overtime, long manakey) {
            this.overtime = overtime;
            this.manakey = manakey;
        }
    }

    public final static class Lazy{
        public static final MANAListener mana = new MANAListener();
    }

}
