package com.origami.origami.base;


import android.app.Activity;
import android.util.Log;

import androidx.fragment.app.Fragment;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @by: origami
 * @date: {2021/4/29}
 * @info:
 **/
public class OriEventBus {

    private final static String TAG = "OriEventBus";

    private final static Map<String, Set<Event>> EVENT_MAP = new HashMap<>();

    public static void registerEvent(String tag, Event event){
        if(tag == null || event == null){ return; }
        Set<Event> events = EVENT_MAP.get(tag);
        if(events == null){ events = new HashSet<>(); }
        if(!events.contains(event)) {
            event.tag = tag;
            events.add(event);
            EVENT_MAP.put(tag, events);
        }
    }

    public static void triggerEvent(String tag, Object... args){
        if(tag == null){ return; }
        Set<Event> events = EVENT_MAP.get(tag);
        if(events != null){
            for (Event event : events) {
                event.triggerEvent(args);
            }
        }
    }

    public static void removeEventsByTag(String tag){
        if(tag == null){ return; }
        EVENT_MAP.remove(tag);
    }

    public static void removeEvent(Event event){
        if(event == null){ return; }
        if(EVENT_MAP.get(event.tag) != null){ EVENT_MAP.get(event.tag).remove(event); }
    }


    public @interface RunThread{
        int MAIN_UI = 0;
        int CURRENT = 1;
        int NEW_THREAD = 2;
    }

    public static abstract class Event{

        private String tag;
        private final int run_on_thread;
        private final WeakReference<Object> weakReference;

        /**
         * @param activity
         * @param run_thread  {@link RunThread}
         */
        public Event(Activity activity, int run_thread) {
            weakReference = new WeakReference<>(activity);
            run_on_thread = run_thread;
        }

        /**
         * @param fragment
         * @param run_thread  {@link RunThread}
         */
        public Event(Fragment fragment, int run_thread) {
            weakReference = new WeakReference<>(fragment);
            run_on_thread = run_thread;
        }

        private void triggerEvent(Object... args){
            Object ob;
            if((ob = weakReference.get()) != null){
                Activity oa = ob instanceof Activity ? ((Activity) ob) : null;
                Fragment of = ob instanceof Fragment ? ((Fragment) ob) : null;
                if((oa == null || oa.isFinishing()) && (of == null || of.isDetached())){
                    log_msg("");  return;
                }
                switch (run_on_thread){
                    case RunThread.MAIN_UI:{
                        if(oa != null) {
                            oa.runOnUiThread(() -> postEvent(args));
                        }else if(of.getActivity() != null){
                            of.getActivity().runOnUiThread(() -> postEvent(args));
                        }else {
                            log_msg("other");
                        }
                    }break;

                    case RunThread.CURRENT:{ postEvent(args); }break;
                    case RunThread.NEW_THREAD:{ new Thread(() -> postEvent(args)).start(); }break;
                }
            }else { log_msg("->weakReference get null"); }
        }

       public abstract void postEvent(Object... args);

    }

    private static void log_msg(String msg){
        Log.e(TAG,"当前事件绑定的Activity 或 Fragment 已销毁" + msg);
    }

}
