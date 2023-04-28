#include <jni.h>
#include <jni.h>

//
// Created by Administrator on 2023/2/24.
//
#include <android/log.h>
#include <android/bitmap.h>
#include <vector>
#include "opencv2/core.hpp"
#include "opencv2/imgproc.hpp"
#include "opencv2/imgproc/types_c.h"
#include "time.h"
#define TAG "jni_ori_cv" // 这个是自定义的LOG的标识
#define LOG_D(...) __android_log_print(ANDROID_LOG_DEBUG, TAG, __VA_ARGS__)
#define LOG_I(...) __android_log_print(ANDROID_LOG_INFO,TAG ,__VA_ARGS__)
#define LOG_W(...) __android_log_print(ANDROID_LOG_WARN,TAG ,__VA_ARGS__)
#define LOG_E(...) __android_log_print(ANDROID_LOG_ERROR,TAG ,__VA_ARGS__)

/**
 * @by: origami
 * @date: {2023/2/24}
 * @info:
 *
 **/
class RectMj{
public:
    cv::Rect rect;
    int32_t m_j;

    RectMj() {}
};
void mat2Bitmap(JNIEnv *env, cv::Mat& mat, jobject& bitmap, jboolean needPremultiplyAlpha);
void bitmap2Mat(JNIEnv * env, jobject bitmap, cv::Mat &dst);
extern "C" {
    //jni 的第一个加载函数
    JNIEXPORT jint JNICALL
    JNI_OnLoad(JavaVM *vm, void *reserved) {
        LOG_E("fun JNI_OnLoad is loading...");
        if (vm == nullptr) {
            return JNI_ERR;
        }
        JNIEnv *env;
        int32_t jni_version = JNI_ERR;
        if (vm->GetEnv((void **) &env, JNI_VERSION_1_6) == JNI_OK) {
            jni_version = JNI_VERSION_1_6;
        } else if (vm->GetEnv((void **) &env, JNI_VERSION_1_4) == JNI_OK) {
            jni_version = JNI_VERSION_1_4;
        } else if (vm->GetEnv((void **) &env, JNI_VERSION_1_2) == JNI_OK) {
            jni_version = JNI_VERSION_1_2;
        } else if (vm->GetEnv((void **) &env, JNI_VERSION_1_1) == JNI_OK) {
            jni_version = JNI_VERSION_1_1;
        }
        LOG_D("当前JNI版本：%d", jni_version);
        return jni_version;
    }

    JNIEXPORT void JNICALL
    Java_com_ori_origami_jni_OriOpenCv_doCv(JNIEnv *env, jobject oriOpenCv, jobject bitmap, jdouble sc) {
        AndroidBitmapInfo info;
        jclass jc = env->FindClass("com/ori/origami/jni/OriOpenCv");
        jmethodID me = env->GetMethodID(jc, "doCvNext", "(IIII)V");
//        jmethodID me_bitmap = env->GetMethodID(jc, "bitmapChange", "(I)V");
        AndroidBitmap_getInfo(env, bitmap, &info);
        void * pixels = nullptr;
        AndroidBitmap_lockPixels(env, bitmap, &pixels);
        cv::Mat mat;
        if(info.format == ANDROID_BITMAP_FORMAT_RGBA_8888){
            cv::Mat tmp(info.height, info.width, CV_8UC4, pixels);
            tmp.copyTo(mat);
        }else{
            cv::Mat tmp(info.height, info.width, CV_8UC2, pixels);
            cv::cvtColor(tmp, mat, CV_RGBA2BGR565);
        }
        AndroidBitmap_unlockPixels(env, bitmap);
        cv::resize(mat, mat, cv::Size(0, 0), sc, sc);
        cv::cvtColor(mat, mat, cv::COLOR_BGR2GRAY);
//        mat2Bitmap(env, mat, bitmap, true);
//        env->CallVoidMethod(oriOpenCv, me_bitmap, 1);
        cv::medianBlur(mat, mat, 5);
//        mat2Bitmap(env, mat, bitmap, true);
//        env->CallVoidMethod(oriOpenCv, me_bitmap, 2);
        cv::threshold(mat, mat, 127, 255, cv::THRESH_BINARY);
//        mat2Bitmap(env, mat, bitmap, true);
//        env->CallVoidMethod(oriOpenCv, me_bitmap, 3);
        std::vector<std::vector<cv::Point>> contours;
        cv::findContours(mat, contours, CV_RETR_EXTERNAL, CV_CHAIN_APPROX_SIMPLE);
        std::vector<RectMj> rectMjs;
        for(auto & contour : contours){
            RectMj mj;
            cv::Rect rect = cv::boundingRect(contour);
            mj.rect = rect;
            mj.m_j = rect.width * rect.height;
            rectMjs.push_back(mj);
            LOG_E("当前轮廓：x: %d, y: %d, width: %d, height: %d, rect: %d",
                  rect.x, rect.y, rect.width, rect.height, mj.m_j);
        }
        RectMj * targetRect = nullptr;
        for(auto & rectMj : rectMjs){
            if(!targetRect){
                targetRect = &rectMj;
            }else if(targetRect->m_j < rectMj.m_j){
                targetRect = &rectMj;
            }
        }
        if(!targetRect){
            env->CallVoidMethod(oriOpenCv, me, 0, 0, -1, -1);
        }else{
            targetRect->rect.x /= sc;
            targetRect->rect.y /= sc;
            targetRect->rect.width /= sc;
            targetRect->rect.height /= sc;
            env->CallVoidMethod(oriOpenCv, me, targetRect->rect.x, targetRect->rect.y, targetRect->rect.width, targetRect->rect.height);
        }
    }

}

void bitmap2Mat(JNIEnv * env, jobject bitmap, cv::Mat &dst){
    AndroidBitmapInfo info;
    void * pixels = nullptr;
    try{
        CV_Assert(AndroidBitmap_getInfo(env, bitmap, &info) >= 0);
        CV_Assert(info.format == ANDROID_BITMAP_FORMAT_RGBA_8888 ||
                    info.format == ANDROID_BITMAP_FORMAT_RGB_565);
        CV_Assert(AndroidBitmap_lockPixels(env, bitmap, &pixels) >= 0);
        CV_Assert(pixels);
        dst.create(info.height, info.width, CV_8UC4);
        if(info.format == ANDROID_BITMAP_FORMAT_RGBA_8888){
            cv::Mat tmp(info.height, info.width, CV_8UC4, pixels);
            tmp.copyTo(dst);
        }else{
            cv::Mat tmp(info.height, info.width, CV_8UC2, pixels);
            cv::cvtColor(tmp, dst, CV_RGBA2BGR565);
        }
        AndroidBitmap_unlockPixels(env, bitmap);
        return;
    }catch (...){
        AndroidBitmap_unlockPixels(env, bitmap);
        jclass  je = env->FindClass("java/lang/Exception");
        env->ThrowNew(je, "Unknown exception in Jni code");
        return;
    }
}

void mat2Bitmap(JNIEnv *env, cv::Mat& mat, jobject& bitmap, jboolean needPremultiplyAlpha){
    AndroidBitmapInfo info;
    void *pixels = nullptr;
    cv::Mat &src = mat;
    try {
        CV_Assert(AndroidBitmap_getInfo(env, bitmap, &info) >= 0);
        CV_Assert(info.format == ANDROID_BITMAP_FORMAT_RGBA_8888 ||
                  info.format == ANDROID_BITMAP_FORMAT_RGB_565);
        CV_Assert(src.dims == 2 && info.height == (uint32_t) src.rows &&
                  info.width == (uint32_t) src.cols);
        CV_Assert(src.type() == CV_8UC1 || src.type() == CV_8UC3 || src.type() == CV_8UC4);
        CV_Assert(AndroidBitmap_lockPixels(env, bitmap, &pixels) >= 0);
        CV_Assert(pixels);
        if (info.format == ANDROID_BITMAP_FORMAT_RGBA_8888) {
            cv::Mat tmp(info.height, info.width, CV_8UC4, pixels);
            if (src.type() == CV_8UC1) {
                LOG_D("nMatToBitmap: CV_8UC1 -> RGBA_8888");
                cvtColor(src, tmp, cv::COLOR_GRAY2RGBA);
            } else if (src.type() == CV_8UC3) {
                LOG_D("nMatToBitmap: CV_8UC3 -> RGBA_8888");
                cvtColor(src, tmp, cv::COLOR_RGB2RGBA);
            } else if (src.type() == CV_8UC4) {
                LOG_D("nMatToBitmap: CV_8UC4 -> RGBA_8888");
                if (needPremultiplyAlpha)
                    cvtColor(src, tmp, cv::COLOR_RGBA2mRGBA);
                else
                    src.copyTo(tmp);
            }
        } else {
            // info.format == ANDROID_BITMAP_FORMAT_RGB_565
            cv::Mat tmp(info.height, info.width, CV_8UC2, pixels);
            if (src.type() == CV_8UC1) {
                LOG_D("nMatToBitmap: CV_8UC1 -> RGB_565");
                cvtColor(src, tmp, cv::COLOR_GRAY2BGR565);
            } else if (src.type() == CV_8UC3) {
                LOG_D("nMatToBitmap: CV_8UC3 -> RGB_565");
                cvtColor(src, tmp, cv::COLOR_RGB2BGR565);
            } else if (src.type() == CV_8UC4) {
                LOG_D("nMatToBitmap: CV_8UC4 -> RGB_565");
                cvtColor(src, tmp, cv::COLOR_RGBA2BGR565);
            }
        }
        AndroidBitmap_unlockPixels(env, bitmap);
        return;
    }  catch (...) {
        AndroidBitmap_unlockPixels(env, bitmap);
        LOG_E("nMatToBitmap catched unknown exception (...)");
        jclass je = env->FindClass("java/lang/Exception");
        env->ThrowNew(je, "Unknown exception in JNI code {nMatToBitmap}");
        return;
    }
}