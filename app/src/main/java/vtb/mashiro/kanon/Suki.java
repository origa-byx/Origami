package vtb.mashiro.kanon;

import com.google.gson.Gson;
import vtb.mashiro.kanon.util.ByteUtil;

import static vtb.mashiro.kanon.util.Log.Lazy.log;

/**
 * @by: origami
 * @date: {2022/5/16}
 * @info:
 **/
public class Suki {

    public static double bit2double(long a){
        int flag = a >>> 63 == 0 ? 1 : -1;
        int offset = ((int) (a >>> 52) & 0x7ff) - 1023;
        if(offset >= 0){
            long z = 1 << offset | a << 12 >>> (64 - offset);
            long l = a << (11 + offset) >>> 1;
            long x = 0;
            for (int i = 0; i < 64; i++) {
                x  |= (l >>> (63 - i) & 1) << i;
            }
            System.out.println(x);
            return (z + 1d / (double) x) * flag;
        }else {//TODO
            System.out.println("< 0");
            return 0;
        }
    }

    public static void main(String[] args) throws Exception{
        Gson gson = new Gson();
        Aria aria = Aria.javaBuilder().setPort(3345).addPostEvent(bean -> {
            log.pr("BEAN", bean.getClass().getSimpleName() + gson.toJson(bean));
        }).build();
        aria.init();
    }


}
