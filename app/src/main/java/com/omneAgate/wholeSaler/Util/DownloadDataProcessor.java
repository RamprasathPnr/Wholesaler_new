package com.omneAgate.wholeSaler.Util;

import android.content.Context;
import android.os.AsyncTask;
import android.provider.Settings;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.omneAgate.wholeSaler.DTO.EnumDTO.TableNames;
import com.omneAgate.wholeSaler.DTO.FirstSynchReqDto;
import com.omneAgate.wholeSaler.DTO.FirstTimeSyncDto;
import com.omneAgate.wholeSaler.DTO.FistSyncInputDto;

import org.apache.commons.lang3.StringUtils;
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
import java.util.Map;

/**
 * Used to download master data
 */
public class DownloadDataProcessor {

    //Activity context
    private final Context context;

    StringEntity stringEntity;

    List<FistSyncInputDto> firstSync;  //FistSync items

    private String serverUrl = "";

    //Constructor
    public DownloadDataProcessor(Context context) {
        this.context = context;
    }


    // Send Request to the server
    public void processor() {
        try {

            String lastModifiedDate = WholesaleDBHelper.getInstance(context).getMasterData("syncTime");
            Log.e("Download data processor",""+lastModifiedDate);
            FirstSynchReqDto fpsRequest = new FirstSynchReqDto();
            String deviceId = Settings.Secure.getString(
                    context.getContentResolver(), Settings.Secure.ANDROID_ID).toUpperCase();
            fpsRequest.setDeviceNum(deviceId);
            fpsRequest.setLastSyncTime(lastModifiedDate);
            serverUrl = WholesaleDBHelper.getInstance(context).getMasterData("serverUrl");
            String updateData = new Gson().toJson(fpsRequest);
            Log.e("Download data processor", updateData);
            stringEntity = new StringEntity(updateData, HTTP.UTF_8);
            new UpdateSyncTask().execute("");

        } catch (Exception e) {
          //  Util.LoggingQueue(context, "Download Error", "Error:" + e.getStackTrace().toString());

            Log.e("Download Error", "Error:" + e.getStackTrace().toString());
        }
    }


    // After response received from server successfully in android
    private void processSyncResponseData(Map<String, Integer> tableDetails) {
        firstSync = new ArrayList<>();
        if (tableDetails != null) {
            if (tableDetails.containsKey("fpsstore")) {
                firstSync.add(getInputDTO("fpsstore", TableNames.fpsstore));
                WholesaleDBHelper.getInstance(context).deleteAll(WholesaleDBTables.TABLE_FPS_STORE);
            }

            if (tableDetails.containsKey("kerosenebunk")) {
                firstSync.add(getInputDTO("kerosenebunk", TableNames.kerosenebunk));
                WholesaleDBHelper.getInstance(context).deleteAll(WholesaleDBTables.TABLE_KEROSENE_BUNK);
            }

            if (tableDetails.containsKey("rrc")) {
                firstSync.add(getInputDTO("rrc", TableNames.rrc));
                WholesaleDBHelper.getInstance(context).deleteAll(WholesaleDBTables.TABLE_RRC);
            }

            if (tableDetails.containsKey("product")) {
                firstSync.add(getInputDTO("product", TableNames.product));
            }


            if (firstSync.size() > 0) {
                FirstSynchReqDto fpsRequest = new FirstSynchReqDto();
                String lastModifiedDate = WholesaleDBHelper.getInstance(context).getMasterData("syncTime");
                fpsRequest.setLastSyncTime(lastModifiedDate);
                String deviceId = Settings.Secure.getString(
                        context.getContentResolver(), Settings.Secure.ANDROID_ID).toUpperCase();
                fpsRequest.setDeviceNum(deviceId);
                fpsRequest.setTableName(firstSync.get(0).getTableName());

                setTableSyncCall(fpsRequest);
            }
        }

    }
    /**
     * Request for datas by giving name of table to server
     * <p/>
     * input FirstSynchReqDto fpsRequest
     */

    private void setTableSyncCall(FirstSynchReqDto fpsRequest) {
        try {
            serverUrl = WholesaleDBHelper.getInstance(context).getMasterData("serverUrl");
            String updateData = new Gson().toJson(fpsRequest);
            Log.e("Request sync call", updateData);
            //Util.LoggingQueue(context, "Download Sync", "Req:" + updateData);
            Log.e("Download Sync", "Req:" + updateData);
            stringEntity = new StringEntity(updateData, HTTP.UTF_8);
            new UpdateTablesTask().execute("");
        } catch (Exception e) {
            Log.e("Error", e.toString(), e);
        }
    }

    /**
     * Response received from server
     */
    private void setTableResponse(String response) {
        if (StringUtils.isNotEmpty(response)) {
            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson = gsonBuilder.create();
            Log.e("setTableResponse", response);
            FirstTimeSyncDto fpsDataDto = gson.fromJson(response, FirstTimeSyncDto.class);
            insertIntoDatabase(fpsDataDto);
        }
    }

    /**
     * Database insertion of received data
     */
    private void insertIntoDatabase(FirstTimeSyncDto firstTimeSyncDto) {
        try {

            FistSyncInputDto fistSyncInputDto = firstSync.get(0);
            switch (fistSyncInputDto.getTableNames()) {

                case product:
                    WholesaleDBHelper.getInstance(context).insertProductData(firstTimeSyncDto.getProducts());
                    break;
                case kerosenebunk:
                   // WholesaleDBHelper.getInstance(context).deleteAll(WholesaleDBTables.TABLE_KEROSENE_BUNK);
                    WholesaleDBHelper.getInstance(context).insertKbunk(firstTimeSyncDto.getKeroseneBunks());
                    break;
                case rrc:
                  //  WholesaleDBHelper.getInstance(context).deleteAll(WholesaleDBTables.TABLE_RRC);
                    WholesaleDBHelper.getInstance(context).insertRrcData(firstTimeSyncDto.getRrcs());
                    break;
                case fpsstore:
                  //  WholesaleDBHelper.getInstance(context).deleteAll(WholesaleDBTables.TABLE_FPS_STORE);
                    WholesaleDBHelper.getInstance(context).insertFPS(firstTimeSyncDto.getFpsStores());
                    break;
                default:
                    break;
            }

            afterDatabaseInsertion(firstTimeSyncDto);

        } catch (Exception e) {
        }
    }

    /*
    * After database insertion by user master this function called
    * */
    private void afterDatabaseInsertion(FirstTimeSyncDto firstSynchResDto) throws Exception {
        FirstSynchReqDto fpsRequest = new FirstSynchReqDto();
        String deviceId = Settings.Secure.getString(
                context.getContentResolver(), Settings.Secure.ANDROID_ID).toUpperCase();
        fpsRequest.setDeviceNum(deviceId);
        String lastModifiedDate = WholesaleDBHelper.getInstance(context).getMasterData("syncTime");
        fpsRequest.setLastSyncTime(lastModifiedDate);
        if (firstSynchResDto.isHasMore()) {
            fpsRequest.setTotalCount(firstSynchResDto.getTotalCount());
            fpsRequest.setTotalSentCount(firstSynchResDto.getTotalSentCount());
            fpsRequest.setCurrentCount(firstSynchResDto.getCurrentCount());
            fpsRequest.setTableName(firstSync.get(0).getTableName());
            setTableSyncCall(fpsRequest);
        } else {
            firstSync.remove(0);
            if (firstSync.size() > 0) {
                fpsRequest.setTableName(firstSync.get(0).getTableName());
                setTableSyncCall(fpsRequest);
            } else {
                firstSyncSuccess();
            }
        }
    }

    /**
     * After sync success this method will call
     */
    private void firstSyncSuccess() {
        try {
            FirstSynchReqDto fpsRequest = new FirstSynchReqDto();
            String deviceId = Settings.Secure.getString(
                    context.getContentResolver(), Settings.Secure.ANDROID_ID).toUpperCase();
            fpsRequest.setDeviceNum(deviceId);
            serverUrl = WholesaleDBHelper.getInstance(context).getMasterData("serverUrl");
            String updateData = new Gson().toJson(fpsRequest);
            stringEntity = new StringEntity(updateData, HTTP.UTF_8);
           // Util.LoggingQueue(context, "Download Sync success", "Req:" + updateData);
            Log.e("Download Sync success", "Req:" + updateData);
            new SyncSuccess().execute("");

        } catch (Exception e) {
            //Util.LoggingQueue(context, "Error", e.toString());
            Log.e("Error in First sync", e.toString(), e);
        }
    }

    private void firstSyncSuccessResponse(String response) {
        try {
            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson = gsonBuilder.create();
            FirstTimeSyncDto fpsDataDto = gson.fromJson(response, FirstTimeSyncDto.class);
            int statusCode = fpsDataDto.getStatusCode();
            Log.e("Regular sync","sync complete time "+fpsDataDto.getLastSyncTime());
            if (statusCode == 0) {
                WholesaleDBHelper.getInstance(context).updateMaserData("syncTime", fpsDataDto.getLastSyncTime());
            }
        } catch (Exception e) {
        }
    }

    /**
     * returns FistSyncInputDto of details of tables received from server
     */
    private FistSyncInputDto getInputDTO(String tableName, TableNames names) {
        FistSyncInputDto inputDto = new FistSyncInputDto();
        inputDto.setTableName(tableName);
        inputDto.setTableNames(names);
        inputDto.setDynamic(true);
        return inputDto;
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
        postRequest.setHeader("Cookie", "SESSION=" + SessionId.getInstance().getSessionId());
        postRequest.setEntity(entity);
        return client.execute(postRequest);

    }

    /**
     * Async   task for Download Sync for data in table
     */
    private class UpdateTablesTask extends AsyncTask<String, String, String> {


        @Override
        protected String doInBackground(String... f_url) {
            BufferedReader in = null;
            try {
                String url = serverUrl + "/wholesale/firstSync";
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
                Log.e("Response", responseData);
                return responseData;
            } catch (Exception e) {
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
            //Util.LoggingQueue(context, "Download Sync", "Response:" + response);
            Log.e("Download Sync", "Response:" + response);
            setTableResponse(response);
        }
    }

    /**
     * Async   task for Download Sync for data in table
     */
    private class SyncSuccess extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... f_url) {
            BufferedReader in = null;
            try {
                String url = serverUrl + "/wholesale/completesynch";
                Log.e("server Url", url);
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
                //Util.LoggingQueue(context, "Download Sync success", "Response:" + response);
                Log.e("Download Sync success", "Response:" + response);
                firstSyncSuccessResponse(response);
            }
        }
    }

    //Async   task for Download Sync
    private class UpdateSyncTask extends AsyncTask<String, String, String> {


        @Override
        protected String doInBackground(String... f_url) {
            BufferedReader in = null;
            try {
                String url = serverUrl + "/wholesale/firstSyncdetails";
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
                //Util.LoggingQueue(context, "Error", "Network exception" + e.getMessage());
                Log.e("Error", "Network exception" + e.getMessage());
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
            try {
                GsonBuilder gsonBuilder = new GsonBuilder();
                Gson gson = gsonBuilder.create();
                Log.e("Response get details", ""+response);
                FirstTimeSyncDto fpsDataDto = gson.fromJson(response, FirstTimeSyncDto.class);
                int statusCode = fpsDataDto.getStatusCode();
                if (statusCode == 0) {
                    processSyncResponseData(fpsDataDto.getTableDetails());
                }
            } catch (Exception e) {
                //Util.LoggingQueue(context, "Error", e.toString());
                Log.e("Excep in Resp", e.toString(), e);
            }
        }
    }

}