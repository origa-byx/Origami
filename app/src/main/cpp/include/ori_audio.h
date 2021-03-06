//
// Created by Administrator on 2022/3/28.
//

/**
 * @by: origami
 * @date: {2022/3/28}
 * @info:
 *
 **/
#ifndef ORIGAMI_ORI_AUDIO_H
#define ORIGAMI_ORI_AUDIO_H

#endif //ORIGAMI_ORI_AUDIO_H

#include <SLES/OpenSLES.h>
#include <SLES/OpenSLES_Android.h>

class AudioPlayer{
private:
    SLint32 currentPlayState = SL_PLAYSTATE_STOPPED;
    bool initOk = false;

    SLObjectItf slObjectItf = nullptr;
    SLEngineItf slEngineItf = nullptr;

    SLObjectItf outputMixObject = nullptr;

    SLObjectItf bqPlayerObject = nullptr;
    SLPlayItf bqPlayerPlay = nullptr;
    SLAndroidSimpleBufferQueueItf bqPlayerBufferQueue = nullptr;

    SLPlaybackRateItf bqPlayerRate = nullptr;
    SLPitchItf bqPlayerVolume = nullptr;

    double time;
    //输出的声道数量
    uint32_t  outChannels;
    //采样率
    uint32_t  sampleRate;
public:

    ~AudioPlayer();

    //init and destroy
    SLresult initPlayer();
    SLresult openPlayerDevice();
    void openSLDestroy();

    SLresult android_setPlayRate(int rateChange);
    double android_getTimestamp() const;

    SLresult startPlay(void (*bqPlayerCallback) (SLAndroidSimpleBufferQueueItf bq, void *context), void* context);
    SLresult setPlayState(SLuint32 state);

    SLresult android_openAudioDevice(uint32_t sample_rate, uint32_t out_channels);

};