//
// Created by Administrator on 2022/3/25.
//

/**
 * @by: origami
 * @date: {2022/3/25}
 * @info:
 *
 **/
#ifndef ORIGAMI_ORI_RTSP_H
#define ORIGAMI_ORI_RTSP_H

#endif //ORIGAMI_ORI_RTSP_H

#include <SLES/OpenSLES.h>
#include <SLES/OpenSLES_Android.h>
#include <queue>
#include "time.h"

#include "ori_queue.h"
#include "ori_audio.h"

class VideoDecode{
private:
public:
    bool * stop;
    //native_window
    ANativeWindow* m_NativeWindow = nullptr;
    int32_t fps;
    AVCodec* m_AVCodec = nullptr;
    double current_clock = 0;
    AVRational time_base;
    //解码器context
    AVCodecContext * m_AVCodecContext = nullptr;
    OriQueue<AVPacket *> * packetQueue = nullptr;
    OriQueue<AVFrame *> * frameQueue = nullptr;
    //Rgb帧
    AVFrame* m_RGBAFrame = nullptr;
    //原始帧
    //yuv2rgb 转换context
    SwsContext* m_SwsContext = nullptr;

    uint8_t* m_FrameBuffer = nullptr;

    int32_t m_VideoWidth, m_VideoHeight;//视频原始大小
    int32_t m_RenderWidth, m_RenderHeight;//外部实际控件大小

    int32_t m_targetWidth, m_targetHeight;//目标转换大小
    int32_t m_offsetWidth = 0, m_offsetHeight = 0;//x, y偏移

    VideoDecode(bool * stopPtr, int av_max){
        stop = stopPtr;
        packetQueue = new OriQueue<AVPacket *>(stopPtr, av_max);
        frameQueue = new OriQueue<AVFrame *>(stopPtr, av_max);
    }

    void yuv2rgbFrame_init();
    void initTargetWH();
    bool findAndOpenDecoder(AVFormatContext &avFormatContext, uint32_t videoStreamIndex);

    ~VideoDecode(){
        release();
        delete packetQueue;
        delete frameQueue;
    }

    void bindNativeWindow(JNIEnv& env, jobject& surface);
    void release();
};

class AudioDecode{
private:
public:
    bool * stop;
    AVCodec * m_AVCodec = nullptr;
    //解码器context
    AVCodecContext * m_AVCodecContext = nullptr;

    AVRational time_base;
    SwrContext * swr = nullptr;
    double current_clock = 0;
    int64_t pts = 0;
    OriQueue<AVPacket *> * packetQueue = nullptr;
    OriQueue<AVFrame *> * frameQueue = nullptr;

    uint8_t * buffer = nullptr;

    AudioPlayer* audioPlayer = nullptr;

    int64_t dst_nb_samples;
    int64_t outSampleRate;
    int out_ch_layout = AV_CH_LAYOUT_STEREO;
    AVSampleFormat out_sample_fmt;
    int32_t out_channel_nb;

    bool findAndOpenDecoder(AVFormatContext &avFormatContext, uint32_t audioStreamIndex);
    AudioDecode(bool * stopPtr, int av_max){
        stop = stopPtr;
        audioPlayer = new AudioPlayer();
        packetQueue = new OriQueue<AVPacket *>(stopPtr, av_max);
        frameQueue = new OriQueue<AVFrame *>(stopPtr, av_max);
        out_sample_fmt = AV_SAMPLE_FMT_S16;
    }

    ~AudioDecode(){
        release();
        delete audioPlayer;
        delete packetQueue;
        delete frameQueue;
    }

    void release();
};


class OriDecode{
private:
    std::mutex * mMutex = nullptr;

    const int waitIndex = 5;
    int safeRelease = 0;
    void (*releaseCallBack)(OriDecode *);

    AVFormatContext * m_AVFormatContext = nullptr;
    //视频通道
    uint32_t videoStreamIndex = -1;
    //音频通道
    uint32_t audioStreamIndex = -1;
    bool decodeV = false, decodeA = false;
public:
    //0 really 1 play 2 stop
    int status = 0;

    VideoDecode * m_videoDecode = nullptr;
    AudioDecode * m_audioDecode = nullptr;
    bool stop = false;

    void setReleaseCallBack(void (*releaseCallBack)(OriDecode*));

    void canSafeReleaseCall(const std::string&);

    void loopVDecode();
    void loopADecode();

    OriDecode(int32_t av_max){
        mMutex = new std::mutex;
        m_videoDecode = new VideoDecode(&stop, av_max);
        m_audioDecode = new AudioDecode(&stop, av_max);
    };

    ~OriDecode(){
        delete mMutex;
    }

    void decodeUrl(const std::string& m_Url, bool autoPlay);
    void startPlay();
    void stopPlay();
    void release();
};

struct decodeParam{
    OriDecode * oriDecode;
    std::string url;
    bool autoPlay;
};

void * openDecodeUrl(void * args);
void * startPlayAV(void * args);

void * loopVideoDecode(void * args);
void * loopAudioDecode(void * args);

void * loopVideoRender(void * args);
void * loopAudioPlay(void * args);

int32_t getBuff2AudioPlay(OriDecode* oriDecode);

std::string jString2str(JNIEnv& env, jstring j_str);
void getOriDecode(OriDecode** m_video, JNIEnv& env, jobject& thiz);