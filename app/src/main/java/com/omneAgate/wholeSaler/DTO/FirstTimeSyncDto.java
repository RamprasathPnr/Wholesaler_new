package com.omneAgate.wholeSaler.DTO;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class FirstTimeSyncDto extends BaseDto implements Serializable {

    int totalCount;

    boolean hasMore;

    int currentCount;

    int totalSentCount;

    String tableName;

    String refNum;

    boolean firstFetch;

    String lastSyncTime;

    Map<String, Integer> tableDetails;


    /** FPS Stores associated with  Wholesaler */
	List<FpsStoreDto> fpsStores;

	/** RRCs associated with wholesaler*/
	List<RrcDto> rrcs;
	/** Kerosene Bunk associated with wholesaler*/

	List<KeroseneBunkDto> keroseneBunks;
	
	/** product information-Kerosene */
	List<ProductDto> products;

    List<WholesalerPostingDto> postingInfo;

    /** Sync Time */
	String updateDate;




}
