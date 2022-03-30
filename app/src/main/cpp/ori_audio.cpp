//
// Created by Administrator on 2022/3/28.
//

/**
 * @by: origami
 * @date: {2022/3/28}
 * @info:
 *
 **/
#include "include/ori_audio.h"

/**
 * 创建 OpenSLES 引擎
 */
SLresult AudioPlayer::initPlayer(){
    if(slObjectItf && slEngineItf)
        return SL_RESULT_SUCCESS;
    SLresult ret;
    SLEngineOption options[] = {SL_ENGINEOPTION_THREADSAFE, SL_BOOLEAN_TRUE};
    ret = slCreateEngine(&slObjectItf, 0, options,
                         0, nullptr, nullptr);
    if(ret != SL_RESULT_SUCCESS) goto end;

    ret = (*slObjectItf)->Realize(slObjectItf, SL_BOOLEAN_FALSE);
    if(ret != SL_RESULT_SUCCESS) goto end;

    ret = (*slObjectItf)->GetInterface(slObjectItf, SL_IID_ENGINE, &slEngineItf);
    if(ret != SL_RESULT_SUCCESS) goto end;

    end : return ret;
}

/**
 * 打开 OpenSLES 驱动
 */
SLresult AudioPlayer::openPlayerDevice() {
    if(initOk) return SL_RESULT_SUCCESS;
    SLresult ret;
    SLuint32 sr = sampleRate;
    SLuint32 channels = outChannels;

    if(channels){
        SLDataLocator_AndroidSimpleBufferQueue loc_bufq = {
                SL_DATALOCATOR_ANDROIDSIMPLEBUFFERQUEUE, 2
        };
        switch(sr){
            case 8000: sr = SL_SAMPLINGRATE_8; break;
            case 11025: sr = SL_SAMPLINGRATE_11_025; break;
            case 16000: sr = SL_SAMPLINGRATE_16; break;
            case 22050: sr = SL_SAMPLINGRATE_22_05; break;
            case 24000: sr = SL_SAMPLINGRATE_24; break;
            case 32000: sr = SL_SAMPLINGRATE_32; break;
            case 44100: sr = SL_SAMPLINGRATE_44_1; break;
            case 48000: sr = SL_SAMPLINGRATE_48; break;
            case 64000: sr = SL_SAMPLINGRATE_64; break;
            case 88200: sr = SL_SAMPLINGRATE_88_2; break;
            case 96000: sr  = SL_SAMPLINGRATE_96; break;
            case 192000: sr = SL_SAMPLINGRATE_192; break;
            default:
                sr *= 1000;
        }
        const SLInterfaceID ids[] = { SL_IID_VOLUME };
        const SLboolean req[] = { SL_BOOLEAN_FALSE };
        ret = (*slEngineItf)->CreateOutputMix(slEngineItf, &outputMixObject, 1, ids, req);
        if(ret != SL_RESULT_SUCCESS) return ret;
        ret = (*outputMixObject)->Realize(outputMixObject, SL_BOOLEAN_FALSE);
        if(ret != SL_RESULT_SUCCESS) return ret;
        int speakers;
        if(channels > 1)
            speakers = SL_SPEAKER_FRONT_LEFT | SL_SPEAKER_FRONT_RIGHT;
        else
            speakers = SL_SPEAKER_FRONT_CENTER;
        SLDataFormat_PCM  format_pcm = {
                SL_DATAFORMAT_PCM, channels,
                sr, SL_PCMSAMPLEFORMAT_FIXED_16,
                SL_PCMSAMPLEFORMAT_FIXED_16,
                (SLuint32) speakers, SL_BYTEORDER_LITTLEENDIAN
        };
        SLDataSource audioSrc = {&loc_bufq, &format_pcm};

        //configure audio sink
        SLDataLocator_OutputMix loc_outmix = {SL_DATALOCATOR_OUTPUTMIX, outputMixObject};
        SLDataSink  audioSnk = {&loc_outmix, nullptr};

        //create audio player
        const SLInterfaceID ids1[] = {
                SL_IID_ANDROIDSIMPLEBUFFERQUEUE, SL_IID_PLAYBACKRATE, SL_IID_VOLUME
        };
        const SLboolean req1[] = {SL_BOOLEAN_TRUE, SL_BOOLEAN_TRUE, SL_BOOLEAN_TRUE};
        ret = (*slEngineItf)->CreateAudioPlayer(slEngineItf, &bqPlayerObject, &audioSrc,
                                          &audioSnk, 3, ids1, req1);
        if(ret != SL_RESULT_SUCCESS) return ret;

        //realize the player
        ret = (*bqPlayerObject)->Realize(bqPlayerObject, SL_BOOLEAN_FALSE);
        if(ret != SL_RESULT_SUCCESS) return ret;

        //get the play interface
        ret = (*bqPlayerObject)->GetInterface(bqPlayerObject, SL_IID_PLAY, &bqPlayerPlay);
        if(ret != SL_RESULT_SUCCESS) return ret;

        //get the play rate
        ret = (*bqPlayerObject)->GetInterface(bqPlayerObject, SL_IID_VOLUME, &bqPlayerVolume);
        if(ret != SL_RESULT_SUCCESS) return ret;

        //get the buffer queue interface
        ret = (*bqPlayerObject)->GetInterface(bqPlayerObject, SL_IID_ANDROIDSIMPLEBUFFERQUEUE, &bqPlayerBufferQueue);
        if(ret != SL_RESULT_SUCCESS) return ret;

        //register callback on the buffer queue
//        ret = (*bqPlayerBufferQueue)->RegisterCallback(bqPlayerBufferQueue, bqPlayerCallback, this);
//        if(ret != SL_RESULT_SUCCESS) return ret;

//        //set the play's state to playing
//        ret = (*bqPlayerPlay)->SetPlayState(bqPlayerPlay, SL_PLAYSTATE_PLAYING);
    }
    initOk = true;
    return SL_RESULT_SUCCESS;
}

SLresult AudioPlayer::startPlay(slAndroidSimpleBufferQueueCallback bqPlayerCallback, void* context){
    //register callback on the buffer queue
    if(currentPlayState == SL_PLAYSTATE_PLAYING)
        return SL_RESULT_SUCCESS;
    SLresult ret;
    ret = (*bqPlayerBufferQueue)->RegisterCallback(bqPlayerBufferQueue, bqPlayerCallback, context);
    if(ret != SL_RESULT_SUCCESS) return ret;
    ret = setPlayState(SL_PLAYSTATE_PLAYING);
    if(ret != SL_RESULT_SUCCESS) return ret;
    bqPlayerCallback(bqPlayerBufferQueue, context);
    return ret;
}

/**
 * eg. SL_PLAYSTATE_PLAYING
 * @param state 3 : play
 * @return
 */
SLresult AudioPlayer::setPlayState(SLuint32 state) {
    //set the play's state to playing
    SLresult ret = -1;
    if(bqPlayerPlay){
        ret = (*bqPlayerPlay)->SetPlayState(bqPlayerPlay, state);
        if(ret == SL_RESULT_SUCCESS)
            currentPlayState = state;
    }
    return ret;
}


/**
 * destroy
 */
void AudioPlayer::openSLDestroy() {
    //destroy buffer queue audio player object
    if(bqPlayerObject){
        (*bqPlayerObject)->Destroy(bqPlayerObject);
        bqPlayerObject = nullptr;
        bqPlayerPlay = nullptr;
        bqPlayerBufferQueue = nullptr;
    }

    //destroy output mix object
    if(outputMixObject){
        (*outputMixObject)->Destroy(outputMixObject);
        outputMixObject = nullptr;
    }

    //destroy engine object
    if(slObjectItf){
        (*slObjectItf)->Destroy(slObjectItf);
        slObjectItf = nullptr;
        slEngineItf = nullptr;
    }
    initOk = false;
}

double AudioPlayer::android_getTimestamp() const {
    return time;
}

SLresult AudioPlayer::android_setPlayRate(int rateChange) {
    SLmillibel val;
    SLresult ret;
    if(!bqPlayerRate) return -1;
    ret = (*bqPlayerRate)->GetRate(bqPlayerRate, &val);
    if(ret != SL_RESULT_SUCCESS) return ret;
    if(rateChange < 0)
        val -= 100;
    else
        val += 100;
    if(val < 500)
        val = 500;
    if(val > 2000)
        val = 2000;
    ret = (*bqPlayerRate)->SetRate(bqPlayerRate, val);
    return ret;
}

SLresult AudioPlayer::android_openAudioDevice(uint32_t sample_rate, uint32_t out_channels) {
    if(initOk)
        return SL_RESULT_SUCCESS;
    SLresult ret;
    sampleRate = sample_rate;
    outChannels = out_channels;
    if((ret = initPlayer()) != SL_RESULT_SUCCESS
        || (ret = openPlayerDevice()) != SL_RESULT_SUCCESS)
            openSLDestroy();
    return ret;
}

AudioPlayer::~AudioPlayer() {
    openSLDestroy();
}