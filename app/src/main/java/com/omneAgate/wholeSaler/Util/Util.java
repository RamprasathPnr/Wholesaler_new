package com.omneAgate.wholeSaler.Util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;

import com.omneAgate.wholeSaler.DTO.LoggingDto;
import com.omneAgate.wholeSaler.DTO.MessageDto;
import com.omneAgate.wholeSaler.UndoBar.UndoBar;
import com.omneAgate.wholeSaler.activity.GlobalAppState;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;


import java.io.UnsupportedEncodingException;
import java.util.Locale;


/**
 * Utility class for entire application
 */
public class Util {

    //simple messageBar for FPS
    public static void messageBar(Activity activity, String message) {
        if (StringUtils.isEmpty(message)) {
            message = "Internal Error";
        }
        UndoBar undoBar = new UndoBar.Builder(activity)//
                .setMessage(message)//
                .setStyle(UndoBar.Style.KITKAT)
                .setAlignParentBottom(true)
                .create();
        undoBar.show();

    }



    /**
     * Change language in android
     *
     * @param languageCode,context
     */
    public static void changeLanguage(Context context, String languageCode) {
        Resources res = context.getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        android.content.res.Configuration conf = res.getConfiguration();
        conf.locale = new Locale(languageCode);
        res.updateConfiguration(conf, dm);
        WholesaleDBHelper.getInstance(context).updateMaserData("language", languageCode);
    }

    /**


    /**
     * get log data and set device id in log
     *
     * @param context,errorType and error string
     */
    private static LoggingDto logging(Context context, String errorType, String error) {
        LoggingDto log = new LoggingDto();
        log.setErrorType(errorType);
        log.setLogMessage(error);
        log.setDeviceId(Settings.Secure.getString(
                context.getContentResolver(), Settings.Secure.ANDROID_ID));
        return log;
    }

    /**
     * Add log to queue
     *
     * @param context,errorType and error string
     */
    public static void LoggingQueue(Context context, String errorType, String error) {

        GlobalAppState appState = (GlobalAppState) context.getApplicationContext();
        if (GlobalAppState.isLoggingEnabled && NetworkUtil.getConnectivityStatus(context) != 0)
            appState.queue.enqueue(logging(context, errorType, error));

    }


    public static String messageSelection(MessageDto messages) {
        String errorMessage = messages.getDescription();
        if (GlobalAppState.language.equalsIgnoreCase("ta")) {
            errorMessage = messages.getLocalDescription();
        }
        return errorMessage;
    }


    public static String EncryptPassword(String password) {
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setProvider(new BouncyCastleProvider());
        encryptor.setAlgorithm("PBEWITHSHA-256AND256BITAES-CBC-BC");
        encryptor.setPassword("fpspos");
        return encryptor.encrypt(password);
    }

    /**
     * Registration store in local preference
     *
     * @param context
     */
    public static void storePreferenceRegister(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("WHOLESALER", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("register", true);
        editor.commit();
    }

    /**
     * Registration store in local preference
     *
     * @param context
     */
    public static void storePreferenceApproved(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("WHOLESALER", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("register", true);
        editor.putBoolean("approved", true);
        editor.commit();
    }

    public String unicodeToLocalLanguage(String keyString){
        String unicodeString   =  null;
        try {
            unicodeString   =  new String(keyString.getBytes(),  "UTF8");
        }
        catch (UnsupportedEncodingException e){
            e.printStackTrace();
            Log.e("Exception ","while parsing UTF"+keyString);
        }
        return  unicodeString;
    }
    public static String DecryptPassword(String encryptedString) {
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        BouncyCastleProvider bouncy = new BouncyCastleProvider();
        encryptor.setProvider(bouncy);
        encryptor.setAlgorithm("PBEWITHSHA-256AND256BITAES-CBC-BC");
        encryptor.setPassword("fpspos");
        String encrypted = encryptor.decrypt(encryptedString);
        return encrypted;
    }
}
