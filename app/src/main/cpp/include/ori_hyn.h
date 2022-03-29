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

#include "ori_audio.h"

class VideoDecode{
private:
public:
    bool * stop;
    //native_window
    ANativeWindow* m_NativeWindow = nullptr;


    AVCodec* m_AVCodec;
    //解码器context
    AVCodecContext* m_AVCodecContext;
    std::queue<AVPacket *> packetQueue;
    std::queue<AVFrame *> frameQueue;
    //Rgb帧
    AVFrame* m_RGBAFrame;
    //原始帧
    //yuv2rgb 转换context
    SwsContext* m_SwsContext;

    uint8_t* m_FrameBuffer;

    int32_t m_VideoWidth, m_VideoHeight;//视频原始大小
    int32_t m_RenderWidth, m_RenderHeight;//外部实际控件大小

    int32_t m_targetWidth, m_targetHeight;//目标转换大小
    int32_t m_offsetWidth = 0, m_offsetHeight = 0;//x, y偏移

    VideoDecode(bool * stopPtr){
        stop = stopPtr;
    }

    void yuv2rgbFrame_init();
    void initTargetWH();
    bool findAndOpenDecoder(AVFormatContext &avFormatContext, uint32_t videoStreamIndex);

    ~VideoDecode(){ release(); }
    void bindNativeWindow(JNIEnv& env, jobject& surface);
    void release();
};

class AudioDecode{
private:
public:
    bool * stop;
    AVCodec * m_AVCodec;
    //解码器context
    AVCodecContext* m_AVCodecContext;

    SwrContext * swr;

    std::queue<AVPacket *> packetQueue;
    std::queue<AVFrame *> frameQueue;

    uint8_t * buffer;

    AudioPlayer* audioPlayer;

    int64_t outSampleRate;
    int out_ch_layout = AV_CH_LAYOUT_STEREO;
    AVSampleFormat out_sample_fmt;
    int32_t out_channel_nb;

    bool findAndOpenDecoder(AVFormatContext &avFormatContext, uint32_t audioStreamIndex);
    AudioDecode(bool * stopPtr){
        stop = stopPtr;
        audioPlayer = new AudioPlayer();
        outSampleRate = 44100;
        out_sample_fmt = AV_SAMPLE_FMT_S16;
        audioPlayer->android_openAudioDevice(outSampleRate, 2);
    }

    ~AudioDecode(){ release(); }
    void release();
};


class OriDecode{
private:
    AVFormatContext* m_AVFormatContext;
public:
    VideoDecode* m_videoDecode;
    AudioDecode* m_audioDecode;
    std::queue<AVPacket *> freePacketQueue;
    std::queue<AVFrame *> freeFrameQueue;
    bool stop = false;

    void get_mAVPacket(AVPacket ** pack);
    AVFrame * get_mAVFrame();

    void loopVDecode();
    void loopADecode();

    OriDecode(){
        m_videoDecode = new VideoDecode(&stop);
        m_audioDecode = new AudioDecode(&stop);
    };

    ~OriDecode(){
        release();
    }

    void decodeUrl(const std::string& m_Url);
    void release();
};

void * loopVideoDecode(void * args);
void * loopAudioDecode(void * args);

void * loopVideoRender(void * args);
void * loopAudioPlay(void * args);

int32_t getBuff2AudioPlay(AudioDecode* audioDecode, std::queue<AVFrame *>& freeFrameQueue);

std::string jString2str(JNIEnv& env, jstring j_str);
void getOriDecode(OriDecode** m_video, JNIEnv& env, jobject& thiz);