package com.omneAgate.wholeSaler.service;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.omneAgate.wholeSaler.DTO.WholesalerPostingDto;
import com.omneAgate.wholeSaler.Util.NetworkConnection;
import com.omneAgate.wholeSaler.Util.SessionId;
import com.omneAgate.wholeSaler.Util.WholesaleDBHelper;
import com.omneAgate.wholeSaler.activity.R;

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
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Offline data sync to server service
 */
public class OfflineTransactionManager extends Service {

    List<WholesalerPostingDto> billList; //list of WholesalerPosting in offline

    Timer WholesalerPostingyncTimer;

    WholesalerPostingyncTimerTask WholesalerPostingyncTimerTask;

    private String serverUrl = "";

    @Override
    public void onCreate() {
        super.onCreate();
        WholesalerPostingyncTimer = new Timer();
        WholesalerPostingyncTimerTask = new WholesalerPostingyncTimerTask();
        Log.e("Info", "Service OfflineTransactionManager created ");
        serverUrl = WholesaleDBHelper.getInstance(this).getMasterData("serverUrl");
        Long timerWaitTime = Long.parseLong(getString(R.string.serviceTimeout));
        Long timerWaitTime2 = 3 * 30 * 1000l;
        Long timerWaitTime3 = 5 * 60 * 1000l;
        WholesalerPostingyncTimer.schedule(WholesalerPostingyncTimerTask, 0, timerWaitTime2);
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onDestroy() {
        try {
            if (WholesalerPostingyncTimer != null) {
                WholesalerPostingyncTimer.cancel();
                WholesalerPostingyncTimer = null;
            }
        } catch (Exception e) {
            Log.e("Offline transaction", "Error in Service", e);
        }

    }

    /**
     * Check bill size
     * <p/>
     * Async task to call WholesalerPosting
     */
    private void syncWholesalerPostingToServer(WholesalerPostingDto wholesalerPostingDto) {
        Log.e("Wholesaler","Posting from back "+wholesalerPostingDto.toString());
        wholesalerPostingDto.setWholesalerCode(SessionId.getInstance().getWholesaleCode());

            String recipientType = wholesalerPostingDto.getRecipientType();
            if(recipientType.equalsIgnoreCase("Kerosene Bunk")){
               wholesalerPostingDto.setKbCode(wholesalerPostingDto.getCode());
            }else if(recipientType.equalsIgnoreCase("FPS")){
              wholesalerPostingDto.setFpsCode(wholesalerPostingDto.getCode());
            }else{
                wholesalerPostingDto.setRrcCode(wholesalerPostingDto.getCode());
            }
        Log.e("offlinewhoelsaledto",""+wholesalerPostingDto);

        new OfflineDataSyncTask().execute(wholesalerPostingDto);

    }

    /**
     * return http POST method using parameters
     */
    private HttpResponse requestType(URI website, StringEntity entity) throws IOException {

        HttpParams httpParameters = new BasicHttpParams();
        int timeoutConnection = 50000;
        HttpConnectionParams.setConnectionTimeout(httpParameters,
                timeoutConnection);
        int timeoutSocket = 50000;
        HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
        HttpClient client = new DefaultHttpClient(httpParameters);
        HttpPost postRequest = new HttpPost();
        postRequest.setURI(website);
        postRequest.setHeader("Content-Type", "application/json");
        postRequest.setHeader("Cookie", "JSESSIONID=" + SessionId.getInstance().getSessionId());
        postRequest.setHeader("Cookie", "SESSION=" + SessionId.getInstance().getSessionId());
        postRequest.setEntity(entity);
        return client.execute(postRequest);
    }

    class WholesalerPostingyncTimerTask extends TimerTask {

        @Override
        public void run() {
            try {
                //Util.LoggingQueue(OfflineTransactionManager.class.getSimpleName() "Info", "Starting up");
                Log.e("Info", "Starting up");
                billList = new ArrayList<>();
                billList = WholesaleDBHelper.getInstance(OfflineTransactionManager.this).getAllWholesalerPostingForSync();

                NetworkConnection network = new NetworkConnection(OfflineTransactionManager.this);
                Log.e("Infobill list ", ""+ network.isNetworkAvailable());
                if (network.isNetworkAvailable()) {
                    for (WholesalerPostingDto bill : billList) {
                        syncWholesalerPostingToServer(bill);
                    }
                }
               Log.e("Info", "Waking up");
            } catch (Exception e) {

                Log.e("Offline Trans", e.toString(), e);
            }
        }

    }

    /**
     * Async   task for connection heartbeat
     */
    private class OfflineDataSyncTask extends AsyncTask<WholesalerPostingDto, String, String> {

        @Override
        protected String doInBackground(WholesalerPostingDto... billData) {
            BufferedReader in = null;
            try {
                String url = serverUrl + "/wholesale/posting";
                URI website = new URI(url);
                Log.e("WholesalerPosting",""+ url);
                Log.e("WholesalerPosting", billData[0].toString());
                String bill = new Gson().toJson(billData[0]);
                StringEntity entity = new StringEntity(bill, HTTP.UTF_8);
                HttpResponse response = requestType(website, entity);
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
               // Util.LoggingQueue(OfflineTransactionManager.class.getSimpleName() "Error", "Network exception" + e.getMessage());
                Log.e(OfflineTransactionManager.class.getSimpleName()+ "Error", "Network exception" + e.getMessage());
                Log.e("Error in connection: ", e.toString());
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
            Log.e("WholesalerPosting","offline manager"+ response);
            try {
                if (response != null) {
                    GsonBuilder gsonBuilder = new GsonBuilder();
                    Gson gson = gsonBuilder.create();
                    WholesalerPostingDto updateStock = gson.fromJson(response, WholesalerPostingDto.class);
                    if (updateStock != null && updateStock.getStatusCode() == 0 ) {
                        WholesaleDBHelper.getInstance(OfflineTransactionManager.this).billUpdate(updateStock.getReferenceNo());

                        Log.e("Info", "Updating status of bill to status T for bill referenceNo " + updateStock.getReferenceNo());
                    } else if (updateStock != null && updateStock.getStatusCode() == 6007) {
                        WholesaleDBHelper.getInstance(OfflineTransactionManager.this).billUpdate(updateStock.getReferenceNo());
                        Log.e( "Info", "Updating status of bill to status T for bill referenceNo " + updateStock.getReferenceNo());
                    } else {
                        Log.e("Error", "Received null response ");
                    }
                }
            } catch (Exception e) {
                Log.e("Insert Error", e.toString(), e);
            }
        }
    }

}
