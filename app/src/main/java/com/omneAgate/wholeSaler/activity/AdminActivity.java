package com.omneAgate.wholeSaler.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.omneAgate.wholeSaler.DTO.BaseDto;
import com.omneAgate.wholeSaler.DTO.EnumDTO.RequestType;
import com.omneAgate.wholeSaler.DTO.EnumDTO.ServiceListenerType;
import com.omneAgate.wholeSaler.DTO.POSLocationDto;
import com.omneAgate.wholeSaler.Util.AndroidDeviceProperties;
import com.omneAgate.wholeSaler.Util.Constants.WholeSaleConstants;
import com.omneAgate.wholeSaler.Util.GPSService;
import com.omneAgate.wholeSaler.Util.LocalDbRecoveryProcess;
import com.omneAgate.wholeSaler.Util.LocationId;
import com.omneAgate.wholeSaler.Util.NetworkConnection;
import com.omneAgate.wholeSaler.Util.SessionId;
import com.omneAgate.wholeSaler.Util.Util;
import com.omneAgate.wholeSaler.Util.WholesaleDBHelper;
import com.omneAgate.wholeSaler.activity.dialog.LocationReceivedDialog;
import com.omneAgate.wholeSaler.activity.dialog.LogoutDialog;
import com.omneAgate.wholeSaler.service.HttpClientWrapper;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HTTP;

import java.text.SimpleDateFormat;


public class AdminActivity extends BaseActivity {

    private Toolbar mToolbar;
    private ActionBar mActionBar;
    private TextView title;
    private DrawerLayout drawer;
    private NavigationView navigationView;

    private TextView welcome, logout, profileName, lastlogintime;
    private LinearLayout logoutView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        status = false;
        setContentView(R.layout.admin_navigation_drawer);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        logout = (TextView) navigationView.findViewById(R.id.logout);
        welcome = (TextView) navigationView.findViewById(R.id.welcome);
        profileName = (TextView) navigationView.findViewById(R.id.profile_name);
        lastlogintime = (TextView) navigationView.findViewById(R.id.last_login_time);
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
        if (SessionId.getInstance().getLastLoginTime() != null)
            lastlogintime.setText(formatter.format(SessionId.getInstance().getLastLoginTime()).toUpperCase());

        profileName.setText(SessionId.getInstance().getUserName().toUpperCase());
        logout.setText(R.string.logout);
        welcome.setText(R.string.welcome_view);

        logoutView = (LinearLayout) navigationView.findViewById(R.id.logoutView);

        logoutView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.END);

                onBackPressed();
            }
        });

        setTitle("");
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        title = (TextView) findViewById(R.id.title_toolbar);
        setSupportActionBar(mToolbar);
        mActionBar = getSupportActionBar();
        if (getSupportActionBar() != null) {
            title.setText(getString(R.string.dashboard));
        }
        setIndicator();
    }

    public void restoreDB(View view) {
        if (ActivityCompat.checkSelfPermission(AdminActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(AdminActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
            return;
        } else {
            LocalDbRecoveryProcess localDbRecoveryProcess = new LocalDbRecoveryProcess(this);
            localDbRecoveryProcess.restoresDb();
        }


    }

    public void retrieveDB(View view) {
        if (ActivityCompat.checkSelfPermission(AdminActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(AdminActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
            return;
        } else {
            LocalDbRecoveryProcess localDbRecoveryPro = new LocalDbRecoveryProcess(this);
            localDbRecoveryPro.backupDb(true, "", WholesaleDBHelper.DATABASE_NAME, "");
        }


    }

    public void getStatistics(View view) {
        if (ActivityCompat.checkSelfPermission(AdminActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            requestCameraPermission();
        } else {
            startActivity(new Intent(this, StatisticsActivity.class));
            finish();
        }

    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 0);
    }

    public void findLocation(View view) {
        if (ActivityCompat.checkSelfPermission(AdminActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(AdminActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 0);
            return;
        } else {
            findGPSLocation();
        }


    }

    public void configureInitialPage(View view) {
        setUpPopUpPageForAdmin();
    }

    private void findGPSLocation() {
        LocationManager manager = (LocationManager) getSystemService(LOCATION_SERVICE);
        boolean enabled = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!enabled) {
            turnGPSOn();
        } else {
            GPSService mGPSService = new GPSService(this);
            Location locationB = mGPSService.getLocation();
            if (locationB != null) {
                new LocationReceivedDialog(this, locationB).show();
            } else {

                if (GlobalAppState.language.equalsIgnoreCase("ta")) {
                    Toast.makeText(this, "இருப்பிடம் பெற முடியவில்லை", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Location can not be received", Toast.LENGTH_SHORT).show();
                }

            }
        }
    }

    public void turnGPSOn() {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivityForResult(intent, 1);
    }


    private void setLocation() {
        final Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (progressBar != null)
                    progressBar.dismiss();
                findGPSLocation();
            }
        }, 5000);
    }

    @Override
    public void onBackPressed() {
        userLogoutResponse();
    }

    //After user give logout this method will call dialog
    private void userLogoutResponse() {
        LogoutDialog logout = new LogoutDialog(this);
        logout.show();

    }

    public void sendLocation(Location location) {
        try {
            httpConnection = new HttpClientWrapper();
            POSLocationDto posLocation = new POSLocationDto();
            posLocation.setLatitude(String.valueOf(location.getLatitude()));
            posLocation.setLongitude(String.valueOf(location.getLongitude()));
            LocationId.getInstance().setLatitude(String.valueOf(location.getLatitude()));
            LocationId.getInstance().setLongitude(String.valueOf(location.getLongitude()));
            AndroidDeviceProperties device = new AndroidDeviceProperties(this);
            posLocation.setDeviceNumber(device.getDeviceProperties().getSerialNumber());
            String deviceLocation = new Gson().toJson(posLocation);
            StringEntity se = new StringEntity(deviceLocation, HTTP.UTF_8);
            String url = "/remoteLogging/addlocation";
            httpConnection.sendRequest(url, null, ServiceListenerType.DEVICE_STATUS,
                    SyncHandler, RequestType.POST, se, this);
        } catch (Exception e) {
            Toast.makeText(this, getString(R.string.internalError), Toast.LENGTH_SHORT).show();
            Util.LoggingQueue(this, "POS location Error", "Locayion Error in Pos");
            Log.e("error", e.toString(), e);
        }
    }

    private void setIndicator() {
        View viewOnline = findViewById(R.id.onLineOffline);
        TextView textViewOnline = (TextView) findViewById(R.id.textOnline);
        networkConnection = new NetworkConnection(this);
        if (StringUtils.isNotEmpty(SessionId.getInstance().getSessionId()) && networkConnection.isNetworkAvailable()) {
            viewOnline.setBackgroundResource(R.drawable.rounded_circle_green);
            textViewOnline.setTextColor(Color.parseColor("#038203"));
            setTamilText(textViewOnline, R.string.onlineText);
        } else {
            viewOnline.setBackgroundResource(R.drawable.rounded_circle_red);
            textViewOnline.setTextColor(Color.parseColor("#FFFF0000"));
            setTamilText(textViewOnline, R.string.offlineText);
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
            drawer.openDrawer(Gravity.RIGHT);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void processMessage(Bundle message, ServiceListenerType what) {
        switch (what) {
            case DEVICE_STATUS:
                setStatusCheck(message);
                break;
            default:
                //Util.messageBar(this, getString(R.string.serviceNotAvailable));
                // Toast.makeText(this,getString(R.string.serviceNotAvailable),Toast.LENGTH_SHORT).show();
                break;
        }
    }

    /**
     * status Response from server
     */
    private void setStatusCheck(Bundle message) {
        try {
            String response = message.getString(WholeSaleConstants.RESPONSE_DATA);
            Log.e("AdminActivity", "response for add location service" + response);
            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson = gsonBuilder.create();
            BaseDto base = gson.fromJson(response, BaseDto.class);
            Log.e("AdminActivity", "response for add location status code" + base.getStatusCode());
            if (base.getStatusCode() == 0) {
                Toast.makeText(this, getString(R.string.successInUpdate), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.internalError), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, getString(R.string.internalError), Toast.LENGTH_SHORT).show();
            Log.e("AdminActivity", "exception while adding location" + e.toString());
        }

    }


}
