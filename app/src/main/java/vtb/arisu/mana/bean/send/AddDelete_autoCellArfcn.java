package vtb.arisu.mana.bean.send;

import vtb.arisu.mana.annotation.Info;
import vtb.arisu.mana.annotation.Nya;
import vtb.arisu.mana.annotation.RspType;
import vtb.arisu.mana.annotation.VtbBean;
import vtb.arisu.mana.bean.OFLType;
import vtb.mashiro.kanon.base.Bean;

/**
 * @by: origami
 * @date: {2022/5/24}
 * @info: 小区自配置后台频点添加/删除
 * 是否立即生效：是
 * 重启是否保留配置：是
 * 应答消息（eNB ->LMT）：
 * O_FL_ENB_TO_LMT_SELFCFG_ARFCN_CFG_REQ_ACK(0xF052)
 * 此接口用于配置小区自配置功能的扫频频点列表
 *
 * CfgResult	U32	0:操作成功；
 * 1:失败，添加频点重复；
 * 2:失败，添加频点溢出；
 * 3:失败，删除不存在的频点
 * 4:失败，频点值无效	配置结果
 **/
@Nya(l = 8)
@Info("小区自配置后台频点添加/删除")
@VtbBean(OFLType.O_FL_LMT_TO_ENB_SELFCFG_ARFCN_CFG_REQ)
@RspType(OFLType.O_FL_ENB_TO_LMT_SELFCFG_ARFCN_CFG_REQ_ACK)
public class AddDelete_autoCellArfcn extends Bean {

    /**
     * 0:增加后台频点
     * 1:删除后台频点
     */
    @Nya
    public int u32_Cfgtype;

    /**
     * 频点值
     * 0~65535
     */
    @Nya(f = 4)
    public int u32_EarfcnValue;

}
