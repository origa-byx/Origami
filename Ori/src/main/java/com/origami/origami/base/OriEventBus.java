package com.origami.origami.base;


import android.app.Activity;
import android.util.Log;

import androidx.fragment.app.Fragment;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @by: origami
 * @date: {2021/4/29}
 * @info:
 **/
public class OriEventBus {

    private final static String TAG = "OriEventBus";

    private final static Map<String, List<Event>> EVENT_MAP = new HashMap<>();

    public static void registerEvent(String tag, Event event){
        if(tag == null || event == null){return;}
        List<Event> events = EVENT_MAP.get(tag);
        if(events == null){ events = new ArrayList<>(); }
        events.add(event);
        EVENT_MAP.put(tag,events);
    }

    public static void triggerEvent(String tag, Object... args){
        if(tag == null){return;}
        List<Event> events = EVENT_MAP.get(tag);
        if(events != null){
            for (Event event : events) {
                event.triggerEvent(args);
            }
        }
    }

    public static void removeEventsByTag(String tag){
        if(tag == null){return;}
        EVENT_MAP.remove(tag);
    }

    public static void removeEvent(Event event){
        if(event == null){return;}
        if(EVENT_MAP.get(event.tag) != null){ EVENT_MAP.get(event.tag).remove(event); }
    }


    public @interface RunThread{
        int MAIN_UI = 0;
        int CURRENT = 1;
        int NEW_THREAD = 2;
    }

    public static abstract class Event{

        private String tag;
        private int run_on_thread = RunThread.CURRENT;
        private final WeakReference<Object> weakReference;

        public Event(Activity activity, int run_thread) {
            weakReference = new WeakReference<>(activity);
            run_on_thread = run_thread;
        }
        public Event(Fragment fragment, int run_thread) {
            weakReference = new WeakReference<>(fragment);
            run_on_thread = run_thread;
        }

        private void triggerEvent(Object... args){
            Object ob;
            if((ob = weakReference.get()) != null){
                Activity oa = ob instanceof Activity ? ((Activity) ob) : null;
                Fragment of = ob instanceof Fragment ? ((Fragment) ob) : null;
                if(oa == null || oa.isFinishing()){
                    log_msg("oa");  return;
                } else if(oa == null && (of == null || of.isDetached())) {
                    log_msg("of");  return;
                }
                switch (run_on_thread){
                    case RunThread.MAIN_UI:{
                        if(oa != null) {
                            oa.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    postEvent(args);
                                }
                            });
                        }else if(of != null && of.getActivity() != null){
                            of.getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    postEvent(args);
                                }
                            });
                        }else {
                            log_msg("other");
                        }
                    }break;

                    case RunThread.CURRENT:{ postEvent(args); }break;
                    case RunThread.NEW_THREAD:{
                        new Thread(new Runnable() {
                            @Override
                            public void run() { postEvent(args); }
                        }).start();
                    }break;
                }
            }else { log_msg("get null"); }
        }

       public abstract void postEvent(Object... args);

    }

    private static void log_msg(String msg){
        Log.e(TAG,"当前事件绑定的Activity 或 Fragment 已销毁" + msg);
    }

}
