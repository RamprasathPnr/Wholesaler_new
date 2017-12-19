package com.omneAgate.wholeSaler.Util;

import lombok.Getter;
import lombok.Setter;

/**
 * SingleTon class for maintain the sessionId
 */
public class LocationId {
    private static com.omneAgate.wholeSaler.Util.LocationId mInstance = null;

    @Getter
    @Setter
    private String longitude;

    @Getter
    @Setter
    private String latitude;

    private LocationId() {
        longitude = "";
        latitude = "";
    }

    public static synchronized  com.omneAgate.wholeSaler.Util.LocationId getInstance() {
        if (mInstance == null) {
            mInstance = new  com.omneAgate.wholeSaler.Util.LocationId();
        }
        return mInstance;
    }

}
