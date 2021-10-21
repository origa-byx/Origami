//
// Created by Administrator on 2021-10-21.
//
#include <jni.h>
#include <string>
#include <android/log.h>
#include <cstdlib>
#include <fstream>
#include <cstdio>
#include "include/libavformat/avformat.h"
#include "include/libavutil/avutil.h"
#include "include/libavcodec/avcodec.h"
#include "include/libavutil/frame.h"

#define TAG "JNI-ori_rtsp" // 这个是自定义的LOG的标识
#define LOG_D(...) __android_log_print(ANDROID_LOG_DEBUG, TAG ,__VA_ARGS__)
#define LOG_I(...) __android_log_print(ANDROID_LOG_INFO,TAG ,__VA_ARGS__)
#define LOG_W(...) __android_log_print(ANDROID_LOG_WARN,TAG ,__VA_ARGS__)
#define LOG_E(...) __android_log_print(ANDROID_LOG_ERROR,TAG ,__VA_ARGS__)
#define LOG_F(...) __android_log_print(ANDROID_LOG_FATAL,TAG ,__VA_ARGS__)

std::string jString2str(JNIEnv* env, jstring j_str);
void codeVideo(const std::string & m_Url);

extern "C"
JNIEXPORT void JNICALL
Java_com_ori_origami_NativeRtspPlay_setUrl(JNIEnv *env, jobject thiz, jstring rtsp_url) {
    codeVideo(jString2str(env, rtsp_url));
}

void codeVideo(const std::string & m_Url){

    avformat_network_init();//初始化网络模块

    //1.创建封装格式上下文
    AVFormatContext* m_AVFormatContext = avformat_alloc_context();

    //2.打开输入文件，解协议封装
    if(avformat_open_input(&m_AVFormatContext, m_Url, NULL, NULL) != 0){
        LOG_E("DecoderBase::InitFFDecoder avformat_open_input fail.");
        return;
    }

    //3.获取音视频流信息
    if(avformat_find_stream_info(m_AVFormatContext, NULL) < 0) {
        LOG_E("DecoderBase::InitFFDecoder avformat_find_stream_info fail.");
        return;
    }
    uint32_t videoStreamIndex = -1;
    //4.获取音视频流索引
    for(int i=0; i < m_AVFormatContext->nb_streams; i++) {
        if(m_AVFormatContext->streams[i]->codecpar->codec_type == AVMEDIA_TYPE_VIDEO) {
            videoStreamIndex = i;
            break;
        }
    }
    if(videoStreamIndex == -1) {
        LOG_E("DecoderBase::InitFFDecoder Fail to find stream index.");
        return;
    }
    //--------------------------查找解码器--------------------------
    AVCodecParameters *codecParameters = m_AVFormatContext->streams[videoStreamIndex]->codecpar;
    //6.根据 codec_id 获取解码器
    AVCodec* m_AVCodec = avcodec_find_decoder(codecParameters->codec_id);
    if(m_AVCodec == nullptr) {
        LOG_E("DecoderBase::InitFFDecoder avcodec_find_decoder fail.");
        return;
    }

    //7.创建解码器上下文
    AVCodecContext* m_AVCodecContext = avcodec_alloc_context3(m_AVCodec);
    if(avcodec_parameters_to_context(m_AVCodecContext, codecParameters) != 0) {
        LOG_E("DecoderBase::InitFFDecoder avcodec_parameters_to_context fail.");
        return;
    }

    //8.打开解码器
    int result = avcodec_open2(m_AVCodecContext, m_AVCodec, NULL);
    if(result < 0) {
        LOG_E("DecoderBase::InitFFDecoder avcodec_open2 fail. result=%d", result);
        return;
    }

    //9.创建存储编码数据和解码数据的结构体
    AVPacket* m_Packet = av_packet_alloc(); //创建 AVPacket 存放编码数据
    AVFrame* m_Frame = av_frame_alloc(); //创建 AVFrame 存放解码后的数据
    //10.解码循环
    while (av_read_frame(m_AVFormatContext, m_Packet) >= 0) {//读取帧
        if (m_Packet->stream_index == videoStreamIndex) {
            if (avcodec_send_packet(m_AVCodecContext, m_Packet) != 0) { //视频解码
                return;
            }
            while (avcodec_receive_frame(m_AVCodecContext, m_Frame) == 0) {
                //获取到 m_Frame 解码数据，在这里进行格式转换，然后进行渲染，下一节介绍 ANativeWindow 渲染过程
            }
        }
        av_packet_unref(m_Packet); //释放 m_Packet 引用，防止内存泄漏
    }

    //11.释放资源，解码完成
    avformat_network_deinit();

    if(m_Frame != nullptr) {
        av_frame_free(&m_Frame);
        m_Frame = nullptr;
    }

    if(m_Packet != nullptr) {
        av_packet_free(&m_Packet);
        m_Packet = nullptr;
    }

    if(m_AVCodecContext != nullptr) {
        avcodec_close(m_AVCodecContext);
        avcodec_free_context(&m_AVCodecContext);
        m_AVCodecContext = nullptr;
        m_AVCodec = nullptr;
    }
    avformat_close_input(&m_AVFormatContext);
    avformat_free_context(m_AVFormatContext);
    m_AVFormatContext = nullptr;
}

std::string jString2str(JNIEnv* env, jstring j_str){
    char* rtn = nullptr;
    jclass clsstring = env->FindClass("java/lang/String");
    jstring strencode = env->NewStringUTF("GB2312");
    jmethodID mid = env->GetMethodID(clsstring,   "getBytes",   "(Ljava/lang/String;)[B");
    jbyteArray barr= (jbyteArray) env->CallObjectMethod(j_str, mid, strencode);
    jsize alen = env->GetArrayLength(barr);
    jbyte* ba = env->GetByteArrayElements(barr,JNI_FALSE);
    if(alen > 0){
        rtn = (char*) malloc(alen+1);
        memcpy(rtn,ba,alen);
        rtn[alen]=0;
    }
    env->ReleaseByteArrayElements(barr,ba,0);
    std::string stemp(rtn);
    free(rtn);
    return stemp;
}
