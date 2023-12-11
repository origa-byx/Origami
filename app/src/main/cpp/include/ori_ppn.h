//
// Created by Administrator on 2023/7/19.
//
#include <cstdint>
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
#include "ori_queue.h"

#ifndef ORIGAMI_ORI_PPN_H
#define ORIGAMI_ORI_PPN_H


#ifdef __cplusplus
extern "C" {
#endif
#include "libswscale/swscale.h"
#include "libavformat/avformat.h"
#include "libavutil/imgutils.h"
#include "libswresample/swresample.h"
#include "libavutil/time.h"
#ifdef __cplusplus
}
#endif

#define TAG "JNI-ori" // 这个是自定义的LOG的标识
#define LOG_D(...) __android_log_print(ANDROID_LOG_EEBUG, TAG ,__VA_ARGS__)
#define LOG_I(...) __android_log_print(ANDROID_LOG_INFO,TAG ,__VA_ARGS__)
#define LOG_W(...) __android_log_print(ANDROID_LOG_WARN,TAG ,__VA_ARGS__)
#define LOG_E(...) __android_log_print(ANDROID_LOG_ERROR,TAG ,__VA_ARGS__)
#define LOG_F(...) __android_log_print(ANDROID_LOG_FATAL,TAG ,__VA_ARGS__)

class OriPPN{
private:
    std::string mPath;
    bool enableAudio = true;
    AVOutputFormat * m_AvOutputFormat;
    //VIDEO
    AVStream * a_AvStream;
    int32_t w, h;
    //AUDIO
    AVCodecContext * a_AvCodecContext;
    SwsContext* m_SwsContext;
    void testStartIoThread();
public:
    std::mutex mMutex;
    int64_t hasDate = 0;
    AVFrame * m_srcFrame;
    AVFrame * m_dstFrame;
    AVStream * v_AvStream;
    AVCodecContext * v_AvCodecContext;
    int64_t mFrame;
    int64_t offsetTime;
    AVFormatContext * m_AVFormatContext;
    bool m_stop = false;
//    OriQueue<AVPacket *> * packetQueue = nullptr;
    void init(const std::string& path, bool audio);
    void initEnV(int32_t frame, int32_t width, int32_t height, int64_t bitRate);
    void initEnA(int64_t bitRate, int32_t sampleRate, bool stereo);
    void openIO();

    void writeV(uint8_t * date);
    void writeYUV420(uint8_t *y, uint8_t *u, uint8_t *v);
    void writeNV21(uint8_t *y, uint8_t *uv);
    void handlerVBytes(uint8_t * date);
    void stop();
    void release();

};

#endif //ORIGAMI_ORI_PPN_H
