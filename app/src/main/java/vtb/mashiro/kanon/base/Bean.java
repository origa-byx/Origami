package vtb.mashiro.kanon.base;

import vtb.arisu.mana.annotation.Nya;
import vtb.mashiro.kanon.util.ByteUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * @by: origami
 * @date: {2022/5/16}
 * @info:
 **/
public abstract class Bean {

    public int u32FrameHeader = 0x5555aaaa;
    public int u16MessageId;
    public int u16MsgLength;
    public int u16Frame;
    public int u16SubSysCode;
    public String sn_20u8;

    public void fromPacket(Packet packet){
        this.u32FrameHeader = packet.u32FrameHeader;
        this.u16MessageId = packet.u16MessageId;
        this.u16MsgLength = packet.u16MsgLength;
        this.u16Frame = packet.u16Frame;
        this.u16SubSysCode = packet.u16SubSysCode;
        this.sn_20u8 = packet.sn_20u8;
        fromBytes(packet.body);
    }

    public void fromBytes(byte[] body){
        for (Field field : this.getClass().getDeclaredFields()) {
            if(field.getModifiers() != Modifier.PUBLIC || !field.isAccessible())
                field.setAccessible(true);
            Nya nya = field.getDeclaredAnnotation(Nya.class);
            if(nya == null) continue;
            try {
                if(field.getType() == int.class){
                    field.set(this, ByteUtil.toInt_s(body, nya.f(), nya.l(), nya.signed()));
                }else if(field.getType() == String.class){
                    field.set(this, ByteUtil.toString(body, nya.f(), nya.l()));
                }else if(field.getType() == long.class){
                    field.set(this, ByteUtil.toLong_s(body, nya.f(), nya.l(), nya.signed()));
                }else if(field.getType() == double.class){
                    field.set(this, Double.longBitsToDouble(ByteUtil.toInt_s(body, nya.f(), nya.l())));
                }else if(field.getType() == float.class){
                    field.set(this, Float.intBitsToFloat(ByteUtil.toInt_s(body, nya.f(), nya.l())));
                }
            }catch (IllegalAccessException ignored){ }
        }
    }

    public int getSize(){
        Nya nyaClazz = getClass().getDeclaredAnnotation(Nya.class);
        if(nyaClazz == null) return 0;
        return nyaClazz.l();
    }

    public byte[] toBytes(){
        int size = getSize();
        byte[] body = new byte[size];
        if(size == 0) return body;
        for (Field field : this.getClass().getDeclaredFields()) {
            if(field.getModifiers() != Modifier.PUBLIC || !field.isAccessible())
                field.setAccessible(true);
            Nya nya = field.getDeclaredAnnotation(Nya.class);
            if(nya == null) continue;
            try {
                if(field.getType() == int.class){
                    ByteUtil.setNum_s(body, nya.f(), nya.l(), ((int) field.get(this)));
                }else if(field.getType() == String.class){
                    ByteUtil.setString(body, nya.f(), nya.l(), ((String) field.get(this)));
                }else if(field.getType() == long.class){
                    ByteUtil.setNum_s(body, nya.f(), nya.l(), ((long) field.get(this)));
                }else if(field.getType() == double.class){
                    ByteUtil.setNum_s(body, nya.f(), nya.l(), Double.doubleToLongBits((double) field.get(this)));
                }else if(field.getType() == float.class){
                    ByteUtil.setNum_s(body, nya.f(), nya.l(), Float.floatToIntBits((float) field.get(this)));
                }
            }catch (IllegalAccessException ignored){ }
        }
        return body;
    }

}
