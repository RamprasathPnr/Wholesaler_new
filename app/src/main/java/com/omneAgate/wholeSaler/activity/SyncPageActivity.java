package com.omneAgate.wholeSaler.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.cardinalsolutions.android.arch.autowire.AndroidView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.omneAgate.wholeSaler.DTO.EnumDTO.ServiceListenerType;
import com.omneAgate.wholeSaler.DTO.EnumDTO.TableNames;
import com.omneAgate.wholeSaler.DTO.FirstSynchReqDto;
import com.omneAgate.wholeSaler.DTO.FirstTimeSyncDto;
import com.omneAgate.wholeSaler.DTO.FistSyncInputDto;
import com.omneAgate.wholeSaler.Util.SessionId;
import com.omneAgate.wholeSaler.Util.WholesaleDBHelper;
import com.omneAgate.wholeSaler.activity.dialog.RetryFailedDialog;
import com.omneAgate.wholeSaler.activity.dialog.RetryDialog;

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;


//This class used for sync
public class SyncPageActivity extends CircleBaseActivity {


    ScrollView loadScroll; //Scroll bar instance

    @AndroidView(R.id.circle_progress_bar)

    private ProgressBar mProgress;

    StringEntity stringEntity;   //StringEntity for sending data

    LinearLayout layout; // Layout for textView insert

    //ProgressBar for loading

    int retryCount = 0;   //user retry count

    List<FistSyncInputDto> firstSync;  //FistSync items

    int totalProgress = 6;  //Progressbar item add

    String serverUrl;

    TextView percentage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync_page);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        layout = (LinearLayout) findViewById(R.id.info);
        loadScroll = (ScrollView) findViewById(R.id.scrollData);
        firstTimeSyncDetails();
        Log.e("Sync Page", "Starting up Sync");
    }


    /**
     * Send request to server
     * <p/>
     * for getting table details
     */
    public void firstTimeSyncDetails() {
        try {
            FirstSynchReqDto firstSynchReqDto = new FirstSynchReqDto();
            String deviceId = Settings.Secure.getString(
                    getContentResolver(), Settings.Secure.ANDROID_ID).toUpperCase();
            firstSynchReqDto.setDeviceNum(deviceId);//WholesalerId
            serverUrl = WholesaleDBHelper.getInstance(this).getMasterData("serverUrl");
            String updateData = new Gson().toJson(firstSynchReqDto);
            Log.e("syncPageActivity", " first sync Request " + updateData);
            stringEntity = new StringEntity(updateData, HTTP.UTF_8);
            new UpdateSyncTask().execute("");

        } catch (Exception e) {
            Log.e("SyncPageActivity", e.toString(), e);
        }
    }


    /**
     * After response received from server successfully in android
     * Table details fetched in MAP
     * if tableDetails is empty or null user need to retry
     */
    private void processSyncResponseData(String response) {
        try {
            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson = gsonBuilder.create();
            FirstTimeSyncDto fpsDataDto = gson.fromJson(response, FirstTimeSyncDto.class);

            Log.e("FpsDatadto", fpsDataDto.toString());
            int statusCode = fpsDataDto.getStatusCode();
            if (statusCode == 0) {
                Log.e("getTableDetails", fpsDataDto.getTableDetails().toString());
                if ((fpsDataDto.getTableDetails() == null || fpsDataDto.getTableDetails().isEmpty())) {
                    errorInSync();
                    return;
                }
                syncTableDetails(fpsDataDto.getTableDetails());

            }
        } catch (Exception e) {
            //Util.LoggingQueue(this, "Error", e.toString());
            Log.e("SyncPageActivity", e.toString(), e);
            errorInSync();
        }

    }

    /**
     * Used to get number of items in server table
     * TableDetails map is input
     * If masters count 0 user should retry
     */
    private void syncTableDetails(Map<String, Integer> tableDetails) {
        firstSync = new ArrayList<>();
        List<String> masterDataEmpty = new ArrayList<>();
        if (tableDetails.get("fpsstore") != null) {
            int count = tableDetails.get("fpsstore");
            Log.e("fpsStoreCount", "" + count);
            if (count == 0) {
                masterDataEmpty.add("fpsstore");
            }
            Log.e("TABLE FPS", "total count :" + count);
            firstSync.add(getInputDTO("fpsstore", count, "Table FPS Store downloading", "Store downloaded with", TableNames.fpsstore));
        }
        if (tableDetails.get("kerosenebunk") != null) {
            int count = tableDetails.get("kerosenebunk");
            if (count == 0) {
                masterDataEmpty.add("kerosenebunk");
            }
            Log.e("TABLE Kerosene Bunk", "total count :" + count);
            firstSync.add(getInputDTO("kerosenebunk", count, "Table Kerosene Bunk downloading", "Kerosene Bunk downloaded with", TableNames.kerosenebunk));

        }
        if (tableDetails.get("rrc") != null) {
            int count = tableDetails.get("rrc");
            if (count == 0) {
                masterDataEmpty.add("rrc");
            }
            Log.e("TABLE RRC", "total count :" + count);
            firstSync.add(getInputDTO("rrc", count, "Table RRC downloading", "RRC downloaded with", TableNames.rrc));
        }

        if (tableDetails.get("stockOutward") != null) {
            int count = tableDetails.get("stockOutward");
            Log.e("stockOutwardCount", "" + count);
            if (count == 0) {
                masterDataEmpty.add("stockOutward");
            }
            Log.e("TABLE STOCK OUTWARD", "total count :" + count);
            firstSync.add(getInputDTO("stockOutward", count, "Table stockOutward downloading", "stockOutward downloaded with", TableNames.stockOutward));

        }

        if (tableDetails.get("product") != null) {
            int count = tableDetails.get("product");

            if (count == 0) {
                masterDataEmpty.add("product");
            }
            Log.e("TABLE PRODUCT", "total count :" + count);
            firstSync.add(getInputDTO("product", count, "Table Product downloading", "Product downloaded with", TableNames.product));

        }

        totalProgress = 100 / firstSync.size();
        FirstSynchReqDto fpsRequest = new FirstSynchReqDto();
        String deviceId = Settings.Secure.getString(
                getContentResolver(), Settings.Secure.ANDROID_ID).toUpperCase();
        fpsRequest.setDeviceNum(deviceId);
        fpsRequest.setTableName(firstSync.get(0).getTableName());
        Log.e("FirstSyncRequest", fpsRequest.toString());
        setTableSyncCall(fpsRequest);
        setTextStrings(firstSync.get(0).getTextToDisplay() + "....");


    }

    /**
     * Request for datas by giving name of table to server
     * <p/>
     * input FirstSynchReqDto fpsRequest
     */

    private void setTableSyncCall(FirstSynchReqDto fpsRequest) {
        try {
            String updateData = new Gson().toJson(fpsRequest);
            Log.e("FirstSyncRequest", fpsRequest.toString());
            Log.e("i", updateData);
            stringEntity = new StringEntity(updateData, HTTP.UTF_8);
            new UpdateTablesTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");
        } catch (Exception e) {
            Log.e("SyncPageActivity", e.toString(), e);
        }
    }

    /**
     * returns FistSyncInputDto of details of tables received from server
     */
    private FistSyncInputDto getInputDTO(String tableName, int count, String textToDisplay, String endText, TableNames names) {
        FistSyncInputDto inputDto = new FistSyncInputDto();
        inputDto.setTableName(tableName);
        inputDto.setCount(count);
        inputDto.setTableNames(names);
        inputDto.setTextToDisplay(textToDisplay);
        inputDto.setEndTextToDisplay(endText);
        inputDto.setDynamic(true);
        return inputDto;
    }

    /**
     * After sync success this method will call
     */
    private void firstSyncSuccess() {
        try {

            FirstSynchReqDto fpsRequest = new FirstSynchReqDto();
            String deviceId = Settings.Secure.getString(
                    getContentResolver(), Settings.Secure.ANDROID_ID).toUpperCase();
            fpsRequest.setDeviceNum(deviceId);
            String updateData = new Gson().toJson(fpsRequest);
            stringEntity = new StringEntity(updateData, HTTP.UTF_8);
            new SyncSuccess().execute("");

        } catch (Exception e) {
            Log.e("SyncPageActivity", e.toString(), e);
        }
    }

    private void firstSyncSuccessResponse(String response) {
        try {
            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson = gsonBuilder.create();
            FirstTimeSyncDto fpsDataDto = gson.fromJson(response, FirstTimeSyncDto.class);
            Log.e("FpsSyncTimeServer", fpsDataDto.toString());
            int statusCode = fpsDataDto.getStatusCode();
            if (statusCode == 0) {

                if ((fpsDataDto.getLastSyncTime() == null)) {
                    errorInSync();
                    return;
                }
                mProgress.setProgress(100);
                percentage.setText("100%");
                WholesaleDBHelper.getInstance(SyncPageActivity.this).updateMaserData("syncTime", fpsDataDto.getLastSyncTime());
                Button continueButton = (Button) findViewById(R.id.syncContinue);
                ((TextView) findViewById(R.id.downloadCompleted)).setText("Download Completed......");
                ((TextView) findViewById(R.id.textView2)).setText("Database Sync Completed........");
//                setTamilText((TextView) findViewById(R.id.downloadCompleted), R.string.download_completed);
//                setTamilText((TextView) findViewById(R.id.syncIndicator), R.string.sync_page_completed);
                continueButton.setBackgroundColor(Color.parseColor("#ffff0000"));
                continueButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Log.e(SyncPageActivity.class.getSimpleName(), "SyncSuccess");
                        startActivity(new Intent(SyncPageActivity.this, AdminActivity.class));
                        finish();
                    }
                });

            }
        } catch (Exception e) {
            Log.e(SyncPageActivity.class.getSimpleName() + "Sync Page", "Error in sync:" + e.getStackTrace().toString());
            errorInSync();
        }
    }

    private void syncSuccessCompletion() {
        String referenceNumberLatest = WholesaleDBHelper.getInstance(this).updateReferenceNumberFirstTimeSync();
        Log.e("ReferenceNo", referenceNumberLatest);
        if (referenceNumberLatest.length() > 5) {
            Log.e("auronumber", "" + referenceNumberLatest.substring(referenceNumberLatest.length() - 4));
            // WholesaleDBHelper.getInstance(this).updateMaserData("autoNumber", "" + referenceNumberLatest.substring(referenceNumberLatest.length()-4));
            WholesaleDBHelper.getInstance(this).updateMaserData("currentDate", getCurrentDate());

        } else {
            Log.e("ReferenceNumberLength", "less than 12");
        }


    }

    private String getCurrentDate() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        String formattedDate = df.format(c.getTime());
        return formattedDate;

    }

    /**
     * Progress bar setting in activity
     */
    private void setDownloadedProgress() {
        try {
            int progress = mProgress.getProgress();
            progress = progress + totalProgress;
            Log.e("sync activity ", "total count " + progress);
            mProgress.setProgress(progress);
            percentage = (TextView) findViewById(R.id.percentagetct);
            percentage.setText("" + progress + " " + "%");
        } catch (Exception e) {
            Log.e("Exception ", " Downloaded progress " + e.toString());
        }
    }

    /**
     * Response received from server
     */
    private void setTableResponse(String response) {
        Log.e("syncpageactivity", "set table response" + response.toString());
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        FirstTimeSyncDto fpsDataDto = gson.fromJson(response, FirstTimeSyncDto.class);
        Log.e("syncpageactivity", "after table response" + fpsDataDto.toString());
        if (fpsDataDto.getStatusCode() == 0) {
            insertIntoDatabase(fpsDataDto);
        } else {
            errorInSync();
        }
    }

    /**
     * Scrolling of received String
     */
    private void setTextStrings(String syncString) {
        TextView tv = new TextView(SyncPageActivity.this);
        tv.setText(syncString);
        tv.setTextColor(Color.parseColor("#5B5B5B"));
        tv.setTextSize(17);
        layout.addView(tv);
        loadScroll.post(new Runnable() {
            @Override
            public void run() {
                loadScroll.fullScroll(View.FOCUS_DOWN);
            }
        });
    }

    /**
     * Database insertion of received data
     */
    private void insertIntoDatabase(FirstTimeSyncDto firstTimeSyncDto) {
        Log.e("syncpageactivity", "insert into db : " + firstTimeSyncDto.toString());
        FistSyncInputDto fistSyncInputDto = firstSync.get(0);
        setTextStrings(firstSync.get(0).getEndTextToDisplay() + " items " + firstTimeSyncDto.getTotalSentCount() + "....");
        switch (fistSyncInputDto.getTableNames()) {

            case product:
                Log.e("GetProduct", firstTimeSyncDto.getProducts().toString());
                WholesaleDBHelper.getInstance(this).insertProductData(firstTimeSyncDto.getProducts());
                break;

            case kerosenebunk:
                WholesaleDBHelper.getInstance(this).insertKbunk(firstTimeSyncDto.getKeroseneBunks());
                break;

            case rrc:
                WholesaleDBHelper.getInstance(this).insertRrcData(firstTimeSyncDto.getRrcs());
                break;
            case fpsstore:
                WholesaleDBHelper.getInstance(this).insertFPS(firstTimeSyncDto.getFpsStores());
                break;

            case stockOutward:
                Log.e("StockOutward", firstTimeSyncDto.getPostingInfo().toString());
                WholesaleDBHelper.getInstance(this).insertTransactionDetails(firstTimeSyncDto.getPostingInfo());
                syncSuccessCompletion();

                break;

            default:
                break;
        }

        afterDatabaseInsertion(firstTimeSyncDto);

    }


    /*
   * After database insertion by user master this function called
   * */
    private void afterDatabaseInsertion(FirstTimeSyncDto firstSynchResDto) {
        Log.e("afterdbinsertion", "first sunch res dto :" + firstSynchResDto.toString());
        FirstSynchReqDto fpsRequest = new FirstSynchReqDto();
        String deviceId = Settings.Secure.getString(
                getContentResolver(), Settings.Secure.ANDROID_ID).toUpperCase();
        fpsRequest.setDeviceNum(deviceId);
        if (firstSynchResDto.isHasMore()) {
            Log.e("syncpageactivity", "hasmore");
            fpsRequest.setTotalCount(firstSynchResDto.getTotalCount());
            fpsRequest.setTotalSentCount(firstSynchResDto.getTotalSentCount());
            fpsRequest.setCurrentCount(firstSynchResDto.getCurrentCount());
            fpsRequest.setTableName(firstSync.get(0).getTableName());
            setTableSyncCall(fpsRequest);
        } else {
            Log.e("syncpageactivity", "before deletion " + firstSync.toString());
            firstSync.remove(0);
            Log.e("syncpageactivity", "after deletion " + firstSync.toString());
            setDownloadedProgress();
            if (firstSync.size() > 0) {
                Log.e("syncpageactivity", "size greater than zero");
                fpsRequest.setTableName(firstSync.get(0).getTableName());
                setTextStrings(firstSync.get(0).getTextToDisplay() + "....");
                setTableSyncCall(fpsRequest);
            } else {
                firstSyncSuccess();
            }
        }
    }


    /**
     * user logout
     */
    public void logOut() {
        WholesaleDBHelper.getInstance(this).closeConnection();
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    private void errorInSync() {
        layout.removeAllViews();
        mProgress.setProgress(0);
        retryCount++;
        if (retryCount >= 3) {
            new RetryFailedDialog(this).show();
        } else {
            new RetryDialog(this, retryCount).show();
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
        postRequest.setHeader("Cookie", "SESSION=" + SessionId.getInstance().getSessionId());
        postRequest.setEntity(entity);
        return client.execute(postRequest);

    }

    /**
     * Concrete method
     */

    @Override
    public void onBackPressed() {

    }

    /**
     * Async   task for Download Sync for table details
     */
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
                //Util.LoggingQueue(SyncPageActivity.this, "Error", "Network exception" + e.getMessage());
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
                Log.e("syncpageactivity", "firstsync response" + response);
                // Util.LoggingQueue(SyncPageActivity.this, "Sync Page", "First sync resp:" + response);
                processSyncResponseData(response);
            } else {
                errorInSync();
            }
        }
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
                return responseData;
            } catch (Exception e) {
                // Util.LoggingQueue(SyncPageActivity.this, "Error", "Network exception" + e.getMessage());
                Log.e("SyncPageActivity", e.toString(), e);
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

            Log.e("testing response", "" + response);
            if (response != null) {
                //Util.LoggingQueue(SyncPageActivity.this, "Sync Page", "Table Wise Sync resp" + response);
                Log.e("FirstSyncResponse", response);
                setTableResponse(response);
            } else {
                errorInSync();
            }
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
                Log.e(SyncPageActivity.class.getSimpleName() + "Error", "Network exception" + e.getMessage());
                Log.e("SyncPageActivity", e.toString(), e);
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
                Log.i("SyncSuccessResponse", response);
                firstSyncSuccessResponse(response);
            }
        }
    }
}
