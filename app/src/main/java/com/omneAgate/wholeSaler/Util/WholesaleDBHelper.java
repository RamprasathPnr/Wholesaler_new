package com.omneAgate.wholeSaler.Util;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.omneAgate.wholeSaler.DTO.FpsStoreDto;
import com.omneAgate.wholeSaler.DTO.KeroseneBunkDto;
import com.omneAgate.wholeSaler.DTO.LoginHistoryDto;
import com.omneAgate.wholeSaler.DTO.MessageDto;
import com.omneAgate.wholeSaler.DTO.ProductDto;
import com.omneAgate.wholeSaler.DTO.RrcDto;
import com.omneAgate.wholeSaler.DTO.UpgradeDetailsDto;
import com.omneAgate.wholeSaler.DTO.UserDetailDto;
import com.omneAgate.wholeSaler.DTO.UserDto.UserFistSyncDto;
import com.omneAgate.wholeSaler.DTO.WholeSaleLoginResponseDto;
import com.omneAgate.wholeSaler.DTO.WholesalerPostingDto;
import com.omneAgate.wholeSaler.DTO.bunkerdto_name;
import com.omneAgate.wholeSaler.DTO.fpsstore_name;
import com.omneAgate.wholeSaler.DTO.rrcdto_name;
import com.omneAgate.wholeSaler.Util.Constants.WholeSaleConstants;
import com.omneAgate.wholeSaler.activity.SplashActivity;
import org.apache.commons.lang3.StringUtils;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Wholesale database Helper
 */
public class WholesaleDBHelper extends SQLiteOpenHelper {

    // Database Name
    public static final String DATABASE_NAME = "WHOLESALER.db";

    //Key for id in tables
    public final static String KEY_ID = "_id";

    // Database Version
    private static final int DATABASE_VERSION = 21;

    // All Static variables
    private static WholesaleDBHelper dbHelper = null;

    private static SQLiteDatabase database = null;

    private static Context contextValue;



    public WholesaleDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        database = this.getWritableDatabase();
        dbHelper = this;
    }


    //Singleton to Instantiate the SQLiteOpenHelper
    public static WholesaleDBHelper getInstance(Context context) {
        if (dbHelper == null) {
            dbHelper = new WholesaleDBHelper(context);
            openConnection();
        }
        contextValue = context;
        return dbHelper;
    }

    // It is used to open database
    private static void openConnection() {
        if (database == null) {
            database = dbHelper.getWritableDatabase();
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        Log.e("Inside DB", "DB Creation");
        db.execSQL(WholesaleDBTables.CREATE_USERS_TABLE);
        db.execSQL(WholesaleDBTables.CREATE_TABLE_LOGIN_HISTORY);
        db.execSQL(WholesaleDBTables.CREATE_PRODUCTS_TABLE);
        db.execSQL(WholesaleDBTables.CREATE_TABLE_PRODUCT_GROUP);
        db.execSQL(WholesaleDBTables.CREATE_STOCK_TABLE);
        db.execSQL(WholesaleDBTables.CREATE_WHOLE_SALE_STOCK_INWARD_TABLE);
        db.execSQL(WholesaleDBTables.CREATE_WHOLE_SALE_STOCK_OUTWARD_TABLE);
        db.execSQL(WholesaleDBTables.CREATE_TABLE_WHOLE_SALE_STOCK_HISTORY);
        db.execSQL(WholesaleDBTables.CREATE_TABLE_ROLE);
        db.execSQL(WholesaleDBTables.CREATE_TABLE_ROLE_FEATURE);
        db.execSQL(WholesaleDBTables.CREATE_TABLE_SUPPLIER);
        db.execSQL(WholesaleDBTables.CREATE_TABLE_FPS_STORE);
        db.execSQL(WholesaleDBTables.CREATE_TABLE_KEROSENE_BUNK);
        db.execSQL(WholesaleDBTables.CREATE_TABLE_RRC);
        db.execSQL(WholesaleDBTables.CREATE_SYNC_MASTER_TABLE);
        db.execSQL(WholesaleDBTables.CREATE_TABLE_LANGUAGE_ERROR);
        db.execSQL(WholesaleDBTables.CREATE_TABLE_CONFIG);
        db.execSQL(WholesaleDBTables.CREATE_TABLE_UPGRADE);
        db.execSQL("alter table users add column is_user_active INTEGER");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        Log.e("oldversion", "" + oldVersion + "--->" + newVersion);
        try {

            switch (oldVersion) {
                case 16:
                    newVersion = 17;
                    try {
                        db.execSQL("alter table kerosene_bunk add column name VARCHAR(30)");
                        db.execSQL("alter table rrc add column name VARCHAR(30)");
                        db.execSQL("alter table stock_outward add column month VARCHAR(30)");
                        db.execSQL("alter table stock_outward add column year VARCHAR(30)");
                        db.execSQL("alter table stock_outward add column inward_type VARCHAR(30)");
                    } catch (Exception e) {
                    }
                case 17:
                    newVersion = 18;
                    try {
                        Log.e("newversion", "17" + "--->" + "18");
                        insertMaserData("wholesalerid", "100");

                    } catch (Exception e) {
                        Log.e("errorversion", e.toString());
                    }

                case 18:
                    newVersion = 19;
                    try {
                        Log.e("newversion", "19" + "--->" + "20");
                        db.execSQL("alter table fps_store add column name VARCHAR(30)");

                    } catch (Exception e) {
                        Log.e("errorversion", e.toString());
                    }
                case 19:
                    newVersion = 20;
                    try {
                        Log.e("newversion", "20" + "--->" + "21");
                        db.execSQL("alter table kerosene_bunk add column kerosene_generated_code VARCHAR(30)");
                        db.execSQL("alter table rrc add column rrc_generated_code VARCHAR(30)");

                    } catch (Exception e) {
                        Log.e("errorversion", e.toString());
                    }
                case 20:
                    newVersion=21;
                    try{
                        db.execSQL("alter table users add column is_user_active INTEGER");
                    }catch (Exception e){
                        Log.e("errorverssion",e.toString());
                    }

                default:
                    break;
            }
            SharedPreferences sharedpreferences;
            if (SplashActivity.context == null) {
                Log.e("Null", "Context null");
            }
            sharedpreferences = SplashActivity.context.getSharedPreferences("DBData", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putInt("version", newVersion);
            editor.apply();
        } catch (Exception e) {
            Log.e("Upgrade Error", e.toString(), e);
        }
    }

    //Insert values for master data
    public void insertValues() {
        insertMaserData("serverUrl", "http://192.168.1.53:9097");
        insertMaserData("syncTime", null);
        insertMaserData("status", null);
        insertMaserData("language", "ta");
        insertMaserData("currentDate", getCurrentDate());
        insertMaserData("autoNumber", "0");
        insertMaserData("wholesalerid", "00");

    }

    //Master First Sync details
    public void insertSyncValue(List<UserFistSyncDto> userSync) {
        if (!userSync.isEmpty()) {
            for (UserFistSyncDto fpsStock : userSync) {
                ContentValues values = new ContentValues();
                values.put("name", fpsStock.getValue());
                values.put("value", fpsStock.getMasterValue());
                database.insert("master_first_sync", null, values);
            }
        }
    }

    //This function gets curren date in yyyyMMdd format
    private String getCurrentDate(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        String formattedDate = df.format(c.getTime());
        return formattedDate;

    }
    public void updateUpgradeExec() {
        ContentValues values = new ContentValues();
        try {
            SimpleDateFormat regDate = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a", Locale.getDefault());
            values.put("execution_time", regDate.format(new Date()));
            values.put("server_status", 1);
            database.update(WholesaleDBTables.TABLE_UPGRADE, values, " _id in (select max(_id) from table_upgrade)", null);
        } catch (Exception e) {
            Log.e("DB ERROR", e.toString(), e);
        }

    }

    //This function inserts master data
    public void insertMaserData(String name, String value) {
        try
        {
            ContentValues values = new ContentValues();
            values.put("name", name);
            values.put("value", value);
            if (database == null) {
                database = dbHelper.getWritableDatabase();
            }
            database.insertWithOnConflict(WholesaleDBTables.TABLE_CONFIG, "name", values, SQLiteDatabase.CONFLICT_REPLACE);

        }catch(Exception e)
        {
            Log.e("exception",e.toString(),e);
        }

    }


    //This function uploads master data
    public void updateMaserData(String name, String value) {

        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("value", value);
        database.update(WholesaleDBTables.TABLE_CONFIG, values, "name='" + name + "'", null);
    }


    //The function loads master data
    public String getMasterData(String key) {
        String selectQuery = "SELECT  * FROM " + WholesaleDBTables.TABLE_CONFIG + " where name = '" + key + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        String value = null;
        try {
            if (cursor.moveToFirst()) {
                value = cursor.getString(cursor.getColumnIndex("value"));
            }
        } catch (Exception e) {
            Log.e("Error", e.toString(), e);
            cursor.close();
        }
        cursor.close();
        return value;
    }


    //This function loads data to language table
    public boolean getFirstSync() {
        String selectQuery = "SELECT  * FROM " + WholesaleDBTables.TABLE_LANGUAGE_ERROR;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        boolean value = cursor.getCount() == 0;
        cursor.close();
        return value;

    }

    //This function loads data to language table
    public void insertErrorMessages(MessageDto message) {
        try {
            ContentValues values = new ContentValues();
            values.put(WholeSaleConstants.KEY_LANGUAGE_CODE, message.getLanguageCode());
            values.put(WholeSaleConstants.KEY_LANGUAGE_MESSAGE, message.getDescription());
            values.put(WholeSaleConstants.KEY_LANGUAGE_L_MESSAGE, message.getLocalDescription());
            database.insert(WholesaleDBTables.TABLE_LANGUAGE_ERROR, null, values);
        } catch (Exception e) {
            Log.e("Error Message", e.toString(), e);
        }
    }

    //Insert for login history
    public void insertLoginHistory(LoginHistoryDto loginHistory) {
        try {
            ContentValues values = new ContentValues();
            values.put("login_time", loginHistory.getLoginTime());
            values.put("login_type", loginHistory.getLoginType());
            values.put("user_id", loginHistory.getUserId());
            values.put("whole_sale_id", loginHistory.getFpsId());
            values.put("transaction_id", loginHistory.getTransactionId());
            values.put("created_time", new Date().getTime());
            values.put("is_sync", 0);
            if (!database.isOpen()) {
                database = dbHelper.getWritableDatabase();
            }
            database.insert(WholesaleDBTables.TABLE_LOGIN_HISTORY, null, values);
        } catch (Exception e) {
            Log.e("Login History", e.toString(), e);
        }
    }

    // Used to retrieve data when no network available in device
    public WholeSaleLoginResponseDto retrieveData(String userName) {
        String selectQuery = "SELECT  * FROM " + WholesaleDBTables.TABLE_USERS + " where " + WholeSaleConstants.KEY_USERS_NAME + " = '" + userName.toLowerCase() + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Log.e("Offline Login Query",selectQuery);
        Cursor cursor = db.rawQuery(selectQuery, null);
        WholeSaleLoginResponseDto loginResponse;
        if (cursor.moveToFirst()) {
            loginResponse = new WholeSaleLoginResponseDto(cursor);
            loginResponse.setUserDetailDto(new UserDetailDto(cursor));
            return loginResponse;
        }
        cursor.close();
        return null;
    }


    // Used to retrieve user name
    public  WholeSaleLoginResponseDto getUserDetails(long userId) {
        String selectQuery = "SELECT  * FROM " + WholesaleDBTables.TABLE_USERS + " where " + KEY_ID + " = " + userId;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        WholeSaleLoginResponseDto loginResponse;
        if (cursor.moveToFirst()) {
            loginResponse = new WholeSaleLoginResponseDto(cursor);
            loginResponse.setUserDetailDto(new UserDetailDto(cursor));
            Log.i("UserDetails", loginResponse.toString());
            return loginResponse;
        }
        cursor.close();
        return null;
    }


    //Get Last Login time
    public String getLastLoginTime(long userId) {
        String selectQuery = "SELECT  last_login_time as login_time FROM users where _id = " + userId;
        SQLiteDatabase db = this.getReadableDatabase();
        String loginTime = null;
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            loginTime = cursor.getString(cursor.getColumnIndex("login_time"));
        }
        cursor.close();
        return loginTime;
    }



    //SELECT count(*) as bill_count FROM bill ;
    public void setLastLoginTime(long userId) {
        try {
            ContentValues values = new ContentValues();
            if (SessionId.getInstance().getLoginTime() != null)
                values.put("last_login_time", SessionId.getInstance().getLoginTime().getTime() + "");
            else {
                values.put("last_login_time", new Date().getTime() + "");
            }
            if (!database.isOpen()) {
                database = dbHelper.getWritableDatabase();
            }
            database.update(WholesaleDBTables.TABLE_USERS, values, KEY_ID + " = " + userId, null);
        } catch (Exception e) {
            Log.e("Login Time Error", e.toString(), e);
        }
    }


    //This function retrieve error description from language table
    public MessageDto retrieveLanguageTable(int errorCode) {
        String selectQuery = "SELECT  * FROM " + WholesaleDBTables.TABLE_LANGUAGE_ERROR + " where  " + WholeSaleConstants.KEY_LANGUAGE_CODE + " = " + errorCode;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        MessageDto messageDto = new MessageDto(cursor);
        cursor.close();
        return messageDto;
    }


    //insert login data inside database
    public void insertLoginUserData(WholeSaleLoginResponseDto loginResponse, String password) {
        ContentValues values = new ContentValues();
        try {
            values.put(KEY_ID, loginResponse.getUserDetailDto().getId());
            values.put(WholeSaleConstants.KEY_USERS_NAME, loginResponse.getUserDetailDto().getUserId().toLowerCase());
            values.put(WholeSaleConstants.KEY_USERS_ID, loginResponse.getUserDetailDto().getUsername().toLowerCase());
            values.put(WholeSaleConstants.KEY_USERS_PASS_HASH, loginResponse.getUserDetailDto().getPassword());
            values.put(WholeSaleConstants.KEY_USERS_PROFILE, loginResponse.getUserDetailDto().getProfile());
            values.put("encrypted_password", Util.EncryptPassword(password));
            values.put(WholeSaleConstants.KEY_USERS_CONTACT_PERSON, loginResponse.getWholeSaler().getContactPersonName());
            values.put(WholeSaleConstants.KEY_USERS_PHONE_NUMBER, loginResponse.getWholeSaler().getContactNumber());
            values.put(WholeSaleConstants.KEY_USERS_ADDRESS, loginResponse.getWholeSaler().getAddress());
            values.put("taluk_name", loginResponse.getWholeSaler().getTalukName());
            values.put("wholesaler_name", loginResponse.getWholeSaler().getName());
            values.put("wholesaler_code", loginResponse.getWholeSaler().getCode());
            values.put("lattitude", loginResponse.getWholeSaler().getLatitude());
            values.put("longitude", loginResponse.getWholeSaler().getLongitude());
            values.put("pincode", loginResponse.getWholeSaler().getPincode());
            values.put("emailId", loginResponse.getWholeSaler().getEmailId());

            if(loginResponse.getUserDetailDto().getProfile().equalsIgnoreCase("ADMIN")){
                values.put(WholeSaleConstants.KEY_USERS_IS_ACTIVE, 1);
            }else{
                int status = 0;
                if (loginResponse.getWholeSaler().getStatus()) {
                    status = 1;
                }
                values.put(WholeSaleConstants.KEY_USERS_IS_ACTIVE, status);
            }

            if(loginResponse.getUserDetailDto().getProfile().equalsIgnoreCase("ADMIN")){
                values.put(WholeSaleConstants.KEY_USERS_IS_USERACTIVE,1);
            }else{
                int active=0;
                if(loginResponse.getUserDetailDto().getActive()){
                    active=1;
                }
                values.put(WholeSaleConstants.KEY_USERS_IS_USERACTIVE,active);
            }
        } catch (Exception e) {
            Log.e("User data", e.toString(), e);
        } finally {
            database.insertWithOnConflict(WholesaleDBTables.TABLE_USERS, KEY_ID, values, SQLiteDatabase.CONFLICT_REPLACE);
        }

    }



    //This funition update Login History
    public void updateLoginHistory(String transactionId, String logoutType) {
        try {
            ContentValues values = new ContentValues();
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault());
            values.put("logout_time", df.format(new Date()));
            values.put("logout_type", logoutType);
            if (logoutType.equalsIgnoreCase("ONLINE_LOGOUT") || logoutType.equalsIgnoreCase("CLOSE_SALE_LOGOUT_ONLINE")) {
                values.put("is_logout_sync", 1);
            } else {
                values.put("is_logout_sync", 0);
            }
            if (!database.isOpen()) {
                database = dbHelper.getWritableDatabase();
            }
            database.update(WholesaleDBTables.TABLE_LOGIN_HISTORY, values, "transaction_id='" + transactionId + "'", null);
        } catch (Exception e) {
            Log.e("Login History", e.toString(), e);
        }

    }



    //This function inserts details to TABLE_PRODUCTS;
    public void insertProductData(List<ProductDto> listproduct) {

            Log.e("ProductList", listproduct.toString());

          for(ProductDto products:listproduct) {
            ContentValues values = new ContentValues();
            values.put(KEY_ID, products.getId());
            values.put(WholeSaleConstants.KEY_PRODUCT_NAME, products.getName().toUpperCase());
            values.put(WholeSaleConstants.KEY_LPRODUCT_NAME, products.getLocalProdName());
            if (products.isDeleted()) {
                values.put("isDeleted", 1);
            } else {
                values.put("isDeleted", 0);
            }
            values.put(WholeSaleConstants.KEY_PRODUCT_CODE, products.getCode());
            values.put("groupId", products.getGroupId());
            values.put(WholeSaleConstants.KEY_LPRODUCT_UNIT, products.getLocalProdUnit());
            values.put(WholeSaleConstants.KEY_PRODUCT_UNIT, products.getProductUnit());
            values.put(WholeSaleConstants.KEY_PRODUCT_PRICE, products.getProductPrice());
            if (products.isNegativeIndicator())
                values.put(WholeSaleConstants.KEY_NEGATIVE_INDICATOR, 0);
            else {
                values.put(WholeSaleConstants.KEY_NEGATIVE_INDICATOR, 1);
            }
            values.put(WholeSaleConstants.KEY_PRODUCT_MODIFIED_DATE, products.getModifiedDate());
            if (products.getModifiedby() != null)
                values.put(WholeSaleConstants.KEY_MODIFIED_BY, products.getModifiedby());
            values.put(WholeSaleConstants.KEY_CREATED_DATE, products.getCreatedDate());
            if (products.getCreatedby() != null)
                values.put(WholeSaleConstants.KEY_CREATED_BY, products.getCreatedby());
            database.insertWithOnConflict(WholesaleDBTables.TABLE_PRODUCTS, KEY_ID, values, SQLiteDatabase.CONFLICT_REPLACE);
        }
    }



    //This function inserts details to Table Kerosene Bunk;
    public void insertKbunk(List<KeroseneBunkDto> keroseneBunkDtoList) {
        try
         {

             Log.e("KBunkList", keroseneBunkDtoList.toString());
             Log.e("No of Bunk ",""+keroseneBunkDtoList.size());
        for(KeroseneBunkDto keroseneBunkDto:keroseneBunkDtoList) {

            ContentValues values = new ContentValues();
            values.put(KEY_ID, keroseneBunkDto.getId());
            //geneatecode for bunker_code
            values.put(WholeSaleConstants.KEY_KEROSENE_BUNK_CODE, keroseneBunkDto.getGeneratedCode());
            values.put(WholeSaleConstants.KEY_KEROSENE_BUNK_GENERATED_CODE, keroseneBunkDto.getGeneratedCode());
            if (keroseneBunkDto.getStatus()) {
                values.put(WholeSaleConstants.KEY_KEROSENE_BUNK_ACTIVE, 0);
            } else {
                values.put(WholeSaleConstants.KEY_KEROSENE_BUNK_ACTIVE, 1);
            }
            values.put(WholeSaleConstants.KEY_KEROSENE_BUNK_NAME, keroseneBunkDto.getName());
            values.put(WholeSaleConstants.KEY_KEROSENE_BUNK_CONTACT_NAME, keroseneBunkDto.getContactPersonName());
            values.put(WholeSaleConstants.KEY_KEROSENE_BUNK_MOBILE_NO, keroseneBunkDto.getContactNumber());
            values.put(WholeSaleConstants.KEY_KEROSENE_BUNK_ADDRESS, keroseneBunkDto.getAddress());
            values.put(WholeSaleConstants.KEY_KBUNK_TALUK_ID, keroseneBunkDto.getTalukId());
            values.put(WholeSaleConstants.KEY_KBUNK_TALUK_CODE, keroseneBunkDto.getTalukCode());
            values.put(WholeSaleConstants.KEY_KBUNK_TALUK_NAME, keroseneBunkDto.getTalukName());
            values.put(WholeSaleConstants.KEY_KEROSENE_BUNK_LAT, keroseneBunkDto.getLatitude());
            values.put(WholeSaleConstants.KEY_KEROSENE_BUNK_LONG, keroseneBunkDto.getLongitude());
            database.insertWithOnConflict(WholesaleDBTables.TABLE_KEROSENE_BUNK, KEY_ID, values, SQLiteDatabase.CONFLICT_REPLACE);

        }
        }catch(Exception e)
        {
            Log.e("InsertBunker",e.toString());
        }
     }


    //This function inserts details to Table RRC;
    public void insertRrcData(List<RrcDto> rrcDtoList){
         try
         {
             Log.e("No of RRC ",""+rrcDtoList.size());
         for(RrcDto rrcDto:rrcDtoList) {
            ContentValues values = new ContentValues();
            values.put(KEY_ID, rrcDto.getId());
             //geneatecode for rrc_code
             values.put(WholeSaleConstants.KEY_RRC_CODE, rrcDto.getGeneratedCode());
             values.put(WholeSaleConstants.KEY_RRC_GENERATED_CODE, rrcDto.getGeneratedCode());
              if (rrcDto.getStatus()) {
                values.put(WholeSaleConstants.KEY_RRC_ACTIVE, 0);
            } else {
                values.put(WholeSaleConstants.KEY_RRC_ACTIVE, 1);
            }
            values.put(WholeSaleConstants.KEY_RRC_NAME, rrcDto.getName());
            values.put(WholeSaleConstants.KEY_RRC_CONTACT_NAME, rrcDto.getContactPersonName());
            values.put(WholeSaleConstants.KEY_RRC_MOBILE_NO, rrcDto.getContactNumber());
            values.put(WholeSaleConstants.KEY_RRC_ADDRESS, rrcDto.getAddress());
            values.put(WholeSaleConstants.KEY_RRC_TALUK_ID, rrcDto.getTalukId());
            values.put(WholeSaleConstants.KEY_RRC_TALUK_CODE, rrcDto.getTalukCode());
            values.put(WholeSaleConstants.KEY_RRC_TALUK_NAME, rrcDto.getTalukName());
            values.put(WholeSaleConstants.KEY_RRC_LAT, rrcDto.getLatitude());
            values.put(WholeSaleConstants.KEY_RRC_LONG, rrcDto.getLongitude());
            database.insertWithOnConflict(WholesaleDBTables.TABLE_RRC, KEY_ID, values, SQLiteDatabase.CONFLICT_REPLACE);

        }
        }catch(Exception e)
        {
            Log.e("InsertBunker",e.toString());
        }

        }


    //This function inserts details to Table FPS store;
    public void insertFPS(List<FpsStoreDto> fpsStoreDtoList){

        try{
            Log.e("No of FPS ",""+fpsStoreDtoList.size());
            for(FpsStoreDto fpsStoreDto:fpsStoreDtoList) {
                ContentValues values = new ContentValues();
                values.put(KEY_ID, fpsStoreDto.getId());
                values.put(WholeSaleConstants.KEY_FPS_CODE, fpsStoreDto.getCode());
                values.put(WholeSaleConstants.KEY_FPS_GENERATED_CODE, fpsStoreDto.getGeneratedCode());
                if(fpsStoreDto.isActive()){
                    values.put(WholeSaleConstants.KEY_FPS_ACTIVE, 0);
                }else{
                    values.put(WholeSaleConstants.KEY_FPS_ACTIVE, 1);
                }
                values.put(WholeSaleConstants.KEY_FPS_CONTACT_NAME, fpsStoreDto.getContactPerson());
                values.put(WholeSaleConstants.KEY_FPS_MOBILE_NO, fpsStoreDto.getPhoneNumber());
                String address = "";
                if(fpsStoreDto.getAddressLine1()!=null )
                    address = fpsStoreDto.getAddressLine1();
                if(fpsStoreDto.getAddressLine2()!=null )
                    address = address + fpsStoreDto.getAddressLine2();
                if(fpsStoreDto.getAddressLine3()!=null )
                    address = address + fpsStoreDto.getAddressLine3();
                values.put(WholeSaleConstants.KEY_FPS_ADDRESS, address);
                values.put(WholeSaleConstants.KEY_FPS_NAME,""+fpsStoreDto.getName());
                values.put(WholeSaleConstants.KEY_FPS_CATEGORY, fpsStoreDto.getFpsCategory().toString());
                values.put(WholeSaleConstants.KEY_FPS_CATEGORY_TYPE, fpsStoreDto.getFpsCategoryType());
                values.put(WholeSaleConstants.KEY_FPS_TYPE, fpsStoreDto.getFpsType().toString());//FPS_TYPE
                values.put(WholeSaleConstants.KEY_FPS_VILLAGEID, fpsStoreDto.getVillageId());
                values.put(WholeSaleConstants.KEY_FPS_VILLAGE_CODE, fpsStoreDto.getVillageCode());
                values.put(WholeSaleConstants.KEY_FPS_VILLAGE_NAME, fpsStoreDto.getVillageName());
                values.put(WholeSaleConstants.KEY_FPS_TALUK_ID, fpsStoreDto.getTalukId());
                values.put(WholeSaleConstants.KEY_FPS_TALUK_CODE, fpsStoreDto.getTalukCode());
                values.put(WholeSaleConstants.KEY_FPS_TALUK_NAME, fpsStoreDto.getTalukName());
                values.put(WholeSaleConstants.KEY_FPS_DISTRICT_ID, fpsStoreDto.getDistrictId());
                values.put(WholeSaleConstants.KEY_FPS_DISTRICT_CODE, fpsStoreDto.getDistrictCode());
                values.put(WholeSaleConstants.KEY_FPS_DISTRICT_NAME, fpsStoreDto.getDistrictName());
                values.put(WholeSaleConstants.KEY_FPS_CONTACT_NAME, fpsStoreDto.getContactPerson());
                values.put(WholeSaleConstants.KEY_FPS_MOBILE_NO, fpsStoreDto.getPhoneNumber());
                values.put(WholeSaleConstants.KEY_FPS_LAT, fpsStoreDto.getLatitude());
                values.put(WholeSaleConstants.KEY_FPS_LONG, fpsStoreDto.getLongitude());
                database.insertWithOnConflict(WholesaleDBTables.TABLE_FPS_STORE, KEY_ID, values, SQLiteDatabase.CONFLICT_REPLACE);
           }

        }catch(Exception e)
        {
            Log.e("fpsstoreError",e.toString());
        }
    }

    public void updateStoreActiveStatusDetails(String user_name) {
        try {
            Util.LoggingQueue(contextValue, "^^^^FPSDBHelper^^^^ ", "updateShopActiveStatusDetails() called  user_name ->" + user_name);

            ContentValues values = new ContentValues();
            values.put(WholeSaleConstants.KEY_USERS_IS_ACTIVE, "0");
            database.update("users", values, "user_name = '" + user_name + "'", null);


        } catch (Exception e) {
          Log.e("update Store Active ","Exception while updating store inactive details" +e.toString());
        }

    }
    public void updateStoreActive(String user_name) {
        try {
            Util.LoggingQueue(contextValue, "^^^^FPSDBHelper^^^^ ", "updateShopActiveStatusDetails() called  user_name ->" + user_name);

            ContentValues values = new ContentValues();
            values.put(WholeSaleConstants.KEY_USERS_IS_ACTIVE, "1");
            database.update("users", values, "user_name = '" + user_name + "'", null);


        } catch (Exception e) {
            Log.e("update Store Active ","Exception while updating store inactive details" +e.toString());
        }

    }
    public void updateUserActive(String user_name) {
        try {
            Util.LoggingQueue(contextValue, "^^^^FPSDBHelper^^^^ ", "updateShopActiveStatusDetails() called  user_name ->" + user_name);

            ContentValues values = new ContentValues();
            values.put(WholeSaleConstants.KEY_USERS_IS_USERACTIVE, "1");
            database.update("users", values, "user_name = '" + user_name + "'", null);


        } catch (Exception e) {
            Log.e("update Store Active ","Exception while updating store inactive details" +e.toString());
        }

    }

    public String getContactPersonDetails(String userName) {
        String name="";
        try {
            String selectQuery = "SELECT contact_person ,wholesaler_name FROM " + WholesaleDBTables.TABLE_USERS +" where user_name = '" + userName +"'";
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                String contactperson = cursor.getString(cursor.getColumnIndex("contact_person"));
                String wholesaler_name = cursor.getString(cursor.getColumnIndex("wholesaler_name"));
                name=contactperson +" & "+wholesaler_name;
            }
            cursor.close();

        } catch (Exception e) {
           Log.e("Exception","while getting the contact person details "+e.toString());
        }
        return name;
    }
    public void deleteShops(String deleterecord) {
        try{
           // database.delete(WholesaleDBTables.TABLE_FPS_STORE, "active='" + deleterecord + "'", null);
           // database.delete(WholesaleDBTables.TABLE_RRC, "active='" + deleterecord + "'", null);
           //  database.delete(WholesaleDBTables.TABLE_KEROSENE_BUNK, "active='" + deleterecord + "'", null);
            database.delete(WholesaleDBTables.TABLE_FPS_STORE, WholeSaleConstants.KEY_FPS_ACTIVE + "=" + deleterecord, null);
            database.delete(WholesaleDBTables.TABLE_RRC, WholeSaleConstants.KEY_RRC_ACTIVE + "=" + deleterecord, null);
            database.delete(WholesaleDBTables.TABLE_KEROSENE_BUNK, WholeSaleConstants.KEY_KEROSENE_BUNK_ACTIVE + "=" + deleterecord, null);

        }catch(Exception e)
        {
            Log.e("delteshopsError",""+e.toString());
        }

    }

    public void deleteAll(String tableName) {
        try {
            Log.e("Dbhelper","Table Name "+tableName);
            database.delete(tableName, null, null);
        } catch (Exception e) {
           Log.e("Dbhelper","exception while deleting records"+e.toString());
        }
    }

    //This function returns recipient code
    public ArrayList<String> getRecipientCode(String recipientType){
        ArrayList<String> codeList = null;
        try
        {
            String tableName = "";
            String code = "";
            if(recipientType.equalsIgnoreCase("Kerosene Bunk")){
                tableName = WholesaleDBTables.TABLE_KEROSENE_BUNK;
                code = WholeSaleConstants.KEY_KEROSENE_BUNK_CODE;
            }else if(recipientType.equalsIgnoreCase("FPS")){
                tableName = WholesaleDBTables.TABLE_FPS_STORE;
                code = WholeSaleConstants.KEY_FPS_GENERATED_CODE;
            }else{
                tableName = WholesaleDBTables.TABLE_RRC;
                code = WholeSaleConstants.KEY_RRC_CODE;
            }
            Log.e("TableName", tableName);
            Log.e("Code", code);
            String selectQuery = "SELECT  "+code+" FROM  " + tableName+ " where active='0' ORDER BY "+code +" ASC ";
            codeList = new ArrayList<String>();
            Cursor cursor = database.rawQuery(selectQuery, null);
            Log.e("cursorSize", "" + cursor.getCount());
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                if(tableName.equalsIgnoreCase(WholesaleDBTables.TABLE_FPS_STORE)){
                    codeList.add(new FpsStoreDto(cursor).getGeneratedCode());
                }else if(tableName.equalsIgnoreCase(WholesaleDBTables.TABLE_KEROSENE_BUNK)){
                    codeList.add(new KeroseneBunkDto(cursor).getCode());
                }else{
                    codeList.add(new RrcDto(cursor).getCode());
                }

                cursor.moveToNext();

            }
            cursor.close();


        }catch (Exception e){
         e.printStackTrace();
        }


        return  codeList;
    }

    //This funicton return Product Details
    public ProductDto getProductDetails_tamil(String productName){
        String selectQuery = "SELECT  * FROM " + WholesaleDBTables.TABLE_PRODUCTS +" where "+WholeSaleConstants.KEY_LPRODUCT_NAME +" = '"+ productName+"'" ;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        ProductDto productDto = null ;
        if (cursor.moveToFirst()) {
            productDto = new ProductDto(cursor);
            return productDto;
        }
        cursor.close();

        return productDto;
    }

    //This funicton return Product Details
    public ProductDto getProductDetails(String productName){
        String selectQuery = "SELECT  * FROM " + WholesaleDBTables.TABLE_PRODUCTS +" where "+WholeSaleConstants.KEY_PRODUCT_NAME +" = '"+ productName+"'" ;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        ProductDto productDto = null ;
        if (cursor.moveToFirst()) {
            productDto = new ProductDto(cursor);
            return productDto;
        }
        cursor.close();

        return productDto;
    }

    //This function returns product type
    public List<ProductDto> getAllProducts() {
        List<ProductDto> listProduct=null;
        try {
            String selectQuery = "SELECT  * FROM " + WholesaleDBTables.TABLE_PRODUCTS;
            listProduct = new ArrayList<>();
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            cursor.moveToFirst();
            ProductDto productDto;
            for (int i = 0; i < cursor.getCount(); i++) {
                productDto = new ProductDto(cursor);
                listProduct.add(productDto);
                cursor.moveToNext();
            }
            cursor.close();

        } catch (Exception e) {
            Log.e("WholesalerDB",e.toString(),e);
        }
        return listProduct;
    }

   //InsertKeroseneStockOutward
   public void insertKeroseneStockOutward(WholesalerPostingDto wholesalerPostingDto,String syncStatus) {
       try {

           ContentValues values = new ContentValues();
           values.put(WholeSaleConstants.KEY_KEROSENE_WHOLE_SALE_ID,wholesalerPostingDto.getId());//Wholesaler Id
           values.put(WholeSaleConstants.KEY_WHOLE_SALE_STOCK_OUTWARD_OUTWARD_DATE,wholesalerPostingDto.getOutwardDate());
           values.put(WholeSaleConstants.KEY_WHOLE_SALE_STOCK_OUTWARD_PRODUCTID,wholesalerPostingDto.getProductId());
           values.put(WholeSaleConstants.KEY_WHOLE_SALE_STOCK_OUTWARD_QUANTITY,wholesalerPostingDto.getQuantity());
           values.put(WholeSaleConstants.KEY_WHOLE_SALE_STOCK_OUTWARD_UNIT,wholesalerPostingDto.getProductUnit());

           values.put("status", syncStatus);
           values.put("recipient_name",wholesalerPostingDto.getRecipientType());
           values.put("recipient_code",wholesalerPostingDto.getCode());
           values.put("recipient_type",wholesalerPostingDto.getRecipientType());
           values.put("referenceNo",wholesalerPostingDto.getReferenceNo());
           SimpleDateFormat regDate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS", Locale.getDefault());
           values.put(WholeSaleConstants.KEY_CREATED_DATE,regDate.format(new Date()));
           values.put("transporterName",wholesalerPostingDto.getTransportName());
           values.put("driverName",wholesalerPostingDto.getDriverName());
           values.put("driverContactNumber",wholesalerPostingDto.getDrivermobileNumber());
           values.put("vehicleNumber",wholesalerPostingDto.getVehicleNumber());
           values.put("month",""+wholesalerPostingDto.getMonth());
           values.put("year", ""+wholesalerPostingDto.getYear());
           values.put("inward_type", ""+wholesalerPostingDto.getInwardType());

           if (!database.isOpen()) {
               database = dbHelper.getWritableDatabase();
           }
           database.insert(WholesaleDBTables.TABLE_WHOLE_SALE_STOCK_OUTWARD, null, values);

       } catch (Exception e) {
           Log.e("Kerosene Stock Outward", e.toString(), e);

       }
   }


  public void  insertTransactionDetails(List<WholesalerPostingDto> listPosting){
      try {
          Log.e("WholesalerPostingDto","size of the list "+listPosting.size());
          for(WholesalerPostingDto wholesalerPostingDto:listPosting) {
              ContentValues values = new ContentValues();
              values.put(WholeSaleConstants.KEY_KEROSENE_WHOLE_SALE_ID, SessionId.getInstance().getWholesaleId());//Wholesaler Id
              values.put(WholeSaleConstants.KEY_WHOLE_SALE_STOCK_OUTWARD_OUTWARD_DATE, wholesalerPostingDto.getOutwardDate());
              values.put(WholeSaleConstants.KEY_WHOLE_SALE_STOCK_OUTWARD_PRODUCTID, wholesalerPostingDto.getProductId());
              values.put(WholeSaleConstants.KEY_WHOLE_SALE_STOCK_OUTWARD_QUANTITY, wholesalerPostingDto.getQuantity());
              values.put(WholeSaleConstants.KEY_WHOLE_SALE_STOCK_OUTWARD_UNIT, wholesalerPostingDto.getProductUnit());
              values.put("status", "T");


              if(wholesalerPostingDto.getKbCode()!=null){
                  values.put("recipient_code", wholesalerPostingDto.getKbCode());
                  values.put("recipient_name", "Kerosene Bunk");
                  values.put("recipient_type","Kerosene Bunk");
              }else if(wholesalerPostingDto.getFpsCode()!=null){
                  values.put("recipient_code", wholesalerPostingDto.getFpsCode());
                  values.put("recipient_name", "FPS");
                  values.put("recipient_type","FPS");
              }else{
                  values.put("recipient_code", wholesalerPostingDto.getRrcCode());
                  values.put("recipient_name", "RRC");
                  values.put("recipient_type","RRC");
              }
              values.put("referenceNo", wholesalerPostingDto.getReferenceNo());
              values.put(WholeSaleConstants.KEY_CREATED_DATE, wholesalerPostingDto.getOutwardDate());
              values.put("transporterName", wholesalerPostingDto.getTransportName());
              values.put("driverName", wholesalerPostingDto.getDriverName());
              values.put("driverContactNumber", wholesalerPostingDto.getDrivermobileNumber());
              values.put("vehicleNumber", wholesalerPostingDto.getVehicleNumber());
              if (!database.isOpen()) {
                  database = dbHelper.getWritableDatabase();
              }
              database.insertWithOnConflict(WholesaleDBTables.TABLE_WHOLE_SALE_STOCK_OUTWARD, KEY_ID, values, SQLiteDatabase.CONFLICT_REPLACE);

          }



      } catch (Exception e) {
          Log.e("Kerosene Stock outward", e.toString(), e);

      }
    }


    public String updateReferenceNumberFirstTimeSync(){
        String referenceNo = "";
        try
        {
            String selectQuery = "SELECT max(cast(referenceNo as SIGNED)) as max_reference_no  FROM "+WholesaleDBTables.TABLE_WHOLE_SALE_STOCK_OUTWARD;
            Cursor cursor = database.rawQuery(selectQuery, null);
            cursor.moveToFirst();
            referenceNo = cursor.getString(cursor.getColumnIndex("max_reference_no"));
            Log.e("LatestReferenceNumber", referenceNo);
            cursor.close();
        }catch (Exception e){
            Log.e("Refenrece Number","First Time Sync Null"+e.toString(),e);
        }

        return  referenceNo;
    }


    //Kerosene Outward stock
    public List<WholesalerPostingDto> showKeroseneStockOutward() {
        List<WholesalerPostingDto> keroseneOutwardList = null;
        try{
            String selectQuery = "SELECT  * FROM " + WholesaleDBTables.TABLE_WHOLE_SALE_STOCK_OUTWARD +" ORDER BY referenceNo DESC ";
            Cursor cursor = database.rawQuery(selectQuery, null);
             keroseneOutwardList = new ArrayList<>();
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                keroseneOutwardList.add(new WholesalerPostingDto(cursor));
                cursor.moveToNext();
            }
            cursor.close();

        }catch (Exception e){
          Log.e("Exception ",e.toString());
        }

        return keroseneOutwardList;
    }

    //Kerosene Outward stock
    public List<WholesalerPostingDto> showKeroseneStockOutwardSearch(String recipienttype, String code, String fromdate, String todate) {
        List<WholesalerPostingDto> keroseneOutwardList = null;
        String selectQuery;
        try {
            if (recipienttype.equals("") && code.equals("") && StringUtils.isNotEmpty(fromdate) && StringUtils.isNotEmpty(todate)) {
                recipienttype = "";
                selectQuery = "SELECT * FROM " + WholesaleDBTables.TABLE_WHOLE_SALE_STOCK_OUTWARD + " where   date(created_date)  BETWEEN '" + fromdate + "'" + " AND '" + todate + "' ORDER BY referenceNo DESC ";
            } else if (StringUtils.isEmpty(fromdate) && StringUtils.isEmpty(todate)) {
                selectQuery = "SELECT * FROM " + WholesaleDBTables.TABLE_WHOLE_SALE_STOCK_OUTWARD + " where recipient_type like '%" + recipienttype + "' AND recipient_code  like'%" + code + "' ORDER BY referenceNo DESC ";
            } else if (StringUtils.isEmpty(code)) {
                selectQuery = "SELECT * FROM " + WholesaleDBTables.TABLE_WHOLE_SALE_STOCK_OUTWARD + " where recipient_type ='" + recipienttype + "' AND date(created_date) BETWEEN '" + fromdate + "'" + " AND '" + todate + "' ORDER BY referenceNo DESC ";
            } else {
                selectQuery = "SELECT * FROM " + WholesaleDBTables.TABLE_WHOLE_SALE_STOCK_OUTWARD + " where recipient_type ='" + recipienttype + "' AND recipient_code ='" + code + "'  AND date(created_date) BETWEEN '" + fromdate + "'" + " AND '" + todate + "' ORDER BY referenceNo DESC ";
            }
            Log.e("dbrecords", "" + recipienttype + "--->" + code);
            Log.e("query", "" + selectQuery);
            Cursor cursor = database.rawQuery(selectQuery, null);
            keroseneOutwardList = new ArrayList<>();
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                keroseneOutwardList.add(new WholesalerPostingDto(cursor));
                cursor.moveToNext();
            }
            cursor.close();

        } catch (Exception e) {
            Log.e("Exception ", e.toString());
        }

        return keroseneOutwardList;
    }

    //Kerosene Outward stock
    public List<WholesalerPostingDto> showKeroseneStockOutward(String status) {
        List<WholesalerPostingDto> keroseneOutwardList = null;
        try{
            long start=System.currentTimeMillis();
            Log.e("transaction dbhelper","starting time"+start);
            String selectQuery = "SELECT  * FROM " + WholesaleDBTables.TABLE_WHOLE_SALE_STOCK_OUTWARD +" Where status "+" = '"+ status+"'" +" ORDER BY referenceNo DESC ";
            Log.e("selected query","getall kerosene outward"+selectQuery);
            Cursor cursor = database.rawQuery(selectQuery, null);
            keroseneOutwardList = new ArrayList<>();
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                keroseneOutwardList.add(new WholesalerPostingDto(cursor));
                cursor.moveToNext();
            }
            long end=System.currentTimeMillis();
            Log.e("transaction dbhelper","ending time"+end);
            Log.e("transaction dbhelper","total"+(end-start));
            cursor.close();
        }catch (Exception e){
            Log.e("Exception ",e.toString());
        }

        return keroseneOutwardList;
    }

    public List<WholesalerPostingDto> getAllKeroseneStockOutward(String status,int position) {
        List<WholesalerPostingDto> keroseneOutwardList = null;
        long offSet = position * 50;
        try{
            long start=System.currentTimeMillis();
            Log.e("transaction dbhelper","starting time"+start);
            String selectQuery = "SELECT  * FROM " + WholesaleDBTables.TABLE_WHOLE_SALE_STOCK_OUTWARD +" Where status "+" = '"+ status+"'" +" ORDER BY referenceNo DESC limit 50 OFFSET  "+offSet;
            Log.e("selected query","getall kerosene outward"+selectQuery);
            Cursor cursor = database.rawQuery(selectQuery, null);
            keroseneOutwardList = new ArrayList<>();
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                keroseneOutwardList.add(new WholesalerPostingDto(cursor));
                cursor.moveToNext();
            }
            long end=System.currentTimeMillis();
            Log.e("transaction dbhelper","ending time"+end);
            Log.e("transaction dbhelper","total"+(end-start));
            cursor.close();
        }catch (Exception e){
            Log.e("Exception ",e.toString());
        }

        return keroseneOutwardList;
    }


    //Transaction count with status
    public int getTransactionCount(String status) {
        String selectQuery = "SELECT  * FROM " + WholesaleDBTables.TABLE_WHOLE_SALE_STOCK_OUTWARD+" Where status "+" = '"+ status+"'" +" group by referenceNo";
        Cursor cursor = database.rawQuery(selectQuery, null);
        List<WholesalerPostingDto> keroseneOutwardList = new ArrayList<>();
        cursor.moveToFirst();
        for (int i = 0; i < cursor.getCount(); i++) {
            keroseneOutwardList.add(new WholesalerPostingDto(cursor));
            cursor.moveToNext();
        }
        cursor.close();
        return keroseneOutwardList.size();
    }

    //Posting Bill for background sync
    public List<WholesalerPostingDto> getAllWholesalerPostingForSync() {
        List<WholesalerPostingDto> bills = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + WholesaleDBTables.TABLE_WHOLE_SALE_STOCK_OUTWARD + " where status "  + "<>'T'";
        Cursor cursor = database.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        for (int i = 0; i < cursor.getCount(); i++) {
            WholesalerPostingDto bill = new WholesalerPostingDto(cursor);
            bills.add(bill);
            Log.e("Wholeposting bills", bill.toString());
            cursor.moveToNext();
        }
        cursor.close();
        return bills;
    }


    //Update the bill
    public void billUpdate(String id) {
        ContentValues values = new ContentValues();
        values.put("status", "T");
        database.update(WholesaleDBTables.TABLE_WHOLE_SALE_STOCK_OUTWARD, values, "referenceNo ='" + id + "'", null);

    }

    public UpgradeDetailsDto getUpgradeData() {
        UpgradeDetailsDto upgradeDto = new UpgradeDetailsDto();

        String unSinkOutwardQuery = "SELECT count(*) as outward_count FROM stock_outward where status ='R' ";
        String outwardQuery = "SELECT count(*) as outward_count FROM stock_outward ";
        String productQuery = "SELECT count(*) as products_count FROM products";
        String fpsQuery = "SELECT count(*) as fps_count FROM fps_store";
        String kBunkQuery = "SELECT count(*) as bunk_count FROM kerosene_bunk";
        String rrcQuery = "SELECT count(*) as rrc_count FROM rrc";


        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursorOutward = db.rawQuery( unSinkOutwardQuery , null);
        cursorOutward.moveToFirst();
        upgradeDto.setOutwardUnsyncCount(cursorOutward.getInt(cursorOutward.getColumnIndex("outward_count")));
        cursorOutward.close();


        Cursor cursorOutwardTotal = db.rawQuery(outwardQuery, null);
        cursorOutwardTotal.moveToFirst();
        upgradeDto.setOutwardCount(cursorOutwardTotal.getInt(cursorOutwardTotal.getColumnIndex("outward_count")));
        cursorOutwardTotal.close();

        Cursor cursorProduct = db.rawQuery(productQuery, null);
        cursorProduct.moveToFirst();
        upgradeDto.setProductCount(cursorProduct.getInt(cursorProduct.getColumnIndex("products_count")));
        cursorProduct.close();


        Cursor cursorFpsList = db.rawQuery(fpsQuery, null);
        cursorFpsList .moveToFirst();
        upgradeDto.setFpsCount(cursorFpsList.getInt(cursorFpsList.getColumnIndex("fps_count")));
        cursorFpsList.close();



        Cursor cursorBunk = db.rawQuery(kBunkQuery, null);
        cursorBunk.moveToFirst();
        upgradeDto.setBunkerCount(cursorBunk.getInt(cursorBunk.getColumnIndex("bunk_count")));
        cursorBunk.close();



        Cursor cursorRrc = db.rawQuery(rrcQuery, null);
        cursorRrc.moveToFirst();
        upgradeDto.setRrcCount(cursorRrc.getInt(cursorRrc.getColumnIndex("rrc_count")));
        cursorRrc.close();

        Log.e("upgradeDto", upgradeDto.toString());
        return upgradeDto;
    }



    public void insertTableUpgrade(int android_version, String userLog, String status, String state, int androidNewVersion, String refId, String serverRefId) {
        ContentValues values = new ContentValues();
        try {
            values.put("android_old_version", android_version);
            values.put("ref_id", refId);
            values.put("android_new_version", androidNewVersion);
            values.put("description", userLog);
            values.put("status", status.toUpperCase());
            values.put("state", state);
            values.put("refer_id", serverRefId);
            SimpleDateFormat regDate = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a", Locale.getDefault());
            values.put("created_date", regDate.format(new Date()));
            Log.e("updateinfo", "" + values);
            database.insert(WholesaleDBTables.TABLE_UPGRADE, null, values);
            Log.e("InsertTable", "working" + "    " + values);
        } catch (Exception e) {
            Log.e("Table Upgrade", "Exception", e);
        }

    }


    public List<RrcDto> getRRC_data() {
        List<RrcDto> rrc_data = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + WholesaleDBTables.TABLE_RRC;
        Cursor cursor = database.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        for (int i = 0; i < cursor.getCount(); i++) {
            RrcDto bill = new RrcDto(cursor);
            rrc_data.add(bill);
            Log.e("rrc_data ", bill.toString());
            cursor.moveToNext();
        }
        cursor.close();
        return rrc_data;
    }

    public List<rrcdto_name> getallRRC() {
        try
        {
            List<rrcdto_name> rrc_data = new ArrayList<>();
            String selectQuery = "SELECT  * FROM " + WholesaleDBTables.TABLE_RRC +" where active=0";
            Cursor cursor = database.rawQuery(selectQuery, null);
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                rrcdto_name bill = new rrcdto_name(cursor);
                rrc_data.add(bill);
                cursor.moveToNext();
            }
            cursor.close();
            return rrc_data;

        }catch(Exception e)
        {
            Log.e("rrrc_count_error",e.toString(),e);
        }


        return null;
    }

    public List<bunkerdto_name> getallBunker() {
        List<bunkerdto_name> rrc_data = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + WholesaleDBTables.TABLE_KEROSENE_BUNK +" where active=0";
        Cursor cursor = database.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        for (int i = 0; i < cursor.getCount(); i++) {
            bunkerdto_name bill = new bunkerdto_name(cursor);
            rrc_data.add(bill);
            cursor.moveToNext();
        }
        cursor.close();
        return rrc_data;
    }


    public List<fpsstore_name> getallFPSStore() {
        List<fpsstore_name> rrc_data = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + WholesaleDBTables.TABLE_FPS_STORE +" where active=0";
        Cursor cursor = database.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        for (int i = 0; i < cursor.getCount(); i++) {
            fpsstore_name bill = new fpsstore_name(cursor);
            rrc_data.add(bill);
            cursor.moveToNext();
        }
        cursor.close();
        return rrc_data;
    }






    public List<FpsStoreDto> getFPSStore_data() {
        List<FpsStoreDto> fpsStore_data = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + WholesaleDBTables.TABLE_FPS_STORE;
        Cursor cursor = database.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        for (int i = 0; i < cursor.getCount(); i++) {
            FpsStoreDto bill = new FpsStoreDto(cursor);
            fpsStore_data.add(bill);
            Log.e("fps_storedata ", bill.toString());
            cursor.moveToNext();
        }
        cursor.close();
        return fpsStore_data;
    }

    public List<KeroseneBunkDto> getKeroseneBunk_data() {
        List<KeroseneBunkDto> kerosenebunk_data = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + WholesaleDBTables.TABLE_KEROSENE_BUNK;
        Cursor cursor = database.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        for (int i = 0; i < cursor.getCount(); i++) {
            KeroseneBunkDto bill = new KeroseneBunkDto(cursor);
            kerosenebunk_data.add(bill);
            Log.e("bunk_data ", bill.toString());
            cursor.moveToNext();
        }
        cursor.close();
        return kerosenebunk_data;
    }

    /*public List<Wholesaler_allData> alldata() {
        List<Wholesaler_allData> kerosenebunk_data = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + WholesaleDBTables.TABLE_KEROSENE_BUNK;
        Cursor cursor = database.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        for (int i = 0; i < cursor.getCount(); i++) {
            Wholesaler_allData bill = new Wholesaler_allData(cursor);
            kerosenebunk_data.add(bill);
            Log.e("bunk_data ", bill.toString());
            cursor.moveToNext();
        }
        cursor.close();
        return kerosenebunk_data;
    }*/


    public void insertTableUpgradeExec(int android_version, String userLog, String status, String state, int androidNewVersion, String refId, String serverRefId) {
        ContentValues values = new ContentValues();
        try {
            values.put("android_old_version", android_version);
            values.put("ref_id", refId);
            values.put("android_new_version", androidNewVersion);
            values.put("description", userLog);
            values.put("status", status.toUpperCase());
            values.put("state", state);
            SimpleDateFormat regDate = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a", Locale.getDefault());
            values.put("created_date", "" + regDate.format(new Date()));
            values.put("server_status", 0);
            values.put("refer_id", serverRefId);
            database.insert(WholesaleDBTables.TABLE_UPGRADE, null, values);
        } catch (Exception e) {
            Log.e("Table Upgrade", e.toString(), e);
        }

    }

    //Bill for background sync
    public List<LoginHistoryDto> getAllLoginHistory() {
        List<LoginHistoryDto> loginHistoryList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + WholesaleDBTables.TABLE_LOGIN_HISTORY + " where is_sync=0 OR is_logout_sync = 0";
        Cursor cursor = database.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        for (int i = 0; i < cursor.getCount(); i++) {
            LoginHistoryDto loginHistory = new LoginHistoryDto(cursor);
            loginHistoryList.add(loginHistory);
            cursor.moveToNext();
        }
        cursor.close();
        return loginHistoryList;
    }

    // database connection  close
    public synchronized void closeConnection() {
        if (dbHelper != null) {
            dbHelper.close();
            database.close();
            dbHelper = null;
            database = null;
        }
    }

    //Transaction count with status
    public String getLastReferenceNumber(String reference_id) {
        try
        {
            String Last_reference_number=null;
            String selectQuery = "SELECT  * FROM " + WholesaleDBTables.TABLE_WHOLE_SALE_STOCK_OUTWARD+" Where referenceNo  LIKE '%" + reference_id +"%' ORDER BY "+ WholesaleDBTables.KEY_ID + " DESC limit 1";
            Cursor cursor = database.rawQuery(selectQuery, null);
            if(cursor.moveToFirst()) {
                Last_reference_number = cursor.getString(cursor.getColumnIndex("referenceNo"));
            }
            cursor.close();
            Log.e("Last_reference_number",""+Last_reference_number);
            return Last_reference_number;

        }catch(Exception e)
        {
            Log.e("reference_number_Error",e.toString(),e);
            return null;
        }
    }

    public boolean checkUpgradeFinished() {
        try {
            String selectQuery = "select case when state = 'EXECUTION' and server_status = 0 then 1 else 0 end as status from table_upgrade order by _id desc limit 1";
            Cursor cursor = database.rawQuery(selectQuery, null);
            cursor.moveToFirst();
            if (cursor.getCount() > 0) {
                int state = cursor.getInt(cursor.getColumnIndex("status"));
                cursor.close();
                if (state == 1) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public Cursor getCurerntVersonExec() {
        String selectQuery = "select * from table_upgrade order by _id desc limit 1";
        Cursor cursor = database.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        return cursor;
    }



}

