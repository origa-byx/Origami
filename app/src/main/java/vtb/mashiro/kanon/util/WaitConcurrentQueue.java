package vtb.mashiro.kanon.util;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.LinkedTransferQueue;

/**
 * @by: origami
 * @date: {2022/6/1}
 * @info:
 **/
public class WaitConcurrentQueue<H> extends LinkedTransferQueue<H> {

    private final Object lock = new Object();

    public H getWait(){
        H ret = null;
        synchronized (lock) {
            while (ret == null) {
                ret = poll();
                if (ret == null) {
                    try {
                        lock.wait();
                    } catch (InterruptedException ignored) { }
                }
            }
        }
        return ret;
    }

    public void addNotify(H item){
        synchronized (lock){
            offer(item);
            lock.notifyAll();
        }
    }


}
