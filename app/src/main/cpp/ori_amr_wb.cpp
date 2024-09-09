//
// Created by 15080 on 2024/7/15.
//

#include "ori_amr_wb.h"

/**
 * @by: origami
 * @date: 2024/7/15 16:44
 * @info:
 **/

jfieldID cxxObjPtr;
jmethodID callBackDataMethod;
extern "C" {
//jni 的第一个加载函数
JNIEXPORT jint JNICALL
JNI_OnLoad(JavaVM *vm, void *reserved) {
    LOG_E("JNI_OnLoad");
    if (vm == nullptr) {
        return JNI_ERR;
    }
    JNIEnv *env;
    int32_t jni_version = JNI_ERR;
    if (vm->GetEnv((void **) &env, JNI_VERSION_1_6) == JNI_OK) {
        jni_version = JNI_VERSION_1_6;
    } else if (vm->GetEnv((void **) &env, JNI_VERSION_1_4) == JNI_OK) {
        jni_version = JNI_VERSION_1_4;
    } else if (vm->GetEnv((void **) &env, JNI_VERSION_1_2) == JNI_OK) {
        jni_version = JNI_VERSION_1_2;
    } else if (vm->GetEnv((void **) &env, JNI_VERSION_1_1) == JNI_OK) {
        jni_version = JNI_VERSION_1_1;
    }
    LOG_E("当前JNI版本：%d", jni_version);
    jclass jClazz = env->FindClass("com/ori/origami/jni/OriAmr");
    cxxObjPtr = env->GetFieldID(jClazz, "cxxObj", "J");
    callBackDataMethod = env->GetMethodID(jClazz, "callBack", "([B)V");
    return jni_version;
}

JNIEXPORT void JNICALL
Java_com_ori_origami_jni_OriAmr_init(JNIEnv *env, jobject thiz) {
    auto* cxxObjInstance = new ori_amr_wb();
    env->SetLongField(thiz, cxxObjPtr, reinterpret_cast<jlong>(cxxObjInstance));
}

JNIEXPORT void JNICALL
Java_com_ori_origami_jni_OriAmr_decode(JNIEnv *env, jobject thiz, jbyteArray data) {
    auto obj = reinterpret_cast<ori_amr_wb*>(env->GetLongField(thiz, cxxObjPtr));
    obj->amr2pcm(env, thiz, data, env->GetArrayLength(data));
}

JNIEXPORT void JNICALL
Java_com_ori_origami_jni_OriAmr_realase(JNIEnv *env, jobject thiz) {
    auto obj = reinterpret_cast<ori_amr_wb*>(env->GetLongField(thiz, cxxObjPtr));
    env->SetLongField(thiz, cxxObjPtr, 0L);
    delete obj;
}

}//#end extern "C"


ori_amr_wb::ori_amr_wb() {
    AVCodec* mAVCodec = avcodec_find_decoder(AV_CODEC_ID_AMR_WB);
    if(!mAVCodec){
        LOG_E("解码器查找失败");
    }
    m_AudioCodecContext = avcodec_alloc_context3(mAVCodec);
    if(!m_AudioCodecContext){
        LOG_E("解码器上下文创建失败");
        return;
    }
    m_AudioCodecContext->sample_rate = 16000;
    m_AudioCodecContext->channel_layout = AV_CH_LAYOUT_MONO;
    //音频通道数
    m_AudioCodecContext->channels = 1;
    int ret = avcodec_open2(m_AudioCodecContext, mAVCodec, nullptr);
    LOG_E("avcodec_open2 %d", ret);
    auto out_channel_nb = av_get_channel_layout_nb_channels(m_AudioCodecContext->channel_layout);
    bufferSize = out_channel_nb * m_AudioCodecContext->sample_rate * 2;
//    bufferBytes = static_cast<uint8_t *>(av_malloc(bufferSize));
//    swrContext = swr_alloc();
//    swr_alloc_set_opts(swrContext, (int64_t) m_AudioCodecContext->channel_layout,
//                       AV_SAMPLE_FMT_S16,
//                       m_AudioCodecContext->sample_rate,//输出格式
//                       (int64_t) m_AudioCodecContext->channel_layout,
//                       m_AudioCodecContext->sample_fmt,
//                       m_AudioCodecContext->sample_rate, 0,
//                       nullptr);//输入格式
//    swr_init(swrContext);
}

ori_amr_wb::~ori_amr_wb() {
    if(m_AudioCodecContext){
        avcodec_free_context(&m_AudioCodecContext);
    }
}

void ori_amr_wb::amr2pcm(JNIEnv * env, jobject javaObj, jbyteArray m_jbyteArray, jint byteSize){
//    auto * buffer = static_cast<int8_t*>(env->GetByteArrayElements(m_jbyteArray, nullptr));
    auto * avPacket = av_packet_alloc();
    avPacket->data = reinterpret_cast<uint8_t*>(env->GetByteArrayElements(m_jbyteArray, nullptr));
    avPacket->size = byteSize;
//    auto buf = reinterpret_cast<uint8_t *>(av_malloc(byteSize));
//    env->GetByteArrayRegion(m_jbyteArray, 0, byteSize, reinterpret_cast<jbyte *>(buf));
//    av_packet_from_data(avPacket, buf, byteSize);
    int ret;
    if ((ret = avcodec_send_packet(m_AudioCodecContext, avPacket)) == 0) {
        for(;;){
            AVFrame * m_Frame = av_frame_alloc();
            if ((ret = avcodec_receive_frame(m_AudioCodecContext, m_Frame)) == 0) {
                LOG_E("avcodec_receive_frame %d", ret);
//                swr_convert(swrContext, &bufferBytes, (int) audioDecode->dst_nb_samples,
//                            const_cast<const uint8_t **>(m_Frame->data), m_Frame->nb_samples);
                jbyteArray jbytes = env->NewByteArray(bufferSize);
                env->SetByteArrayRegion(jbytes, 0, m_Frame->linesize[0], (jbyte*) m_Frame->data[0]);
                env->CallVoidMethod(javaObj, callBackDataMethod, jbytes);
                env->DeleteLocalRef(jbytes);
                av_frame_free(&m_Frame);
            } else{
                LOG_E("avcodec_receive_frame %d", ret);
                av_frame_free(&m_Frame);
                break;
            }
        }
    }else{
        LOG_E("avcodec_send_packet %d", ret);
    }
    av_packet_free(&avPacket);
}
