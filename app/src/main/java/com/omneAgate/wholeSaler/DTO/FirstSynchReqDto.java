package com.omneAgate.wholeSaler.DTO;

import lombok.Data;

/**
 * Created by user1 on 29/5/15.
 */

@Data
public class FirstSynchReqDto {


    String deviceNum;

    String tableName;

    int totalCount;

    int currentCount;

    int totalSentCount;

    String refNum;

    String lastSyncTime;

    boolean isEndOfSynch;


}
