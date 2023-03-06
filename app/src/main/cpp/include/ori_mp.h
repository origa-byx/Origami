//
// Created by Administrator on 2022/12/21.
//

/**
 * @by: origami
 * @date: {2022/12/21}
 * @info:
 *
 **/
#ifndef ORIGAMI_ORI_MP_H
#define ORIGAMI_ORI_MP_H

#include <cstdint>
//
// Created by Administrator on 2021-10-21.
//
#include <jni.h>
#include <string>
#include <android/log.h>
#include <android/native_window.h>
#include <android/native_window_jni.h>
#include <cstdlib>
#include <fstream>
#include <cstdio>
//C++ 线程
#include <pthread.h>
#ifdef __cplusplus
extern "C"
{
#endif
#include "libswscale/swscale.h"
#include "libavformat/avformat.h"
#include "libavutil/imgutils.h"
#include "libswresample/swresample.h"
#include "libavutil/time.h"
#ifdef __cplusplus
}
#endif

#define TAG "JNI-ori_mp" // 这个是自定义的LOG的标识
#define LOG_D(...) __android_log_print(ANDROID_LOG_DEBUG, TAG, __VA_ARGS__)
#define LOG_I(...) __android_log_print(ANDROID_LOG_INFO,TAG ,__VA_ARGS__)
#define LOG_W(...) __android_log_print(ANDROID_LOG_WARN,TAG ,__VA_ARGS__)
#define LOG_E(...) __android_log_print(ANDROID_LOG_ERROR,TAG ,__VA_ARGS__)

class ori_mp {
private:
    AVCodecContext * m_AVCodecContext = nullptr;

public:
    ori_mp(){
        AVCodec * avCodec = avcodec_find_encoder(AVCodecID::AV_CODEC_ID_MPEG4);
        if(!avCodec){
            LOG_E("编码器查找失败");
        }
        AVCodecContext * avCodecContext = avcodec_alloc_context3(avCodec);
        if(!avCodecContext){
            LOG_E("编码器上下文创建失败");
            return;
        }
        avCodecContext->bit_rate = 200000;
        avCodecContext->width = 540;
        avCodecContext->height = 1080;

    }

    void write_data(int8_t * buffer);
    void save_file();

    ~ori_mp(){

    }
};


#endif //ORIGAMI_ORI_MP_H
