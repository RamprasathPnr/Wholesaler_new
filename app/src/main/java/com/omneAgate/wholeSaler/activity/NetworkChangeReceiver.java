package com.omneAgate.wholeSaler.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.provider.Settings;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.omneAgate.wholeSaler.DTO.LoginDto;
import com.omneAgate.wholeSaler.DTO.WholeSaleLoginDto;
import com.omneAgate.wholeSaler.DTO.WholeSaleLoginResponseDto;
import com.omneAgate.wholeSaler.Util.NetworkUtil;
import com.omneAgate.wholeSaler.Util.SessionId;
import com.omneAgate.wholeSaler.Util.Util;
import com.omneAgate.wholeSaler.Util.WholesaleDBHelper;
import com.omneAgate.wholeSaler.service.OfflineTransactionManager;
import com.omneAgate.wholeSaler.service.UpdateDataService;


import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;

/**
 * Created by user1 on 30/3/15.
 */
public class NetworkChangeReceiver extends BroadcastReceiver {

    String serverUrl = "";
    StringEntity stringEntity;
    Context context;

    @Override
    public void onReceive(final Context context, final Intent intent) {

        Log.e("networkchangerrecevier", "networkcall");

        int status = NetworkUtil.getConnectivityStatus(context);
        Log.e("networkchangerrecevier", "" + status);
        if (status != 0) {
            Log.e("NetworkChangeReceiver", "Global Appstate " + GlobalAppState.localLogin);
            if (GlobalAppState.localLogin) {
                this.context = context;
                try {

                    serverUrl = WholesaleDBHelper.getInstance(context).getMasterData("serverUrl");
                    WholeSaleLoginResponseDto loginResponseDto = WholesaleDBHelper.getInstance(context).getUserDetails(SessionId.getInstance().getUserId());
                    Log.e("offlinelogin", "" + loginResponseDto);
                    WholeSaleLoginDto loginCredentials = new WholeSaleLoginDto();
                    loginCredentials.setUserId(loginResponseDto.getUserDetailDto().getUserId());
                    loginCredentials.setPassword(Util.DecryptPassword(loginResponseDto.getUserDetailDto().getEncryptedPassword()));
                    loginCredentials.setMacId(Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID).toUpperCase());
                    String login = new Gson().toJson(loginCredentials);
                    Log.e("logindata request", "" + login);
                    stringEntity = new StringEntity(login, HTTP.UTF_8);
                    new BackgroundSuccess().execute("");
                } catch (Exception e) {
                    Log.e("NetworkChangeReceiver", e.toString(), e);
                }
            }
        }

    }

    /*return http POST method using parameters*/
    private HttpResponse requestType(URI website, StringEntity entity) throws IOException {

        HttpParams httpParameters = new BasicHttpParams();
        int timeoutConnection = 50000;
        HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
        int timeoutSocket = 50000;
        HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
        HttpClient client = new DefaultHttpClient(httpParameters);
        HttpPost postRequest = new HttpPost();
        postRequest.setURI(website);
        postRequest.setHeader("Content-Type", "application/json");
        postRequest.setHeader("Cookie", "JSESSIONID=" + SessionId.getInstance().getSessionId());
        postRequest.setEntity(entity);
        return client.execute(postRequest);

    }

    /**
     * Async   task for Download Sync for data in table
     */
    private class BackgroundSuccess extends AsyncTask<String, String, String> {


        @Override
        protected String doInBackground(String... f_url) {
            BufferedReader in = null;
            try {
                String url = serverUrl + "/login/wholesale";
                URI website = new URI(url);
                HttpResponse response = requestType(website, stringEntity);
                in = new BufferedReader(new InputStreamReader(response
                        .getEntity().getContent()));
                StringBuffer sb = new StringBuffer("");
                String l;
                String nl = System.getProperty("line.separator");
                while ((l = in.readLine()) != null) {
                    sb.append(l + nl);
                }
                in.close();
                String responseData = sb.toString();
                return responseData;
            } catch (Exception e) {
                Log.e("NetworkChangeReceiver", e.toString(), e);
                try {
                    if (in != null)
                        in.close();
                } catch (Exception e1) {
                    // Intentional swallow of exception
                }
            }
            return null;
        }


        @Override
        protected void onPostExecute(String response) {
            if (response != null) {
                try {
                    Log.e("NetworkChangeReceiver", "Background login response" + response);
                    GsonBuilder gsonBuilder = new GsonBuilder();
                    Gson gson = gsonBuilder.create();
                    WholeSaleLoginResponseDto loginResponse = gson.fromJson(response, WholeSaleLoginResponseDto.class);
                    SessionId.getInstance().setSessionId(loginResponse.getSessionid());
                    SessionId.getInstance().setWholesaleId(loginResponse.getWholeSaler().getId());
                    if (!loginResponse.getUserDetailDto().getProfile().equalsIgnoreCase("ADMIN")) {
                        context.startService(new Intent(context, UpdateDataService.class));
                        context.startService(new Intent(context, OfflineTransactionManager.class));
                        GlobalAppState.localLogin = false;
                    }
                } catch (Exception e) {
                    Log.e("NetworkChangeReceiver", e.toString(), e);
                }
            }
        }
    }
}