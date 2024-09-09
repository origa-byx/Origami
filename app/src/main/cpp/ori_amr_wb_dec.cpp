//
// Created by 15080 on 2024/7/16.
//

#include "include/ori_amr_wb_dec.h"

/**
 * @by: origami
 * @date: 2024/7/16 16:04
 * @info:
 **/
jfieldID cxxObjPtr;
jmethodID callBackDataMethod;
extern "C"
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
    jclass jClazz = env->FindClass("com/ori/origami/jni/OriAmrWbDec");
    cxxObjPtr = env->GetFieldID(jClazz, "cxxObj", "J");
    callBackDataMethod = env->GetMethodID(jClazz, "callBack", "([B)V");
    return jni_version;
}

extern "C"
JNIEXPORT void JNICALL
Java_com_ori_origami_jni_OriAmrWbDec_init(JNIEnv *env, jobject thiz) {
    void* ins = D_IF_init();
    env->SetLongField(thiz, cxxObjPtr, reinterpret_cast<jlong>(ins));
}
extern "C"
JNIEXPORT void JNICALL
Java_com_ori_origami_jni_OriAmrWbDec_decode(JNIEnv *env, jobject thiz, jbyteArray data, jint bfi) {
    auto ins = reinterpret_cast<void*>(env->GetLongField(thiz, cxxObjPtr));
    auto datas = reinterpret_cast<unsigned char*>(env->GetByteArrayElements(data, nullptr));
    short buffer[320];
    unsigned char li[640];
    unsigned char* ptr = li;
//    int mode = (datas[0] >> 3) & 0x0f;
//    LOG_E("MODE %d", mode);
    D_IF_decode(ins, datas, buffer, 0);
    for (short i : buffer) {
        *ptr++ = (i >> 0) & 0xff;
        *ptr++ = (i >> 8) & 0xff;
    }
    jbyteArray jbytes = env->NewByteArray(640);
    env->SetByteArrayRegion(jbytes, 0, 640, (jbyte*) ptr);
    env->CallVoidMethod(thiz, callBackDataMethod, jbytes);
    env->DeleteLocalRef(jbytes);
}
extern "C"
JNIEXPORT void JNICALL
Java_com_ori_origami_jni_OriAmrWbDec_exit(JNIEnv *env, jobject thiz) {
    auto ins = reinterpret_cast<void*>(env->GetLongField(thiz, cxxObjPtr));
    D_IF_exit(ins);
}