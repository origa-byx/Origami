package vtb.mashiro.kanon.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

/**
 * @by: origami
 * @date: {2022/5/12}
 * @info:
 **/
public class Log {

    /**
     * return true is intercept;
     */
    private final Set<Fun<String[], Boolean>> interceptFUNs = new HashSet<>();
    private Funichi<String> logInstance = System.out::println;

    private String c = "33";
    public static final class Lazy{
        private static final SimpleDateFormat FORMAT = new SimpleDateFormat("MM-dd hh:mm:ss.SSS");
        public static Log log = new Log();
    }

    private Log() { }

    public void resetCfg(){
        c = "33";
        logInstance = System.out::println;
        clearInterceptFun();
    }

    public void setLogMethod(Funichi<String> logMethod){
        logInstance = logMethod;
    }

    public void setDefPrColor(String c){
        this.c = c;
    }

    public void addInterceptFun(Fun<String[], Boolean> fun){
        synchronized (interceptFUNs){
            interceptFUNs.add(fun);
        }
    }

    public void clearInterceptFun(){
        synchronized (interceptFUNs){
            interceptFUNs.clear();
        }
    }

    public void pr(String tag, Throwable e){
        pr(tag, e.getMessage());
    }

    public void pr(String tag, String msg){
        String[] args = new String[]{tag, msg};
        synchronized (interceptFUNs){
            for (Fun<String[], Boolean> fun : interceptFUNs) {
                if(fun.invoke(args)) return;
            }
        }
        String format = (c.equalsIgnoreCase("null")? "" : "\33[" + c + ";1m") +
                "[%s] [%-10s] [%-10s] : %s";
        String prMsg = String.format(format, Lazy.FORMAT.format(new Date()),
                Thread.currentThread().getName(),
                args[0],
                args[1]);
        if(logInstance != null)
            logInstance.invoke(prMsg);
    }

}
