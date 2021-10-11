//
// Created by Administrator on 2021-10-08.
//

#include "include/color_util.h"
class Color {
public:
    static void HSVtoRGB(unsigned char *r, unsigned char *g, unsigned char *b, int h, int s, int v){
        float RGB_min, RGB_max;
        RGB_max = v*2.55f;
        RGB_min = RGB_max*(100 - s)/ 100.0f;

        int i = h / 60;
        int difs = h % 60;

        float RGB_Adj = (RGB_max - RGB_min)*difs / 60.0f;

        switch (i) {
            case 0:
                *r = RGB_max;
                *g = RGB_min + RGB_Adj;
                *b = RGB_min;
                break;
            case 1:
                *r = RGB_max - RGB_Adj;
                *g = RGB_max;
                *b = RGB_min;
                break;
            case 2:
                *r = RGB_min;
                *g = RGB_max;
                *b = RGB_min + RGB_Adj;
                break;
            case 3:
                *r = RGB_min;
                *g = RGB_max - RGB_Adj;
                *b = RGB_max;
                break;
            case 4:
                *r = RGB_min + RGB_Adj;
                *g = RGB_min;
                *b = RGB_max;
                break;
            default:
                *r = RGB_max;
                *g = RGB_min;
                *b = RGB_max - RGB_Adj;
                break;
        }
    }
};