package vtb.arisu.mana.bean.send;

import vtb.arisu.mana.annotation.Info;
import vtb.arisu.mana.annotation.Nya;
import vtb.arisu.mana.annotation.RspType;
import vtb.arisu.mana.annotation.VtbBean;
import vtb.arisu.mana.bean.OFLType;
import vtb.mashiro.kanon.base.Bean;

/**
 * @by: origami
 * @date: {2022/5/23}
 * @info: 小区激活去激活配置
 * 是否立即生效：是
 * 重启是否保留配置：否
 * 应答消息（eNB ->LMT）：O_FL_ENB_TO_LMT_SET_ADMIN_STATE_ACK(0xF00E)
 * 在基站IDLE状态下，可通过此消息指示基站采用当前小区配置参数激活小区，
 * 如果workAdminState配置为1，TDD基站则不进行同步流程，直接激活小区，
 * 如果workAdminState配置为2；TDD基站先执行同步流程，同步成功后再激活小区，
 * 如果同步失败，基站仍然回到IDLE状态。
 *
 * 在基站激活状态下，通过配置workAdminState为0，
 * 基站则会执行去激活小区的操作，进入IDLE状态。
 **/
@Nya
@Info("小区激活去激活配置")
@VtbBean(OFLType.O_FL_LMT_TO_ENB_SET_ADMIN_STATE_CFG)
@RspType(OFLType.O_FL_ENB_TO_LMT_SET_ADMIN_STATE_ACK)
public class Cfg_cellStatus extends Bean {

    /**
     * 0:去激活小区
     * 1:采用当前配置激活小区，如果是 TDD 不执行同步
     * 2:激活小区&同步，仅 TDD 支持
     */
    @Nya
    public int u32_workAdminState;

}
