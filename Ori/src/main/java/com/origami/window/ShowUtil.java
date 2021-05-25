package com.origami.window;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.origami.origami.R;
import com.origami.utils.Dp2px;

/**
 * @by: origami
 * @date: {2021-05-25}
 * @info:
 **/
public class ShowUtil {

    public interface OnSelectListener{
        void onSelect(String txt, int index);
    }

    public static void showSelect(Activity activity, String[] texts, OnSelectListener selectListener, boolean showCancelView){
        if(activity == null || selectListener == null){ return; }
        View selectView = LayoutInflater.from(activity)
                .inflate(
                        R.layout._base_show_select,
                        activity.getWindow().getDecorView().findViewById(android.R.id.content),
                        false);
        LinearLayout showLayout = selectView.findViewById(R.id.select_list);
        WindowUtil windowUtil = WindowUtil.build(activity).bindView(selectView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                RelativeLayout.ALIGN_PARENT_BOTTOM,
                RelativeLayout.CENTER_HORIZONTAL).setCanCancel();
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v instanceof  TextView){
                    int index =(int) v.getTag();
                    windowUtil.dismiss();
                    selectListener.onSelect(texts[index], index);
                }
            }
        };
        for (int i = 0; i < texts.length; i++) {
            TextView textView = new TextView(activity);
            textView.setTag(i);
            textView.setTextSize(16);
            textView.setBackground(activity.getDrawable(R.drawable._select_white_gray));
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Dp2px.dp2px(45));
            layoutParams.gravity = Gravity.CENTER;
            textView.setTextColor(Color.BLACK);
            textView.setGravity(Gravity.CENTER);
            textView.setText(texts[i]);
            textView.setOnClickListener(listener);
            showLayout.addView(textView, layoutParams);
        }
        TextView cancel_view = selectView.findViewById(R.id.select_cancel);
        if(showCancelView){
            cancel_view.setVisibility(View.VISIBLE);
            cancel_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    windowUtil.dismiss();
                }
            });
        }else {
            cancel_view.setVisibility(View.GONE);
        }
        windowUtil.showByAnimator();
    }

}
