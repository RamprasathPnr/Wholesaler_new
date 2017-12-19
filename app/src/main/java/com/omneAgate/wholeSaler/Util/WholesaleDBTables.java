package com.omneAgate.wholeSaler.Util;

import com.omneAgate.wholeSaler.Util.Constants.WholeSaleConstants;

/**
 * Created for tables creation
 */
public class WholesaleDBTables {

    //Key for id in tables
    public final static String KEY_ID = "_id";

    // WHOLE_SALE Users table name
    public static final String TABLE_USERS = "users";

    // Products table name
    public static final String TABLE_PRODUCTS = "products";

    // Products Group Table
    public static final String TABLE_PRODUCT_GROUP = "product_group";


    public static final String TABLE_UPGRADE = "table_upgrade";


    // STOCK table name
    public static final String TABLE_STOCK = "stock";

    public static final String TABLE_CONFIG = "configuration";

    public static final String TABLE_LOGIN_HISTORY = "login_history";


    //Lanuage Database Table for Error Message
    public static final String TABLE_LANGUAGE_ERROR = "error_messages";

    public static final String TABLE_STOCK_HISTORY = "stock_history";


    //Whole sale  inward table
    public static final String TABLE_WHOLE_SALE_STOCK_INWARD = "stock_inward";


    //Whole sale  inward table
    public static final String TABLE_WHOLE_SALE_STOCK_OUTWARD = "stock_outward";


    //LPG PROVIDER
    public static final String TABLE_SUPPLIER = "supplier";

    //Roll type
    public static final String TABLE_ROLE = "role";
    //Roll future table
    public static final String TABLE_ROLE_FEATURE = "role_feature";

    //Table FPS Store
    public static final String TABLE_FPS_STORE = "fps_store";

    //Table Kerosene Bunk
    public static final String TABLE_KEROSENE_BUNK = "kerosene_bunk";

    //Table RRC
    public static final String TABLE_RRC= "rrc";


    // Users table with username and passwordHash
    public static final String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
            + KEY_ID + " INTEGER PRIMARY KEY ," + WholeSaleConstants.KEY_USERS_NAME + " VARCHAR(150) NOT NULL UNIQUE," + WholeSaleConstants.KEY_USERS_ID + " VARCHAR(150) NOT NULL,"
            + WholeSaleConstants.KEY_USERS_PASS_HASH + " VARCHAR(150),"  + WholeSaleConstants.KEY_USERS_CONTACT_PERSON + "  VARCHAR(30)," + WholeSaleConstants.KEY_USERS_PHONE_NUMBER + " VARCHAR(15), "
            + WholeSaleConstants.KEY_USERS_ADDRESS + " VARCHAR(60),"+ "taluk_name VARCHAR(30),wholesaler_name VARCHAR(30),wholesaler_code VARCHAR(30),"
            + "taluk_code VARCHAR(30),last_login_time VARCHAR(30),"
            + "whole_sale_category VARCHAR(30),whole_sale_type VARCHAR(30),encrypted_password VARCHAR(300),"
            + WholeSaleConstants.KEY_USERS_PROFILE + " VARCHAR(150),"+"pincode VARCHAR(150),emailId VARCHAR(150), "
            + WholeSaleConstants.KEY_USERS_IS_ACTIVE + " INTEGER," +"lattitude INTEGER,"+"longitude INTEGER"+")";

    // Users Table Login History
    public static final String CREATE_TABLE_LOGIN_HISTORY = "CREATE TABLE " + TABLE_LOGIN_HISTORY + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,login_time VARCHAR(60),login_type VARCHAR(50),user_id INTEGER,logout_time VARCHAR(60),logout_type VARCHAR(50)," +
            "whole_sale_id INTEGER,transaction_id VARCHAR(50),created_time INTEGER,is_sync INTEGER,is_logout_sync INTEGER)";



    // card type table with card types
    public static final String CREATE_SYNC_MASTER_TABLE = "CREATE TABLE  master_first_sync (" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
            + "name VARCHAR(50) NOT NULL UNIQUE,value VARCHAR(20)" + " )";



    // Products  table with unique product name and unique product code
    public static final String CREATE_PRODUCTS_TABLE = "CREATE TABLE " + TABLE_PRODUCTS + "("
            + KEY_ID + " INTEGER PRIMARY KEY," + WholeSaleConstants.KEY_PRODUCT_NAME + " VARCHAR(150) NOT NULL UNIQUE,"
            + WholeSaleConstants.KEY_PRODUCT_PRICE + " DOUBLE NOT NULL," + WholeSaleConstants.KEY_PRODUCT_UNIT + " VARCHAR(150) NOT NULL,"
            + WholeSaleConstants.KEY_PRODUCT_CODE + " VARCHAR(150) NOT NULL UNIQUE,isDeleted INTEGER,groupId INTEGER," + WholeSaleConstants.KEY_LPRODUCT_UNIT + " VARCHAR(150),"
            + WholeSaleConstants.KEY_LPRODUCT_NAME + " VARCHAR(250)," + WholeSaleConstants.KEY_NEGATIVE_INDICATOR + " INTEGER ,"
            + WholeSaleConstants.KEY_PRODUCT_MODIFIED_DATE + " INTEGER," + WholeSaleConstants.KEY_MODIFIED_BY + " VARCHAR(150)," + WholeSaleConstants.KEY_CREATED_DATE + " INTEGER,"
            + WholeSaleConstants.KEY_CREATED_BY + " VARCHAR(150)" + ")";




    // Close Sale Master  table
    public static final String CREATE_TABLE_PRODUCT_GROUP = "CREATE TABLE " + TABLE_PRODUCT_GROUP + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,group_id INTEGER,name VARCHAR(100),product_id INTEGER,is_deleted INTEGER,UNIQUE (product_id,group_id)  ON CONFLICT REPLACE) ";


    // card type table with card types
    public static final String CREATE_TABLE_CONFIG = "CREATE TABLE " + TABLE_CONFIG + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
            + "name VARCHAR(50) NOT NULL UNIQUE,value VARCHAR(150)  UNIQUE" + " )";


    // Stock  table
    public static final String CREATE_STOCK_TABLE = "CREATE TABLE " + TABLE_STOCK + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT  NOT NULL,"  + WholeSaleConstants.KEY_STOCK_PRODUCT_ID + " INTEGER NOT NULL UNIQUE,"
            + WholeSaleConstants.KEY_STOCK_QUANTITY + " DOUBLE NOT NULL, "
            + WholeSaleConstants.KEY_STOCK_REORDER_LEVEL + " DOUBLE, " + WholeSaleConstants.KEY_STOCK_EMAIL_ACTION + " INTEGER,"
            + WholeSaleConstants.KEY_STOCK_SMS_ACTION + " INTEGER )";


    public static final String CREATE_WHOLE_SALE_STOCK_INWARD_TABLE = "CREATE TABLE " + TABLE_WHOLE_SALE_STOCK_INWARD + "("
            + WholeSaleConstants.KEY_WHOLE_SALE_STOCK_INWARD_SUPPLIERID + " INTEGER NOT NULL," + WholeSaleConstants.KEY_WHOLE_SALE_STOCK_INWARD_WHOLE_SALEID + " INTEGER NOT NULL,"
            + WholeSaleConstants.KEY_WHOLE_SALE_STOCK_INWARD_INWARD_DATE + " INTEGER NOT NULL," + WholeSaleConstants.KEY_WHOLE_SALE_STOCK_INWARD_PRODUCTID + " INTEGER NOT NULL,"
            + WholeSaleConstants.KEY_WHOLE_SALE_STOCK_INWARD_QUANTITY + " DOUBLE NOT NULL, " + WholeSaleConstants.KEY_WHOLE_SALE_STOCK_INWARD_UNIT + " VARCHAR(150),supplier_name VARCHAR(100),supplier_code VARCHAR(100),"
            + WholeSaleConstants.KEY_WHOLE_SALE_STOCK_INWARD_BATCH_NO + " INTEGER NOT NULL,referenceNo VARCHAR(150)," + WholeSaleConstants.KEY_WHOLE_SALE_STOCK_INWARD_IS_WHOLE_SALEACKSTATUS + " INTEGER(1),is_server_add INTEGER(1)," +
            WholeSaleConstants.KEY_WHOLE_SALE_STOCK_INWARD_WHOLE_SALEACKDATE + " INTEGER," + WholeSaleConstants.KEY_WHOLE_SALE_STOCK_INWARD_WHOLE_SALERECEIVEIQUANTITY + " DOUBLE ,"
           + WholeSaleConstants.KEY_WHOLE_SALE_STOCK_INWARD_CREATEDBY + WholeSaleConstants.KEY_CREATED_DATE + " INTEGER," + WholeSaleConstants.KEY_WHOLE_SALE_STOCK_INWARD_DELIEVERY_CHELLANID + " INTEGER" + ")";


    public static final String CREATE_WHOLE_SALE_STOCK_OUTWARD_TABLE = "CREATE TABLE " + TABLE_WHOLE_SALE_STOCK_OUTWARD + "("
            + KEY_ID + "  INTEGER PRIMARY KEY AUTOINCREMENT  NOT NULL,"+ WholeSaleConstants.KEY_WHOLE_SALE_STOCK_OUTWARD_WHOLE_SALEID + " INTEGER NOT NULL,"
            + WholeSaleConstants.KEY_WHOLE_SALE_STOCK_OUTWARD_OUTWARD_DATE + " INTEGER NOT NULL," + WholeSaleConstants.KEY_WHOLE_SALE_STOCK_OUTWARD_PRODUCTID + " INTEGER NOT NULL,"
            + WholeSaleConstants.KEY_WHOLE_SALE_STOCK_OUTWARD_QUANTITY + " DOUBLE NOT NULL, " + WholeSaleConstants.KEY_WHOLE_SALE_STOCK_OUTWARD_UNIT + " VARCHAR(150),recipient_name VARCHAR(100),recipient_code VARCHAR(100),"
            +" recipient_type VARCHAR(100) ,"  +" status VARCHAR(150),"+" inward_type VARCHAR(150),"+" year VARCHAR(150),"+" month VARCHAR(150),"+" referenceNo VARCHAR(150),"+" transporterName VARCHAR(150),"+" driverName VARCHAR(150),"+" driverContactNumber VARCHAR(150),"+" vehicleNumber VARCHAR(150),"+ WholeSaleConstants.KEY_CREATED_DATE + " INTEGER "+")";


    public static final String CREATE_TABLE_LANGUAGE_ERROR = "CREATE TABLE " + TABLE_LANGUAGE_ERROR + "("
            + KEY_ID + " INTEGER  PRIMARY KEY AUTOINCREMENT  NOT NULL," + WholeSaleConstants.KEY_LANGUAGE_CODE + " INTEGER UNIQUE,"
            + WholeSaleConstants.KEY_LANGUAGE_L_MESSAGE + "  VARCHAR(1000)," + WholeSaleConstants.KEY_LANGUAGE_MESSAGE + " VARCHAR(1000) )";



    public static final String CREATE_TABLE_WHOLE_SALE_STOCK_HISTORY = "CREATE TABLE " + TABLE_STOCK_HISTORY + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," + WholeSaleConstants.KEY_STOCK_HISTORY_PRODUCT_ID + " INTEGER,"
            + WholeSaleConstants.KEY_STOCK_HISTORY_DATE + " INTEGER,"
            + WholeSaleConstants.KEY_STOCK_DATE + " VARCHAR(100),"
            + WholeSaleConstants.KEY_STOCK_HISTORY_OPEN_BALANCE + " DOUBLE," + WholeSaleConstants.KEY_STOCK_HISTORY_CLOSE_BALANCE + " DOUBLE,"
            + WholeSaleConstants.KEY_STOCK_HISTORY_CHANGE_BALANCE + " DOUBLE,"
            + WholeSaleConstants.KEY_STOCK_HISTORY_ACTION + " VARCHAR(100)" + ")";


    public static final String CREATE_TABLE_SUPPLIER = "CREATE TABLE " + TABLE_SUPPLIER + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," + WholeSaleConstants.KEY_SUPPLIER_NAME + " VARCHAR(150) UNIQUE,"
            + WholeSaleConstants.KEY_SUPPLIER_CREATEDBY + " INTEGER,"
            + WholeSaleConstants.KEY_SUPPLIER_CREATEDDATE + " INTEGER," + WholeSaleConstants.KEY_SUPPLIER_MODIFIEDDATE + " INTEGER,"
            + WholeSaleConstants.KEY_SUPPLIER_STATUS + " INTEGER" + ")";

    public static final String CREATE_TABLE_ROLE = "CREATE TABLE " + TABLE_ROLE + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," + WholeSaleConstants.KEY_ROLE_USERID + " INTEGER," + WholeSaleConstants.KEY_ROLE_TYPE + " VARCHAR(150)," +
            " UNIQUE (" + WholeSaleConstants.KEY_ROLE_USERID + "," + WholeSaleConstants.KEY_ROLE_TYPE + ")  ON CONFLICT REPLACE )";

    public static final String CREATE_TABLE_ROLE_FEATURE = "CREATE TABLE " + TABLE_ROLE_FEATURE + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,isDeleted INTEGER," + WholeSaleConstants.KEY_ROLE_USERID + " INTEGER,role_id INTEGER," +
            WholeSaleConstants.KEY_ROLE_FEATUREID + " INTEGER NOT NULL," + WholeSaleConstants.KEY_ROLE_NAME + " VARCHAR(150)," + WholeSaleConstants.KEY_ROLE_PARENTID + " VARCHAR(150)," +
            WholeSaleConstants.KEY_ROLE_TYPE + " VARCHAR(150),UNIQUE (" + WholeSaleConstants.KEY_ROLE_USERID + ",role_id) ON CONFLICT REPLACE) ";


    public static final String CREATE_TABLE_FPS_STORE= "CREATE TABLE " + TABLE_FPS_STORE + "("
            + KEY_ID + " INTEGER PRIMARY KEY NOT NULL," + WholeSaleConstants.KEY_FPS_ACTIVE + " VARCHAR(100) NOT NULL,"
            + WholeSaleConstants.KEY_FPS_NAME + " VARCHAR(100),"   + WholeSaleConstants.KEY_FPS_ADDRESS + " VARCHAR(100)," + WholeSaleConstants.KEY_FPS_CODE + " INTEGER NOT NULL,"+WholeSaleConstants.KEY_FPS_GENERATED_CODE + " VARCHAR(150) NOT NULL,"
            + WholeSaleConstants.KEY_FPS_DISTRICT_ID + " INTEGER,"+WholeSaleConstants.KEY_FPS_DISTRICT_CODE + " VARCHAR(150) ,"+WholeSaleConstants.KEY_FPS_DISTRICT_NAME + " INTEGER"+WholeSaleConstants.KEY_WHOLE_SALE_ID + " INTEGER ,"+WholeSaleConstants.KEY_FPS_TALUK_ID + " INTEGER ,"+WholeSaleConstants.KEY_FPS_TALUK_CODE + " VARCHAR(150) ,"+WholeSaleConstants.KEY_FPS_TALUK_NAME + " VARCHAR(150) ,"
            + WholeSaleConstants.KEY_FPS_VILLAGEID + " INTEGER," +WholeSaleConstants.KEY_FPS_VILLAGE_CODE + " VARCHAR(150) ,"+WholeSaleConstants.KEY_FPS_VILLAGE_NAME + " VARCHAR(150) ,"+ WholeSaleConstants.KEY_FPS_CATEGORY+ " VARCHAR(100) NOT NULL, " + WholeSaleConstants.KEY_FPS_CATEGORY_TYPE + " VARCHAR(150),"+WholeSaleConstants.KEY_FPS_CONTACT_NAME+" VARCHAR(100),whole_sale_code VARCHAR(100),"
            + WholeSaleConstants.KEY_FPS_BATCH_NO + " INTEGER "+ WholeSaleConstants.KEY_FPS_STATUS + " INTEGER(1),"+WholeSaleConstants.KEY_FPS_TYPE+" VARCHAR(150) ,"
            + WholeSaleConstants.KEY_FPS_MOBILE_NO + " INTEGER NOT NULL,"+ WholeSaleConstants.KEY_FPS_EMAILID + " VARCHAR(100)," + WholeSaleConstants.KEY_FPS_CREATEDBY + " INTEGER,"
            + WholeSaleConstants.KEY_FPS_LAT + " INTEGER ,"+ WholeSaleConstants.KEY_FPS_LONG + " INTEGER(1)" +")";


    public static final String CREATE_TABLE_KEROSENE_BUNK= "CREATE TABLE " + TABLE_KEROSENE_BUNK + "("
            + KEY_ID + " INTEGER PRIMARY KEY NOT NULL," + WholeSaleConstants.KEY_KEROSENE_BUNK_ACTIVE + " VARCHAR(100) NOT NULL," + WholeSaleConstants.KEY_KEROSENE_BUNK_GENERATED_CODE + " VARCHAR(150) NOT NULL,"
            + WholeSaleConstants.KEY_KEROSENE_BUNK_NAME + " VARCHAR(100) ," + WholeSaleConstants.KEY_KEROSENE_BUNK_ADDRESS + " VARCHAR(100) NOT NULL," + WholeSaleConstants.KEY_KEROSENE_BUNK_CODE + " INTEGER NOT NULL,"
            + WholeSaleConstants.KEY_KEROSENE_BUNK_DISTRICT_ID + " INTEGER,"+WholeSaleConstants.KEY_KEROSENE_WHOLE_SALE_ID+ " INTEGER,"+WholeSaleConstants.KEY_KBUNK_TALUK_ID + " INTEGER ,"+WholeSaleConstants.KEY_KBUNK_TALUK_CODE + " VARCHAR(150) ,"+WholeSaleConstants.KEY_KBUNK_TALUK_NAME + " VARCHAR(150) ,"
            + WholeSaleConstants.KEY_KEROSENE_BUNK_VILLAGEID + " INTEGER," + WholeSaleConstants.KEY_KEROSENE_BUNK_CATEGORY+ " VARCHAR(100) , " + WholeSaleConstants.KEY_KEROSENE_BUNK_TYPE + " VARCHAR(150),"+WholeSaleConstants.KEY_KEROSENE_BUNK_CONTACT_NAME+" VARCHAR(100),whole_sale_code VARCHAR(100),"
            + WholeSaleConstants.KEY_KEROSENE_BUNK_BATCH_NO + " INTEGER ,"+ WholeSaleConstants.KEY_KEROSENE_BUNK_STATUS + " INTEGER(1),"
            + WholeSaleConstants.KEY_KEROSENE_BUNK_MOBILE_NO + " INTEGER NOT NULL,"+ WholeSaleConstants.KEY_KEROSENE_BUNK_EMAILID + " VARCHAR(100)," + WholeSaleConstants.KEY_KEROSENE_BUNK_CREATEDBY + " INTEGER,"
            + WholeSaleConstants.KEY_KEROSENE_BUNK_LAT + " INTEGER ,"+ WholeSaleConstants.KEY_KEROSENE_BUNK_LONG + " INTEGER(1)" +")";

    public static final String CREATE_TABLE_RRC= "CREATE TABLE " + TABLE_RRC + "("
            + KEY_ID + " INTEGER PRIMARY KEY NOT NULL," + WholeSaleConstants.KEY_RRC_ACTIVE + " VARCHAR(100) NOT NULL," + WholeSaleConstants.KEY_RRC_GENERATED_CODE + " VARCHAR(150) NOT NULL,"
            + WholeSaleConstants.KEY_RRC_ADDRESS + " VARCHAR(100) NOT NULL," + WholeSaleConstants.KEY_RRC_CODE + " INTEGER NOT NULL,"
            + WholeSaleConstants.KEY_RRC_NAME + " VARCHAR(100) ,"+ WholeSaleConstants.KEY_RRC_DISTRICT_ID + " INTEGER,"+WholeSaleConstants.KEY_RRC_WHOLE_SALE_ID+ " INTEGER,"+WholeSaleConstants.KEY_RRC_TALUK_ID + " INTEGER ,"+WholeSaleConstants.KEY_RRC_TALUK_CODE + " VARCHAR(150) ,"+WholeSaleConstants.KEY_RRC_TALUK_NAME + " VARCHAR(150) ,"
            + WholeSaleConstants.KEY_RRC_VILLAGEID + " INTEGER," + WholeSaleConstants.KEY_RRC_CATEGORY+ " VARCHAR(100) , " + WholeSaleConstants.KEY_RRC_TYPE + " VARCHAR(150),"+WholeSaleConstants.KEY_RRC_CONTACT_NAME+" VARCHAR(100),whole_sale_code VARCHAR(100),"
            + WholeSaleConstants.KEY_RRC_BATCH_NO + " INTEGER ,"+ WholeSaleConstants.KEY_RRC_STATUS + " INTEGER(1),"
            + WholeSaleConstants.KEY_RRC_MOBILE_NO + " INTEGER NOT NULL,"+ WholeSaleConstants.KEY_RRC_EMAILID + " VARCHAR(100)," + WholeSaleConstants.KEY_RRC_CREATEDBY + " INTEGER,"
            + WholeSaleConstants.KEY_RRC_LAT + " INTEGER ,"+ WholeSaleConstants.KEY_RRC_LONG + " INTEGER(1)" +")";



    public static final String CREATE_TABLE_UPGRADE = "CREATE TABLE if not exists " + TABLE_UPGRADE + "(_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,ref_id VARCHAR(30),android_old_version INTEGER,android_new_version INTEGER,"
            + "state VARCHAR(30),description VARCHAR(250),status VARCHAR(20),refer_id VARCHAR(30),created_date VARCHAR(30),server_status INTEGER,execution_time VARCHAR(30))";



}
