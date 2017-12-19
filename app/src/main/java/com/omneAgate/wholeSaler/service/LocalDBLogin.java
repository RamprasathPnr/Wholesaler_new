package com.omneAgate.wholeSaler.service;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.omneAgate.wholeSaler.DTO.LoginHistoryDto;
import com.omneAgate.wholeSaler.DTO.UserDetailDto;
import com.omneAgate.wholeSaler.Util.CustomProgressDialog;
import com.omneAgate.wholeSaler.Util.WholesaleDBHelper;
import com.omneAgate.wholeSaler.Util.SessionId;
import com.omneAgate.wholeSaler.Util.StringDigesterString;
import com.omneAgate.wholeSaler.Util.Util;
import com.omneAgate.wholeSaler.activity.AdminActivity;
import com.omneAgate.wholeSaler.activity.DashboardActivity;
import com.omneAgate.wholeSaler.activity.GlobalAppState;
import com.omneAgate.wholeSaler.activity.R;

import org.apache.commons.lang3.StringUtils;
import org.jasypt.digest.StringDigester;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by user1 on 3/3/15.
 */
public class LocalDBLogin {

    Activity context;
    CustomProgressDialog progressBar;
    UserDetailDto profile;

    public LocalDBLogin(Activity context, CustomProgressDialog progressBar, UserDetailDto profile) {
        this.context = context;
        this.progressBar = progressBar;
        this.profile = profile;
    }

    /**
     * async task for login
     *
     * @param passwordUser and hash
     */
    public void setLoginProcess(String passwordUser, String hashDbPassword) {
        new LocalLoginProcess(passwordUser, hashDbPassword).execute();

    }

    private boolean localDbPassword(String passwordUser, String passwordDbHash) {

        StringDigester stringDigester = StringDigesterString.getPasswordHash(context);
        Log.e("stringDigester",""+stringDigester);
        return stringDigester.matches(passwordUser, passwordDbHash);
    }

    private void insertLoginHistoryDetails() {
        LoginHistoryDto loginHistoryDto = new LoginHistoryDto();
        if (profile.getWholesalerDto() != null)
        loginHistoryDto.setFpsId(""+profile.getWholesalerDto().getId());
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        loginHistoryDto.setLoginTime(df.format(new Date()));
        loginHistoryDto.setLoginType("OFFLINE_LOGIN");
        loginHistoryDto.setUserId(""+profile.getId());
        df = new SimpleDateFormat("ddMMyyHHmmss");
        String transactionId = df.format(new Date());
        loginHistoryDto.setTransactionId(transactionId);
        SessionId.getInstance().setTransactionId(transactionId);
        WholesaleDBHelper.getInstance(context).insertLoginHistory(loginHistoryDto);
    }

    //Local login Process
    private class LocalLoginProcess extends AsyncTask<String, Void, Boolean> {
        // user password and local db password
        String passwordUser, hashDbPassword;
        // LocalLoginProcess Constructor
        LocalLoginProcess(String passwordUser, String hashDbPassword) {
            this.passwordUser = passwordUser;
            this.hashDbPassword = hashDbPassword;
        }
        /**
         * Local login Background Process
         * return true if user hash and dbhash equals else false
         */
        protected Boolean doInBackground(String... params) {
            try {
                return localDbPassword(passwordUser, hashDbPassword);
            } catch (Exception e) {
                Log.e("loca lDb", "Interrupted", e);
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (progressBar != null) progressBar.dismiss();
            if (result) {
                GlobalAppState.localLogin = true;
                insertLoginHistoryDetails();
                String lastModifiedDate = WholesaleDBHelper.getInstance(context).getMasterData("syncTime");
                Log.e("LastModifiedDate",lastModifiedDate);
                if (StringUtils.isNotEmpty(lastModifiedDate)) {
                    if ("ADMIN".equalsIgnoreCase(profile.getProfile())) {
                        context.startActivity(new Intent(context, AdminActivity.class));
                        context.finish();
                    } else {
                        insertLoginHistoryDetails();
                        context.startActivity(new Intent(context, DashboardActivity.class));
                        context.finish();
                    }

                } else {
                    Toast.makeText(context,context.getString(R.string.noNetworkConnection),Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(context,context.getString(R.string.inCorrectUnamePword),Toast.LENGTH_SHORT).show();
            }
        }
    }
}
