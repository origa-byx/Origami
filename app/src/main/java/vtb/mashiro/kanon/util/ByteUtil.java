package vtb.mashiro.kanon.util;

import java.nio.charset.StandardCharsets;

/**
 * @by: origami
 * @date: {2021-09-16}
 * @info:       用于 TCP 报文 的 byte 数组的一般操作
 *
 **/
public class ByteUtil {

    static String nullString = new String(new byte[]{(byte) 0x00}, StandardCharsets.UTF_8);

    public static int signedIntWithNum(int re, int num){
        int flag = re >>> (num * 8 - 1);
        if(flag == 1) {
            re = flag << 31 | re;
            re |= ~0 << (num * 8);
        }
        return re;
    }

    public static long signedLongWithNum(long re, int num){
        long flag = re >>> (num * 8 - 1);
        if(flag == 1) {
            re = flag << 63 | re;
            re |= ~0 << (num * 8);
        }
        return re;
    }

    public static float toFloat_s(byte[] bytes, int from){
        return Float.intBitsToFloat(toInt_s(bytes, from, 4));
    }

    public static float toFloat_b(byte[] bytes, int from){
        return Float.intBitsToFloat(toInt_b(bytes, from, 4));
    }

    public static double toDouble_s(byte[] bytes, int from){
        return Double.longBitsToDouble(toLong_s(bytes, from, 8));
    }

    public static double toDouble_b(byte[] bytes, int from){
        return Double.longBitsToDouble(toLong_b(bytes, from, 8));
    }

    /**
     * 小端
     * bytes 转 int
     * @param bytes     源数组
     * @param from      开始的索引下标
     * @param num       转换几个字节
     * @return  int
     */
    public static int toInt_s(byte[] bytes, int from , int num){
        return toInt_s(bytes, from, num, false);
    }
    public static int toInt_s(byte[] bytes, int from , int num, boolean signed){
        int re = 0;
        for (int i = (num - 1); i >= 0; i--) {
            re = re | (bytes[from + i] & 0xff) << (8 * i);
        }
        if(signed)
            return signedIntWithNum(re, num);
        return re;
    }

    /**
     * 小端
     * bytes 转 long
     * @param bytes     源数组
     * @param from      开始的索引下标
     * @param num       转换几个字节
     * @return  long
     */
    public static long toLong_s(byte[] bytes, int from , int num){
        return toLong_s(bytes, from, num, false);
    }

    public static long toLong_s(byte[] bytes, int from , int num, boolean signed){
        long re = 0;
        for (int i = (num - 1); i >= 0; i--) {
            re = re | ((long) bytes[from + i] & 0xffL) << (8 * i);
        }
        if(signed)
            return signedLongWithNum(re, num);
        return re;
    }

    /**
     * 大端
     * bytes 转 int
     * @param bytes     源数组
     * @param from      开始的索引下标
     * @param num       转换几个字节
     * @return  int
     */
    public static int toInt_b(byte[] bytes, int from, int num){
        return toInt_b(bytes, from, num, false);
    }
    public static int toInt_b(byte[] bytes, int from, int num, boolean signed){
        int re = 0;
        for (int i = 0; i < num; i++) {
            re = re | (bytes[from + i] & 0xff) << (8 * (num - i - 1));
        }
        if(signed)
            return signedIntWithNum(re, num);
        return re;
    }

    /**
     * 大端
     * bytes 转 long
     * @param bytes     源数组
     * @param from      开始的索引下标
     * @param num       转换几个字节
     * @return  long
     */
    public static long toLong_b(byte[] bytes, int from, int num){
        return toLong_b(bytes, from, num, false);
    }
    public static long toLong_b(byte[] bytes, int from, int num, boolean signed){
        long re = 0;
        for (int i = 0; i < num; i++) {
            re = re | ((long) bytes[from + i] & 0xffL) << (8 * (num - i - 1));
        }
        if(signed)
            return signedLongWithNum(re, num);
        return re;
    }

    /**
     * 小端
     * 将num设置进bytes
     * @param target     源数组
     * @param from      开始的索引下标
     * @param num       设置几个字节
     * @param val       设置的值    用 long 是因为8个字节大小兼容所有基本类型
     */
    public static void setNum_s(byte[] target, int from, int num, long val){
        for (int i = (num - 1); i >= 0; i--) {
            target[from + i] = (byte) ((val >>> (8 * i)) & 0xff);
        }
    }

    public static void setNum_s(byte[] target, int from, int num, double fVal){
        long val = Double.doubleToLongBits(fVal);
        setNum_s(target, from, num, val);
    }

    public static void setNum_s(byte[] target, int from, int num, float fVal){
        int val = Float.floatToIntBits(fVal);
        setNum_s(target, from, num, val);
    }

    /**
     * 大端
     * 将num设置进bytes
     * @param target     源数组
     * @param from      开始的索引下标
     * @param num       设置几个字节
     * @param val       设置的值    用 long 是因为8个字节大小兼容所有基本类型
     */
    public static void setNum_b(byte[] target, int from, int num, long val){
        for (int i = 0; i < num; i++) {
            target[from + i] = (byte) ((val >>> (8 * (num - i - 1))) & 0xff);
        }
    }

    /**
     * 将 String 设置进bytes
     * @param target
     * @param from
     * @param num
     * @param text
     */
    public static void setString(byte[] target, int from, int num, String text){
        if(num <= 0) return;
        byte[] devIdBytes = ByteUtil.getBytes(text);
        setByteArray(target, from, num, devIdBytes);
    }

    public static void setByteArray(byte[] target, int from, int num, byte[] src){
        if(num <= 0) return;
        System.arraycopy(src, 0, target, from, Math.min(src.length, num));
    }


    /**
     * 转换为 String 并会去除 0x00的字符
     * @param bytes 源数组
     * @return  string
     */
    public static String toString(byte[] bytes){
        String str = new String(bytes, StandardCharsets.UTF_8);
        int index = str.indexOf(nullString);
        if(index == -1)
            return str;
        return str.substring(0, index);
    }

    public static byte[] getBytes(String str){
        if(TextUtils.isEmpty(str))
            return new byte[0];
        else
            return str.getBytes(StandardCharsets.UTF_8);
    }

    /**
     * 转换为 String 并会去除 0x00的字符
     * @param bytes 源数组
     * @return  string
     */
    public static String toString(byte[] bytes, int from, int length){
        byte[] buffer = new byte[length];
        if(length <= 0)
            return "";
        System.arraycopy(bytes, from, buffer, 0, length);
        return toString(buffer);
    }

    /**
     * char at 单字符拼接
     * @param target
     * @param from
     * @param num
     * @param val
     */
    public static void setCharAtString(byte[] target, int from, int num, String val){
        for (int i = 0; i < num; i++) {
            target[from + i] = (byte) (Integer.parseInt(String.valueOf(val.charAt(i))) & 0xff);
        }
    }

}
