package vtb.arisu.mana.bean.report;

import vtb.arisu.mana.annotation.Info;
import vtb.arisu.mana.annotation.Nya;
import vtb.mashiro.kanon.base.Bean;

/**
 * @by: origami
 * @date: {2022/5/23}
 * @info: 此接口用于基站回复客户端的配置应答消息，
 * 非特殊指定，基站统一使用该结构体格式回复客户端，
 * 通过消息头中的消息ID区分应答类型
 **/
@Nya
@Info("基站通用配置-ACK")
public class CfgResult extends Bean {

    /**
     * 配置结果
     * 0:成功
     * >0:错误编号
     */
    @Nya
    public int u32_CfgResult;

}
