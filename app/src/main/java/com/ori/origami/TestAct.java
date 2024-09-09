package com.ori.origami;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.ori.origami.databinding.ActTestBinding;
import com.ori.origami.jni.OriAmrWbDec;
import com.origami.origami.base.act.OriBaseActivity;
import com.origami.origami.base.annotation.BContentView;

/**
 * @by: origami
 * @date: 2024/7/24 15:45
 * @info:
 **/
@SuppressLint("NonConstantResourceId")
@BContentView(R.layout.act_test)
public class TestAct extends OriBaseActivity<ActTestBinding> {

    @Override
    public void init(@Nullable Bundle savedInstanceState) {
        new Thread(()-> OriAmrWbDec.test(this, b->{
            runOnUiThread(()->{
                mViews.testAs.write(b);
            });
        })).start();
//        mViews.testAs.write(new byte[]{100, -100, 120, -40, 50, -60, 70, -100, -40, 50});
    }

}
