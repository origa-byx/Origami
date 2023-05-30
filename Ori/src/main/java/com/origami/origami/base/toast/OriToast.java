package com.origami.origami.base.toast;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.os.Looper;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.origami.origami.R;
import com.origami.utils.Dp2px;
import com.origami.utils.UiThreadUtil;

/**
 * @by: origami
 * @date: {2021-10-27}
 * @info:
 **/
@SuppressLint("StaticFieldLeak")
public class OriToast {

    private static OriToast instance;
    private static final int defDy = Dp2px.dp2px(50);
    private static final int defMx = Dp2px.dp2px(30);

    private View sToastView;
    private final Context context;

    public static void init(Application context){
        if(instance == null)
            instance = new OriToast(context);
    }

    private OriToast(Context context) {
        this.context = context;
    }

    @SuppressLint("InflateParams")
    private void initToastIfNeed(){
        if(context == null) throw new RuntimeException("OriToast#context is null, you must invoke init at first");
        if(sToastView == null){
            sToastView = LayoutInflater.from(context).inflate(R.layout._base_sys_toast, null);
            ViewGroup.LayoutParams params = sToastView.getLayoutParams();
            if(params == null){ params = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            }else {
                params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
                params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            }
            sToastView.setLayoutParams(params);
        }
    }

    private void toastShow(boolean shortDur, String msg, Boolean icon, int gravity, int dy){
        if(TextUtils.isEmpty(msg)) return;
        if(Looper.myLooper() != Looper.getMainLooper())
            UiThreadUtil.get().run(()-> t(shortDur, msg, icon, gravity, dy));
        else t(shortDur, msg, icon, gravity, dy);
    }

    private void t(boolean shortDur, String msg, Boolean icon, int gravity, int dy){
        initToastIfNeed();
        ImageView iconView = sToastView.findViewById(R.id._base_show_toast_icon);
        if(icon == null)
            iconView.setVisibility(View.GONE);
        else {
            iconView.setVisibility(View.VISIBLE);
            iconView.setImageResource(icon?R.mipmap._toast_ok:R.mipmap._toast_no);
        }
        ((TextView) sToastView.findViewById(R.id._base_show_toast_msg)).setText(msg);
        Toast sToast = new Toast(context);
        sToast.setView(sToastView);
        sToast.setDuration(shortDur? Toast.LENGTH_SHORT: Toast.LENGTH_LONG);
        sToast.setGravity(gravity, 0, dy);
        sToast.show();
    }

    /**
     * 显示Toast
     */
    public static void show(String msg){
        OriToast.show(msg, null, true, Gravity.TOP);
    }

    /**
     * 显示Toast
     */
    public static void show(String msg, boolean icon){
        OriToast.show(msg, icon, true, Gravity.TOP);
    }

    /**
     * 显示Toast
     */
    public static void show(String msg, boolean icon, boolean shortDur){
        OriToast.show(msg, icon, shortDur, Gravity.TOP);
    }

    /**
     * 显示Toast
     * @param msg 内容
     * @param icon  true : 图标为 √ ;false : 图标为 × ;null : 无图标
     * @param shortDur  true : 短时显示   false : 长时显示
     * @param gravity   Toast 位置 def->  {@link Gravity#TOP}
     * @param dy    垂直方向距离屏幕边界的距离
     */
    public static void show(String msg, Boolean icon, boolean shortDur, int gravity, int... dy){
        int m_y;
        if(dy != null && dy.length > 0) m_y = dy[0]; else m_y = defDy;
        OriToast.instance.toastShow(shortDur, msg, icon, gravity, m_y);
    }

}
