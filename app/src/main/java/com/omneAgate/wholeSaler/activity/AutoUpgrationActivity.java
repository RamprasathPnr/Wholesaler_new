package com.omneAgate.wholeSaler.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.ProgressCallback;
import com.omneAgate.wholeSaler.DTO.EnumDTO.CommonStatuses;
import com.omneAgate.wholeSaler.DTO.EnumDTO.RequestType;
import com.omneAgate.wholeSaler.DTO.EnumDTO.ServiceListenerType;
import com.omneAgate.wholeSaler.DTO.UpgradeDetailsDto;
import com.omneAgate.wholeSaler.Util.Constants.WholeSaleConstants;
import com.omneAgate.wholeSaler.Util.LocalDbRecoveryProcess;
import com.omneAgate.wholeSaler.Util.SessionId;
import com.omneAgate.wholeSaler.Util.Util;
import com.omneAgate.wholeSaler.Util.WholesaleDBHelper;
import com.omneAgate.wholeSaler.service.HttpClientWrapper;

import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HTTP;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


//Auto Upgradation of apk file
public class AutoUpgrationActivity extends BaseActivity {

    String refId = "", serverRefId = "";
    Integer oldVersion, newVersion;

    String downloadApkPath;
    //Downloading the progressbar
    private ProgressBar progressBar;
    // Download percentage
    private TextView tvUploadCount;

    @Override
    public void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        status = false;
        setContentView(R.layout.activity_auto_upgration);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        progressBar = (ProgressBar) findViewById(R.id.autoUpgradeprogressBar);
        progressBar.setVisibility(View.VISIBLE);
        Log.e(AutoUpgrationActivity.class.getSimpleName(), "Working zzz");
        tvUploadCount = (TextView) findViewById(R.id.tvUploadCount);
        SimpleDateFormat regDate = new SimpleDateFormat("ddMMyyhhmmss", Locale.getDefault());
        refId = regDate.format(new Date());
        downloadApkPath = getIntent().getStringExtra("downloadPath");
        Log.i("downloadApkPath", downloadApkPath);
        newVersion = getIntent().getIntExtra("newVersion", 0);

        try {
          //  findViewById(R.id.imageViewUserProfile).setVisibility(View.GONE);
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            oldVersion = pInfo.versionCode;
            UpgradeDetailsDto upgradeDto = WholesaleDBHelper.getInstance(this).getUpgradeData();
            upgradeDto.setCreatedTime(new Date().getTime());
            upgradeDto.setPreviousVersion(pInfo.versionCode);
            upgradeDto.setCurrentVersion(newVersion);
            upgradeDto.setStatus(CommonStatuses.UPDATE_START);
            upgradeDto.setDeviceNum(Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID).toUpperCase());
            upgradeData(upgradeDto, ServiceListenerType.CHECKVERSION);
        } catch (Exception e) {
            Log.e("AutoUpgrade", e.toString(), e);
            WholesaleDBHelper.getInstance(this).insertTableUpgrade(oldVersion, "Upgrade failed because:" + e.toString(), "fail", "FAILURE", newVersion, refId, serverRefId);
            Toast.makeText(this, getString(R.string.internalError), Toast.LENGTH_SHORT).show();
        }
    }


    private void upgradeData(UpgradeDetailsDto upgradeDto, ServiceListenerType type) throws Exception {
        httpConnection = new HttpClientWrapper();
        String checkVersion = new Gson().toJson(upgradeDto);
        StringEntity se = new StringEntity(checkVersion, HTTP.UTF_8);
        String url = "/wholesaler/adddetails";
        Log.e("VersionHisRequest", checkVersion);
        Log.e(AutoUpgrationActivity.class.getSimpleName() + "Device Register Version", "Checking version of apk in device");
        httpConnection.sendRequest(url, null, type, SyncHandler, RequestType.POST, se, this);
    }

    public void showPopupMenu(View v) {
    }

    /**
     * Tamil text textView typeface
     * input  textView name and id for string.xml
     */
    public void setTamilHeader(TextView textName, int id) {
        Typeface tfBamini = Typeface.createFromAsset(getAssets(), "Impact.ttf");
        textName.setTypeface(tfBamini);
        textName.setText(getString(id));
    }


    private void getFutureFile(String path) {

        Ion.with(AutoUpgrationActivity.this).load(downloadApkPath)
                .progressBar(progressBar)
                .progressHandler(new ProgressCallback() {
                    @Override
                    public void onProgress(long downloaded, long total) {
                        double ratio = downloaded / (double) total;
                        DecimalFormat percentFormat = new DecimalFormat("#.#%");
                        tvUploadCount.setText("" + percentFormat.format(ratio));

                    }
                })
                .write(new File(path))
                .setCallback(new FutureCallback<File>() {
                    @Override
                    public void onCompleted(Exception e, File file) {
                        if (e != null) {
                            Toast.makeText(AutoUpgrationActivity.this, "Error downloading file.Try again", Toast.LENGTH_LONG).show();
                            WholesaleDBHelper.getInstance(AutoUpgrationActivity.this).insertTableUpgrade(oldVersion, "Download failed because of" + e.toString(), "failed", "DOWNLOAD_FAIL", newVersion, refId, serverRefId);
                            onBackPressed();
                            return;
                        }

                        upGradeComplete();
                        WholesaleDBHelper.getInstance(AutoUpgrationActivity.this).insertTableUpgrade(oldVersion, "Download completed successfully in:" + file.getAbsolutePath(), "success", "DOWNLOAD_END", newVersion, refId, serverRefId);
                        Intent i = new Intent();
                        i.setAction(Intent.ACTION_VIEW);
                        i.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
                        startActivity(i);
                        WholesaleDBHelper.getInstance(AutoUpgrationActivity.this).insertTableUpgradeExec(oldVersion, "Download completed successfully in:" + file.getAbsolutePath(), "success", "EXECUTION", newVersion, refId, serverRefId);
                        finish();
                    }

                });
    }

    @Override
    protected void processMessage(Bundle message, ServiceListenerType what) {
        Log.e("serviceListenertype", "" + what);
        switch (what) {
            case CHECKVERSION:
                setData(message);
                break;

            case CARD_ACTIVATION:
                break;

            default:
                Log.e("AutoUpgradeDefault", "Auto upgrade");
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                break;
        }

    }

    private void setData(Bundle message) {
        try {
            String response = message.getString(WholeSaleConstants.RESPONSE_DATA);
            if (response != null) {
                GsonBuilder gsonBuilder = new GsonBuilder();
                Gson gson = gsonBuilder.create();
                Log.e(AutoUpgrationActivity.class.getSimpleName() + "Mobile OTP Reg", "Response for otp:" + response);
                UpgradeDetailsDto benefActivNewDto = gson.fromJson(response, UpgradeDetailsDto.class);
                serverRefId = benefActivNewDto.getReferenceNumber();
                downloadStart();
            }
        } catch (Exception e) {
            Log.e("AutoUpgrade ", e.toString(), e);
            WholesaleDBHelper.getInstance(this).insertTableUpgrade(oldVersion, "Upgrade failed because:" + e.toString(), "fail", "FAILURE", newVersion, refId, serverRefId);
        }
    }


    private void downloadStart() throws Exception {
        PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        oldVersion = pInfo.versionCode;
        WholesaleDBHelper.getInstance(this).insertTableUpgrade(oldVersion, "Upgrade Available in the paths:" + downloadApkPath, "success", "UPGRADE_START", newVersion, refId, serverRefId);
        String dbName = refId + ".db";
        WholesaleDBHelper.getInstance(this).insertTableUpgrade(oldVersion, "Back up the DB file:" + dbName, "success", "BACKUP_START", newVersion, refId, serverRefId);
        LocalDbRecoveryProcess localDbRecoveryPro = new LocalDbRecoveryProcess(this);
        if (localDbRecoveryPro.backupDb(false, refId, dbName, serverRefId)) {
            WholesaleDBHelper.getInstance(this).insertTableUpgrade(oldVersion, "Back up the DB file finished:" + dbName, "success", "BACKUP_END", newVersion, refId, serverRefId);
            File file = new File(Environment.getExternalStorageDirectory(), "WHOLESALER");
            if (!file.exists()) {
                file.mkdir();
            }
            final String path = Environment.getExternalStorageDirectory() + "/WHOLESALER/WHOLESALER.apk";
            WholesaleDBHelper.getInstance(this).insertTableUpgrade(pInfo.versionCode, "Download starts:" + path, "success", "DOWNLOAD_START", newVersion, refId, serverRefId);
            getFutureFile(path);
        } else {
            Toast.makeText(this, getString(R.string.internalError), Toast.LENGTH_SHORT).show();
        }
    }


    private void upGradeComplete() {
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            UpgradeDetailsDto upgradeDto = WholesaleDBHelper.getInstance(this).getUpgradeData();
            Log.e("UpgradeDetails", upgradeDto.toString());
            upgradeDto.setCreatedTime(new Date().getTime());
            upgradeDto.setPreviousVersion(pInfo.versionCode);
            upgradeDto.setCurrentVersion(newVersion);
            upgradeDto.setReferenceNumber(serverRefId);
            upgradeDto.setStatus(CommonStatuses.APK_DOWNLOAD);
            upgradeDto.setDeviceNum(Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID).toUpperCase());
            upgradeData(upgradeDto, ServiceListenerType.CARD_ACTIVATION);
        } catch (Exception e) {
            Log.e("AutoUpgradeException", e.toString(), e);
        }
    }


    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}