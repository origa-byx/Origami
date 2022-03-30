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
#include "include/libswscale/swscale.h"
#include "include/libavformat/avformat.h"
#include "include/libavutil/imgutils.h"
#include "include/libswresample/swresample.h"
#include "include/libavutil/time.h"
#ifdef __cplusplus
}
#endif

#include "include/ori_hyn.h"

#define TAG "JNI-ori" // 这个是自定义的LOG的标识
#define LOG_D(...) __android_log_print(ANDROID_LOG_EEBUG, TAG ,__VA_ARGS__)
#define LOG_I(...) __android_log_print(ANDROID_LOG_INFO,TAG ,__VA_ARGS__)
#define LOG_W(...) __android_log_print(ANDROID_LOG_WARN,TAG ,__VA_ARGS__)
#define LOG_E(...) __android_log_print(ANDROID_LOG_ERROR,TAG ,__VA_ARGS__)
#define LOG_F(...) __android_log_print(ANDROID_LOG_FATAL,TAG ,__VA_ARGS__)

//java层用来保存C++对象地址的指针成员变量
jfieldID objAtJava_ptr;

extern "C" {
    //jni 的第一个加载函数
    JNIEXPORT jint JNICALL
    JNI_OnLoad(JavaVM* vm, void* reserved){
        LOG_E("JNI_OnLoad start");
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
        jclass jClazz = env->FindClass("com/ori/origami/NativeRtspPlay");
        objAtJava_ptr = env->GetFieldID(jClazz, "native_obj_ptr", "J");
        return jni_version;
    }

    JNIEXPORT void JNICALL
    Java_com_ori_origami_NativeRtspPlay_setUrl(JNIEnv* env, jobject thiz, jstring rtsp_url) {
        LOG_E("Java_com_ori_origami_NativeRtspPlay_setUrl");
        OriDecode* m_video;
        getOriDecode(&m_video, *env, thiz);
        if(!m_video){
            LOG_E("VideoDecode is null, no Java_com_ori_origami_NativeRtspPlay_setNativeWindow init");
        }else{
            m_video->decodeUrl(jString2str(*env, rtsp_url));
        }
//        setUrl(*env, thiz, rtsp_url);
    }

    JNIEXPORT void JNICALL
    Java_com_ori_origami_NativeRtspPlay_setNativeWindow(JNIEnv *env, jobject thiz, jobject surface) {
        LOG_E("Java_com_ori_origami_NativeRtspPlay_setNativeWindow");
        if(objAtJava_ptr){
            auto* c_ori = new OriDecode();
            env->SetLongField(thiz, objAtJava_ptr, reinterpret_cast<jlong>(c_ori));
            c_ori->m_videoDecode->bindNativeWindow(*env, surface);
        } else{
            LOG_E("ERROR, objAtJava_ptr is null( at java param native_obj_ptr) ");
        }
    }

    JNIEXPORT void JNICALL
    Java_com_ori_origami_NativeRtspPlay_release(JNIEnv *env, jobject thiz) {
        LOG_E("Java_com_ori_origami_NativeRtspPlay_release");
        OriDecode* m_video;
        getOriDecode(&m_video, *env, thiz);
        delete m_video;
    }

    JNIEXPORT void JNICALL
    Java_com_ori_origami_NativeRtspPlay_play(JNIEnv *env, jobject thiz){
        OriDecode* m_video;
        getOriDecode(&m_video, *env, thiz);
        m_video->stop = false;
    }

    JNIEXPORT void JNICALL
    Java_com_ori_origami_NativeRtspPlay_stop(JNIEnv *env, jobject thiz){
        OriDecode* m_video;
        getOriDecode(&m_video, *env, thiz);
        m_video->stop = true;
    }

    JNIEXPORT jboolean JNICALL
    Java_com_ori_origami_NativeRtspPlay_isPlay(JNIEnv *env, jobject thiz){
        OriDecode* m_video;
        getOriDecode(&m_video, *env, thiz);
        return m_video->stop;
    }
}

void getOriDecode(OriDecode** m_video, JNIEnv& env, jobject& thiz){
    if(objAtJava_ptr){
        *m_video = reinterpret_cast<OriDecode*>(env.GetIntField(thiz, objAtJava_ptr));
    }
}

//-------------------分割线------------------------------------------------------

/**
 * 绑定android SurfaceView
 * @param env
 * @param surface
 */
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

/**
 * 解封装
 * @param m_Url
 */
void OriDecode::decodeUrl(const std::string & m_Url){
//    avformat_network_init();//初始化网络模块，此编译的ffmpeg so库版本无需此操作了

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

    //视频通道
    uint32_t videoStreamIndex = -1;
    //音频通道
    uint32_t audioStreamIndex = -1;
    //4.获取音视频流索引
    for(int i=0; i < m_AVFormatContext->nb_streams; i++) {
        if(audioStreamIndex == -1 && m_AVFormatContext->streams[i]->codecpar->codec_type == AVMEDIA_TYPE_AUDIO){
            audioStreamIndex = i;
            if(videoStreamIndex != -1)
                break;
            continue;
        }
        if(videoStreamIndex == -1 && m_AVFormatContext->streams[i]->codecpar->codec_type == AVMEDIA_TYPE_VIDEO) {
            videoStreamIndex = i;
            if(audioStreamIndex != -1)
                break;
            continue;
        }
    }
    if(videoStreamIndex == -1) {
        LOG_E("DecoderBase::InitFFDecoder Fail to find stream index(videoStreamIndex).");
        return;
    }

    //--------------------------查找视频解码器--------------------------
    if(!m_videoDecode->findAndOpenDecoder(*m_AVFormatContext, videoStreamIndex))
        return;
    else
        loopVDecode();

    //--------------------------查找音频解码器--------------------------
    if(audioStreamIndex != -1){
        if(!m_audioDecode->findAndOpenDecoder(*m_AVFormatContext, audioStreamIndex)){
             LOG_E("DecoderBase::InitFFDecoder Fail to find stream index(audioStreamIndex).");
             return;
        }
        else
             loopADecode();
    }

    LOG_E("开始解码...");
    //解码循环
    while (!stop) {//读取帧
        AVPacket * freeAvPacket;
        get_mAVPacket(&freeAvPacket);
        int ret = av_read_frame(m_AVFormatContext, freeAvPacket);
        if(ret < 0) break;
        if(freeAvPacket->stream_index == videoStreamIndex){
            m_videoDecode->packetQueue->push(freeAvPacket);
        }else if(freeAvPacket->stream_index == audioStreamIndex){
            m_audioDecode->packetQueue->push(freeAvPacket);
        } else {
            av_packet_free(&freeAvPacket);
            LOG_E("...unknown streamIndex");
        }
    }

}

void OriDecode::get_mAVPacket(AVPacket ** pack) const{
    freePacketQueue->popFirstWithDef(pack, av_packet_alloc);
}

void OriDecode::get_mAVFrame(AVFrame ** freeAvFrame) const{
    freeFrameQueue->popFirstWithDef(freeAvFrame, av_frame_alloc);
}

/**
 * 查找并打开视频编码器
 * @param avFormatContext
 * @param videoStreamIndex
 * @return
 */
bool VideoDecode::findAndOpenDecoder(AVFormatContext &avFormatContext, uint32_t videoStreamIndex) {
    AVCodecParameters *codecParameters = avFormatContext.streams[videoStreamIndex]->codecpar;
    //根据 codec_id 获取解码器
    m_AVCodec = avcodec_find_decoder(codecParameters->codec_id);
    if(m_AVCodec == nullptr) {
        LOG_E("DecoderBase::InitFFDecoder avcodec_find_decoder fail.");
        return false;
    }

    //创建解码器上下文
    m_AVCodecContext = avcodec_alloc_context3(m_AVCodec);
    if(avcodec_parameters_to_context(m_AVCodecContext, codecParameters) != 0) {
        LOG_E("DecoderBase::InitFFDecoder avcodec_parameters_to_context fail.");
        return false;
    }

    //打开解码器
    int result = avcodec_open2(m_AVCodecContext, m_AVCodec, nullptr);
    if(result < 0) {
        LOG_E("DecoderBase::InitFFDecoder avcodec_open2 fail. result=%d", result);
        return false;
    }

    //创建存储编码数据和解码数据的结构体
//    m_Packet = av_packet_alloc(); //创建 AVPacket 存放编码数据
//    m_Frame = av_frame_alloc(); //创建 AVFrame 存放解码后的数据
    yuv2rgbFrame_init();
    return true;
}

/**
 * 查找并打开音频编码器
 * @param avFormatContext
 * @param audioStreamIndex
 * @return
 */
bool AudioDecode::findAndOpenDecoder(AVFormatContext &avFormatContext, uint32_t audioStreamIndex){
    AVCodecParameters *codecParameters = avFormatContext.streams[audioStreamIndex]->codecpar;
    //根据 codec_id 获取解码器
    m_AVCodec = avcodec_find_decoder(codecParameters->codec_id);
    if(m_AVCodec == nullptr) {
        LOG_E("DecoderBase::InitFFDecoder avcodec_find_decoder fail.");
        return false;
    }
    //创建解码器上下文
    m_AVCodecContext = avcodec_alloc_context3(m_AVCodec);
    if(avcodec_parameters_to_context(m_AVCodecContext, codecParameters) != 0) {
        LOG_E("DecoderBase::InitFFDecoder avcodec_parameters_to_context fail.");
        return false;
    }
    //打开解码器
    int result = avcodec_open2(m_AVCodecContext, m_AVCodec, NULL);
    if(result < 0) {
        LOG_E("DecoderBase::InitFFDecoder avcodec_open2 fail. result=%d", result);
        return false;
    }
    swr = swr_alloc();
    out_ch_layout = m_AVCodecContext->channel_layout;
    outSampleRate = m_AVCodecContext->sample_rate;
    swr_alloc_set_opts(swr, out_ch_layout,
                             out_sample_fmt,
                             outSampleRate,//输出格式
                             m_AVCodecContext->channel_layout,
                             m_AVCodecContext->sample_fmt,
                             m_AVCodecContext->sample_rate, 0,
                             nullptr);//输入格式
    swr_init(swr);
    out_channel_nb = av_get_channel_layout_nb_channels(out_ch_layout);
    buffer = static_cast<uint8_t *>(av_malloc(out_channel_nb * outSampleRate));
    audioPlayer->android_openAudioDevice(outSampleRate, out_channel_nb);
    return true;
}

void VideoDecode::yuv2rgbFrame_init(){
    //分配存储 RGB 图像的 buffer
    m_VideoWidth = m_AVCodecContext->width;
    m_VideoHeight = m_AVCodecContext->height;
    initTargetWH();
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
                                  SWS_FAST_BILINEAR, nullptr, nullptr, nullptr);
}

void OriDecode::loopVDecode() {
    pthread_t loopV_d, loopRender_d;
    pthread_create(&loopV_d, nullptr, loopVideoDecode, this);
    pthread_create(&loopRender_d, nullptr, loopVideoRender, this);
    pthread_detach(loopV_d);
    pthread_detach(loopRender_d);
}
void OriDecode::loopADecode() {
    pthread_t loopA_d, loopAudio_d;
    pthread_create(&loopA_d, nullptr, loopAudioDecode, this);
    pthread_create(&loopAudio_d, nullptr, loopAudioPlay, this);
    pthread_detach(loopA_d);
    pthread_detach(loopAudio_d);
}

void * loopAudioPlay(void * args){
    auto* oriDecode = reinterpret_cast<OriDecode* >(args);
    oriDecode->m_audioDecode->audioPlayer->startPlay([](SLAndroidSimpleBufferQueueItf pd, void* context){
        auto* audioD = reinterpret_cast<OriDecode*>(context);
        int32_t size = getBuff2AudioPlay(audioD->m_audioDecode, *audioD->freeFrameQueue);
        (*pd)->Enqueue(pd, audioD->m_audioDecode->buffer, size);
    }, oriDecode);
    return nullptr;
}

void * loopVideoRender(void * args){
    auto * oriDecode = reinterpret_cast<OriDecode* >(args);
    auto videoDecode = oriDecode->m_videoDecode;
    while (!oriDecode->stop){
        AVFrame * m_Frame;
        videoDecode->frameQueue->popFirst(&m_Frame);

        sws_scale(videoDecode->m_SwsContext, m_Frame->data,
                  m_Frame->linesize, 0,
                  videoDecode->m_VideoHeight, videoDecode->m_RGBAFrame->data,
                  videoDecode->m_RGBAFrame->linesize);
        //渲染
        ANativeWindow_Buffer m_NativeWindowBuffer;
        //锁定当前 Window ，获取屏幕缓冲区 Buffer 的指针
        ANativeWindow_lock(videoDecode->m_NativeWindow, &m_NativeWindowBuffer, nullptr);
        auto *dstBuffer = static_cast<uint8_t *>(m_NativeWindowBuffer.bits);

        int srcLineSize = videoDecode->m_RGBAFrame->linesize[0];//输入图的步长（一行像素有多少字节）
        int dstLineSize = m_NativeWindowBuffer.stride * 4;//RGBA 缓冲区步长
//                LOG_I("srcLineSize: %d", srcLineSize);
//                LOG_I("dstLineSize: %d", dstLineSize);
        for (int i = videoDecode->m_offsetHeight; i < videoDecode->m_targetHeight; ++i) {
            //一行一行地拷贝图像数据
            memcpy(dstBuffer + i * dstLineSize + videoDecode->m_offsetWidth * 4,
                   videoDecode->m_FrameBuffer + i * srcLineSize, srcLineSize);
        }
        //解锁当前 Window ，渲染缓冲区数据
        ANativeWindow_unlockAndPost(videoDecode->m_NativeWindow);
//        av_frame_unref(m_Frame);
        oriDecode->freeFrameQueue->push(m_Frame);
    }
    return nullptr;
}

int32_t getBuff2AudioPlay(AudioDecode* audioDecode, OriQueue<AVFrame *>& freeFrameQueue){
    while (!*audioDecode->stop){
        AVFrame * m_Frame;
        audioDecode->frameQueue->popFirst(&m_Frame);
        audioDecode->dst_nb_samples = av_rescale_rnd(
                swr_get_delay(audioDecode->swr, m_Frame->sample_rate) + m_Frame->nb_samples,
                audioDecode->outSampleRate,
                m_Frame->sample_rate,
                AV_ROUND_UP);
        swr_convert(audioDecode->swr, &audioDecode->buffer, audioDecode->dst_nb_samples,
                    const_cast<const uint8_t **>(m_Frame->data), m_Frame->nb_samples);

        int out_buffer_size = av_samples_get_buffer_size(nullptr, audioDecode->out_channel_nb,
                                                         m_Frame->nb_samples, audioDecode->out_sample_fmt, 1);
//        av_frame_unref(m_Frame);
        freeFrameQueue.push(m_Frame);
        return out_buffer_size;
    }

    return 0;
}

/**
 * 解码并渲染视频的线程
 * @param videoDecode
 */
void * loopVideoDecode(void * args){
    auto * oriDecode = reinterpret_cast<OriDecode *>(args);
    VideoDecode* videoDecode = oriDecode->m_videoDecode;
    while (!oriDecode->stop) {
        AVPacket* m_Packet;
        videoDecode->packetQueue->popFirst(&m_Packet);
        if (avcodec_send_packet(videoDecode->m_AVCodecContext, m_Packet) == 0) {
            AVFrame * m_Frame;
            oriDecode->get_mAVFrame(&m_Frame);
            if (avcodec_receive_frame(videoDecode->m_AVCodecContext, m_Frame) == 0) {
                videoDecode->frameQueue->push(m_Frame);
            }
        }
//        av_packet_unref(m_Packet);
        oriDecode->freePacketQueue->push(m_Packet);
    }
    return nullptr;
}

/**
 * 解码并播放音频的线程
 * @param audioDecode
 */
void * loopAudioDecode(void * args){
    auto oriDecode = reinterpret_cast<OriDecode *>(args);
    AudioDecode * audioDecode = oriDecode->m_audioDecode;
    while (!oriDecode->stop){
        AVPacket* m_Packet;
        audioDecode->packetQueue->popFirst(&m_Packet);
        if (avcodec_send_packet(audioDecode->m_AVCodecContext, m_Packet) == 0) {
            AVFrame * m_Frame;
            oriDecode->get_mAVFrame(&m_Frame);
            if (avcodec_receive_frame(audioDecode->m_AVCodecContext, m_Frame) == 0) {
                audioDecode->frameQueue->push(m_Frame);
            }
        }
        oriDecode->freePacketQueue->push(m_Packet);
    }
    return nullptr;
}

void OriDecode::stopPlay() {
    stop = true;
}

void OriDecode::release() {
    LOG_E("release");
    stop = true;
    av_usleep(1000000);
    if(m_AVFormatContext){
        avformat_close_input(&m_AVFormatContext);
        avformat_free_context(m_AVFormatContext);
        m_AVFormatContext = nullptr;
    }
}

void VideoDecode::release() {
    LOG_E("release VideoDecode");

    if(m_RGBAFrame) {
        av_frame_free(&m_RGBAFrame);
    }

    if(m_SwsContext) {
        sws_freeContext(m_SwsContext);
        m_SwsContext = nullptr;
    }

    frameQueue->clear([](AVFrame* item){
        av_frame_free(&item);
    });

    packetQueue->clear([](AVPacket* item){
        av_packet_free(&item);
    });

    if(m_AVCodecContext) {
        avcodec_free_context(&m_AVCodecContext);
        m_AVCodecContext = nullptr;
        m_AVCodec = nullptr;
    }

    if(m_NativeWindow)
        ANativeWindow_release(m_NativeWindow);
}

void AudioDecode::release() {
    LOG_E("release AudioDecode");

    if(m_AVCodecContext) {
        avcodec_free_context(&m_AVCodecContext);
        m_AVCodecContext = nullptr;
        m_AVCodec = nullptr;
    }

    if(swr) {
        swr_free(&swr);
        swr = nullptr;
    }

    frameQueue->clear([](AVFrame * item){
        av_frame_free(&item);
    });

    packetQueue->clear([](AVPacket * packet){
        av_packet_free(&packet);
    });

    if(buffer){
        av_free(buffer);
    }

    audioPlayer->openSLDestroy();

}

/**
 * 初始化大小，确保不拉伸的情况下自适应填充播放器界面
 */
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
    auto barr= (jbyteArray) env.CallObjectMethod(j_str, mid, strencode);
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
