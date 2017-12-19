package com.omneAgate.wholeSaler.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.omneAgate.wholeSaler.Util.DownloadDataProcessor;
import com.omneAgate.wholeSaler.Util.NetworkConnection;
import com.omneAgate.wholeSaler.Util.SessionId;
import com.omneAgate.wholeSaler.Util.WholesaleDBHelper;

import java.util.Timer;
import java.util.TimerTask;


/**
 * Service for syncing connection of server
 */
public class UpdateDataService extends Service {

    Timer syncTimer;
    SyncTimerTask syncTimerTask;

    @Override
    public void onCreate() {
        super.onCreate();
        syncTimer = new Timer();
        syncTimerTask = new SyncTimerTask();
        //Util.LoggingQueue(this, "Info", "Service DB sync manager started");
        Log.e("Info", "Service DB sync manager started");
        Long timerWaitTime = 15 * 60 * 1000l;
        syncTimer.schedule(syncTimerTask, 0, timerWaitTime);
       // Util.LoggingQueue(UpdateDataService.this, "Info", "Service DB sync manager created");
        Log.e("Info", "Service DB sync manager created");
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onDestroy() {
        try {
            if (syncTimer != null) {
                syncTimer.cancel();
                syncTimer = null;
            }
        } catch (Exception e) {
            Log.e("Error in UPDATE destroy", "Error in Service", e);
        }

    }

    class SyncTimerTask extends TimerTask {

        @Override
        public void run() {
            try {
                if (SessionId.getInstance().getSessionId().length() > 0) {
                    NetworkConnection network = new NetworkConnection(UpdateDataService.this);
                    if (network.isNetworkAvailable()) {
                        Log.e("regular sync", "Download Data processor");
                        String lastModifiedDate = WholesaleDBHelper.getInstance(UpdateDataService.this).getMasterData("syncTime");
                        Log.e("regular sync", "Download Data processor"+lastModifiedDate);
                        if(lastModifiedDate !=null) {
                            DownloadDataProcessor dataDownload = new DownloadDataProcessor(UpdateDataService.this);
                            dataDownload.processor();
                        }
                       
                    }
                }
                //Util.LoggingQueue(UpdateDataService.this, "Info", "Service DB sync manager ended");
                Log.e("Info", "Service DB sync manager created");
            } catch (Exception e) {
              //  Util.LoggingQueue(UpdateDataService.this, "Error", e.getMessage());
                Log.e("Error", e.toString(), e);
            }
        }

    }

}
