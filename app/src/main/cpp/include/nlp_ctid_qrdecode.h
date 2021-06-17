#ifndef NLP_CTID_QRDECODE_H
#define NLP_CTID_QRDECODE_H

/**********************************************************************************
Copyright (C) 2021 NLP
Created by caiqm at 2020.05.17
Version V1.0.0
CTID QRdecode library for third party
**********************************************************************************/

/**
 * @brief 串口写函数函数指针
 * @param[in] *writeData 待写数据
 * @param[in] dataLength 数据长度
**/
typedef void (*uartWriteCallback)(unsigned char *writeData,unsigned int dataLength);

/**
 * @brief 串口读函数函数指针
 * @param[out] *readData 存储串口读取的数据
 * @param[out] dataLength 读取到的数据长度
**/
typedef void (*uartReadCallback)(unsigned char *readData,unsigned int dataLength);

/**
 * @brief 设置本地串口写回调函数
 * @param[in] uartWriteFunc 本地实现的串口写函数指针
**/
void setUartWriteCallback(uartWriteCallback uartWriteFunc);

/**
 * @brief 设置本地串口写回调函数
 * @param[in] uartReadFunc 本地实现的串口读函数指针
**/
void setUartReadCallback(uartReadCallback uartReadFunc);

/**
 * @brief CTID解码(接收返回值对象可设为空，表示不接收返回数据)
 * @param[in] qrData  收到的码图数据
 * @param[in] qrDataLen 码图数据长度
 * @param[out] aRawBase A段base64编码内容
 * @param[out] bRawBase B段base64编码内容
 * @param[out] endTime[] 码图截止有效时间
 * @param[out] bDec[] B段业务数据内容
 * @param[out] basId[] basID，用于在线业务校验的参数
**/
int nlp_ctid_qrdecode(char qrData[],int qrDataLen,char aRawBase[],char bRawBase[],unsigned char endTime[],unsigned char bDec[],unsigned char basId[]);

#endif
