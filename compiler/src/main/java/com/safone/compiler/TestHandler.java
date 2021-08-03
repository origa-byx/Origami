package com.safone.compiler;

import com.safone.compiler.annotation.TestAnn;


import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

/**
 * 编译期注解解析
 */
public class TestHandler extends AbstractProcessor {

    //跟日志相关的辅助类。
    private Messager messager;
    //与生成java代码源文件相关的类
    private Filer mFiler;
    //跟元素相关的辅助类，帮助我们去获取一些元素相关的信息。
    private Elements elementUtils;

    /**
     * 初始化
     * @param processingEnvironment
     */
    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);

        mFiler = processingEnvironment.getFiler();
        messager = processingEnvironment.getMessager();
        elementUtils = processingEnvironment.getElementUtils();
    }

    /**
     * 解析注解并生成java代码
     * @param set
     * @param roundEnvironment
     * @return
     */
    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        Set<? extends Element> annotatedWith = roundEnvironment.getElementsAnnotatedWith(TestAnn.class);
        for (Element element : annotatedWith) {

        }
        return false;
    }

    /**
     * @return 支持的自定义注解
     */
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> annSet = new LinkedHashSet<>();
        annSet.add(TestAnn.class.getCanonicalName());
        return annSet;
    }

    /**
     * @return 支持的java版本
     */
    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.RELEASE_7;
    }

}