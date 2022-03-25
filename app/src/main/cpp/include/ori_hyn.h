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

class VideoDecode{
public:
    //native_window
    ANativeWindow* m_NativeWindow = nullptr;


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
    bool findAndOpenDecoder(AVFormatContext &avFormatContext, uint32_t videoStreamIndex);

    ~VideoDecode(){ release(); }
    void bindNativeWindow(JNIEnv& env, jobject& surface);
    void release();
};

class AudioDecode{
public:
    AVCodec* m_AVCodec;
    //解码器context
    AVCodecContext* m_AVCodecContext;
    AVPacket* m_Packet;

    bool findAndOpenDecoder(AVFormatContext &avFormatContext, uint32_t audioStreamIndex);

    ~AudioDecode(){ release(); }
    void release();
};

class OriDecode{
private:
    AVFormatContext* m_AVFormatContext;
public:
    VideoDecode* m_videoDecode;
    AudioDecode* m_audioDecode;
    bool stop = false;

    OriDecode(){
        m_videoDecode = new VideoDecode();
        m_audioDecode = new AudioDecode();
    };

    ~OriDecode(){
        release();
    }

    void decodeUrl(const std::string& m_Url);
    void release();
};

std::string jString2str(JNIEnv& env, jstring j_str);
void getOriDecode(OriDecode** m_video, JNIEnv& env, jobject& thiz);