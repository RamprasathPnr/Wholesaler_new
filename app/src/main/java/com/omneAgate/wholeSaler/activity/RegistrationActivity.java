package com.omneAgate.wholeSaler.activity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.omneAgate.wholeSaler.DTO.DeviceDto;
import com.omneAgate.wholeSaler.DTO.DeviceRegistrationResponseDto;
import com.omneAgate.wholeSaler.DTO.DeviceStatusRequest;
import com.omneAgate.wholeSaler.DTO.EnumDTO.RequestType;
import com.omneAgate.wholeSaler.DTO.EnumDTO.ServiceListenerType;
import com.omneAgate.wholeSaler.Util.Constants.WholeSaleConstants;
import com.omneAgate.wholeSaler.Util.NetworkConnection;
import com.omneAgate.wholeSaler.Util.Util;
import com.omneAgate.wholeSaler.service.HttpClientWrapper;

import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HTTP;

public class RegistrationActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        status = false;
        setContentView(R.layout.activity_registration);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        httpConnection = new HttpClientWrapper();
        networkConnection = new NetworkConnection(this);
        ((TextView) findViewById(R.id.textRegistration)).setText(getResources().getString(R.string.deviceRegistration));
        ((Button) findViewById(R.id.registrationButton)).setText(getResources().getString(R.string.status));
    }

    public void registrationStatus(View view) {

        try {
            if (networkConnection.isNetworkAvailable()) {
                DeviceDto deviceRegister = new DeviceDto();
                deviceRegister.setDeviceNumber(Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID).toUpperCase());
                String device = new Gson().toJson(deviceRegister);
                Log.e("device", "" + device);
                StringEntity se = new StringEntity(device, HTTP.UTF_8);
                String url = "/device/wsdevicestatus";
                httpConnection.sendRequest(url, null, ServiceListenerType.DEVICE_STATUS, SyncHandler, RequestType.POST, se, this);
            } else {
                Toast.makeText(RegistrationActivity.this, getString(R.string.connectionRefused), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.e("RegistrationActivity", e.toString(), e);
        }
    }


    /*Concrete method*/
    @Override
    public void processMessage(Bundle message, ServiceListenerType what) {
        switch (what) {
            case DEVICE_STATUS:
                setStatusCheck(message);
                break;
            default:
                Toast.makeText(RegistrationActivity.this, getString(R.string.connectionRefused), Toast.LENGTH_SHORT).show();
                break;
        }

    }

    private void setStatusCheck(Bundle message) {
        String response = message.getString(WholeSaleConstants.RESPONSE_DATA);
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        DeviceRegistrationResponseDto deviceRegistrationResponse = gson.fromJson(response,
                DeviceRegistrationResponseDto.class);
        Log.i("Resp", response);
        try {
            if (deviceRegistrationResponse.getDeviceRegistrationStatus().equals("APPROVED")) {
                com.omneAgate.wholeSaler.Util.Util.storePreferenceApproved(this);
                startActivity(new Intent(this, LoginActivity.class));
                finish();
            } else {
                //   com.omneAgate.wholeSaler.Util.Util.messageBar(RegistrationActivity.this, getString(R.string.deviceRegistration));
                Toast.makeText(RegistrationActivity.this, getString(R.string.deviceRegistration), Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            Log.e("error", "" + e.toString());
        }

    }


    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}
