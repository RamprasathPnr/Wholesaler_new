package com.omneAgate.wholeSaler.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.WindowManager;

import com.omneAgate.wholeSaler.DTO.EnumDTO.ServiceListenerType;
import com.omneAgate.wholeSaler.Util.InsertIntoDatabase;
import com.omneAgate.wholeSaler.Util.Util;
import com.omneAgate.wholeSaler.Util.WholesaleDBHelper;

//Splash Activity
public class SplashActivity extends BaseActivity {
    public static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        context = this;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        if (WholesaleDBHelper.getInstance(this).getFirstSync()) {
            InsertIntoDatabase db = new InsertIntoDatabase(this);
            db.insertIntoDatabase();
            WholesaleDBHelper.getInstance(this).insertValues();
        }
        try {
            SQLiteDatabase db = new WholesaleDBHelper(this).getWritableDatabase();
            SharedPreferences sharedpreferences = getSharedPreferences("DBData", Context.MODE_PRIVATE);
            int oldVersion = sharedpreferences.getInt("version", db.getVersion());
            WholesaleDBHelper.getInstance(this).onUpgrade(db, oldVersion, 21);
            // WholesaleDBHelper.getInstance(this).insertMaserData("wholesalerid", "110");
            TelephonyManager telMgr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
            int simState = telMgr.getSimState();
            switch (simState) {
                case TelephonyManager.SIM_STATE_ABSENT:
                    GlobalAppState.smsAvailable = false;
                    break;
                case TelephonyManager.SIM_STATE_UNKNOWN:
                    GlobalAppState.smsAvailable = false;
                    break;
            }

            String languageCode = WholesaleDBHelper.getInstance(this).getMasterData("language");
            Log.e("checkLang", languageCode);
            if (languageCode == null) {
                languageCode = "ta";
            }
            Util.changeLanguage(this, languageCode);
            GlobalAppState.language = languageCode;


        } catch (Exception e) {
            Log.e("SplashActivity", e.toString(), e);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        startActivity(new Intent(this, LoginActivity.class));
        /*startActivity(new Intent(this,AdminActivity.class));*/
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    /*Concrete method*/
    @Override
    public void processMessage(Bundle message, ServiceListenerType what) {

    }
}
