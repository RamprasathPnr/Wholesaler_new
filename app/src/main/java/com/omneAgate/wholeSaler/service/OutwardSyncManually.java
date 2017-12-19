package com.omneAgate.wholeSaler.service;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.provider.Settings;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.omneAgate.wholeSaler.DTO.WholesalerPostingDto;
import com.omneAgate.wholeSaler.Util.NetworkConnection;
import com.omneAgate.wholeSaler.Util.SessionId;
import com.omneAgate.wholeSaler.Util.WholesaleDBHelper;
import com.omneAgate.wholeSaler.activity.R;
import com.omneAgate.wholeSaler.activity.SearchHistoryActivity;

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
 * Created by root on 19/8/16.
 */
public class OutwardSyncManually {
    Activity context;

    List<WholesalerPostingDto> billList; //list of WholesalerPosting in offline

    int sentCount = 1;

    int billCount = 0;

    Dialog dialog;



    private String serverUrl = "";


   public OutwardSyncManually(Activity context){
        this.context=context;

    }
    public void billSync() {
        serverUrl = WholesaleDBHelper.getInstance(context).getMasterData("serverUrl");
        billList = new ArrayList<>();
        billList = WholesaleDBHelper.getInstance(context).getAllWholesalerPostingForSync();
        billCount = billList.size();
        showDialog();
        billData();
    }

    private void billData() {
        try {
            if (billList.size() > 0) {
                syncWholesalerPostingToServer(billList.get(0));
                if (dialog != null && dialog.isShowing()) {
                    String textData = sentCount + " / " + billCount + " " + context.getString(R.string.outwardsync);
                    ((TextView) dialog.findViewById(R.id.billText)).setText(textData);
                }
            } else {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }

                context.startActivity(new Intent(context, SearchHistoryActivity.class));
                context.finish();

            }
        }catch (Exception e){
            Log.e("OutwardSyncMaually","Exception while processing"+e.toString());
        }
    }
    private void showDialog() {
        try {
            dialog = new Dialog(context, android.R.style.Theme_Translucent);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            //here we set layout of progress dialog
            dialog.setContentView(R.layout.dialog_waiting);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            String textData = sentCount + " / " + billCount + " " + context.getString(R.string.outwardsync);
            ((TextView) dialog.findViewById(R.id.billText)).setText(textData);
            dialog.show();
        }catch (Exception e){
            Log.e("showdialog","error"+e.toString());
           e.printStackTrace();
        }
    }

    private void syncWholesalerPostingToServer(WholesalerPostingDto wholesalerPostingDto) {

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
            Log.e("WholesalerPosting","Manual processing"+ response);
            try {
                if (response != null) {
                    GsonBuilder gsonBuilder = new GsonBuilder();
                    Gson gson = gsonBuilder.create();
                    WholesalerPostingDto updateStock = gson.fromJson(response, WholesalerPostingDto.class);
                    if (updateStock != null && updateStock.getStatusCode() == 0 ) {
                        WholesaleDBHelper.getInstance(context).billUpdate(updateStock.getReferenceNo());

                        Log.e("Info", "Updating status of bill to status T for bill referenceNo " + updateStock.getReferenceNo());
                    } else if (updateStock != null && updateStock.getStatusCode() == 6007) {
                        WholesaleDBHelper.getInstance(context).billUpdate(updateStock.getReferenceNo());
                        Log.e( "Info", "Updating status of bill to status T for bill referenceNo " + updateStock.getReferenceNo());
                    } else {
                        Log.e("Error", "Received null response ");
                    }
                }
            } catch (Exception e) {
                Log.e("Insert Error", e.toString(), e);
            }
            finally {
                sentCount++;
                billList.remove(0);
                billData();
            }
        }
    }

}
