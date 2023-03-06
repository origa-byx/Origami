package vtb.arisu.mana.bean.report;

import vtb.arisu.mana.annotation.Info;
import vtb.arisu.mana.annotation.Nya;
import vtb.arisu.mana.annotation.VtbBean;
import vtb.arisu.mana.bean.OFLType;
import vtb.mashiro.kanon.base.Bean;

/**
 * @by: origami
 * @date: {2022/5/31}
 * @info: 基站执行状态实时上报
 * 此消息用于基站实时上报操作流程的执行结果，具体信息参见上报值
 **/
@Nya
@Info("基站执行状态实时上报")
@VtbBean(OFLType.O_FL_ENB_TO_LMT_ENB_STATE_IND)
public class CellStateRpt extends Bean {

    /**
     * @see #toMsg()
     */
    @Nya
    public int u32_CellStateInd;

    public String toMsg(){
        switch (u32_CellStateInd){
            case 0: return "空口同步成功";
            case 1: return "空口同步失败";
            case 2: return "GPS同步成功";
            case 3: return "GPS同步失败";
            case 4: return "扫频成功";
            case 5: return "扫频失败";
            case 6: return "小区激活成功";
            case 7: return "小区激活失败";
            case 8: return "小区去激活";
            case 9: return "空口同步中";
            case 10: return "GPS同步中";
            case 11: return "扫频中";
            case 12: return "小区激活中";
            case 13: return "小区去激活中";
            case 14: return "频偏校准开始";
            case 15: return "频偏校准进行中";
            case 16: return "频偏校准结束";
            case 0xFFFF: return "无效状态";
            default:
                return "未知上报 CellStateInd：" + u32_CellStateInd;
        }
    }

}
