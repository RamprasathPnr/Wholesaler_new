package com.omneAgate.wholeSaler.activity.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.omneAgate.wholeSaler.DTO.BaseDto;
import com.omneAgate.wholeSaler.DTO.EnumDTO.RequestType;
import com.omneAgate.wholeSaler.DTO.EnumDTO.ServiceListenerType;
import com.omneAgate.wholeSaler.DTO.POSLocationDto;
import com.omneAgate.wholeSaler.Util.AndroidDeviceProperties;
import com.omneAgate.wholeSaler.Util.Constants.WholeSaleConstants;
import com.omneAgate.wholeSaler.Util.Util;
import com.omneAgate.wholeSaler.activity.AdminActivity;
import com.omneAgate.wholeSaler.activity.GlobalAppState;
import com.omneAgate.wholeSaler.activity.R;
import com.omneAgate.wholeSaler.service.HttpClientWrapper;

import org.apache.http.entity.StringEntity;

/**
 * This dialog will appear on the time of user logout
 */
public class LocationReceivedDialog extends Dialog implements
        View.OnClickListener {
    private final Activity context;  //    Context from the user
    Location location;
    TextView longitude, latitude;

    /*Constructor class for this dialog*/
    public LocationReceivedDialog(Activity _context, Location location) {
        super(_context);
        context = _context;
        this.location = location;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setContentView(R.layout.dialog_location_received);
        setCancelable(false);
        Button yesButton = (Button) findViewById(R.id.buttonYes);
        Button noButton = (Button) findViewById(R.id.buttonNo);
        yesButton.setOnClickListener(this);
        noButton.setOnClickListener(this);

        longitude = (TextView) findViewById(R.id.longitude);
        latitude = (TextView) findViewById(R.id.latitude);

        longitude.setText("" + location.getLongitude());
        latitude.setText("" + location.getLatitude());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonYes:
                dismiss();
                sendLocation();
                break;
            case R.id.buttonNo:
                dismiss();
                break;
        }
    }

    private void sendLocation() {
        ((AdminActivity) context).sendLocation(location);
    }


    /**
     * Tamil text textView typeface
     * input  textView name and id for string.xml
     */
    public void setTamilText(TextView textName, String id) {
        textName.setText(id);
    }


}