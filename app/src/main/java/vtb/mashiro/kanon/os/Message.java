package vtb.mashiro.kanon.os;

/**
 * @by: origami
 * @date: {2022/4/14}
 * @info:
 **/
public class Message {

    public int what;
    public Object obj;
    public Runnable callBack;

    final long invokeTime;

    public Message() {
        this.invokeTime = 0;
    }

    public Message(long delay) {
        this.invokeTime = System.currentTimeMillis() + delay;
    }

    public static Message obtainCallBack(Runnable callBack, long delay){
        Message message = new Message(delay);
        message.callBack = callBack;
        return message;
    }

    public static Message obtainCallBack(Runnable callBack){
        Message message = new Message();
        message.callBack = callBack;
        return message;
    }

}
