package com.ori.origami;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.origami.origami.base.AnnotationActivity;
import com.origami.origami.base.annotation.BContentView;

@SuppressLint("NonConstantResourceId")
@BContentView(R.layout.activity_test)
public class TestActivity extends AnnotationActivity {

    @Override
    public void init(@Nullable Bundle savedInstanceState) {

    }

}