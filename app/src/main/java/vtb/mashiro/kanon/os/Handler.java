package vtb.mashiro.kanon.os;

/**
 * @by: origami
 * @date: {2022/4/14}
 * @info:
 **/
public abstract class Handler {

    private final Looper looper;

    public Handler(Looper looper) {
        this.looper = looper;
        looper.setHandler(this);
    }

    public void post(Runnable callback){
        looper.addMessage(Message.obtainCallBack(callback));
    }

    public void post(Runnable callback, long delay){
        looper.addMessage(Message.obtainCallBack(callback, delay));
    }

    public void post(Message msg){
        looper.addMessage(msg);
    }

    public void quit(){
        this.looper.exit();
    }

    public abstract void handleMessage(Message message);


}
