package vtb.mashiro.kanon.os;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @by: origami
 * @date: {2022/4/14}
 * @info:
 **/
public class Looper {

    private static final ThreadLocal<Looper> LOOPER_THREAD_LOCAL = new ThreadLocal<>();

    private Handler handler;
    private boolean exit = false;
    private final Object lock = new Object();//锁对象
    private final AtomicBoolean lock_flag = new AtomicBoolean(false);
    private final LinkedBlockingQueue<Message> msgQueue = new LinkedBlockingQueue<>();

    private Looper() { }

    public static Looper myLooper(){
        Looper looper = LOOPER_THREAD_LOCAL.get();
        if(looper == null)
            throw new RuntimeException("must be invoke method ready at first");
        return looper;
    }

    public static void prepare(){
        if(LOOPER_THREAD_LOCAL.get() != null)
            throw new RuntimeException("only one looper can be created");
        LOOPER_THREAD_LOCAL.set(new Looper());
    }

    public static void loop(){
        myLooper().run();
    }

    public static void quit(){
        Looper looper = LOOPER_THREAD_LOCAL.get();
        if(looper == null)
            throw new RuntimeException("must be invoke method ready at first");
        looper.exit();
    }

    void setHandler(Handler handler){
        this.handler = handler;
    }

    void addMessage(Message message){
        if(message == null)
            return;
        msgQueue.offer(message);
        if(lock_flag.get()){
            synchronized (lock) {
                lock_flag.set(false);
                lock.notifyAll();
            }
        }
    }

    public void exit(){
        if(lock_flag.get()){
            synchronized (lock) {
                lock_flag.set(false);
                lock.notifyAll();
            }
        }
        exit = true;
    }

    private void run(){
        while (!exit){
            Message poll = msgQueue.poll();
            if(poll == null){
                synchronized (lock){
                    try {
                        lock_flag.set(true);
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }else if(System.currentTimeMillis() >= poll.invokeTime) {
                if(handler != null)
                    handler.handleMessage(poll);
                if(poll.callBack != null)
                    poll.callBack.run();
            }else
                addMessage(poll);
        }
        msgQueue.clear();
        handler = null;
        LOOPER_THREAD_LOCAL.remove();
    }

}
