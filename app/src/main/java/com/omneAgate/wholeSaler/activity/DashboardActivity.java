package com.omneAgate.wholeSaler.activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.NavigationView;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.omneAgate.wholeSaler.DTO.BaseDto;
import com.omneAgate.wholeSaler.DTO.EnumDTO.CommonStatuses;
import com.omneAgate.wholeSaler.DTO.EnumDTO.RequestType;
import com.omneAgate.wholeSaler.DTO.EnumDTO.ServiceListenerType;
import com.omneAgate.wholeSaler.DTO.UpgradeDetailsDto;
import com.omneAgate.wholeSaler.Util.Constants.WholeSaleConstants;
import com.omneAgate.wholeSaler.Util.LocalDbRecoveryProcess;
import com.omneAgate.wholeSaler.Util.NetworkConnection;
import com.omneAgate.wholeSaler.Util.SessionId;
import com.omneAgate.wholeSaler.Util.WholesaleDBHelper;
import com.omneAgate.wholeSaler.activity.dialog.LogoutDialog;
import com.omneAgate.wholeSaler.service.HttpClientWrapper;

import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HTTP;

import java.util.Date;

//Dashboard Activity to see all options
public class DashboardActivity extends BaseActivity {

    private Toolbar mToolbar;
    TextView title, wholesaleName;
    private ActionBar mActionBar;
    private static SQLiteDatabase database = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        status = true;
        setContentView(R.layout.activity_dashboard);

        setUpPopUpPage();
        SetTopLayout();
        WholesaleDBHelper.getInstance(this).deleteShops("1");
        upgradeSuccessMessage();
        String name = WholesaleDBHelper.getInstance(this).getContactPersonDetails(SessionId.getInstance().getUserName());
        wholesaleName = (TextView) findViewById(R.id.wholesaleName);
        wholesaleName.setText(name);
    }

    private void SetTopLayout() {
        try {
            mToolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(mToolbar);
            title = (TextView) findViewById(R.id.title_toolbar);
            title.setText(R.string.dashboard);
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

    public void configureInitialPage(View view) {
        setUpPopUpPage();
    }

    public void assocaitedShop(View view) {
        Intent intent = new Intent(DashboardActivity.this, AssociatedCardActivity.class);
        startActivity(intent);
        finish();
    }

    public void transactionHistory(View view) {
        Intent intent = new Intent(DashboardActivity.this, SearchHistoryActivity.class);
        startActivity(intent);
        finish();
    }

    public void outward(View view) {
        startActivity(new Intent(DashboardActivity.this, OutwardEntryActivity.class).putExtra("activityName", DashboardActivity.class.getSimpleName()));
        finish();
    }

    @Override
    protected void processMessage(Bundle message, ServiceListenerType what) {

        switch (what) {
            case CHECKVERSION:
                ChangeValue(message);
                break;
            default:
                break;
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

    private void ChangeValue(Bundle message) {
        try {
            String response = message.getString(WholeSaleConstants.RESPONSE_DATA);
            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson = gsonBuilder.create();
            if (response != null && !response.contains("Empty")) {
                BaseDto base = gson.fromJson(response, BaseDto.class);
                if (base.getStatusCode() == 0) {
                    WholesaleDBHelper.getInstance(this).updateUpgradeExec();
                    Cursor cursor = WholesaleDBHelper.getInstance(this).getCurerntVersonExec();
                    cursor.moveToFirst();
                    WholesaleDBHelper.getInstance(this).insertTableUpgrade(cursor.getInt(cursor.getColumnIndex("android_old_version")), "Upgrade completed successfully", "success", "UPGRADE_END", cursor.getInt(cursor.getColumnIndex("android_new_version")),
                            cursor.getString(cursor.getColumnIndex("ref_id")), cursor.getString(cursor.getColumnIndex("refer_id")));
                    cursor.close();
                }
            }
        } catch (Exception e) {
            Log.e("SaleActivity", e.toString(), e);
        }

    }

    @Override
    public void onBackPressed() {
        LogoutDialog logout = new LogoutDialog(this);
        logout.show();
    }

    private void upgradeSuccessMessage() {
        try {
            NetworkConnection netStatus = new NetworkConnection(this);
            if (WholesaleDBHelper.getInstance(this).checkUpgradeFinished() && netStatus.isNetworkAvailable()) {
                httpConnection = new HttpClientWrapper();
                UpgradeDetailsDto upgradeDto = WholesaleDBHelper.getInstance(this).getUpgradeData();
                upgradeDto.setCreatedTime(new Date().getTime());
                upgradeDto.setStatus(CommonStatuses.UPDATE_COMPLETE);
                Cursor cursor = WholesaleDBHelper.getInstance(this).getCurerntVersonExec();
                upgradeDto.setPreviousVersion(cursor.getInt(cursor.getColumnIndex("android_old_version")));
                upgradeDto.setCurrentVersion(cursor.getInt(cursor.getColumnIndex("android_new_version")));
                upgradeDto.setReferenceNumber(cursor.getString(cursor.getColumnIndex("refer_id")));
                upgradeDto.setDeviceNum(Settings.Secure.getString(
                        getContentResolver(), Settings.Secure.ANDROID_ID).toUpperCase());
                String checkVersion = new Gson().toJson(upgradeDto);
                StringEntity se = new StringEntity(checkVersion, HTTP.UTF_8);
                String url = "/wholesaler/adddetails";
                httpConnection.sendRequest(url, null, ServiceListenerType.CHECKVERSION,
                        SyncHandler, RequestType.POST, se, this);
            }
        } catch (Exception e) {
            Log.e("SaleActivity", e.toString(), e);
        }
    }

}
