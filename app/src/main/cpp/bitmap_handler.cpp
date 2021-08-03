//
// Created by Administrator on 2021-06-25.
//
#include <android/bitmap.h>
#include <cstring>
#include <malloc.h>
//#include <pthread.h>


unsigned int max(int i, unsigned int radius);
unsigned int min(unsigned int max, int i);
void gaussianBlur(unsigned char * img, unsigned  int x, unsigned int y, unsigned int w, unsigned int h, unsigned int comp, unsigned int radius);

//线程函数
//void * gauss_thread(void* arg){
//    gaussianBlur((unsigned char*)arg, 0, 0, info.width, info.height, comp, 50);
//}

extern "C" {
    JNIEXPORT jint JNICALL
    Java_com_ori_origami_NativeBitmap_testBitmap(JNIEnv *env, jclass clazz, jobject bitmap) {
        AndroidBitmapInfo info = {0, 0, 0, 0, 0};
        if (AndroidBitmap_getInfo(env, bitmap, &info) != ANDROID_BITMAP_RESULT_SUCCESS) { return -1; }
        void *px;
        if (AndroidBitmap_lockPixels(env, bitmap, &px) !=
            ANDROID_BITMAP_RESULT_SUCCESS) { return -2; }
        int comp = 3;
        if(info.format != ANDROID_BITMAP_FORMAT_RGB_565 && info.format != ANDROID_BITMAP_FORMAT_NONE){
            comp = 4;
        }
//        pthread_t thread_id_bitmap = 1;       // 线程ID
//        int re = pthread_create(&thread_id_bitmap, NULL, gauss_thread, NULL);
//        if(re){ return -3; }
//        pthread_join(thread_id_bitmap, NULL); //同步 -> 参考 wait notify
        gaussianBlur((unsigned char*)px, 0, 0, info.width, info.height, comp, 50);
        AndroidBitmap_unlockPixels(env, bitmap);
        return 0;
    }
}

void gaussianBlur(unsigned char * img, unsigned  int x, unsigned int y, unsigned int w, unsigned int h, unsigned int comp, unsigned int radius)
{
    unsigned int i, j ;
    radius = min(max(1, radius), 248);
    unsigned int kernelSize = 1 + radius * 2;
    unsigned int * kernel = (unsigned int *) malloc (kernelSize* sizeof (unsigned int ));
    memset (kernel, 0, kernelSize* sizeof (unsigned int ));
    unsigned int (*mult)[256] = (unsigned int (*)[256]) malloc (kernelSize * 256 * sizeof (unsigned int ));
    memset (mult, 0, kernelSize * 256 * sizeof (unsigned int ));
    unsigned    int sum = 0;
    for (i = 1; i < radius; i++){
        unsigned int szi = radius - i;
        kernel[radius + i] = kernel[szi] = szi*szi;
        sum += kernel[szi] + kernel[szi];
        for (j = 0; j < 256; j++){
            mult[radius + i][j] = mult[szi][j] = kernel[szi] * j;
        }
    }
    kernel[radius] = radius*radius;
    sum += kernel[radius];
    for (j = 0; j < 256; j++){

        mult[radius][j] = kernel[radius] * j;
    }

    unsigned int   cr, cg, cb;
    unsigned int   xl, yl, yi, ym, riw;
    unsigned int   read, ri, p,   n;
    unsigned    int imgWidth = w;
    unsigned    int imgHeight = h;
    unsigned    int imageSize = imgWidth*imgHeight;
    unsigned char * rgb = (unsigned char *) malloc ( sizeof (unsigned char ) * imageSize * 3);
    unsigned char * r = rgb;
    unsigned char * g = rgb + imageSize;
    unsigned char * b = rgb + imageSize * 2;
    unsigned char * rgb2 = (unsigned char *) malloc ( sizeof (unsigned char ) * imageSize * 3);
    unsigned char * r2 = rgb2;
    unsigned char * g2 = rgb2 + imageSize;
    unsigned char * b2 = rgb2 + imageSize * 2;

    for ( size_t yh = 0; yh < imgHeight; ++yh) {

        for ( size_t xw = 0; xw < imgWidth; ++xw) {
            n = xw + yh* imgWidth;
            p = n*comp;
            r[n] = img[p];
            g[n] = img[p + 1];
            b[n] = img[p + 2];
        }
    }

    x = max(0, x);
    y = max(0, y);
    w = x + w - max(0, (x + w) - imgWidth);
    h = y + h - max(0, (y + h) - imgHeight);
    yi = y*imgWidth;

    for (yl = y; yl < h; yl++){

        for (xl = x; xl < w; xl++){
            cb = cg = cr = sum = 0;
            ri = xl - radius;
            for (i = 0; i < kernelSize; i++){
                read = ri + i;
                if (read >= x && read < w)
                {
                    read += yi;
                    cr += mult[i][r[read]];
                    cg += mult[i][g[read]];
                    cb += mult[i][b[read]];
                    sum += kernel[i];
                }
            }
            ri = yi + xl;
            r2[ri] = cr / sum;
            g2[ri] = cg / sum;
            b2[ri] = cb / sum;
        }
        yi += imgWidth;
    }
    yi = y*imgWidth;

    for (yl = y; yl < h; yl++){
        ym = yl - radius;
        riw = ym*imgWidth;
        for (xl = x; xl < w; xl++){
            cb = cg = cr = sum = 0;
            ri = ym;
            read = xl + riw;
            for (i = 0; i < kernelSize; i++){
                if (ri < h && ri >= y)
                {
                    cr += mult[i][r2[read]];
                    cg += mult[i][g2[read]];
                    cb += mult[i][b2[read]];
                    sum += kernel[i];
                }
                ri++;
                read += imgWidth;
            }
            p = (xl + yi)*comp;
            img[p] = (unsigned char )(cr / sum);
            img[p + 1] = (unsigned char )(cg / sum);
            img[p + 2] = (unsigned char )(cb / sum);
        }
        yi += imgWidth;
    }

    free (rgb);
    free (rgb2);
    free (kernel);
    free (mult);
}

unsigned int min(unsigned int max, int i) {
    return max < i? max : i;
}

unsigned int max(int i, unsigned int radius) {
    return i > radius? i : radius;
}
