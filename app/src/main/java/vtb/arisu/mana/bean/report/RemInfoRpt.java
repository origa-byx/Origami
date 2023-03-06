package vtb.arisu.mana.bean.report;

import vtb.arisu.mana.annotation.Info;
import vtb.arisu.mana.annotation.Nya;
import vtb.arisu.mana.annotation.VtbBean;
import vtb.arisu.mana.bean.OFLType;
import vtb.mashiro.kanon.base.Bean;
import vtb.mashiro.kanon.util.ByteUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @by: origami
 * @date: {2022/5/31}
 * @info: 扫频/同步小区信息上报
 * TDD同步过程中，通过此消息上报尝试同步的小区信息(collectionTypeFlag=1)。
 * 扫频完成后，通过此消息上报扫频的结果信息(collectionTypeFlag=0)。
 *
 * 由于此消息体长度比较大，对于数组的信息，
 * 基站会按照实际Num填充上报信息，请客户端解析时，根据相应数组的Num解析数组信息
 *
 * ps: 这个上报是真的恶心啊 ~
 **/
@Nya
@Info("扫频/同步小区信息上报")
@VtbBean(OFLType.O_FL_ENB_TO_LMT_REM_INFO_RPT)
public class RemInfoRpt extends Bean {

    /**
     * 扫频信息标识/同步信息标识
     * 0：扫频小区
     * 1：同步小区
     */
    public int u16_collectionTypeFlag;

    public List<WrFLCollectionCellInfo> stCollCellInfo = new ArrayList<>();

    @Override
    public void fromBytes(byte[] body) {
        int collectionCellNum = ByteUtil.toInt_s(body, 0, 2);
        u16_collectionTypeFlag = ByteUtil.toInt_s(body, 2, 2);
        byte[] other = new byte[body.length - 4];
        int f = 4;
        System.arraycopy(body, f, other, 0, other.length);
        for (int i = 0; i < collectionCellNum; i++) {
            WrFLCollectionCellInfo wrFLCollectionCellInfo = new WrFLCollectionCellInfo();
            wrFLCollectionCellInfo.fromBytes(other);
            f += wrFLCollectionCellInfo.getSize();
            System.arraycopy(body, f, other, 0, body.length - f);
        }
    }

    @Override
    public byte[] toBytes() {
        return super.toBytes();
    }

    @Override
    public int getSize() {
        int size = 4;
        for (WrFLCollectionCellInfo info : stCollCellInfo) {
            size += info.getSize();
        }
        return size;
    }

    public static class WrFLCollectionCellInfo extends Bean{

        public WrFLCellInfo stCellInfo;

        public List<WrIntraFreqNeighCellInfo> stIntraFreqNeighCellInfo = new ArrayList<>();

        public List<StFlLteIntreFreqLst> stInterFreqLstInfo = new ArrayList<>();

        @Override
        public void fromBytes(byte[] body) {
            Nya nya = WrFLCellInfo.class.getDeclaredAnnotation(Nya.class);
            if(nya == null) return;
            byte[] stCellInfoBytes = new byte[nya.l()];
            System.arraycopy(body, 0, stCellInfoBytes, 0, nya.l());
            stCellInfo = new WrFLCellInfo();
            stCellInfo.fromBytes(stCellInfoBytes);
            int u32_IntraFreqNeighCellNum = ByteUtil.toInt_s(body, nya.f(), 4);
            int f = nya.l() + 4;
            Nya nya1 = WrIntraFreqNeighCellInfo.class.getDeclaredAnnotation(Nya.class);
            if(nya1 == null) return;
            byte[] WrIntraFreqNeighCellInfoBytes = new byte[nya1.l()];
            for (int i = 0; i < u32_IntraFreqNeighCellNum; i++) {
                System.arraycopy(body, f, WrIntraFreqNeighCellInfoBytes, 0, nya1.l());
                WrIntraFreqNeighCellInfo info = new WrIntraFreqNeighCellInfo();
                info.fromBytes(WrIntraFreqNeighCellInfoBytes);
                stIntraFreqNeighCellInfo.add(info);
                f += nya1.l();
            }
            int InterFreqNum = ByteUtil.toInt_s(body, f, 4);
            f += 4;
            int otherNeedJx = body.length - f;
            byte[] otherBytes = new byte[otherNeedJx];
            System.arraycopy(body, f, otherBytes, 0, otherNeedJx);
            for (int i = 0; i < InterFreqNum; i++) {
                StFlLteIntreFreqLst freqLst = new StFlLteIntreFreqLst();
                freqLst.fromBytes(otherBytes);
                stInterFreqLstInfo.add(freqLst);
                f += freqLst.getSize();
                System.arraycopy(body, f, otherBytes, 0, body.length - f);
            }
        }

        @Override
        public byte[] toBytes() {
            byte[] body = new byte[getSize()];
            System.arraycopy(stCellInfo.toBytes(), 0, body, 0, stCellInfo.getSize());
            int f = stCellInfo.getSize();
            ByteUtil.setNum_s(body, f, 4, stIntraFreqNeighCellInfo.size());
            f += 4;
            for (WrIntraFreqNeighCellInfo info : stIntraFreqNeighCellInfo) {
                int size = info.getSize();
                ByteUtil.setByteArray(body, f, size, info.toBytes());
                f += size;
            }
            ByteUtil.setNum_s(body, f, 4, stInterFreqLstInfo.size());
            f += 4;
            for (StFlLteIntreFreqLst freqLst : stInterFreqLstInfo) {
                int size = freqLst.getSize();
                ByteUtil.setByteArray(body, f, size, freqLst.toBytes());
                f += size;
            }
            return body;
        }

        @Override
        public int getSize() {
            int size = 8;
            for (WrIntraFreqNeighCellInfo cellInfo : stIntraFreqNeighCellInfo) {
                size += cellInfo.getSize();
            }
            for (WrIntraFreqNeighCellInfo info : stIntraFreqNeighCellInfo) {
                size += info.getSize();
            }
            size += stCellInfo.getSize();
            return size;
        }

        @Nya(l = 24)
        public static class WrFLCellInfo extends Bean{
            /**
             * 下行频点
             * 0~65535
             */
            @Nya
            public int u32_dlEarfcn;

            /**
             * PhysCellId is used to indicate the physical layer identity of the cell
             * 0~ 503
             */
            @Nya(f = 4, l = 2)
            public int u16_PCI;

            /**
             * TrackingAreaCode is used to identify a tracking area within the scope of a PLMN
             * 0~65535
             */
            @Nya(f = 6, l = 2)
            public int U16_TAC;

            /**
             * 仅支持5位PLMN
             */
            @Nya(f = 8, l = 2)
            public int u16_PLMN;

            /**
             * TDD子帧配置值
             */
            @Nya(f = 10, l = 2)
            public int u16_TddSfAssignment;

            /**
             * CellIdentity is used to unambiguously identify a cell within a PLMN
             * 0x00000000 ~ 0x0FFFFFFF
             */
            @Nya(f = 12)
            public int u32_CellId;

            /**
             * 本小区频点优先级
             * 1~7
             */
            @Nya(f = 16)
            public int u32_Priority;

            /**
             * 下行参考信号强度
             * 0~97
             */
            @Nya(f = 20, l = 1)
            public int u8_RSRP;

            /**
             * LTE参考信号接收质量
             * 0~33
             */
            @Nya(f = 21, l = 1)
            public int u8_RSRQ;

            /**
             * 小区工作带宽
             * 6, 15, 25, 50, 75, 100
             */
            @Nya(f = 22, l = 1)
            public int u8_Bandwidth;

            /**
             * TDD特殊子帧配置
             */
            @Nya(f = 23, l = 1)
            public int u8_TddSpecialSfPatterns;
        }

        @Nya(l = 8)
        public static class WrIntraFreqNeighCellInfo extends Bean{

            /**
             * 下行频点
             * 0~65535
             */
            @Nya
            public int u32_dlEarfcn;

            /**
             * PhysCellId is used to indicate the physical layer identity of the cell
             * 0 ~ 503
             */
            @Nya(f = 4, l = 2)
            public int u16_PCI;

            /**
             * indicate a cell specific offset.取值依次对应dB如下所示：
             * ENUMERATED
             * {dB-24=0, dB-22, dB-20, dB-18, dB-16, dB-14,
             * dB-12, dB-10, dB-8, dB-6, dB-5, dB-4, dB-3,
             * dB-2, dB-1, dB0, dB1, dB2, dB3, dB4, dB5,dB6,
             * dB8, dB10, dB12, dB14, dB16, dB18, dB20, dB22, dB24}
             * 0~31
             */
            @Nya(f = 6, l = 2)
            public int u16_QoffsetCell;
        }

        @Nya
        public static class StFlLteIntreFreqLst extends Bean{

            /**
             * 下行频点
             * 0~65535
             */
            @Nya
            public int u32_dlEarfcn;

            /**
             * 值越小优先级越低
             * 0~7
             */
            @Nya(f = 4, l = 1)
            public int u8_cellReselectPriotry;

            /**
             * indicate a freq specific offset.取值依次对应dB如下所示：
             * ENUMERATED
             * {dB-24=0, dB-22, dB-20, dB-18, dB-16, dB-14,
             * dB-12, dB-10, dB-8, dB-6, dB-5, dB-4, dB-3, dB-2,
             * dB-1, dB0, dB1, dB2, dB3, dB4, dB5,dB6, dB8, dB10,
             * dB12, dB14, dB16, dB18, dB20, dB22, dB24}
             * 0~31
             */
            @Nya(f = 5, l = 1)
            public int u8_Q_offsetFreq;

            /**
             * indicate the maximum allowed measurement bandwidth on a
             * carrier frequency;取值依次带宽如下所示
             * ENUMERATED {mbw6=0, mbw15=1, mbw25=2, mbw50=3, mbw75=4, mbw100=5}
             * 0~5
             */
            @Nya(f = 6, l = 2)
            public int u16_measBandWidth;

            public List<WrFLInterNeighCellInfo> stInterFreqNeighCell = new ArrayList<>();

            @Override
            public int getSize(){
                Nya nya = WrFLInterNeighCellInfo.class.getDeclaredAnnotation(Nya.class);
                return 12 + stInterFreqNeighCell.size() * (nya == null? 0 : nya.l());
            }

            @Override
            public void fromBytes(byte[] body) {
                super.fromBytes(body);
                int size = ByteUtil.toInt_s(body, 8, 4);
                int f = 12;
                Nya nya = WrFLInterNeighCellInfo.class.getDeclaredAnnotation(Nya.class);
                if(nya == null) return;
                for (int i = 0; i < size; i++) {
                    WrFLInterNeighCellInfo item = new WrFLInterNeighCellInfo();
                    byte[] itemBytes = new byte[nya.l()];
                    System.arraycopy(body, f, itemBytes, 0, nya.l());
                    item.fromBytes(itemBytes);
                    f += nya.l();
                }
            }

            @Override
            public byte[] toBytes() {
                byte[] body = super.toBytes();
                ByteUtil.setNum_s(body, 8, 4, stInterFreqNeighCell.size());
                if(stInterFreqNeighCell.isEmpty()) return body;
                int f = 12;
                Nya nya = WrFLInterNeighCellInfo.class.getDeclaredAnnotation(Nya.class);
                if(nya == null) return body;
                for (WrFLInterNeighCellInfo cellInfo : stInterFreqNeighCell) {
                    ByteUtil.setByteArray(body, f, nya.l(), cellInfo.toBytes());
                    f += nya.l();
                }
                return body;
            }

            @Nya
            public static class WrFLInterNeighCellInfo extends Bean{

                /**
                 * PhysCellId is used to indicate the physical layer identity of the cell
                 * 0 ~ 503
                 */
                @Nya(l = 2)
                public int u16_PCI;

                /**
                 * indicate a cell specific offset.取值依次对应dB如下所示：
                 * ENUMERATED
                 * {dB-24=0, dB-22, dB-20, dB-18, dB-16, dB-14,
                 * dB-12, dB-10, dB-8, dB-6, dB-5, dB-4, dB-3, dB-2, dB-1, dB0,
                 * dB1, dB2, dB3, dB4, dB5,dB6, dB8, dB10, dB12, dB14,
                 * dB16, dB18, dB20, dB22, dB24}
                 * 0~31
                 */
                @Nya(f = 2, l = 2)
                public int u16_QoffsetCell;

            }

        }

    }

}
