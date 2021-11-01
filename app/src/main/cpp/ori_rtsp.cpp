#include <stdint.h>
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
#include <thread>
//C++ 线程同步 等待唤醒
#include <condition_variable>
#ifdef __cplusplus
extern "C"
{
#endif
#include "include/libswscale/swscale.h"
#include "include/libavformat/avformat.h"
#include "include/libavutil/imgutils.h"
#ifdef __cplusplus
}
#endif


#define TAG "JNI-ori_rtsp" // 这个是自定义的LOG的标识
#define LOG_D(...) __android_log_print(ANDROID_LOG_EEBUG, TAG ,__VA_ARGS__)
#define LOG_I(...) __android_log_print(ANDROID_LOG_INFO,TAG ,__VA_ARGS__)
#define LOG_W(...) __android_log_print(ANDROID_LOG_WARN,TAG ,__VA_ARGS__)
#define LOG_E(...) __android_log_print(ANDROID_LOG_ERROR,TAG ,__VA_ARGS__)
#define LOG_F(...) __android_log_print(ANDROID_LOG_FATAL,TAG ,__VA_ARGS__)

class VideoDecode;
std::string jString2str(JNIEnv& env, jstring j_str);
void getVideoDecode(VideoDecode** m_video, JNIEnv& env, jobject& thiz);
class VideoDecode{
    private:
        //native_window
        ANativeWindow* m_NativeWindow = nullptr;
        AVFormatContext* m_AVFormatContext;
        AVCodec* m_AVCodec;
        //解码器context
        AVCodecContext* m_AVCodecContext;
        AVPacket* m_Packet;
        //Rgb帧
        AVFrame* m_RGBAFrame;
        //原始帧
        AVFrame* m_Frame;
        //yuv2rgb 转换context
        SwsContext* m_SwsContext;

        uint8_t* m_FrameBuffer;

        int32_t m_VideoWidth, m_VideoHeight;//视频原始大小
        int32_t m_RenderWidth, m_RenderHeight;//外部实际控件大小

        int32_t m_targetWidth, m_targetHeight;//目标转换大小
        int32_t m_offsetWidth = 0, m_offsetHeight = 0;//x, y偏移
        void yuv2rgbFrame_init();
        void initTargetWH();
    public:
        bool stop = false;
        ~VideoDecode(){ release(); }
        void bindNativeWindow(JNIEnv& env, jobject& surface);
        void codeVideo(const std::string& m_Url);
        void release();
};
//java层用来保存C++对象地址的指针成员变量
jfieldID objAtJava_ptr;

extern "C" {
    //jni 的第一个加载函数
    JNIEXPORT jint JNICALL
    JNI_OnLoad(JavaVM* vm, void* reserved){
        LOG_E("JNI_OnLoad 执行了");
        if (vm == nullptr){
            return JNI_ERR;
        }

        JNIEnv *env;
        int32_t jni_version = JNI_ERR;
        if(vm->GetEnv((void**)&env,JNI_VERSION_1_6) == JNI_OK){
            jni_version = JNI_VERSION_1_6;
        }else if(vm->GetEnv((void**)&env,JNI_VERSION_1_4) == JNI_OK){
            jni_version = JNI_VERSION_1_4;
        }else if(vm->GetEnv((void**)&env,JNI_VERSION_1_2) == JNI_OK){
            jni_version = JNI_VERSION_1_2;
        }else if(vm->GetEnv((void**)&env,JNI_VERSION_1_1) == JNI_OK){
            jni_version = JNI_VERSION_1_1;
        }
        LOG_E("当前JNI版本：%d", jni_version);

//        myClass = (env)->FindClass("com/safone/hostregister/jni/QrNative");
//        global_class = (jclass)env->NewGlobalRef(myClass);
//        mid_method_w = (env)->GetStaticMethodID(global_class,"sendSerialPortData", "([BI)V");
        jclass jClazz = env->FindClass("com/ori/origami/NativeRtspPlay");
        objAtJava_ptr = env->GetFieldID(jClazz, "native_obj_ptr", "J");
        return jni_version;
    }

    JNIEXPORT void JNICALL
    Java_com_ori_origami_NativeRtspPlay_setUrl(JNIEnv* env, jobject thiz, jstring rtsp_url) {
        LOG_E("Java_com_ori_origami_NativeRtspPlay_setUrl");
        VideoDecode* m_video;
        getVideoDecode(&m_video, *env, thiz);
        if(!m_video){
            LOG_E("VideoDecode is null, no Java_com_ori_origami_NativeRtspPlay_setNativeWindow init");
        }else{
            m_video->codeVideo(jString2str(*env, rtsp_url));
        }
//        setUrl(*env, thiz, rtsp_url);
    }

    JNIEXPORT void JNICALL
    Java_com_ori_origami_NativeRtspPlay_setNativeWindow(JNIEnv *env, jobject thiz, jobject surface) {
        LOG_E("Java_com_ori_origami_NativeRtspPlay_setNativeWindow");
        if(objAtJava_ptr){
            auto* c_video = new VideoDecode();
            env->SetLongField(thiz, objAtJava_ptr, reinterpret_cast<jlong>(c_video));
            c_video->bindNativeWindow(*env, surface);
        } else{
            LOG_E("ERROR, objAtJava_ptr is null( at java param native_obj_ptr) ");
        }
    }

    JNIEXPORT void JNICALL
    Java_com_ori_origami_NativeRtspPlay_release(JNIEnv *env, jobject thiz) {
        LOG_E("Java_com_ori_origami_NativeRtspPlay_release");
        VideoDecode* m_video;
        getVideoDecode(&m_video, *env, thiz);
        delete m_video;
    }

    JNIEXPORT void JNICALL
    Java_com_ori_origami_NativeRtspPlay_play(JNIEnv *env, jobject thiz){
        VideoDecode* m_video;
        getVideoDecode(&m_video, *env, thiz);
        m_video->stop = true;
    }

    JNIEXPORT void JNICALL
    Java_com_ori_origami_NativeRtspPlay_stop(JNIEnv *env, jobject thiz){
        VideoDecode* m_video;
        getVideoDecode(&m_video, *env, thiz);
        m_video->stop = false;
    }

    JNIEXPORT jboolean JNICALL
    Java_com_ori_origami_NativeRtspPlay_isPlay(JNIEnv *env, jobject thiz){
        VideoDecode* m_video;
        getVideoDecode(&m_video, *env, thiz);
        return m_video->stop;
    }
}

void getVideoDecode(VideoDecode** m_video, JNIEnv& env, jobject& thiz){
    if(objAtJava_ptr){
        *m_video = reinterpret_cast<VideoDecode*>(env.GetIntField(thiz, objAtJava_ptr));
    }
}

void VideoDecode::bindNativeWindow(JNIEnv& env, jobject& surface) {
    //获取surfaceView的window
    m_NativeWindow = ANativeWindow_fromSurface(&env, surface);
    m_RenderWidth = ANativeWindow_getWidth(m_NativeWindow);
    m_RenderHeight = ANativeWindow_getHeight(m_NativeWindow);
    LOG_E("width: %d", m_RenderWidth);
    LOG_E("height: %d", m_RenderHeight);
    //2. 设置渲染区域和输入格式
    ANativeWindow_setBuffersGeometry(m_NativeWindow, m_RenderWidth, m_RenderHeight, WINDOW_FORMAT_RGBA_8888);
}

void VideoDecode::codeVideo(const std::string & m_Url){

//    avformat_network_init();//初始化网络模块

    //1.创建封装格式上下文
    m_AVFormatContext = avformat_alloc_context();

    //2.打开输入文件，解协议封装
    if(avformat_open_input(&m_AVFormatContext, m_Url.c_str(), NULL, NULL) != 0){
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
    m_AVCodec = avcodec_find_decoder(codecParameters->codec_id);
    if(m_AVCodec == nullptr) {
        LOG_E("DecoderBase::InitFFDecoder avcodec_find_decoder fail.");
        return;
    }

    //7.创建解码器上下文
    m_AVCodecContext = avcodec_alloc_context3(m_AVCodec);
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
    m_Packet = av_packet_alloc(); //创建 AVPacket 存放编码数据
    m_Frame = av_frame_alloc(); //创建 AVFrame 存放解码后的数据

    yuv2rgbFrame_init();
    LOG_E("开始解码...");
    //10.解码循环
    while (av_read_frame(m_AVFormatContext, m_Packet) >= 0) {//读取帧
        while (stop){ }
        if (m_Packet->stream_index == videoStreamIndex) {
            if (avcodec_send_packet(m_AVCodecContext, m_Packet) != 0) { //视频解码
                return;
            }
            LOG_I("---------------------单帧解码-----------------------");
            while (avcodec_receive_frame(m_AVCodecContext, m_Frame) == 0) {
                //获取到 m_Frame 解码数据，在这里进行格式转换，然后进行渲染
                //格式转换yuv -> rgb
                sws_scale(m_SwsContext, m_Frame->data, m_Frame->linesize, 0, m_VideoHeight, m_RGBAFrame->data, m_RGBAFrame->linesize);

                //3. 渲染
                ANativeWindow_Buffer m_NativeWindowBuffer;
                //锁定当前 Window ，获取屏幕缓冲区 Buffer 的指针
                ANativeWindow_lock(m_NativeWindow, &m_NativeWindowBuffer, nullptr);
                auto *dstBuffer = static_cast<uint8_t *>(m_NativeWindowBuffer.bits);

                int srcLineSize = m_RGBAFrame->linesize[0];//输入图的步长（一行像素有多少字节）
                int dstLineSize = m_NativeWindowBuffer.stride * 4;//RGBA 缓冲区步长
                LOG_I("srcLineSize: %d", srcLineSize);
                LOG_I("dstLineSize: %d", dstLineSize);
                for (int i = m_offsetHeight; i < m_targetHeight; ++i) {
                    //一行一行地拷贝图像数据
                    memcpy(dstBuffer + i * dstLineSize + m_offsetWidth * 4, m_FrameBuffer + i * srcLineSize, srcLineSize);
                }
                //解锁当前 Window ，渲染缓冲区数据
                ANativeWindow_unlockAndPost(m_NativeWindow);
            }
        }
        av_packet_unref(m_Packet); //释放 m_Packet 引用，防止内存泄漏
    }

    //释放资源，解码完成
//    avformat_network_deinit();

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

void VideoDecode::yuv2rgbFrame_init(){
    //1. 分配存储 RGB 图像的 buffer
    m_VideoWidth = m_AVCodecContext->width;
    m_VideoHeight = m_AVCodecContext->height;
    initTargetWH();
    LOG_W("m_VideoWidth: %d", m_VideoWidth);
    LOG_W("m_VideoHeight: %d", m_VideoHeight);
    LOG_W("m_targetWidth: %d", m_targetWidth);
    LOG_W("m_targetHeight: %d", m_targetHeight);
    LOG_W("m_offsetWidth: %d", m_offsetWidth);
    LOG_W("m_offsetHeight: %d", m_offsetHeight);
    m_RGBAFrame = av_frame_alloc();
    //计算 Buffer 的大小
    int bufferSize = av_image_get_buffer_size(AV_PIX_FMT_RGBA, m_targetWidth, m_targetHeight, 1);
    //为 m_RGBAFrame 分配空间
    m_FrameBuffer = (uint8_t *) av_malloc(bufferSize * sizeof(uint8_t));
    av_image_fill_arrays(m_RGBAFrame->data, m_RGBAFrame->linesize, m_FrameBuffer, AV_PIX_FMT_RGBA,
                         m_targetWidth, m_targetHeight, 1);
    LOG_E("m_RGBAFrame->linesize: %d", m_RGBAFrame->linesize[0]);
    //2. 获取转换的上下文
    m_SwsContext = sws_getContext(m_VideoWidth, m_VideoHeight, m_AVCodecContext->pix_fmt,
                                  m_targetWidth, m_targetHeight, AV_PIX_FMT_RGBA,
                                  SWS_FAST_BILINEAR, NULL, NULL, NULL);
}

void VideoDecode::release() {
//    avformat_network_deinit();

    if(m_RGBAFrame != nullptr) {
        av_frame_free(&m_RGBAFrame);
        m_RGBAFrame = nullptr;
    }

    if(m_FrameBuffer != nullptr) {
        free(m_FrameBuffer);
        m_FrameBuffer = nullptr;
    }

    if(m_SwsContext != nullptr) {
        sws_freeContext(m_SwsContext);
        m_SwsContext = nullptr;
    }

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

    //todo??释放了java层的surface也释放了??
    if(m_NativeWindow)
        ANativeWindow_release(m_NativeWindow);
}

void VideoDecode::initTargetWH(){
    float_t sc_w = (float_t) m_RenderWidth / (float_t) m_VideoWidth;
    float_t sc_h = (float_t) m_RenderHeight / (float_t) m_VideoHeight;
    if(sc_w <= sc_h){
        m_targetWidth = m_RenderWidth;
        m_targetHeight = (int32_t) (m_VideoHeight * sc_w);
        m_offsetWidth = 0;
        m_offsetHeight = (m_RenderHeight - m_targetHeight) / 2;
    } else{
        m_targetWidth = (int32_t) (m_VideoWidth * sc_h);
        m_targetHeight = m_RenderHeight;
        m_offsetWidth = (m_RenderWidth - m_targetWidth) / 2;
        m_offsetHeight = 0;
    }
}

std::string jString2str(JNIEnv& env, jstring j_str){
    char* rtn = nullptr;
    jclass clsstring = env.FindClass("java/lang/String");
    jstring strencode = env.NewStringUTF("GB2312");
    jmethodID mid = env.GetMethodID(clsstring,   "getBytes",   "(Ljava/lang/String;)[B");
    jbyteArray barr= (jbyteArray) env.CallObjectMethod(j_str, mid, strencode);
    jsize alen = env.GetArrayLength(barr);
    jbyte* ba = env.GetByteArrayElements(barr,JNI_FALSE);
    if(alen > 0){
        rtn = (char*) malloc(alen+1);
        memcpy(rtn,ba,alen);
        rtn[alen]=0;
    }
    env.ReleaseByteArrayElements(barr,ba,0);
    std::string stemp(rtn);
    free(rtn);
    return stemp;
}
