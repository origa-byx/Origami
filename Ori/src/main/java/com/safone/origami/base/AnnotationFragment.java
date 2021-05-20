package com.safone.origami.base;

import android.content.Context;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.safone.origami.base.annotation.BClick;
import com.safone.origami.base.annotation.BContentView;
import com.safone.origami.base.annotation.BView;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * @author xiao gan
 * @date 2020/12/3
 * @description:
 **/
public abstract class AnnotationFragment extends Fragment implements View.OnClickListener {

    protected final SparseArray<Method> methodSparseArray = new SparseArray<>();
    protected Context mContext;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        BContentView annotation = getClass().getAnnotation(BContentView.class);
        if(annotation != null){
            View view = inflater.inflate(annotation.value(), container, false);
            Field[] fields = getClass().getDeclaredFields();
            for (Field field : fields) {
                BView bindMyView = field.getAnnotation(BView.class);
                if(bindMyView != null){
                    boolean accessible = field.isAccessible();
                    if(field.getModifiers() != Modifier.PUBLIC && !accessible){
                        field.setAccessible(true);
                        try {
                            field.set(this,view.findViewById(bindMyView.value()));
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                        field.setAccessible(accessible);
                    }
                }
            }
            Method[] methods = getClass().getDeclaredMethods();
            for (Method method : methods) {
                BClick bindClickListener = method.getAnnotation(BClick.class);
                if(bindClickListener != null){
                    int[] value = bindClickListener.value();
                    for (int i : value) {
                        methodSparseArray.put(i,method);
                        view.findViewById(i).setOnClickListener(this);
                    }
                }
            }
            init(inflater,container,savedInstanceState);
            return view;
        }else{
            init(inflater,container,savedInstanceState);
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public abstract void init(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState);

    @Override
    public void onDetach() {
        super.onDetach();
        mContext = null;
    }


    @Override
    public void onClick(View v) {
        Method method = methodSparseArray.get(v.getId());
        if(method != null){
            try {
                boolean accessible = method.isAccessible();
                if(method.getModifiers() != Modifier.PUBLIC && !accessible){
                    method.setAccessible(true);
                    method.invoke(this);
                    method.setAccessible(accessible);
                }else{
                    method.invoke(this);
                }
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }
}
