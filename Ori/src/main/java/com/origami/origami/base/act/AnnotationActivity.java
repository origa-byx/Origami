package com.origami.origami.base.act;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.origami.origami.base.annotation.BClick;
import com.origami.origami.base.annotation.BContentView;
import com.origami.origami.base.annotation.BView;
import com.origami.origami.base.bus.OriEvent;
import com.origami.origami.base.callback.RequestPermissionNext;
import com.origami.utils.StatusUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * @author xiao gan
 * @date 2020/12/2
 * @description:
 * @see BContentView
 * @see BClick
 **/
public abstract class AnnotationActivity extends AppCompatActivity implements View.OnClickListener,
        TitleAutoActivity {

    protected final static int per_requestCode = 1028;
    //点击事件集合
    protected final SparseArray<Method> methodSparseArray = new SparseArray<>();

    private BaseTitleView mTitleView;
    private boolean busEventFlag = false;
    private RequestPermissionNext next;

    public abstract void init(@Nullable Bundle savedInstanceState);

    protected void initContentView(@LayoutRes int resId){
        setContentView(resId);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Log.e("ORI",String.format("init -> %s", this.getClass().getSimpleName()));
        Log.e("ORI", "-> " + android.os.Process.myPid());
        onCreateBefore(savedInstanceState);
        BContentView contentView = getClass().getAnnotation(BContentView.class);
        if(contentView != null){
            initContentView(contentView.value());
            busEventFlag = contentView.oriEventBus();
            if(busEventFlag) OriEvent.bindEvent(this);
        }else {
            setContentView(getLayout());
            setStatusBar();
            init(savedInstanceState);
            AnnotationActivityManager.addActivity(this);
            return;
        }
        Method[] methods = getClass().getDeclaredMethods();
        for (Method method : methods) {
            BClick bindClickListener = method.getAnnotation(BClick.class);
            if(bindClickListener != null){
                int[] value = bindClickListener.value();
                for (int i : value) {
                    methodSparseArray.put(i, method);
                    findViewById(i).setOnClickListener(this);
                }
            }
        }
        AnnotationActivityManager.addActivity(this);
        setStatusBar();
        init(savedInstanceState);
    }

    protected void onCreateBefore(Bundle savedInstanceState){ }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.e("ORI",String.format("onNewIntent -> %s", this.getClass().getSimpleName()));
        setIntent(intent);
        initNewIntent(intent);
    }

    /**
     * 特殊情况下 singleTop 与 singleTask 模式需要重新处理数据用
     */
    protected void initNewIntent(Intent intent){ }

    /**
     * 动态权限申请
     * @param permissions
     * @param permissionNext
     */
    public void checkPermissionAndThen(String[] permissions, RequestPermissionNext permissionNext){
        if(!checkPermissionsAllGranted(permissions)){
            this.next = permissionNext;
            ActivityCompat.requestPermissions(this, permissions, per_requestCode);
        }else if(permissionNext != null) {
            permissionNext.next();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == per_requestCode) {
            boolean isAllGranted = true;
            for (int grantResult : grantResults) {
                if (grantResult != PackageManager.PERMISSION_GRANTED) {
                    isAllGranted = false;
                    break;
                }
            }
            if (isAllGranted) {
                if(next != null){ next.next(); }
            }else if(next != null) { next.failed(); }
            next = null;
        }
    }

    private boolean checkPermissionsAllGranted(String[] permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                // 只要有一个权限没有被授予, 则直接返回 false
                return false;
            }
        }
        return true;
    }

    /**
     * 处理状态栏
     */
    protected void setStatusBar(){
        mTitleView = initStatusAndTitleBar(this);
    }

    public BaseTitleView getTitleView() {
        if(mTitleView == null)
            throw new IllegalStateException("mTitleView is null, " +
                    "you may be missed @Title at your Activity[ " + getClass().getSimpleName() + " ]");
        return mTitleView;
    }

    @Override
    public void onClick(View v) {
        Method method = methodSparseArray.get(v.getId());
        if(method != null){
            try {
                Class<?>[] parameterTypes = method.getParameterTypes();
                boolean accessible = method.getModifiers() != Modifier.PUBLIC && !method.isAccessible();
                if(accessible)
                    method.setAccessible(true);
                if(parameterTypes.length == 1){
                    if(parameterTypes[0] == int.class)
                        method.invoke(this, v.getId());
                    else method.invoke(this, v);
                }else method.invoke(this);
                if(accessible)
                    method.setAccessible(false);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(busEventFlag)
            OriEvent.unbindByBindObj(this);
        AnnotationActivityManager.removeActivity(this);
    }

    /**
     * @see BContentView
     * @return
     * @deprecated use {@link BContentView}
     */
    protected int getLayout(){ return 0; }

}
