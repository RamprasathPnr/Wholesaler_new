package com.omneAgate.wholeSaler.activity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.omneAgate.wholeSaler.DTO.EnumDTO.RequestType;
import com.omneAgate.wholeSaler.DTO.EnumDTO.ServiceListenerType;
import com.omneAgate.wholeSaler.DTO.LoginDto;
import com.omneAgate.wholeSaler.DTO.WholeSaleLoginResponseDto;
import com.omneAgate.wholeSaler.DTO.WholesalerPostingDto;
import com.omneAgate.wholeSaler.Util.Constants.WholeSaleConstants;
import com.omneAgate.wholeSaler.Util.CustomProgressDialog;
import com.omneAgate.wholeSaler.Util.NetworkConnection;
import com.omneAgate.wholeSaler.Util.NetworkUtil;
import com.omneAgate.wholeSaler.Util.SessionId;
import com.omneAgate.wholeSaler.Util.Util;
import com.omneAgate.wholeSaler.Util.WholesaleDBHelper;
import com.omneAgate.wholeSaler.activity.dialog.OutwardUpdationDialog;
import com.omneAgate.wholeSaler.service.HttpClientWrapper;

import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HTTP;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class OutwardConformationActivity extends BaseActivity {

    private Toolbar mToolbar;
    TextView title;
    private ActionBar mActionBar;

    //WholeSaler Posting Details-outward
    WholesalerPostingDto wholesalerPostingDto;
    //Submited or not
    private boolean isSubmitted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        status = true;
        getReferenceNumber();
        setContentView(R.layout.activity_outward_conformation);
        String intentResponse = getIntent().getExtras().getString("IntentPostingRequest");
        Log.e("Intent Response", "" + intentResponse);
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        wholesalerPostingDto = gson.fromJson(intentResponse, WholesalerPostingDto.class);
        Log.e("WholesalerPostingDto", "" + wholesalerPostingDto);
        setPage(wholesalerPostingDto);
        networkConnection = new NetworkConnection(this);
        appState = (GlobalAppState) getApplication();
        httpConnection = new HttpClientWrapper();
    }

    //Initialise Page
    private void setPage(WholesalerPostingDto wholesalerPostingDto) {
        setUpPopUpPage();
        SetTopLayout();

        // ((TextView)findViewById(R.id.top_textView)).setText(getString(R.string.outward));
        //  ((TextView)findViewById(R.id.keroceneLabel)).setText(getString(R.string.kerosene));
        ((TextView) findViewById(R.id.tvOutwardLabel)).setText(getString(R.string.outwardTo));
        ((TextView) findViewById(R.id.tvDriverNameLabel)).setText(getString(R.string.driverName));
        ((TextView) findViewById(R.id.tvContactNumberLabel)).setText(getString(R.string.driverContactNumber));
        ((TextView) findViewById(R.id.tvTransporterNameLabel)).setText(getString(R.string.transporterName));
        ((TextView) findViewById(R.id.tvVehicleNumberLabel)).setText(getString(R.string.vehicleNo));
        ((TextView) findViewById(R.id.tvProductNameLabel)).setText(getString(R.string.productType));
        ((TextView) findViewById(R.id.tvQuantityLabel)).setText(getString(R.string.productQuantity) + " *");
        ((Button) findViewById(R.id.btnEdit)).setText(getString(R.string.edit));
        ((Button) findViewById(R.id.btnSubmit)).setText(getString(R.string.confirm));
        //  ((TextView)findViewById(R.id.currentdate)).setText(""+getCurrentDate());
        String receipientType = wholesalerPostingDto.getRecipientType();

        if (GlobalAppState.language.equalsIgnoreCase("ta")) {
            if (receipientType.equalsIgnoreCase("Kerosene Bunk")) {
                receipientType = "மண்ணெண்ணெய் கடை";
            } else if (receipientType.equalsIgnoreCase("FPS")) {
                receipientType = " நியாய விலைக் கடை";
            } else {
                receipientType = " ஆர்ஆர்சி";
            }
        }

        ((TextView) findViewById(R.id.tvOutwardTo)).setText(receipientType);
        ((TextView) findViewById(R.id.tvCode)).setText(wholesalerPostingDto.getCode());
        ((TextView) findViewById(R.id.tvCodeLabel)).setText(receipientType + " " + getResources().getString(R.string.code) + " *");
        ((TextView) findViewById(R.id.tvDriverName)).setText(wholesalerPostingDto.getDriverName());
        ((TextView) findViewById(R.id.tvContactNumber)).setText(wholesalerPostingDto.getDrivermobileNumber());
        ((TextView) findViewById(R.id.tvTransporterName)).setText(wholesalerPostingDto.getTransportName());
        ((TextView) findViewById(R.id.tvVehicleNumber)).setText(wholesalerPostingDto.getVehicleNumber());
        ((TextView) findViewById(R.id.tvProductName)).setText(wholesalerPostingDto.getPruductName());
        NumberFormat unitFormat = new DecimalFormat("#0.000");
        ((TextView) findViewById(R.id.tvQuantity)).setText("" + unitFormat.format(wholesalerPostingDto.getQuantity()));
    }

    private void SetTopLayout() {
        try {
            mToolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(mToolbar);
            title = (TextView) findViewById(R.id.title_toolbar);
            title.setText(R.string.outward);
            title.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.outward_size));
            mActionBar = getSupportActionBar();
            if (getSupportActionBar() != null) {
                getSupportActionBar().setHomeAsUpIndicator(R.drawable.arrow_back);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }

            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBackPressed();
                }
            });
            setTitle("");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_right, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_drawer) {
            mDrawerLayout.openDrawer(Gravity.RIGHT);
        }
        return super.onOptionsItemSelected(item);
    }

    //On Submit
    public void onSubmit(View view) {
        try {
            Log.e("outward submitted", "<==========  outward submitted started  ===========>");
            ((Button) findViewById(R.id.btnSubmit)).setEnabled(false);
            ((Button) findViewById(R.id.btnSubmit)).setClickable(false);
            ((Button) findViewById(R.id.btnEdit)).setEnabled(false);
            isSubmitted = true;
            progressBar = new CustomProgressDialog(this);
            progressBar.setCanceledOnTouchOutside(false);

            String wholesaler_id = WholesaleDBHelper.getInstance(this).getMasterData("wholesalerid");
            wholesalerPostingDto.setReferenceNo("" + getReferenceNumber());
            if (wholesalerPostingDto.getReferenceNo() == null) {
                Toast.makeText(this, getString(R.string.internalError), Toast.LENGTH_SHORT).show();
                Log.e("outward confirmation", "Reference Number null");
            }

            WholesaleDBHelper.getInstance(this).insertKeroseneStockOutward(wholesalerPostingDto, "R");//Store in Local DB for outward with status 'R'

            if (networkConnection.isNetworkAvailable()) {
                String url = "/wholesale/posting";
                Log.e("Submit called", "" + url);
                String postingRequest = new Gson().toJson(wholesalerPostingDto);
                Log.e("PostingRequest", "" + postingRequest);
                StringEntity se = new StringEntity(postingRequest, HTTP.UTF_8);
                progressBar.show();
                if (wholesalerPostingDto.getReferenceNo() == null) {
                    Toast.makeText(this, getString(R.string.internalError), Toast.LENGTH_SHORT).show();
                    Log.e("outward confirmation", "Reference Number null");
                }
                httpConnection.sendRequest(url, null, ServiceListenerType.OUTWARD,
                        SyncHandler, RequestType.POST, se, this);
            } else {
                progressBar = new CustomProgressDialog(this);
                progressBar.setCanceledOnTouchOutside(false);
                dismissProgress();
                OutwardUpdationDialog outwardUpdationDialog = new OutwardUpdationDialog(this, wholesalerPostingDto.getReferenceNo());
                outwardUpdationDialog.show();

            }
        } catch (Exception e) {
            dismissProgress();
            Log.e("OutwardConfirmation", e.toString(), e);
        }
    }

    @Override
    protected void processMessage(Bundle message, ServiceListenerType what) {

        switch (what) {
            case OUTWARD:
                Log.e("outward confirmation", "outward response");
                postingResponse(message);
                break;

            default:
                Log.e("outward confirmation", "default response is called");
                if (progressBar != null) progressBar.dismiss();
                OutwardUpdationDialog outwardUpdationDialog = new OutwardUpdationDialog(this, wholesalerPostingDto.getReferenceNo());
                outwardUpdationDialog.show();
                break;
        }
    }

    /**
     * After Outward response received from server successfully in android
     *
     * @params bundle of message that received
     */
    private void postingResponse(Bundle message) {
        try {
            Log.e("outwardResponse ", "" + message.toString());
            String response = message.getString(WholeSaleConstants.RESPONSE_DATA);
            if (response.contains("Unauthorized") || response == null) {
                Log.e("outwardResponse ", "Unauthorized");
                dismissProgress();
                OutwardUpdationDialog outwardUpdationDialog = new OutwardUpdationDialog(this, wholesalerPostingDto.getReferenceNo());
                outwardUpdationDialog.show();
                return;
            } else {
                Log.e("OutwardConfirmation", "else Request response:" + response);
                GsonBuilder gsonBuilder = new GsonBuilder();
                Gson gson = gsonBuilder.create();
                WholesalerPostingDto wholesalerPostingResponse = gson.fromJson(response, WholesalerPostingDto.class);
                if (wholesalerPostingResponse != null) {
                    dismissProgress();
                    Log.e("OutwardConfirmation", "status code : " + wholesalerPostingResponse.getStatusCode());
                    if (wholesalerPostingResponse.getStatusCode() == 0) {
                        OutwardUpdationDialog outwardUpdationDialog = new OutwardUpdationDialog(this, wholesalerPostingResponse.getReferenceNo());
                        outwardUpdationDialog.show();
                        WholesaleDBHelper.getInstance(this).billUpdate(wholesalerPostingDto.getReferenceNo());

                    } else if (wholesalerPostingResponse.getStatusCode() == 2000) {
                        OutwardUpdationDialog outwardUpdationDialog = new OutwardUpdationDialog(this, wholesalerPostingDto.getReferenceNo());
                        outwardUpdationDialog.show();
                    } else {
                        //  ((Button)findViewById(R.id.btnSubmit)).setEnabled(true);
                        String messages = Util.messageSelection(WholesaleDBHelper.getInstance(this).retrieveLanguageTable(wholesalerPostingResponse.getStatusCode()));
                        Toast.makeText(this, "" + messages, Toast.LENGTH_SHORT).show();
                        if (wholesalerPostingResponse.getStatusCode() == 6007) {
                            OutwardUpdationDialog outwardUpdationDialog = new OutwardUpdationDialog(this, wholesalerPostingResponse.getReferenceNo());
                            outwardUpdationDialog.show();
                            //   WholesaleDBHelper.getInstance(this).updateMaserData("autoNumber", "" + wholesalerPostingDto.getReferenceNo().substring(8));
                            WholesaleDBHelper.getInstance(this).updateMaserData("currentDate", wholesalerPostingDto.getReferenceNo().substring(0, 8));
                        }
                    }

                } else {
                    dismissProgress();
                    Toast.makeText(this, getString(R.string.serviceNotAvailable), Toast.LENGTH_SHORT).show();

                }
            }
        } catch (Exception e) {
            dismissProgress();
            Log.e("OutwardConfirmation", "Exception" + e.toString());
        } finally {
            Log.e("OutwardConfirmation", "<====== outward posting completed======>");
        }
    }

    private void dismissProgress() {
        if (progressBar != null) {
            if (progressBar.isShowing()) {
                progressBar.dismiss();
            }
        }
    }


    public void loginDevice(String password) {
        try {
            WholeSaleLoginResponseDto loginResponseDto = WholesaleDBHelper.getInstance(this).getUserDetails(SessionId.getInstance().getUserId());
            LoginDto loginCredentials = new LoginDto();
            loginCredentials.setUserName(loginResponseDto.getUserDetailDto().getUserId());
            loginCredentials.setDeviceId(Settings.Secure.getString(
                    getContentResolver(), Settings.Secure.ANDROID_ID).toUpperCase());
            loginCredentials.setPassword(password);
            String login = new Gson().toJson(loginCredentials);
            StringEntity stringEntity = new StringEntity(login, HTTP.UTF_8);
            if (NetworkUtil.getConnectivityStatus(this) == 0) {
                Toast.makeText(this, getString(R.string.noNetworkConnection), Toast.LENGTH_SHORT).show();
            } else {
                httpConnection = new HttpClientWrapper();
                String url = "/login/wholesale";
                Log.e(OutwardConformationActivity.class.getSimpleName(), "Posting" + "Sending outward request to server" + stringEntity);
                progressBar = new CustomProgressDialog(this);
                progressBar.setCancelable(false);
                progressBar.show();
                httpConnection.sendRequest(url, null, ServiceListenerType.LOGIN_USER,
                        SyncHandler, RequestType.POST, stringEntity, this);
            }
        } catch (Exception e) {
            Log.e("Error", "LoginError", e);
        }
    }

    public void onBackArrow(View view) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        if (isSubmitted) {
            //on Back Pressed disabled
        } else {
            String postingRequest = new Gson().toJson(wholesalerPostingDto);
            startActivity(new Intent(this, OutwardEntryActivity.class).putExtra("activityName", OutwardConformationActivity.class.getSimpleName()).putExtra("IntentPostingRequest", postingRequest));
            finish();
        }

    }

    public void onCancel(View view) {
        onBackPressed();
    }

    private String getReferenceNumber() {
        try {
            String referenceNumber;
            String wholesaler_id = WholesaleDBHelper.getInstance(this).getMasterData("wholesalerid");
            String previousDate = WholesaleDBHelper.getInstance(this).getMasterData("currentDate");
            String search_reference = wholesaler_id + getCurrentDate();
            Log.e("OutwardConfirmation", "search_reference_id" + search_reference);
            String lastreference_no = WholesaleDBHelper.getInstance(this).getLastReferenceNumber(search_reference);

            int nextVal = 0;
            Log.e("OutwardConfirmation", "previousdate" + previousDate + "--->" + getCurrentDate());
            Log.e("OutwardConfirmation", "lastreference_no" + lastreference_no);
            if (lastreference_no == null) {
                if (previousDate.equals(getCurrentDate())) {
                    nextVal = 1;

                }
                referenceNumber = wholesaler_id + getCurrentDate() + String.format("%04d", nextVal);
                Log.e("OutwardConfirmation", "ReferenceNumber" + referenceNumber);
                Log.e("OutwardConfirmation", "previousValue1" + String.format("%04d", nextVal));
                //  WholesaleDBHelper.getInstance(this).updateMaserData("autoNumber", "" + nextVal);
                WholesaleDBHelper.getInstance(this).updateMaserData("currentDate", getCurrentDate());
            } else {
                long record = Long.parseLong(lastreference_no) + 1;
                referenceNumber = record + "";
                Log.e("OutwardConfirmation", "ReferenceNumber_dbbased" + "----" + record);
            }
            Log.e("OutwardConfirmation", "" + referenceNumber);
            return referenceNumber;

        } catch (Exception e) {
            Log.e("reference_no", e.toString(), e);
            return null;
        }
    }

    //This funciton returns current Date
    private String getCurrentDate() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }
}
