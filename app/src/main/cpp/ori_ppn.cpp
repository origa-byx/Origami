//
// Created by Administrator on 2023/7/19.
//

/**
 * @by: origami
 * @date: {2023/7/19}
 * @info:
 *
 **/
#include "include/ori_ppn.h"
std::string jString2str(JNIEnv& env, jstring j_str);

jfieldID objAtJava_ptr;
extern "C" {
//jni 的第一个加载函数
JNIEXPORT jint JNICALL
JNI_OnLoad(JavaVM *vm, void *reserved) {
    LOG_E("JNI_OnLoad start");
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
    jclass jClazz = env->FindClass("com/ori/origami/jni/OriPPN");
    objAtJava_ptr = env->GetFieldID(jClazz, "oriPpn_obj", "J");
    return jni_version;
}

JNIEXPORT void JNICALL
Java_com_ori_origami_jni_OriPPN_init(JNIEnv *env, jobject this_z, jstring path, jint w, jint h) {
    auto* ppn = new OriPPN();
    ppn->init(jString2str(*env, path), false);
    ppn->initEnV(20, w, h, 8000000);
    ppn->openIO();
    env->SetLongField(this_z, objAtJava_ptr, reinterpret_cast<jlong>(ppn));
}

JNIEXPORT void JNICALL
Java_com_ori_origami_jni_OriPPN_write(JNIEnv *env, jobject this_z, jbyteArray dates) {
    auto* ppn = reinterpret_cast<OriPPN*>(env->GetLongField(this_z, objAtJava_ptr));
    ppn->writeV(reinterpret_cast<uint8_t *>(env->GetByteArrayElements(dates, nullptr)));
}

JNIEXPORT void JNICALL
Java_com_ori_origami_jni_OriPPN_writeYUV420(JNIEnv *env, jobject this_z,
                                      jbyteArray dates0, jbyteArray dates1, jbyteArray dates2) {
    auto* ppn = reinterpret_cast<OriPPN*>(env->GetLongField(this_z, objAtJava_ptr));
    ppn->writeYUV420(reinterpret_cast<uint8_t *>(env->GetByteArrayElements(dates0, nullptr)),
                reinterpret_cast<uint8_t *>(env->GetByteArrayElements(dates1, nullptr)),
                reinterpret_cast<uint8_t *>(env->GetByteArrayElements(dates2, nullptr)));
}

JNIEXPORT void JNICALL
Java_com_ori_origami_jni_OriPPN_writeNV21(JNIEnv *env, jobject this_z,
                                            jbyteArray dates0, jbyteArray dates1) {
    auto* ppn = reinterpret_cast<OriPPN*>(env->GetLongField(this_z, objAtJava_ptr));
    ppn->writeNV21(reinterpret_cast<uint8_t *>(env->GetByteArrayElements(dates0, nullptr)),
                     reinterpret_cast<uint8_t *>(env->GetByteArrayElements(dates1, nullptr)));
}

JNIEXPORT void JNICALL
Java_com_ori_origami_jni_OriPPN_end(JNIEnv *env, jobject this_z) {
    auto* ppn = reinterpret_cast<OriPPN*>(env->GetLongField(this_z, objAtJava_ptr));
    ppn->release();
//    delete ppn;
}

}

void OriPPN::init(const std::string& path, bool audio) {
    this->enableAudio = audio;
    this->mPath = path;
    int ret;
    ret = avformat_alloc_output_context2(&m_AVFormatContext, nullptr, nullptr, path.c_str());
    if(ret != 0){
        LOG_E("avformat_alloc_output_context2 failed");
        return;
    }
    m_AvOutputFormat = m_AVFormatContext->oformat;
    m_AvOutputFormat->video_codec = m_AvOutputFormat->video_codec;
    if(this->enableAudio)
        m_AvOutputFormat->audio_codec = m_AVFormatContext->audio_codec->id;
    LOG_E("video_codec: %u audio_codec: %u", m_AvOutputFormat->video_codec, m_AvOutputFormat->audio_codec);
}

void OriPPN::initEnV(int32_t frame, int32_t width, int32_t height, int64_t bitRate) {
    this->w = width;
    this->h = height;
    AVCodec * v_avCodec = avcodec_find_encoder(m_AvOutputFormat->video_codec);
    v_AvCodecContext = avcodec_alloc_context3(v_avCodec);
    v_AvCodecContext->codec_id = m_AvOutputFormat->video_codec;
    v_AvCodecContext->pix_fmt = AV_PIX_FMT_YUV420P;
    v_AvCodecContext->bit_rate = bitRate;
    v_AvCodecContext->width = width;
    v_AvCodecContext->height = height;
    v_AvCodecContext->time_base = {1, frame};
    v_AvCodecContext->framerate = {frame, 1};
    v_AvCodecContext->gop_size = 10;
    v_AvCodecContext->qmax = 51;
    v_AvCodecContext->qmin = 10;
    v_AvCodecContext->max_b_frames = 0;
    v_AvCodecContext->codec_type = AVMEDIA_TYPE_VIDEO;
    if(m_AvOutputFormat->flags & AVFMT_GLOBALHEADER){
        v_AvCodecContext->flags |= AV_CODEC_FLAG_GLOBAL_HEADER;
    }
    this->mFrame = (int64_t) (av_q2d(v_AvCodecContext->time_base) * 1000000);
    LOG_E("V:mFrame:av_q2d-> %ld", this->mFrame);
    this->offsetTime = -this->mFrame;
    int ret = avcodec_open2(v_AvCodecContext, v_avCodec, nullptr);
    if(ret != 0){
        LOG_E("V:avcodec_open2-> %d , %s", ret, av_err2str(ret));
        return;
    }
    v_AvStream = avformat_new_stream(m_AVFormatContext, v_avCodec);
    if(v_AvStream == nullptr){
        LOG_E("V:avformat_new_stream-> %d , %s", ret, av_err2str(ret));
        return;
    }
    ret = avcodec_parameters_from_context(v_AvStream->codecpar, v_AvCodecContext);
    if(ret != 0){
        LOG_E("V:avcodec_parameters_from_context-> %d , %s", ret, av_err2str(ret));
        return;
    }
    m_SwsContext = sws_getContext(w, h, AV_PIX_FMT_NV21,
                                  w, h, AV_PIX_FMT_YUV420P,
                                  SWS_FAST_BILINEAR, nullptr, nullptr, nullptr);
}

void OriPPN::initEnA(int64_t bitRate, int32_t sampleRate, bool stereo) {
    if(!this->enableAudio) return;
    AVCodec * a_avCodec = avcodec_find_encoder(m_AvOutputFormat->audio_codec);
    a_AvCodecContext = avcodec_alloc_context3(a_avCodec);
    a_AvCodecContext->codec_id = m_AvOutputFormat->audio_codec;
    a_AvCodecContext->sample_fmt = AV_SAMPLE_FMT_S16;
    a_AvCodecContext->bit_rate = bitRate;
    a_AvCodecContext->sample_rate = sampleRate;
    a_AvCodecContext->channel_layout = stereo? AV_CH_LAYOUT_STEREO : AV_CH_FRONT_CENTER;
    a_AvCodecContext->channels = av_get_channel_layout_nb_channels(a_AvCodecContext->channel_layout);
    a_AvCodecContext->flags |= AV_CODEC_FLAG_GLOBAL_HEADER;
    int ret = avcodec_open2(a_AvCodecContext, a_avCodec, nullptr);
    if(ret != 0){
        LOG_E("A:avcodec_open2-> %d , %s", ret, av_err2str(ret));
        return;
    }
    a_AvStream = avformat_new_stream(m_AVFormatContext, a_avCodec);
    if(a_AvStream == nullptr){
        LOG_E("A:avformat_new_stream-> %d , %s", ret, av_err2str(ret));
        return;
    }
    ret = avcodec_parameters_from_context(a_AvStream->codecpar, a_AvCodecContext);
    if(ret != 0){
        LOG_E("A:avcodec_parameters_from_context-> %d , %s", ret, av_err2str(ret));
        return;
    }
}

void OriPPN::openIO() {
    int ret = avio_open(&m_AVFormatContext->pb, this->mPath.c_str(), AVIO_FLAG_WRITE);
    if(ret != 0){
        LOG_E("avio_open-> %d , %s", ret, av_err2str(ret));
        return;
    }
    ret = avformat_write_header(m_AVFormatContext, nullptr);
    if(ret != 0){
        LOG_E("avformat_write_header-> %d , %s", ret, av_err2str(ret));
        return;
    }
//    packetQueue = new OriQueue<AVPacket *>(&m_stop, 100);
    m_srcFrame = av_frame_alloc();
    m_srcFrame->width = w;
    m_srcFrame->height = h;
//    AV_PIX_FMT_YUV420P AV_PIX_FMT_NV21
    m_srcFrame->format = AV_PIX_FMT_NV21;
    av_frame_get_buffer(m_srcFrame, 1);
    LOG_E("m_srcFrame: %d, %d, %d", m_srcFrame->linesize[0], m_srcFrame->linesize[1], m_srcFrame->linesize[2]);
    m_dstFrame = av_frame_alloc();
    m_dstFrame->width = w;
    m_dstFrame->height = h;
    m_dstFrame->format = AV_PIX_FMT_YUV420P;
    av_frame_get_buffer(m_dstFrame, 1);
    LOG_E("m_dstFrame: %d, %d, %d", m_dstFrame->linesize[0], m_dstFrame->linesize[1], m_dstFrame->linesize[2]);
}



void * writeIoV(void * args){
    auto * oriPpn = reinterpret_cast<OriPPN *>(args);
    timeval old_t{0, 0}, current_t{0, 0};
    gettimeofday(&old_t, nullptr);
    int64_t checkRunT = oriPpn->hasDate;
    while (!oriPpn->m_stop && checkRunT == oriPpn->hasDate){
        gettimeofday(&current_t, nullptr);
        int64_t diffTime = (current_t.tv_sec - old_t.tv_sec) * 1000000 + (current_t.tv_usec - old_t.tv_usec);
        int64_t diff = oriPpn->mFrame - diffTime + oriPpn->offsetTime;
        LOG_E("diff: %ld, diffTime：%ld, offsetTime：%ld", diff, diffTime, oriPpn->offsetTime);
        old_t = current_t;
        if(diff > 0){
            av_usleep(diff);
            oriPpn->offsetTime = 0;
        }else {
            oriPpn->offsetTime = diff;
        }
        oriPpn->mMutex.lock();
        int ret = avcodec_send_frame(oriPpn->v_AvCodecContext, oriPpn->m_dstFrame);
        oriPpn->mMutex.unlock();
        if(ret == 0){
            AVPacket* packet = av_packet_alloc();
            //        av_init_packet(&packet);
            ret = avcodec_receive_packet(oriPpn->v_AvCodecContext, packet);
            if(ret == 0){
                av_packet_rescale_ts(packet, oriPpn->v_AvCodecContext->time_base, oriPpn->v_AvStream->time_base);
                av_write_frame(oriPpn->m_AVFormatContext, packet);
            }
            av_packet_free(&packet);
        }else {
            LOG_E("CODE: %d", ret);
        }
    }
    if(checkRunT == oriPpn->hasDate || oriPpn->m_stop) oriPpn->hasDate = 0;
    return nullptr;
}

void OriPPN::stop() {
    m_stop = true;
}

void OriPPN::release() {
    stop();
    while (hasDate != 0){
        av_usleep(50000);
    }
    if(m_AVFormatContext != nullptr){
        av_write_trailer(m_AVFormatContext);
        LOG_E("av_write_trailer");
    }
    if(v_AvCodecContext != nullptr){
        avcodec_close(v_AvCodecContext);
        avcodec_free_context(&v_AvCodecContext);
    }
    if(a_AvCodecContext != nullptr){
        avcodec_close(a_AvCodecContext);
        avcodec_free_context(&a_AvCodecContext);
    }
    if(m_AVFormatContext != nullptr){
        avio_close(m_AVFormatContext->pb);
        avformat_free_context(m_AVFormatContext);
        m_AVFormatContext = nullptr;
    }
    if(m_SwsContext != nullptr){
        sws_freeContext(m_SwsContext);
        m_SwsContext = nullptr;
    }
    if(m_dstFrame != nullptr){
        av_frame_free(&m_dstFrame);
        m_dstFrame = nullptr;
    }
    if(m_srcFrame != nullptr){
        av_frame_free(&m_srcFrame);
        m_srcFrame = nullptr;
    }
    LOG_E("OVER");
}

void OriPPN::handlerVBytes(uint8_t *date) {

}
void OriPPN::testStartIoThread() {
    if(hasDate == 0) {
        pthread_t loopV_E;
        pthread_create(&loopV_E, nullptr, writeIoV, this);
        pthread_detach(loopV_E);
        timeval t_val{0, 0};
        gettimeofday(&t_val, nullptr);
        hasDate = t_val.tv_sec * 1000 + t_val.tv_usec / 1000;
    }
}

void OriPPN::writeNV21(uint8_t *y, uint8_t *uv) {
    mMutex.lock();
    m_srcFrame->data[0] = y;
    m_srcFrame->data[1] = uv;
    m_srcFrame->data[2] = uv;
    sws_scale(m_SwsContext, m_srcFrame->data,
              m_srcFrame->linesize, 0,
              h, m_dstFrame->data,
              m_dstFrame->linesize);
    mMutex.unlock();
    testStartIoThread();
}

void OriPPN::writeYUV420(uint8_t *y, uint8_t *u, uint8_t *v) {
    mMutex.lock();
    m_dstFrame->data[0] = y;
    m_dstFrame->data[1] = u;
    m_dstFrame->data[2] = v;
    mMutex.unlock();
    testStartIoThread();
}

void OriPPN::writeV(uint8_t *date) {
//    handlerVBytes(date);
    av_image_fill_arrays(m_dstFrame->data, m_dstFrame->linesize, date,
                         AV_PIX_FMT_YUV420P, m_srcFrame->width,
                         m_srcFrame->height, 1);
//    int ret = avcodec_send_frame(v_AvCodecContext, m_srcFrame);
//    if(ret == 0){
//        AVPacket* packet = av_packet_alloc();
//        ret = avcodec_receive_packet(v_AvCodecContext, packet);
//        if(ret == 0){
//            av_packet_rescale_ts(packet, v_AvCodecContext->time_base, v_AvStream->time_base);
//            av_write_frame(m_AVFormatContext, packet);
//        }
//        av_packet_free(&packet);
//    }else {
//        LOG_E("CODE: %d", ret);
//    }
    testStartIoThread();
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

