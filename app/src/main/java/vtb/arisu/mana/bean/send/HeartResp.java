package vtb.arisu.mana.bean.send;

import vtb.arisu.mana.annotation.Info;
import vtb.arisu.mana.annotation.VtbBean;
import vtb.arisu.mana.bean.OFLType;
import vtb.mashiro.kanon.base.Bean;

/**
 * @by: origami
 * @date: {2022/5/17}
 * @info: 客户端收到基站发出的心跳指示后，
 * 回复应答消息给基站，基站收到此消息，
 * 代表客户端到基站侧的下行链路正常
 **/
@Info("心跳应答")
@VtbBean(OFLType.O_FL_LMT_TO_ENB_SYS_INIT_SUCC_RSP)
public class HeartResp extends Bean {

}
