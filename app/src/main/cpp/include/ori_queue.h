//
// Created by Administrator on 2022/3/29.
//

/**
 * @by: origami
 * @date: {2022/3/29}
 * @info:
 *
 **/
#ifndef ORIGAMI_ORI_QUEUE_H
#define ORIGAMI_ORI_QUEUE_H

#endif //ORIGAMI_ORI_QUEUE_H

//C++ 线程同步 等待唤醒
#include <queue>
#include <mutex>
#include <condition_variable>

template<typename T>
class OriQueue{
private:
    bool* stopFlag;

    std::mutex mMutex;
    std::condition_variable cVariable;
    int maxSize;
    std::queue<T> mQueue;
public:
    OriQueue(bool * m_stopFlag, int max = 100){
        stopFlag = m_stopFlag;
        maxSize = max;
    }

    void popFirstWithDef(T * out_t, T (*defVal)()){
        std::unique_lock<std::mutex> mLock(mMutex);
        if (mQueue.empty()) {
            *out_t = defVal();
        }else{
            *out_t = mQueue.front();
            mQueue.pop();
        }
        cVariable.notify_all();
    }

    int popFirst(T * out_t){
        std::unique_lock<std::mutex> mLock(mMutex);
        while (mQueue.empty() && !*stopFlag){
            cVariable.wait(mLock);
        }
        if(*stopFlag)
            return -1;
        *out_t = mQueue.front();
        mQueue.pop();
        cVariable.notify_all();
        return 0;
    }

    void push(T in_t){
        std::unique_lock<std::mutex> mLock(mMutex);
        while (mQueue.size() >= maxSize && !*stopFlag){
            cVariable.wait(mLock);
        }
        mQueue.push(in_t);
        cVariable.notify_all();
    }

    void clear(void (*release)(T)){
        std::unique_lock<std::mutex> mLock(mMutex);
        while (!mQueue.empty()){
            T item = mQueue.front();
            mQueue.pop();
            release(item);
        }
    }

    void notifyAll(){
        cVariable.notify_all();
    }

};
