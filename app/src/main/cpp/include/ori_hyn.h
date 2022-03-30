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

#include "ori_queue.h"
#include "ori_audio.h"

class VideoDecode{
private:
public:
    bool * stop;
    //native_window
    ANativeWindow* m_NativeWindow = nullptr;

    AVCodec* m_AVCodec = nullptr;
    //解码器context
    AVCodecContext* m_AVCodecContext = nullptr;
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

    VideoDecode(bool * stopPtr){
        stop = stopPtr;
        packetQueue = new OriQueue<AVPacket *>(50);
        frameQueue = new OriQueue<AVFrame *>(50);
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

    SwrContext * swr = nullptr;

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
    AudioDecode(bool * stopPtr){
        stop = stopPtr;
        audioPlayer = new AudioPlayer();
        packetQueue = new OriQueue<AVPacket *>(50);
        frameQueue = new OriQueue<AVFrame *>(50);
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
    AVFormatContext * m_AVFormatContext = nullptr;
public:
    VideoDecode * m_videoDecode = nullptr;
    AudioDecode * m_audioDecode = nullptr;
    OriQueue<AVPacket *> * freePacketQueue = nullptr;
    OriQueue<AVFrame *> * freeFrameQueue = nullptr;
    bool stop = false;

    void get_mAVPacket(AVPacket ** pack) const;
    void get_mAVFrame(AVFrame ** freeAvFrame) const;

    void loopVDecode();
    void loopADecode();

    OriDecode(){
        m_videoDecode = new VideoDecode(&stop);
        m_audioDecode = new AudioDecode(&stop);
        freePacketQueue = new OriQueue<AVPacket *>(50);
        freeFrameQueue = new OriQueue<AVFrame *>(50);
    };

    ~OriDecode(){
        release();
        delete m_videoDecode;
        delete m_audioDecode;
        delete freePacketQueue;
        delete freeFrameQueue;
    }

    void decodeUrl(const std::string& m_Url);
    void stopPlay();
    void release();
};

void * loopVideoDecode(void * args);
void * loopAudioDecode(void * args);

void * loopVideoRender(void * args);
void * loopAudioPlay(void * args);

int32_t getBuff2AudioPlay(AudioDecode* audioDecode, OriQueue<AVFrame *>& freeFrameQueue);

std::string jString2str(JNIEnv& env, jstring j_str);
void getOriDecode(OriDecode** m_video, JNIEnv& env, jobject& thiz);