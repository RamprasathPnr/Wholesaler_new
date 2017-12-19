package com.omneAgate.wholeSaler.DTO;

import android.database.Cursor;

import lombok.Data;

/**
 * Created by user1 on 27/7/15.
 */
@Data
public class LoginHistoryDto {

    String loginTime;

    String loginType;

    String userId;

    String logoutTime;

    String logoutType;

    String fpsId;

    String transactionId;

    String deviceId;

    public LoginHistoryDto() {

    }

    public LoginHistoryDto(Cursor cur) {
        loginTime = cur.getString(cur.getColumnIndex("login_time"));
        loginType = cur.getString(cur.getColumnIndex("login_type"));
        logoutTime = cur.getString(cur.getColumnIndex("logout_time"));
        logoutType = cur.getString(cur.getColumnIndex("logout_type"));
        transactionId = cur.getString(cur.getColumnIndex("transaction_id"));
        fpsId = cur.getString(cur.getColumnIndex("whole_sale_id"));
        userId = cur.getString(cur.getColumnIndex("user_id"));
    }
}
