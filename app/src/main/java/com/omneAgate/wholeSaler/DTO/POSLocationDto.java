package com.omneAgate.wholeSaler.DTO;

import lombok.Data;

/**
 * Created by user1 on 25/7/15.
 */
@Data
public class POSLocationDto extends BaseDto {

    String longitude;

    String latitude;

    String deviceNumber;
}
