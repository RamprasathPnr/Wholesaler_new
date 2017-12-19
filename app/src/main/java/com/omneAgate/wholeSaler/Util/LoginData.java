package com.omneAgate.wholeSaler.Util;

import com.omneAgate.wholeSaler.DTO.WholeSaleLoginResponseDto;
import lombok.Getter;
import lombok.Setter;

/**
 * SingleTon class for maintain the sessionId
 */
public class LoginData {
    private static LoginData mInstance = null;

    @Getter
    @Setter
    private WholeSaleLoginResponseDto loginData;


    @Getter
    @Setter
    private long fpsId;

    private LoginData() {
        loginData = new WholeSaleLoginResponseDto();
    }

    public static synchronized LoginData getInstance() {
        if (mInstance == null) {
            mInstance = new LoginData();
        }
        return mInstance;
    }

}
