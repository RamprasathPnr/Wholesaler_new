package com.omneAgate.wholeSaler.Util;

import java.util.Date;

import lombok.Data;

/**
 * SingleTon class for maintain the sessionId
 */
@Data
public class SessionId {
    private static SessionId mInstance = null;


    private String sessionId;


    private long userId;


    private String transactionId;


    private String userName;




    private boolean qrOTPEnabled = false;


    private String fpsCode;


    private long fpsId;

    private Date loginTime;

    private Date lastLoginTime;

    private long wholesaleId;

    private String wholesaleCode;

    private String longitude;

    private String latitude;





    private SessionId() {
        sessionId = "";
        userName = "";
        fpsCode = "";
        userId = 0l;
        fpsId = 0l;
        wholesaleId=0l;
        latitude="";
        longitude = "";

        transactionId = "";
        loginTime = new Date();
        lastLoginTime = new Date();
    }

    public static synchronized SessionId getInstance() {
        if (mInstance == null) {
            mInstance = new SessionId();
        }
        return mInstance;
    }

}
