package vtb.mashiro.kanon.util;

import vtb.arisu.mana.bean.report.*;
import vtb.arisu.mana.bean.send.*;

/**
 * @by: origami
 * @date: {2022/6/2}
 * @info:
 **/
public class ClassToAndroid {
    public static final Class<?>[] CLASSESToAndroid = new Class[]{
            Ack_JwAddress.class
            , Ack_QueryArfcnSelfcfg.class
            , Ack_QueryBandPowerDerease.class
            , Ack_QueryCellAutoSet.class
            , Ack_QueryCellStateInfo.class
            , Ack_QueryDwFlowType.class
            , Ack_QueryFddRedirectInfo.class
            , Ack_QueryGps.class
            , Ack_QueryGpsOrBeiDou.class
            , Ack_QueryGpsPpsls.class
            , Ack_QueryImsiBlackList.class
            , Ack_QueryImsiFileCfg.class
            , Ack_QueryLocationUeBlack.class
            , Ack_QueryNtpSyncState.class
            , Ack_QueryRedirectInfo.class
            , Ack_QueryRxPower.class
            , Ack_QueryScanAfrcn.class
            , Ack_QueryScanSyncPort.class
            , Ack_QuerySecondaryPlmns.class
            , Ack_QuerySelectFreqCfg.class
            , Ack_QueryServingCell.class
            , Ack_QuerySrsCfg.class
            , Ack_QueryTddChildCfgInfo.class
            , Ack_QueryUeMeasCfg.class
            , Ack_QueryUeNasRejectCfg.class
            , Ack_VerLevelUp.class
            , Ack_WBlist.class
            , CellStateRpt.class
            , CfgResult.class
            , FrqOffsetAdjRpt.class
            , Heart.class
            , MeasInfo.class
            , RemInfoRpt.class
            , UeInfoRpt.class
            , WarnRpt.class
            , AddDelete_autoCellArfcn.class
            , Cfg_autoSetCell.class
            , Cfg_autoStartWithPower.class
            , Cfg_bandPowerDEREASE.class
            , Cfg_cellStatus.class
            , Cfg_changeCellArgsMod.class
            , Cfg_dereaseDelta.class
            , Cfg_dwFlowDyn.class
            , Cfg_dwFlowType.class
            , Cfg_dwMEAS.class
            , Cfg_freqOffsetCheck.class
            , Cfg_gain.class
            , Cfg_gpsLossRebootTime.class
            , Cfg_gpsOrBeiDou.class
            , Cfg_gpsPps1s.class
            , Cfg_gpsSoftwareRenovate.class
            , Cfg_gspFDDReSync.class
            , Cfg_jwAddressReset.class
            , Cfg_locBlackList.class
            , Cfg_msg4PowerBoost.class
            , Cfg_ntpServerIp.class
            , Cfg_openIMEIGet.class
            , Cfg_powerControlALPHA.class
            , Cfg_reboot.class
            , Cfg_rebootAtTime.class
            , Cfg_reDirectionFDD.class
            , Cfg_scanArfcn.class
            , Cfg_scanPort.class
            , Cfg_secondaryPLMN.class
            , Cfg_SelectFreq.class
            , Cfg_servingCellArgs.class
            , Cfg_setAsyncTF.class
            , Cfg_setClientIp.class
            , Cfg_setIp.class
            , Cfg_setPINXSwitch.class
            , Cfg_srsArgs.class
            , Cfg_switchAES.class
            , Cfg_syncARFCN.class
            , Cfg_systemTDDFDD.class
            , Cfg_tddChildrenFrame.class
            , Cfg_transmitPowerAttenuation.class
            , Cfg_ueNasRejectCause.class
            , Cfg_ueRedirectIMSIList.class
            , Cfg_ueRedirectInfo.class
            , Cfg_VerLevelUp.class
            , Cfg_WBList.class
            , Cfg_wlMEAS.class
            , GetIMSI_withFtp.class
            , HeartResp.class
            , Query_arfcnSelfcfg.class
            , Query_bandPowerDerease.class
            , Query_cellAutoSet.class
            , Query_cellStateInfo.class
            , Query_cfg.class
            , Query_dwFlowType.class
            , Query_fddRedirectInfo.class
            , Query_gps.class
            , Query_gpsOrBeiDou.class
            , Query_gspPpsls.class
            , Query_imsiBlackList.class
            , Query_imsiFileCfg.class
            , Query_jwAddress.class
            , Query_locationUeBlack.class
            , Query_ntpSyncState.class
            , Query_redirectInfo.class
            , Query_rxGainPower.class
            , Query_rxPower.class
            , Query_scanAfrcn.class
            , Query_scanSyncPort.class
            , Query_secondaryPlmns.class
            , Query_selectFreqCfg.class
            , Query_servingCell.class
            , Query_syncInfo.class
            , Query_tddChildCfgInfo.class
            , Query_ueMeasCfg.class
            , Query_ueNasRejectCfg.class
            , UeInfoRptAck.class
    };
}
