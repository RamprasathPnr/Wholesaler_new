package com.omneAgate.wholeSaler.ActivityBusinessClass;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.omneAgate.wholeSaler.DTO.WholeSaleLoginDto;
import com.omneAgate.wholeSaler.DTO.WholeSaleLoginResponseDto;
import com.omneAgate.wholeSaler.Util.CustomProgressDialog;
import com.omneAgate.wholeSaler.Util.LoginData;
import com.omneAgate.wholeSaler.Util.SessionId;
import com.omneAgate.wholeSaler.Util.Util;
import com.omneAgate.wholeSaler.Util.WholesaleDBHelper;
import com.omneAgate.wholeSaler.activity.R;
import com.omneAgate.wholeSaler.service.LocalDBLogin;

import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 * Created for local Login
 */
public class LoginCheck {
    Activity context;
    CustomProgressDialog progressBar;

    public LoginCheck(Activity context, CustomProgressDialog progressBar){
    this.context = context;
        this.progressBar = progressBar;
    }

    /**
     * sending login details to server if network connection available
     *
     * @params loginDto
     */

    /**
     * IF NO NETWORK AVAILABLE LOGIN WILL BE DONE USING LOCAL DATABASE
     */
    public void localLogin(WholeSaleLoginDto loginCredentials) {
        try {
            SharedPreferences prefs = context.getSharedPreferences("WHOLESALER", Context.MODE_PRIVATE);

            if (!prefs.getBoolean("approved", false)) {
                Toast.makeText(context,context.getString(R.string.noNetworkConnection),Toast.LENGTH_SHORT).show();
                Log.e("uljlj","No Network");
                return;
            }
            progressBar.show();
           WholeSaleLoginResponseDto hashDbPasswordWholesalerResponse = WholesaleDBHelper.getInstance(context).retrieveData(loginCredentials.getUserId());
           Log.e("Response LocalDB",""+hashDbPasswordWholesalerResponse);

            if (hashDbPasswordWholesalerResponse == null) {
                Toast.makeText(context,context.getString(R.string.inCorrectUnamePword),Toast.LENGTH_SHORT).show();
                dismissProgress();
                return;

            }
                LoginData.getInstance().setLoginData(hashDbPasswordWholesalerResponse);
                 String hashPassword = hashDbPasswordWholesalerResponse.getUserDetailDto().getPassword();
                 String deviceStatus= WholesaleDBHelper.getInstance(context).getMasterData("status");


            try {
                if (!hashDbPasswordWholesalerResponse.getUserDetailDto().getWholesalerDto().getStatus()) {
                    dismissProgress();
                    Toast.makeText(context,context.getString(R.string.storeInactive),Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!hashDbPasswordWholesalerResponse.getUserDetailDto().getActive()){
                    dismissProgress();
                    Toast.makeText(context,context.getString(R.string.userInactive),Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            catch(Exception e) {
                e.printStackTrace();
                dismissProgress();
                Log.e("","Error while retrive the local db"+e.toString());
                Toast.makeText(context,context.getString(R.string.storeInactive),Toast.LENGTH_SHORT).show();
                return;
            }

              if(deviceStatus !=null){
                  if(!hashDbPasswordWholesalerResponse.getUserDetailDto().getProfile().equalsIgnoreCase("Admin")) {
                      if (deviceStatus.equalsIgnoreCase("UNASSOCIATED")) {
                          dismissProgress();
                          Toast.makeText(context,context.getString(R.string.unassociated),Toast.LENGTH_SHORT).show();
                          return;

                      } else if (deviceStatus.equalsIgnoreCase("INACTIVE")) {
                          dismissProgress();
                          Toast.makeText(context,context.getString(R.string.deviceInvalid),Toast.LENGTH_SHORT).show();
                          return;
                      }
                  }

              }


                if (StringUtils.isNotEmpty(hashPassword)) {
                    if (hashDbPasswordWholesalerResponse.getUserDetailDto().getWholesalerDto().getStatus()) {
                        SessionId.getInstance().setUserId(hashDbPasswordWholesalerResponse.getUserDetailDto().getId());
                        SessionId.getInstance().setWholesaleId(hashDbPasswordWholesalerResponse.getUserDetailDto().getWholesalerDto().getId());
                        SessionId.getInstance().setWholesaleCode(hashDbPasswordWholesalerResponse.getUserDetailDto().getWholesalerDto().getCode());
                        SessionId.getInstance().setUserName(hashDbPasswordWholesalerResponse.getUserDetailDto().getUserId());

                        String lastLoginTime = WholesaleDBHelper.getInstance(context).getLastLoginTime(hashDbPasswordWholesalerResponse.getUserDetailDto().getId());
                        if (StringUtils.isNotEmpty(lastLoginTime)) {
                            SessionId.getInstance().setLastLoginTime(new Date(Long.parseLong(lastLoginTime)));
                        } else {
                            SessionId.getInstance().setLastLoginTime(new Date());
                        }
                        WholesaleDBHelper.getInstance(context).setLastLoginTime(hashDbPasswordWholesalerResponse.getUserDetailDto().getId());
                        SessionId.getInstance().setLoginTime(new Date());
                        LocalDBLogin localDBLogin = new LocalDBLogin(context, progressBar, hashDbPasswordWholesalerResponse.getUserDetailDto());
                        localDBLogin.setLoginProcess(loginCredentials.getPassword(), hashPassword);
                    } else {
                        dismissProgress();
                        Toast.makeText(context,context.getString(R.string.storeInactive),Toast.LENGTH_SHORT).show();
                    }
                } else {
                    dismissProgress();
                    Toast.makeText(context,context.getString(R.string.inCorrectUnamePword),Toast.LENGTH_SHORT).show();

                }

        } catch (Exception e) {
            Log.e("LoginActivity", "Error in Local Login", e);
        }
    }

    private void dismissProgress() {
        if (progressBar != null) {
            if (progressBar.isShowing()) {
                progressBar.dismiss();
            }
        }
    }
}
