package com.omneAgate.wholeSaler.Util;

import android.content.Context;
import android.content.SharedPreferences;
import com.omneAgate.wholeSaler.DTO.UserDto.UserFistSyncDto;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by user1 on 21/9/15.
 */
public class SyncPageUpdate {

    Context context;

    public SyncPageUpdate(Context context){
        this.context = context;
    }

    public void setSync() {

        SharedPreferences mySharedPreferences = context.getSharedPreferences("FPS_POS",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putBoolean("sync_complete", true);
        editor.apply();
        List<UserFistSyncDto> userSync = new ArrayList<>();
        UserFistSyncDto userValueSync = new UserFistSyncDto();
        userValueSync.setValue("FirstSync");
        userValueSync.setMasterValue("0");
        userSync.add(userValueSync);

        UserFistSyncDto userValue = new UserFistSyncDto();
        userValue.setValue("FirstSyncTime");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault());
        userValue.setMasterValue(df.format(new Date()));
        userSync.add(userValue);

        WholesaleDBHelper.getInstance(context).insertSyncValue(userSync);
    }
}
