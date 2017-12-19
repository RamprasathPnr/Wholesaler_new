package com.omneAgate.wholeSaler.DTO;

import com.omneAgate.wholeSaler.DTO.EnumDTO.CommonStatuses;

import lombok.Data;

@Data
public class UpgradeDetailsDto extends BaseDto {
    /**
     * created time
     */
    long createdTime;

    /**
     * created time
     */
    long updatedTime;

    /**
     * device number
     */
    String deviceNum;



    /**
     * beneficiary table count
     */
    int beneficiaryCount;

    /**
     * beneficiary table count
     */
    int beneficiaryUnsyncCount;

    /**
     * bill table count
     */
    int billCount;

    /**
     * bill table count
     */
    int billUnsyncCount;

    /**
     * card type table count
     */
    int cardTypeCount;

    /**
     * fps stock count
     */
    int fpsStockCount;

    /**
     * Stock Outward table count
     */
    int outwardCount;

    /**
     * Stock Outward table Unsync count
     */
    int outwardUnsyncCount;

    /**
     * product table count
     */
    int productCount;

    /**
     * Fps store  count
     */
    int fpsCount;

    /**
     * rrc count
     */
    int rrcCount;

    /**
     * bunkCount
     */
    int bunkerCount;


    /**
     * previous version
     */
    int previousVersion;


    /**
     * current version
     */
    int currentVersion;

    String referenceNumber;

    /**
     * Staus for update
     */
    CommonStatuses status;

   //Wholesaler Id
    long wholesalerId;
}