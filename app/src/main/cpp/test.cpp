//
// Created by Administrator on 2021-05-31.
//
#include <jni.h>
#include <string>
#include <stdlib.h>
#include <stdio.h>

#ifdef __cplusplus
extern "C"
{
#endif
#include "nlp_ctid_qrdecode.h"
#ifdef __cplusplus
}
#endif
//#include "nlp_ctid_qrdecode.h"

void readcall(unsigned char *readData,unsigned int dataLength){

}

extern "C"
JNIEXPORT jint JNICALL
Java_com_ori_origami_Test_nativeTest(JNIEnv* env, jclass jcal, jbyteArray qrData,jint size, jbyteArray j_aRawBase, jbyteArray j_bRawBase, jbooleanArray j_endTime, jbooleanArray j_bDec, jbooleanArray j_basId) {

//    char *a = (char *) (env->GetByteArrayElements(qrData, nullptr));
//    char qr_data[size];
//    for(int i = 0; i < size ; i++){
//        qr_data[i] = *(a + i);
//    }
//    char aRawBase[size];
//    char bRawBase[size];
//    unsigned char endTime[30];
//    unsigned char bDec[size];
//    unsigned char basId[30] = "a";
//    setUartReadCallback(readcall);
//    int re = nlp_ctid_qrdecode(qr_data, (int) size, aRawBase, bRawBase, endTime, bDec, basId);
//    env->SetBooleanArrayRegion(j_basId, 0, 30, basId);
    return (jint) 0;
}

using namespace std;
void test(){

}