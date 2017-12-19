package com.omneAgate.wholeSaler.activity;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.location.Location;
import android.net.ConnectivityManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.provider.Settings;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.omneAgate.wholeSaler.DTO.EnumDTO.ServiceListenerType;
import com.omneAgate.wholeSaler.DTO.StatisticsDto;
import com.omneAgate.wholeSaler.DTO.UpgradeDetailsDto;
import com.omneAgate.wholeSaler.Util.LocationId;
import com.omneAgate.wholeSaler.Util.SessionId;
import com.omneAgate.wholeSaler.Util.WholesaleDBHelper;
import com.omneAgate.wholeSaler.activity.dialog.LogoutDialog;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class StatisticsActivity extends BaseActivity {

    private Toolbar mToolbar;
    private ActionBar mActionBar;
    private TextView title;
    private DrawerLayout drawer;
    private NavigationView navigationView;

    private TextView welcome, logout, profileName, lastlogintime;
    private LinearLayout logoutView;


    StatisticsDto statisticsDto;
    String stringLatitude, stringLongitude;
    int scale, health, level, plugged, status, temperature, voltage;
    String technology;
    boolean present;
    private int batteryLevel = 0;
    Location location;
    //Broadcast receiver for battery
    private final BroadcastReceiver batteryLevelReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            context.unregisterReceiver(this);
            int currentLevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
            health = intent.getIntExtra(BatteryManager.EXTRA_HEALTH, 0);
            level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
            plugged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, 0);
            present = intent.getExtras().getBoolean(BatteryManager.EXTRA_PRESENT);
            status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, 0);
            technology = intent.getExtras().getString(BatteryManager.EXTRA_TECHNOLOGY);
            temperature = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0);
            voltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 0);
            if (currentLevel >= 0 && scale > 0) {
                batteryLevel = (currentLevel * 100) / scale;
                Log.e("Heart beat", "Current:" + currentLevel + "::" + "scale:" + scale + "::" + batteryLevel);
            }
            statisticsDto.setScale(scale);
            statisticsDto.setHealth(health);
            statisticsDto.setLevel(level);
            statisticsDto.setPlugged(plugged);
            statisticsDto.setStatus(status);
            statisticsDto.setTemperature(temperature);
            statisticsDto.setVoltage(voltage);
            statisticsDto.setTechnology(technology);
            statisticsDto.setPresent(present);
            changeData();
            Log.e("statisticsDto", statisticsDto.toString());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BaseActivity.status = false;
        setContentView(R.layout.activity_statistics_navigation_drawer);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        IntentFilter batteryLevelFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(batteryLevelReceiver, batteryLevelFilter);
        configureInitialPage();

    }

    @Override
    protected void processMessage(Bundle message, ServiceListenerType what) {

    }

    private void configureInitialPage() {
        try {


            drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            navigationView = (NavigationView) findViewById(R.id.nav_view);
            setTitle("");
            mToolbar = (Toolbar) findViewById(R.id.toolbar);
            title = (TextView) findViewById(R.id.title_toolbar);
            setSupportActionBar(mToolbar);
            mActionBar = getSupportActionBar();
            if (getSupportActionBar() != null) {
                title.setText(getString(R.string.statistics));
                getSupportActionBar().setHomeAsUpIndicator(R.drawable.arrow_back);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }

            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBackPressed();
                }
            });
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

                    LogoutDialog logout = new LogoutDialog(StatisticsActivity.this);
                    logout.show();
                }
            });


            setTamilText((TextView) findViewById(R.id.deviceNumLabel), R.string.deviceNumber);
            setTamilText((TextView) findViewById(R.id.HealthLabel), R.string.health);
            setTamilText((TextView) findViewById(R.id.scaleLabel), R.string.scale);
            setTamilText((TextView) findViewById(R.id.LevelLabel), R.string.level);
            setTamilText((TextView) findViewById(R.id.pluggedLabel), R.string.plugged);
            setTamilText((TextView) findViewById(R.id.technologyLabel), R.string.technology);
            setTamilText((TextView) findViewById(R.id.TemperatureLabel), R.string.temparature);
            setTamilText((TextView) findViewById(R.id.voltageLabel), R.string.voltage);
            setTamilText((TextView) findViewById(R.id.PresentLabel), R.string.present);
            setTamilText((TextView) findViewById(R.id.latitudeLabel), R.string.latitude);
            setTamilText((TextView) findViewById(R.id.LongitudeLabel), R.string.longitude);
            setTamilText((TextView) findViewById(R.id.versionNumberLabel), R.string.versionNumber);
            setTamilText((TextView) findViewById(R.id.versionNameLabel), R.string.versionName);
            setTamilText((TextView) findViewById(R.id.CpuUtilLabel), R.string.cpuUtilization);
            setTamilText((TextView) findViewById(R.id.AppinstalledTimeLabel), R.string.appInstalledTime);
            setTamilText((TextView) findViewById(R.id.FreeMemoryLabel), R.string.freeMemory);
            setTamilText((TextView) findViewById(R.id.AppUpdatedTimeLabel), R.string.appUpdatedTime);
            setTamilText((TextView) findViewById(R.id.TotalMemoryLabel), R.string.totalMemory);
            setTamilText((TextView) findViewById(R.id.StatusLabel), R.string.status);
            setTamilText((TextView) findViewById(R.id.MemoryUsedLabel), R.string.memoryUsed);
            setTamilText((TextView) findViewById(R.id.HardDiskSizeLabel), R.string.harddiskSize);
            setTamilText((TextView) findViewById(R.id.NetworkTypeLabel), R.string.networkType);
            setTamilText((TextView) findViewById(R.id.SimIdLabel), R.string.simId);
            setTamilText((TextView) findViewById(R.id.UnsyncLoginLabel), R.string.unsyncLogin);
            setTamilText((TextView) findViewById(R.id.tv_Nooffps_Label), R.string.nofps);
            setTamilText((TextView) findViewById(R.id.tv_noofbunk_Label), R.string.noofbunk);
            setTamilText((TextView) findViewById(R.id.tv_noofrrcLabel), R.string.noofrrc);
            setTamilText((TextView) findViewById(R.id.no_of_unsynctrans_Label), R.string.nounsync);
            setTamilText((TextView) findViewById(R.id.RegcountLabel), R.string.reg_count);
            setTamilText((TextView) findViewById(R.id.batteryLevelLabel), R.string.battery_level);


            statisticsDto = new StatisticsDto();
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            statisticsDto.setVersionNum(pInfo.versionCode);
            statisticsDto.setApkInstalledTime(pInfo.firstInstallTime);
            statisticsDto.setLastUpdatedTime(pInfo.lastUpdateTime);
            statisticsDto.setVersionName(pInfo.versionName);
            long totalFreeMemory = getAvailableInternalMemorySize() + getAvailableExternalMemorySize();
            statisticsDto.setHardDiskSizeFree(formatSize(totalFreeMemory));
            statisticsDto.setUserId(String.valueOf(SessionId.getInstance().getUserId()));
            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            //   statisticsDto.setSimId(telephonyManager.getDeviceId());
            try {
                statisticsDto.setSimId(telephonyManager.getSimSerialNumber());
            } catch (Exception e) {
            }


            statisticsDto.setDeviceNum((Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID)).toUpperCase());
            UpgradeDetailsDto upgradeDetailsDto = WholesaleDBHelper.getInstance(this).getUpgradeData();
            statisticsDto.setNumberOfFps(upgradeDetailsDto.getFpsCount());
            statisticsDto.setNumberOfKbunk(upgradeDetailsDto.getBunkerCount());
            statisticsDto.setNumberOfRrc(upgradeDetailsDto.getRrcCount());
            statisticsDto.setTotalUnSyncBillCountToday(upgradeDetailsDto.getBillUnsyncCount());
            statisticsDto.setCpuUtilisation(String.valueOf(readUsage()));
            ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
            ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
            activityManager.getMemoryInfo(mi);
            long availableMegs = mi.availMem / 1048576L;
            long totalMegs = mi.totalMem / 1048576L;
            statisticsDto.setMemoryRemaining(String.valueOf(availableMegs));
            statisticsDto.setTotalMemory(String.valueOf(totalMegs));
            statisticsDto.setMemoryUsed(String.valueOf(totalMegs - availableMegs));
            statisticsDto.setBatteryLevel(batteryLevel);
            statisticsDto.setLatitude(LocationId.getInstance().getLatitude());
            statisticsDto.setLongtitude(LocationId.getInstance().getLongitude());
            /*GPSService mGPSService = new GPSService(this);
            Location locationB = mGPSService.getLocation();
            location = locationB;
            statisticsDto.setLatitude("" + location.getLatitude());
            statisticsDto.setLongtitude("" + location.getLongitude());*/
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            if (cm != null) {
                statisticsDto.setNetworkInfo(cm.getActiveNetworkInfo().getTypeName());
            }
            // Log.e("Statics",""+statisticsDto.toString());


        } catch (Exception e) {
            Log.e("statistics error", e.toString(), e);
        } finally {
            setData();

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

    private void setData() {
       /* GPSTracker gpsTracker = new GPSTracker(this);
        if (gpsTracker.getIsGPSTrackingEnabled()) {
             stringLatitude = String.valueOf(gpsTracker.getLatitude());
             stringLongitude = String.valueOf(gpsTracker.getLongitude());
        }*/
        int unSynccount = WholesaleDBHelper.getInstance(this).getTransactionCount("R");
        ((TextView) findViewById(R.id.deviceNum)).setText(statisticsDto.getDeviceNum());
        ((TextView) findViewById(R.id.latitudeDevice)).setText(statisticsDto.getLatitude());
        ((TextView) findViewById(R.id.longitudeData)).setText(statisticsDto.getLongtitude());
        ((TextView) findViewById(R.id.cpuUtil)).setText(statisticsDto.getCpuUtilisation());
        ((TextView) findViewById(R.id.versionNo)).setText(statisticsDto.getVersionNum() + "");
        ((TextView) findViewById(R.id.noOfFps)).setText(statisticsDto.getNumberOfFps() + "");
        ((TextView) findViewById(R.id.noOfKbunk)).setText(statisticsDto.getNumberOfKbunk() + "");
        ((TextView) findViewById(R.id.noOfRRC)).setText(statisticsDto.getNumberOfRrc() + "");
        ((TextView) findViewById(R.id.unSyncBill)).setText(unSynccount + "");
        ((TextView) findViewById(R.id.versionName)).setText(statisticsDto.getVersionName() + "");
        ((TextView) findViewById(R.id.regCount)).setText(statisticsDto.getRegistrationCount() + "");

        try {
            String SimID = statisticsDto.getSimId();
            if (SimID != null && !SimID.isEmpty()) {
                ((TextView) findViewById(R.id.simId)).setText(SimID + "");
            } else {
                ((TextView) findViewById(R.id.simId)).setText("");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        ((TextView) findViewById(R.id.memUsed)).setText(statisticsDto.getMemoryUsed() + " MB");
        ((TextView) findViewById(R.id.memoryRemain)).setText(statisticsDto.getMemoryRemaining() + " MB");
        ((TextView) findViewById(R.id.totMemory)).setText(statisticsDto.getTotalMemory() + " MB");
        ((TextView) findViewById(R.id.hardDiskSize)).setText(statisticsDto.getHardDiskSizeFree());
        ((TextView) findViewById(R.id.networkType)).setText(statisticsDto.getNetworkInfo());
        ((TextView) findViewById(R.id.unSyncLoginCount)).setText(WholesaleDBHelper.getInstance(this).getAllLoginHistory().size() + "");
        SimpleDateFormat dateApp = new SimpleDateFormat("dd-MM-yy HH:mm", Locale.getDefault());
        ((TextView) findViewById(R.id.appInstalledTime)).setText(dateApp.format(new Date(statisticsDto.getApkInstalledTime())));
        ((TextView) findViewById(R.id.appUpdateTime)).setText(dateApp.format(new Date(statisticsDto.getLastUpdatedTime())));
    }

    private void changeData() {
        ((TextView) findViewById(R.id.deviceScale)).setText(statisticsDto.getScale() + "");
        ((TextView) findViewById(R.id.batteryLevel)).setText(statisticsDto.getBatteryLevel() + "");
        ((TextView) findViewById(R.id.batteryPlugged)).setText(statisticsDto.getPlugged() + "");
        ((TextView) findViewById(R.id.batteryTech)).setText(statisticsDto.getTechnology());
        ((TextView) findViewById(R.id.batteryVoltage)).setText(statisticsDto.getVoltage() + "V");
        ((TextView) findViewById(R.id.batteryHealth)).setText(statisticsDto.getHealth() + "");
        ((TextView) findViewById(R.id.batteryLvl)).setText(statisticsDto.getLevel() + "");
        ((TextView) findViewById(R.id.batteryStatus)).setText(statisticsDto.getStatus() + "");
        ((TextView) findViewById(R.id.batteryTemp)).setText(statisticsDto.getTemperature() + " C");
        ((TextView) findViewById(R.id.batteryPresent)).setText(statisticsDto.isPresent() + "");
    }


    private float readUsage() {
        try {
            RandomAccessFile reader = new RandomAccessFile("/proc/stat", "r");
            String load = reader.readLine();

            String[] toks = load.split(" ");

            long idle1 = Long.parseLong(toks[5]);
            long cpu1 = Long.parseLong(toks[2]) + Long.parseLong(toks[3]) + Long.parseLong(toks[4])
                    + Long.parseLong(toks[6]) + Long.parseLong(toks[7]) + Long.parseLong(toks[8]);

            try {
                Thread.sleep(360);
            } catch (Exception e) {
            }

            reader.seek(0);
            load = reader.readLine();
            reader.close();

            toks = load.split(" ");

            long idle2 = Long.parseLong(toks[5]);
            long cpu2 = Long.parseLong(toks[2]) + Long.parseLong(toks[3]) + Long.parseLong(toks[4])
                    + Long.parseLong(toks[6]) + Long.parseLong(toks[7]) + Long.parseLong(toks[8]);

            return (float) (cpu2 - cpu1) / ((cpu2 + idle2) - (cpu1 + idle1));

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return 0;
    }

    private boolean externalMemoryAvailable() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }

    private long getAvailableInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSizeLong();
        long availableBlocks = stat.getAvailableBlocksLong();
        return availableBlocks * blockSize;
    }

    private long getAvailableExternalMemorySize() {
        if (externalMemoryAvailable()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getAvailableBlocksLong();
            long availableBlocks = stat.getBlockSizeLong();
            return availableBlocks * blockSize;
        } else {
            return 0;
        }
    }

    private String formatSize(long size) {
        String suffix = null;

        if (size >= 1024) {
            suffix = " KB";
            size /= 1024;
            if (size >= 1024) {
                suffix = " MB";
                size /= 1024;
            }
        }

        StringBuilder resultBuffer = new StringBuilder(Long.toString(size));

        int commaOffset = resultBuffer.length() - 3;
        while (commaOffset > 0) {
            resultBuffer.insert(commaOffset, ',');
            commaOffset -= 3;
        }

        if (suffix != null) resultBuffer.append(suffix);
        return resultBuffer.toString();
    }


    @Override
    public void onBackPressed() {
//        unregisterReceiver(batteryLevelReceiver);
        startActivity(new Intent(this, AdminActivity.class));
        finish();
    }
}
