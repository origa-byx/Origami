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
        jclass jClazz = env->FindClass("com/ori/origami/NativeOriPlay");
        objAtJava_ptr = env->GetFieldID(jClazz, "native_obj_ptr", "J");
        return jni_version;
    }

    JNIEXPORT void JNICALL
    Java_com_ori_origami_NativeOriPlay_setUrl(JNIEnv* env, jobject this_z, jstring url) {
        LOG_E("Java_com_ori_origami_NativeRtspPlay_setUrl");
        OriDecode* m_video;
        getOriDecode(&m_video, *env, this_z);
        if(!m_video){
            LOG_E("VideoDecode is null, no Java_com_ori_origami_NativeRtspPlay_setNativeWindow init");
        }else{
            auto * param = new decodeParam{m_video, jString2str(*env, url), true};
            pthread_t startDecodeUrl;
            pthread_create(&startDecodeUrl, nullptr, openDecodeUrl, param);
            pthread_detach(startDecodeUrl);
        }
    }

    JNIEXPORT void JNICALL
    Java_com_ori_origami_NativeOriPlay_setNativeWindow(JNIEnv *env, jobject this_z, jobject surface, jint av_max) {
        LOG_E("Java_com_ori_origami_NativeRtspPlay_setNativeWindow");
        if(objAtJava_ptr){
            auto* c_ori = new OriDecode(av_max);
            env->SetLongField(this_z, objAtJava_ptr, reinterpret_cast<jlong>(c_ori));
            c_ori->m_videoDecode->bindNativeWindow(*env, surface);
        } else{
            LOG_E("ERROR, objAtJava_ptr is null (at java param native_obj_ptr) ");
        }
    }

    JNIEXPORT void JNICALL
    Java_com_ori_origami_NativeOriPlay_release(JNIEnv *env, jobject this_z) {
        LOG_E("Java_com_ori_origami_NativeRtspPlay_release");
        OriDecode* m_video;
        getOriDecode(&m_video, *env, this_z);
        m_video->release();
//        delete m_video;
    }

    JNIEXPORT void JNICALL
    Java_com_ori_origami_NativeOriPlay_play(JNIEnv *env, jobject this_z){
        OriDecode* m_video;
        getOriDecode(&m_video, *env, this_z);
        if(m_video->status == 1)
            return;
        pthread_t startAv;
        pthread_create(&startAv, nullptr, startPlayAV, m_video);
        pthread_detach(startAv);
    }

    JNIEXPORT void JNICALL
    Java_com_ori_origami_NativeOriPlay_stop(JNIEnv *env, jobject this_z){
        OriDecode* m_video;
        getOriDecode(&m_video, *env, this_z);
        m_video->stopPlay();
    }

    JNIEXPORT jboolean JNICALL
    Java_com_ori_origami_NativeOriPlay_isPlay(JNIEnv *env, jobject this_z){
        OriDecode* m_video;
        getOriDecode(&m_video, *env, this_z);
        return m_video->status == 1;
    }
}

void * openDecodeUrl(void * args){
    auto * param = reinterpret_cast<decodeParam *>(args);
    param->oriDecode->decodeUrl(param->url, param->autoPlay);
    return nullptr;
}

void * startPlayAV(void * args){
    auto oriAv = reinterpret_cast<OriDecode *>(args);
    oriAv->startPlay();
    return nullptr;
}

void getOriDecode(OriDecode** m_video, JNIEnv& env, jobject& this_z){
    if(objAtJava_ptr){
        *m_video = reinterpret_cast<OriDecode*>(env.GetLongField(this_z, objAtJava_ptr));
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
void OriDecode::decodeUrl(const std::string & m_Url, bool autoPlay){
//    avformat_network_init();//初始化网络模块，此编译的ffmpeg so库版本无需此操作了

    //创建封装格式上下文
    m_AVFormatContext = avformat_alloc_context();
//    m_AVFormatContext->iformat = av_find_input_format("sdp");
    int ret;
    //打开输入文件，解协议封装
    if((ret = avformat_open_input(&m_AVFormatContext, m_Url.c_str(), nullptr, nullptr)) != 0){
        char * bufferError = new char[100];
        LOG_E("OriDecode::decodeUrl avformat_open_input fail. %d, %d, %s",
              av_strerror(ret, bufferError, 100), ret, bufferError);
        delete[] bufferError;
        return;
    }

    //获取音视频流信息
    if(avformat_find_stream_info(m_AVFormatContext, nullptr) < 0) {
        LOG_E("OriDecode::decodeUrl avformat_find_stream_info fail.");
        return;
    }

    //获取音视频流索引
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
    LOG_E("videoStreamIndex:%d  audioStreamIndex:%d", videoStreamIndex, audioStreamIndex);
    decodeA = false;
    decodeV = false;
    //--------------------------查找视频解码器--------------------------
    if(videoStreamIndex != -1){
        if(!m_videoDecode->findAndOpenDecoder(*m_AVFormatContext, videoStreamIndex)){
            return;
        }
        else decodeV = true;
    } else{
        LOG_E("OriDecode::decodeUrl Fail to find stream index(videoStreamIndex).");
    }

    //--------------------------查找音频解码器--------------------------
    if(audioStreamIndex != -1){
        if(!m_audioDecode->findAndOpenDecoder(*m_AVFormatContext, audioStreamIndex)){
             return;
        }
        else decodeA = true;
    } else {
        LOG_E("OriDecode::decodeUrl Fail to find stream index(audioStreamIndex).");
    }

    if(!decodeA && !decodeV){
        release();
        return;
    }

    status = 0;
    if(autoPlay)
        startPlay();
}

void OriDecode::startPlay() {
    if(status == 1) return;
    safeRelease = 0;
    status = 1;
    waitIndex = 1;
    stop = false;
    releaseCallBack = nullptr;
    LOG_E("解码开始...");
    loopADecode();
    loopVDecode();
    while (!stop) {//读取帧
        AVPacket * freeAvPacket = av_packet_alloc();
//        get_mAVPacket(&freeAvPacket);
        int ret = av_read_frame(m_AVFormatContext, freeAvPacket);
        if(ret < 0) {
            av_packet_free(&freeAvPacket);
            break;
        }
        if(freeAvPacket->stream_index == videoStreamIndex){
            m_videoDecode->packetQueue->push(freeAvPacket);
        } else if(freeAvPacket->stream_index == audioStreamIndex){
            m_audioDecode->packetQueue->push(freeAvPacket);
        } else {
            av_packet_free(&freeAvPacket);
            LOG_E("...unknown streamIndex");
        }
    }
    canSafeReleaseCall("解封装 stop");
}

/**
 * 查找并打开视频编码器
 * @param avFormatContext
 * @param videoStreamIndex
 * @return
 */
bool VideoDecode::findAndOpenDecoder(AVFormatContext &avFormatContext, uint32_t videoStreamIndex) {
    AVStream * avStream = avFormatContext.streams[videoStreamIndex];
    time_base = avStream->time_base;
    fps = avStream->avg_frame_rate.num / avStream->avg_frame_rate.den;
    LOG_E("视频帧数：%u", fps);
    AVCodecParameters *codecParameters = avStream->codecpar;
    //根据 codec_id 获取解码器
    m_AVCodec = avcodec_find_decoder(codecParameters->codec_id);
    if(m_AVCodec == nullptr) {
        LOG_E("VideoDecode::findAndOpenDecoder avcodec_find_decoder fail.");
        return false;
    }

    //创建解码器上下文
    m_AVCodecContext = avcodec_alloc_context3(m_AVCodec);
    if(avcodec_parameters_to_context(m_AVCodecContext, codecParameters) != 0) {
        LOG_E("VideoDecode::findAndOpenDecoder avcodec_parameters_to_context fail.");
        return false;
    }

    //打开解码器
    int result = avcodec_open2(m_AVCodecContext, m_AVCodec, nullptr);
    if(result < 0) {
        LOG_E("VideoDecode::findAndOpenDecoder avcodec_open2 fail. result=%d", result);
        return false;
    }
    LOG_E("V_TIME-Stream: %d  %d", avStream->time_base.den, avStream->time_base.num);
    LOG_E("V_TIME: %d  %d", m_AVCodecContext->time_base.den, m_AVCodecContext->time_base.num);
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
    AVStream * avStream = avFormatContext.streams[audioStreamIndex];
    AVCodecParameters *codecParameters = avStream->codecpar;
    //根据 codec_id 获取解码器
    m_AVCodec = avcodec_find_decoder(codecParameters->codec_id);
    if(m_AVCodec == nullptr) {
        LOG_E("AudioDecode::findAndOpenDecoder avcodec_find_decoder fail.");
        return false;
    }
    //创建解码器上下文
    m_AVCodecContext = avcodec_alloc_context3(m_AVCodec);
    if(avcodec_parameters_to_context(m_AVCodecContext, codecParameters) != 0) {
        LOG_E("AudioDecode::findAndOpenDecoder avcodec_parameters_to_context fail.");
        return false;
    }
    //打开解码器
    int result = avcodec_open2(m_AVCodecContext, m_AVCodec, nullptr);
    if(result < 0) {
        LOG_E("AudioDecode::findAndOpenDecoder avcodec_open2 fail. result=%d", result);
        return false;
    }
    time_base = avStream->time_base;
    LOG_E("A_TIME_Stream: %d  %d", avFormatContext.streams[audioStreamIndex]->time_base.den, avFormatContext.streams[audioStreamIndex]->time_base.num);
    LOG_E("A_TIME: %d  %d", m_AVCodecContext->time_base.den, m_AVCodecContext->time_base.num);
    swr = swr_alloc();
    out_ch_layout = (int32_t) m_AVCodecContext->channel_layout;
    outSampleRate = m_AVCodecContext->sample_rate;
    swr_alloc_set_opts(swr, out_ch_layout,
                             out_sample_fmt,
                       (int32_t) outSampleRate,//输出格式
                       (int32_t) m_AVCodecContext->channel_layout,
                             m_AVCodecContext->sample_fmt,
                             m_AVCodecContext->sample_rate, 0,
                             nullptr);//输入格式
    swr_init(swr);
    out_channel_nb = av_get_channel_layout_nb_channels(out_ch_layout);
    buffer = static_cast<uint8_t *>(av_malloc(out_channel_nb * outSampleRate * 2));
    audioPlayer->android_openAudioDevice(outSampleRate, out_channel_nb);
    return true;
}

void VideoDecode::yuv2rgbFrame_init(){
    //分配存储 RGB 图像的 buffer
    m_VideoWidth = m_AVCodecContext->width;
    m_VideoHeight = m_AVCodecContext->height;
    initTargetWH();
//    LOG_W("m_VideoWidth: %d", m_VideoWidth);
//    LOG_W("m_VideoHeight: %d", m_VideoHeight);
//    LOG_W("m_targetWidth: %d", m_targetWidth);
//    LOG_W("m_targetHeight: %d", m_targetHeight);
//    LOG_W("m_offsetWidth: %d", m_offsetWidth);
//    LOG_W("m_offsetHeight: %d", m_offsetHeight);
    m_RGBAFrame = av_frame_alloc();
    //计算 Buffer 的大小
    int bufferSize = av_image_get_buffer_size(AV_PIX_FMT_RGBA, m_targetWidth, m_targetHeight, 1);
    //为 m_RGBAFrame 分配空间
    m_FrameBuffer = (uint8_t *) av_malloc(bufferSize * sizeof(uint8_t));
    av_image_fill_arrays(m_RGBAFrame->data, m_RGBAFrame->linesize, m_FrameBuffer, AV_PIX_FMT_RGBA,
                         m_targetWidth, m_targetHeight, 1);
    LOG_E("m_RGBAFrame->linesize: %d", m_RGBAFrame->linesize[0]);
    //获取转换的上下文
    m_SwsContext = sws_getContext(m_VideoWidth, m_VideoHeight, m_AVCodecContext->pix_fmt,
                                  m_targetWidth, m_targetHeight, AV_PIX_FMT_RGBA,
                                  SWS_FAST_BILINEAR, nullptr, nullptr, nullptr);
}

void OriDecode::loopVDecode() {
    if(!decodeV) return;
    waitIndex += 2;
    pthread_t loopV_d, loopRender_d;
    pthread_create(&loopV_d, nullptr, loopVideoDecode, this);
    pthread_create(&loopRender_d, nullptr, loopVideoRender, this);
    pthread_detach(loopV_d);
    pthread_detach(loopRender_d);
}
void OriDecode::loopADecode() {
    if(!decodeA) return;
    waitIndex += 2;
    pthread_t loopA_d, loopAudio_d;
    pthread_create(&loopA_d, nullptr, loopAudioDecode, this);
    pthread_create(&loopAudio_d, nullptr, loopAudioPlay, this);
    pthread_detach(loopA_d);
    pthread_detach(loopAudio_d);
}

void * loopAudioPlay(void * args){
    auto* oriDecode = reinterpret_cast<OriDecode* >(args);
    oriDecode->m_audioDecode->audioPlayer->startPlay([](SLAndroidSimpleBufferQueueItf pd, void* context){
//        LOG_E("PLAY AUDIO");
        auto* oriD = reinterpret_cast<OriDecode*>(context);
        int32_t size = getBuff2AudioPlay(oriD);
        if(size == -1){
            oriD->m_audioDecode->audioPlayer->setPlayState(SL_PLAYSTATE_PAUSED);
        } else
            (*pd)->Enqueue(pd, oriD->m_audioDecode->buffer, size);
    }, oriDecode);
    return nullptr;
}

void * loopVideoRender(void * args){
    auto * oriDecode = reinterpret_cast<OriDecode* >(args);
    auto videoDecode = oriDecode->m_videoDecode;
    bool seekCurrentFrame = false;
    LOG_E("渲染视频");
    while (!oriDecode->stop){
        AVFrame * m_Frame;
        if(videoDecode->frameQueue->popFirst(&m_Frame) != 0){
            break;
        }
//        if(true){
//            LOG_E("free render");
//            av_frame_free(&m_Frame);
//            av_usleep(330000);
//            continue;
//        }
        if(seekCurrentFrame){
            seekCurrentFrame = false;
            av_frame_free(&m_Frame);
            continue;
        }
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
        for (int i = 0; i <= videoDecode->m_targetHeight; i++) {
            //一行一行地拷贝图像数据
            memcpy(dstBuffer + (i + videoDecode->m_offsetHeight) * dstLineSize + videoDecode->m_offsetWidth * 4,
                   videoDecode->m_FrameBuffer + i * srcLineSize,
                   srcLineSize);
        }
        //解锁当前 Window ，渲染缓冲区数据
        ANativeWindow_unlockAndPost(videoDecode->m_NativeWindow);
//        av_frame_unref(videoDecode->m_RGBAFrame);
        // m_Frame->pts 是在重编码阶段赋值的，同步需要估计解码的耗时
        //注意此处的time_base来自于AVStream, AVCodecContext的time_base是错误的，分子0好像默认了60帧速率去计算时间
        double current_clock = (double) m_Frame->pts * av_q2d(oriDecode->m_videoDecode->time_base);

        double delay = ((double) m_Frame->repeat_pict) / (2 * oriDecode->m_videoDecode->fps);
        double frameDur = (double) 1 / oriDecode->m_videoDecode->fps;
        double audioClock = oriDecode->m_audioDecode->current_clock;
        double diff = current_clock - audioClock;
        double sleepT = delay + frameDur;

//        LOG_E("delay: %f -> frame: %f -> v: %f -> a: %f -> diff -> %f -> pts: %ld -> ptsA: %ld",
//              delay, frameDur, current_clock,
//              audioClock, diff,
//              m_Frame->pts, oriDecode->m_audioDecode->pts);

        if(diff > 0){//视频快
            if(diff > 1){
                //视频快了一秒时，以0.5倍速慢慢追赶
                av_usleep((uint32_t) (sleepT * 2 * 1000000));
            }else{
                av_usleep((uint32_t) ((sleepT + diff) * 1000000));
            }
        } else{//音频快
            if(diff < -frameDur){
                //音频快了大于一帧时间时，直接跳过下一帧
                seekCurrentFrame = true;
            } else{
                //音频快了小于一帧时间, 等待更少时间
                av_usleep((uint32_t) ((sleepT + diff) * 1000000));
            }
        }
        av_frame_free(&m_Frame);
    }
    oriDecode->canSafeReleaseCall("渲染 stop");
    return nullptr;
}

int32_t getBuff2AudioPlay(OriDecode* oriDecode){
    auto * audioDecode = oriDecode->m_audioDecode;
    while (!*audioDecode->stop){
        AVFrame * m_Frame;
//        LOG_E("尝试获取播放数据");
        if(audioDecode->frameQueue->popFirst(&m_Frame) != 0)
            break;
        //同步当前时间
        audioDecode->current_clock = (double) m_Frame->pts * av_q2d(audioDecode->time_base);

        audioDecode->pts = m_Frame->pts;
        int64_t sample_delay = swr_get_delay(audioDecode->swr, m_Frame->sample_rate);
        audioDecode->dst_nb_samples = av_rescale_rnd(
                sample_delay + m_Frame->nb_samples,
                audioDecode->outSampleRate,
                m_Frame->sample_rate,
                AV_ROUND_UP);

        swr_convert(audioDecode->swr, &audioDecode->buffer, (int) audioDecode->dst_nb_samples,
                    const_cast<const uint8_t **>(m_Frame->data), m_Frame->nb_samples);

//        int out_buffer_size = av_samples_get_buffer_size(nullptr, audioDecode->out_channel_nb,
//                                                         m_Frame->nb_samples, audioDecode->out_sample_fmt, 1);
        int out_buffer_size = av_samples_get_buffer_size(nullptr, audioDecode->out_channel_nb,
                                                         (int32_t) audioDecode->dst_nb_samples, audioDecode->out_sample_fmt, 1);
//        av_frame_free(&m_Frame);
//        LOG_E("sample_delay-> %ld nb_samples-> %d outSampleRate-> %ld", sample_delay, m_Frame->nb_samples, audioDecode->outSampleRate);
//        LOG_E("pts-> %ld dst_nb_samples-> %ld out_buffer_size-> %u", audioDecode->pts, audioDecode->dst_nb_samples, out_buffer_size);
        return out_buffer_size;
    }
    oriDecode->canSafeReleaseCall("音频播放 stop");
    return -1;
}

/**
 * 解码视频线程
 * @param videoDecode
 */
void * loopVideoDecode(void * args){
    auto * oriDecode = reinterpret_cast<OriDecode *>(args);
    VideoDecode* videoDecode = oriDecode->m_videoDecode;
    while (!oriDecode->stop) {
        AVPacket* m_Packet;
        if(videoDecode->packetQueue->popFirst(&m_Packet) != 0)
            break;
        if (avcodec_send_packet(videoDecode->m_AVCodecContext, m_Packet) == 0) {
            for(;;){
                AVFrame * m_Frame = av_frame_alloc();
                if (avcodec_receive_frame(videoDecode->m_AVCodecContext, m_Frame) == 0) {
                    videoDecode->frameQueue->push(m_Frame);
//                    av_frame_free(&m_Frame);
                } else{
                    av_frame_free(&m_Frame);
                    break;
                }
            }
        }
        av_packet_free(&m_Packet);
    }
    oriDecode->canSafeReleaseCall("视频解码 stop");
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
//        LOG_E("解码音频");
        if(audioDecode->packetQueue->popFirst(&m_Packet) != 0)
            break;
        if (avcodec_send_packet(audioDecode->m_AVCodecContext, m_Packet) == 0) {
            for(;;){
                AVFrame * m_Frame = av_frame_alloc();
                if (avcodec_receive_frame(audioDecode->m_AVCodecContext, m_Frame) == 0) {
                    audioDecode->frameQueue->push(m_Frame);
                }else{
                    av_frame_free(&m_Frame);
                    break;
                }
            }
        }
        av_packet_free(&m_Packet);
    }
    oriDecode->canSafeReleaseCall("音频解码 stop");
    return nullptr;
}

void OriDecode::stopPlay() {
    if(stop) return;
    status = 2;
    stop = true;
    m_audioDecode->frameQueue->notifyAll();
    m_audioDecode->packetQueue->notifyAll();
    m_videoDecode->frameQueue->notifyAll();
    m_videoDecode->packetQueue->notifyAll();
}

void OriDecode::release() {
    setReleaseCallBack([](OriDecode * oriDecode){
        LOG_E("release by callback");
        av_usleep(50000);//deng dai 50ms
        delete oriDecode->m_videoDecode;
        delete oriDecode->m_audioDecode;
        if(oriDecode->m_AVFormatContext){
            avformat_close_input(&oriDecode->m_AVFormatContext);
//            avformat_free_context(oriDecode->m_AVFormatContext);
            oriDecode->m_AVFormatContext = nullptr;
        }
        delete oriDecode;
        LOG_E("release finish");
    });
    stopPlay();
}

void OriDecode::canSafeReleaseCall(const std::string& log) {
    std::unique_lock<std::mutex> lock(*mMutex);
    safeRelease++;
    LOG_E("%s -> %d",log.data(), safeRelease);
    if(safeRelease >= waitIndex && releaseCallBack){
        releaseCallBack(this);
        releaseCallBack = nullptr;
    }
}

void OriDecode::setReleaseCallBack(void (*m_releaseCallBack)(OriDecode*)) {
    releaseCallBack = m_releaseCallBack;
    if(safeRelease >= waitIndex){
        releaseCallBack(this);
        releaseCallBack = nullptr;
    }
}

void VideoDecode::release() {
    LOG_E("release VideoDecode");

    frameQueue->clear([](AVFrame* item){
        av_frame_free(&item);
    });

    packetQueue->clear([](AVPacket* packet){
        av_packet_free(&packet);
    });

    if(m_RGBAFrame) {
        av_frame_free(&m_RGBAFrame);
    }

    if(m_SwsContext) {
        sws_freeContext(m_SwsContext);
        m_SwsContext = nullptr;
    }

    if(m_FrameBuffer){
        av_freep(&m_FrameBuffer);
    }

    if(m_AVCodecContext) {
//        avcodec_close(m_AVCodecContext);
        avcodec_free_context(&m_AVCodecContext);
        m_AVCodecContext = nullptr;
        m_AVCodec = nullptr;
    }

    if(m_NativeWindow)
        ANativeWindow_release(m_NativeWindow);
}

void AudioDecode::release() {
    LOG_E("release AudioDecode");

    frameQueue->clear([](AVFrame * item){
        av_frame_free(&item);
    });

    packetQueue->clear([](AVPacket * packet){
        av_packet_free(&packet);
    });

    if(swr) {
        swr_free(&swr);
        swr = nullptr;
    }

    if(buffer){
        av_freep(&buffer);
    }

    if(m_AVCodecContext) {
//        avcodec_close(m_AVCodecContext);
        avcodec_free_context(&m_AVCodecContext);
        m_AVCodecContext = nullptr;
        m_AVCodec = nullptr;
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
    jclass cls_string = env.FindClass("java/lang/String");
    jstring str_encode = env.NewStringUTF("GB2312");
    jmethodID mid = env.GetMethodID(cls_string,   "getBytes",   "(Ljava/lang/String;)[B");
    auto barr= (jbyteArray) env.CallObjectMethod(j_str, mid, str_encode);
    jsize alen = env.GetArrayLength(barr);
    jbyte* ba = env.GetByteArrayElements(barr,JNI_FALSE);
    if(alen > 0){
        rtn = (char*) malloc(alen+1);
        memcpy(rtn,ba,alen);
        rtn[alen]=0;
    }
    env.ReleaseByteArrayElements(barr,ba,0);
    std::string s_temp(rtn);
    free(rtn);
    return s_temp;
}
