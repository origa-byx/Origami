package vtb.mashiro.kanon;

import vtb.arisu.mana.annotation.VtbBean;
import vtb.arisu.mana.connect.PacketHandler;
import vtb.arisu.mana.connect.TCP;
import vtb.mashiro.kanon.base.Bean;
import vtb.mashiro.kanon.util.*;

import java.lang.reflect.Constructor;
import java.util.List;

import static vtb.mashiro.kanon.util.Log.Lazy.log;

/**
 * @by: origami
 * @date: {2022/6/1}
 * @info:
 **/
public final class Aria {

    private final Builder builder;
    private final TCP tcp;

    public void addLogIntercept(Fun<String[], Boolean> fun){
        log.addInterceptFun(fun);
    }

    public void clearLogIntercept(){
        log.clearInterceptFun();
    }

    public void init(){
        tcp.initServer(builder.port);
    }

    public void exit(){
        tcp.close();
    }


    //-----------------------------------------------------------------------------------------

    public static JavaBuilder javaBuilder(){
        return new JavaBuilder();
    }

    public static AndroidBuilder androidBuilder(){
        return new AndroidBuilder();
    }

    private static abstract class Builder{

        protected Funichi<Bean> fun;
        protected int port = 3345;
        protected Funichi<String> logMethod = System.out::println;

        protected final void addListenerClassSuper(Class<?> cl){
            VtbBean vtbBean = cl.getAnnotation(VtbBean.class);
            if(vtbBean == null) return;
            try {
                Constructor<? extends Bean> constructor = (Constructor<? extends Bean>) cl.getConstructor();
                PacketHandler.addMap(vtbBean.value(), constructor);
            } catch (NoSuchMethodException ignored) { }
        }

    }

    public final static class JavaBuilder extends Builder{

        public JavaBuilder setLogMethod(Funichi<String> logMethod){
            this.logMethod = logMethod;
            return this;
        }

        public JavaBuilder setPort(int port){
            this.port = port;
            return this;
        }

        public JavaBuilder addPostEvent(Funichi<Bean> fun){
            this.fun = fun;
            return this;
        }

        public JavaBuilder addListenerClass(Class<? extends Bean> clazz){
            addListenerClassSuper(clazz);
            return this;
        }

        public JavaBuilder addListenerClass(String packageName){
            for (Class<?> aClass : Mana.scanForPackage(packageName)) {
                if(!Bean.class.isAssignableFrom(aClass)) continue;
                addListenerClassSuper(aClass);
            }
            return this;
        }

        private JavaBuilder() {
            for (Class<?> aClass : Mana.scanForPackage("vtb.arisu.mana.bean")) {
                if(!Bean.class.isAssignableFrom(aClass)) continue;
                addListenerClassSuper(aClass);
            }
        }

        public Aria build(){
            return new Aria(this);
        }

    }

    public final static class AndroidBuilder extends Builder{

        public AndroidBuilder setLogMethod(Funichi<String> logMethod){
            this.logMethod = logMethod;
            return this;
        }

        public AndroidBuilder addPostEvent(Funichi<Bean> fun){
            this.fun = fun;
            return this;
        }

        public AndroidBuilder setPort(int port){
            this.port = port;
            return this;
        }

        public AndroidBuilder addListenerClass(List<Class<? extends Bean>> clazzList){
            for (Class<? extends Bean> aClass : clazzList) {
                addListenerClassSuper(aClass);
            }
            return this;
        }

        public AndroidBuilder addListenerClass(Class<? extends Bean> clazz){
            addListenerClassSuper(clazz);
            return this;
        }

        private AndroidBuilder() {
            for (Class<?> aClass : ClassToAndroid.CLASSESToAndroid) {
                if(!Bean.class.isAssignableFrom(aClass)) continue;
                addListenerClassSuper(aClass);
            }
        }

        public Aria build(){
            return new Aria(this);
        }

    }

    public Aria(Builder builder) {
        this.tcp = new TCP(builder.fun);
        this.builder = builder;
        log.setLogMethod(builder.logMethod);
    }

}
