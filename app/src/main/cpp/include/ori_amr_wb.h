//
// Created by 15080 on 2024/7/15.
//

#ifndef ORIGAMI_ORI_AMR_WB_H
#define ORIGAMI_ORI_AMR_WB_H

#include <jni.h>
#include <string>
#include <android/log.h>
#include <fstream>
#ifdef __cplusplus
extern "C"
{
#endif
#include <libavcodec/avcodec.h>
#include <libavformat/avformat.h>
#include <libswresample/swresample.h>
#include <libavutil/avutil.h> // ���߿�
#include <libavutil/opt.h>
#include <libavutil/error.h>
#ifdef __cplusplus
}
#endif

#define TAG "JNI-ori_amr_wb" // 这个是自定义的LOG的标识
#define LOG_D(...) __android_log_print(ANDROID_LOG_DEBUG, TAG, __VA_ARGS__)
#define LOG_I(...) __android_log_print(ANDROID_LOG_INFO,TAG ,__VA_ARGS__)
#define LOG_W(...) __android_log_print(ANDROID_LOG_WARN,TAG ,__VA_ARGS__)
#define LOG_E(...) __android_log_print(ANDROID_LOG_ERROR,TAG ,__VA_ARGS__)
/**
 * @by: origami
 * @date: 2024/7/15 16:44
 * @info:
 **/
class ori_amr_wb {
private:
    int32_t bufferSize;
//    uint8_t* bufferBytes;
//    SwrContext* swrContext;
    AVCodecContext* m_AudioCodecContext;
public:
    ori_amr_wb();
    void amr2pcm(JNIEnv * env, jobject javaObj, jbyteArray m_jbyteArray, jint byteSize);
    ~ori_amr_wb();
};


#endif //ORIGAMI_ORI_AMR_WB_H
