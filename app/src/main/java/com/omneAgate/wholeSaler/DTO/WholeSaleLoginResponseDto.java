package com.omneAgate.wholeSaler.DTO;

import android.database.Cursor;

import com.omneAgate.wholeSaler.DTO.EnumDTO.DeviceStatus;

import java.io.Serializable;

import lombok.Data;

@Data
public class WholeSaleLoginResponseDto extends BaseDto implements Serializable {

    /* */
    private static final long serialVersionUID = 5500373380746933439L;

    /** id of the Wholesaler  */
    Long id;

    boolean authenticationStatus;

    String sessionid;

    UserDetailDto userDetailDto;

    WholesalerDto wholeSaler;

    long serverTime;

    DeviceStatus deviceStatus;


   public WholeSaleLoginResponseDto() {

    }

    public WholeSaleLoginResponseDto(Cursor cur) {
        userDetailDto = new UserDetailDto(cur);
    }

}
