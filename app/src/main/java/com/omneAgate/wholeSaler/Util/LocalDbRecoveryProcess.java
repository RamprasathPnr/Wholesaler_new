package com.omneAgate.wholeSaler.Util;


import android.app.Activity;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;
import android.widget.Toast;

import com.omneAgate.wholeSaler.activity.R;
import com.omneAgate.wholeSaler.activity.dialog.RestorationLocalDbDialog;
import com.omneAgate.wholeSaler.activity.dialog.RetrieveLocalDbDialog;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;


public class LocalDbRecoveryProcess {
    // Context
    final private Activity context;

    // Database data
    final private String dataDbpath = "//data/";

    //Database database
    final private String databaseDatabase = "/databases/";

    //ExternalStorage availability
    private boolean mExternalStorageAvailable = false;

    //ExternalStorage Writable
    private boolean mExternalStorageWriteable = false;
    //Current database path
    private String currentDBPath = null;
    // path for local db path and sdcard.
    private File mSdPath, mData;

    //Constructor for Local DB Recovery
    public LocalDbRecoveryProcess(Activity context) {
        this.context = context;
    }

    // This function restores backup folder to Fps database
    public void restoresDb() {

        initialisePathDbSdcard();//Initialise db and sdcard path

        checkExternalMedia();//Check external storage availability and Writable

        if (mExternalStorageAvailable & mExternalStorageWriteable & getExternalAvailableSize()) {

            File fileDestinationFpsDb = new File(mData, currentDBPath);   // database is the destination

            File fileSourceBackupFolder = new File(mSdPath + "/backup");
            File fileSourceBackup;
            boolean success = true;

            if (!fileSourceBackupFolder.exists()) {
                success = fileSourceBackupFolder.mkdir();
            }
            try {
                if (success) {

                    fileSourceBackup = new File(mSdPath + "/backup/", WholesaleDBHelper.DATABASE_NAME);
                    if (!fileSourceBackup.exists()) {
                        Toast.makeText(context,context.getString(R.string.noFileSdcard),Toast.LENGTH_SHORT).show();
                        return;
                    }


                    RestorationLocalDbDialog restoration = new RestorationLocalDbDialog(context, fileSourceBackup, fileDestinationFpsDb);
                    restoration.show();

                } else {
                    Toast.makeText(context,context.getString(R.string.backupFolderNot),Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Log.e("restores Db exception", e.toString());

            }
        }
    }

    private void sourceCreation(File fileSourceDb, File fileDestinationDb) {

        try {
            FileChannel source = new FileInputStream(fileSourceDb).getChannel();
            FileChannel destination = new FileOutputStream(fileDestinationDb).getChannel();
            destination.transferFrom(source, 0, source.size());
            source.close();
            destination.close();

        } catch (IOException e) {
            Log.e("source recovery", e.toString());
        }
    }


    //Load db from sdcard
    public boolean backupDb(boolean isDialogBox, String refId, String dbName, String serverRefId) {

        initialisePathDbSdcard();//Initialise db and sdcard path

        checkExternalMedia();//Check external storage availability and Writable

        if (mExternalStorageAvailable & mExternalStorageWriteable & getExternalAvailableSize()) {

            File fileDestinationDb = new File(mData, currentDBPath);   // database is the destination
            File fileDestinationDbOldFileFolder = new File(mSdPath + "/backup");
            File fileDestinationDbOldFile;
            boolean success = true;

            if (!fileDestinationDbOldFileFolder.exists()) {
                success = fileDestinationDbOldFileFolder.mkdir();
            }
            try {
                if (success) {
                    fileDestinationDbOldFile = new File(mSdPath + "/backup/", dbName);
                    sourceCreation(fileDestinationDb, fileDestinationDbOldFile);
                    if (isDialogBox) {
                        RetrieveLocalDbDialog retrieveLocalDbDialog = new RetrieveLocalDbDialog(context, fileDestinationDb, fileDestinationDbOldFile);
                        retrieveLocalDbDialog.show();
                    }
                    return true;
                    // Util.messageBar(context, context.getString(R.string.oldDbBackup));
                } else {
                    if (isDialogBox) {
                        Toast.makeText(context,context.getString(R.string.backupFolderNot),Toast.LENGTH_SHORT).show();

                    } else {
                        WholesaleDBHelper.getInstance(context).insertTableUpgrade(0, "Backup folder not created", "fail", "Backup", 0, refId, serverRefId);
                    }
                    return false;
                }

            } catch (Exception e) {
                Log.e("backup exception", e.toString());
                if (!isDialogBox)
                    WholesaleDBHelper.getInstance(context).insertTableUpgrade(0, "Backup folder not created because:" + e.toString(), "fail", "Backup", 0, refId, serverRefId);
                return false;
            }

        }
        return false;
    }

    private void checkExternalMedia() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            mExternalStorageAvailable = mExternalStorageWriteable = true;
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            mExternalStorageAvailable = true;
            mExternalStorageWriteable = false;
            Toast.makeText(context,context.getString(R.string.externalStorageNotAvailable),Toast.LENGTH_SHORT).show();
        } else {
            mExternalStorageAvailable = mExternalStorageWriteable = false;
            Toast.makeText(context,context.getString(R.string.externalStorageNotAvailable),Toast.LENGTH_SHORT).show();
        }
    }


    private void initialisePathDbSdcard() {
        currentDBPath = dataDbpath + context.getPackageName() + databaseDatabase + WholesaleDBHelper.DATABASE_NAME;
        mSdPath = Environment.getExternalStorageDirectory();
        mData = Environment.getDataDirectory();
    }


    private boolean getExternalAvailableSize() {
        StatFs statFs = new StatFs(Environment.getExternalStorageDirectory().getAbsolutePath());
        long blockSize = statFs.getBlockSize();
        long freeSize = statFs.getFreeBlocks() * blockSize;
        String currentDBPath = dataDbpath + context.getPackageName() + databaseDatabase + WholesaleDBHelper.DATABASE_NAME;
        File file = context.getDatabasePath(currentDBPath);
        long dbSize = file.length();
        if (freeSize > dbSize) {
            return true;
        }
        Toast.makeText(context,context.getString(R.string.noFreeInSdcard),Toast.LENGTH_SHORT).show();
        return false;
    }


}

