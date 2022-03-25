package com.origami.origami.base.bus;

import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;

import com.origami.utils.UiThreadUtil;

import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @by: origami
 * @date: {2021/12/17}
 * @info:  obj 中 先调用{@link #bindEvent(Object)}
 *      方法上添加注解{@link BindEvent}
 *          触发事件为  方法名
 **/
public class OriEvent {
    public static final String TAG = "ORI-EVENT";
    /**
     * 事件集合
     */
    private static final Map<String, Set<O_Event<?>>> EVENT_MAP = new ConcurrentHashMap<>();

    public static void triggerEventAndRemove(String tag, Object... args){
        if(tag == null){ return; }
        Set<O_Event<?>> events = EVENT_MAP.remove(tag);
        if(events != null){
            for (O_Event<?> event : events) {
                event.remove();
                event.postEvent(args);
            }
        }
    }

    public static void triggerEvent(String tag, Object... args){
        if(tag == null){ return; }
        Set<O_Event<?>> events = EVENT_MAP.get(tag);
        if(events != null){
            for (O_Event<?> event : events) {
                event.postEvent(args);
            }
        }
    }

    /**
     * 绑定一个类下的所有事件
     * @param obj obj
     * @see BindEvent
     */
    public static void bindEvent(Object obj){
        for (Method method : obj.getClass().getDeclaredMethods()) {
            BindEvent bindEvent = method.getAnnotation(BindEvent.class);
            if(bindEvent != null)
                bindEvent(new O_Event<>(obj,
                        TextUtils.isEmpty(bindEvent.value())? method.getName() : bindEvent.value(),
                        bindEvent.runAt(), method));
        }
    }

    /**
     * 解绑一个事件
     * @param obj 绑定的类
     * @param tag tag ,如果是注解绑定{@link BindEvent} tag = 方法名
     */
    public static void unbindByTag(Object obj, String tag){
        Set<O_Event<?>> o_events = EVENT_MAP.get(tag);
        if(o_events != null) {
            Lifecycle lifecycle;
            if(obj instanceof LifecycleOwner){
                lifecycle = ((LifecycleOwner) obj).getLifecycle();
            }else lifecycle = null;
            Iterator<O_Event<?>> iterator = o_events.iterator();
            while (iterator.hasNext()){
                O_Event<?> next = iterator.next();
                if(next.testIsBelongObjOrNull(obj)) {
                    iterator.remove();
                    if(lifecycle != null && next.eventObserver != null)
                        lifecycle.removeObserver(next.eventObserver);
                }
            }
        }
    }

    /**
     * 解绑一个绑定类下全部事件
     * @param obj 绑定的类
     */
    public static void unbindByBindObj(Object obj){
        for (Method method : obj.getClass().getDeclaredMethods()) {
            BindEvent bindEvent = method.getAnnotation(BindEvent.class);
            if(bindEvent != null)
                unbindByTag(obj, method.getName());
        }
    }

    private static void unbindByEvent(O_Event<?> event){
        Set<O_Event<?>> o_events = EVENT_MAP.get(event.tag);
        if(o_events != null)
            o_events.remove(event);
    }

    private static void bindEvent(O_Event<?> event){
        Set<O_Event<?>> o_events = EVENT_MAP.get(event.tag);
        if(o_events == null)
            EVENT_MAP.put(event.tag, new HashSet<O_Event<?>>(){ { add(event); } });
        else
            o_events.add(event);
    }

    private static class O_Event<T>{
        private final String tag;
        private final int runAt;
        private final WeakReference<T> bindObj;
        private final Method method_m;
        private LifecycleEventObserver eventObserver;

        public O_Event(T obj, String tag, int runAt, Method method_m) {
            this.tag = tag;
            this.runAt = runAt;
            this.bindObj = new WeakReference<>(obj);
            this.method_m = method_m;
            if(obj instanceof LifecycleOwner) {
                ((LifecycleOwner) obj).getLifecycle().addObserver(eventObserver= new LifecycleEventObserver() {
                    @Override
                    public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
                        if (event == Lifecycle.Event.ON_DESTROY) {
                            source.getLifecycle().removeObserver(this);
                            unbindByEvent(O_Event.this);
                        }
                    }
                });
            }
        }

        public boolean testIsBelongObjOrNull(Object obj){
            T t = bindObj.get();
            return t == null || t == obj;
        }

        private void remove(){
            T t = bindObj.get();
            if(t instanceof LifecycleOwner && eventObserver != null){
                ((LifecycleOwner) t).getLifecycle().removeObserver(eventObserver);
            }
        }

        /**
         * 执行事件
         * @param args 执行参数
         */
        private void postEvent(Object... args){
            T t = bindObj.get();
            if(t == null) {
                Log.e(TAG, "bind obj is release, stop invoke and return");
                unbindByEvent(this);
                return;
            }
            if(runAt == BindEvent.MAIN_T)
                UiThreadUtil.get().run(()-> invokeEvent(t, args));
            else if(runAt == BindEvent.CURRENT_T)
                invokeEvent(t, args);
            else if(runAt == BindEvent.NEW_T)
                new Thread(()-> invokeEvent(t, args)).start();
            else
                unbindByEvent(this);
        }

        /**
         * invoke by {@link #postEvent(Object...)}
         */
        private void invokeEvent(Object bindObj, Object... args){
            if(method_m != null) {
                try {
                    boolean accessible = method_m.isAccessible();
                    if(method_m.getModifiers() != Modifier.PUBLIC && !accessible)
                        method_m.setAccessible(true);
                    method_m.invoke(bindObj, args);
                    method_m.setAccessible(accessible);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    Log.e(TAG, "invoke error, your args may be not match event_method's args");
                    Log.e(TAG, e.getMessage());
                }
            } else
                unbindByEvent(this);
        }
    }

}
