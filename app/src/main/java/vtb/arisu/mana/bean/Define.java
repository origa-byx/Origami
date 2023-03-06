package vtb.arisu.mana.bean;

/**
 * @by: origami
 * @date: {2022/5/23}
 * @info:
 **/
public @interface Define {
    /**
     * IMSI数据长度
     */
    int C_MAX_IMSI_LEN = 17;

    /**
     * IMEI数据长度
     */
    int C_MAX_IMEI_LEN = 17;

    /**
     * 黑白名单配置时每次最大可以添加 /删除的UE数
     */
    int C_MAX_CONTROL_PROC_UE_NUM = 10;

    /**
     * 黑白名单中可以各含有的最大 UE数
     */
    int C_MAX_CONTROL_LIST_UE_NUM = 100;

    /**
     * 定位黑名单中可以含有的最大 UE数
     */
    int C_MAX_LOCATION_BLACKLIST_UE_CFG_NUM = 20;

    /**
     * 查询定位黑名单中最大 UE数
     */
    int C_MAX_LOCATION_BLACKLIST_UE_QUERY_NUM = 100;

    /**
     * 小区自配置对应的默认扫频 ARFCN 总数
     */
    int C_MAX_DEFAULT_ARFCN_NUM = 50;

    /**
     * 重定向白名单中单次配置IMSI最大数目
     */
    int C_MAX_UE_REDIRECT_IMSI_ADD_NUM = 20;

    /**
     * 最大扫频频点数量
     */
    int C_MAX_REM_ARFCN_NUM = 10;

    /**
     * 小区的同频邻区数目，来自SIB4
     */
    int C_MAX_INTRA_NEIGH_NUM = 32;

    /**
     * 扫频上报最大的小区数目
     */
    int C_MAX_COLLTECTION_INTRA_CELL_NUM = 8;
}
