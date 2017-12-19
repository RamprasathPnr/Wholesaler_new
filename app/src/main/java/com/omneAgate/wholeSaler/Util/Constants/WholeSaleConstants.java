package com.omneAgate.wholeSaler.Util.Constants;

/**
 * WHOLE_SALE database helper constant  variables.
 */
public class WholeSaleConstants {

    //Response
    public static final String RESPONSE_DATA = "RESPONSE_DATA";

    //User Table
    public static final String KEY_USERS_NAME = "user_name";
    public static final String KEY_USERS_ID = "user_id";
    public static final String KEY_USERS_PASS_HASH = "password_hash";
    public static final String KEY_USERS_WHOLE_SALE_ID = "whole_sale_id";
    public static final String KEY_USERS_PROFILE = "user_profile";
    public static final String KEY_USERS_PHONE_NUMBER = "phone_number";
    public static final String KEY_USERS_ADDRESS = "address";
    public static final String KEY_USERS_DISTRICT_ID = "district_id";
    public static final String KEY_USERS_TALUK_ID = "taluk_id";
    public static final String KEY_USERS_VILLAGE_ID = "village_id";
    public static final String KEY_USERS_CODE = "wholesaler_code";
    public static final String KEY_USERS_CONTACT_PERSON = "contact_person";
    public static final String KEY_USERS_IS_ACTIVE = "is_active";
    public static final String KEY_USERS_IS_USERACTIVE="is_user_active";


    //Product Table
    public static final String KEY_PRODUCT_NAME = "name";
    public static final String KEY_LPRODUCT_NAME = "local_name";
    public static final String KEY_LPRODUCT_UNIT = "local_unit";
    public static final String KEY_PRODUCT_UNIT = "unit";
    public static final String KEY_PRODUCT_CODE = "code";
    public static final String KEY_PRODUCT_PRICE = "price";
    public static final String KEY_NEGATIVE_INDICATOR = "negative_indicator";
    public static final String KEY_PRODUCT_MODIFIED_DATE = "modified_date";
    public static final String KEY_MODIFIED_BY = "modified_by";
    public static final String KEY_CREATED_DATE = "created_date";
    public static final String KEY_CREATED_BY = "created_by";


    //Stock Table
    public static final String KEY_STOCK_WHOLE_SALE_ID = "whole_sale_id";
    public static final String KEY_STOCK_PRODUCT_ID = "product_id";
    public static final String KEY_STOCK_QUANTITY = "quantity";
    public static final String KEY_STOCK_REORDER_LEVEL = "reorder_level";
    public static final String KEY_STOCK_EMAIL_ACTION = "email_action";
    public static final String KEY_STOCK_SMS_ACTION = "sms_action";


    //Stock Inward
    public static final String KEY_WHOLE_SALE_STOCK_INWARD_SUPPLIERID = "supplier_id";
    public static final String KEY_WHOLE_SALE_STOCK_INWARD_WHOLE_SALEID = "whole_sale_id";
    public static final String KEY_WHOLE_SALE_STOCK_INWARD_INWARD_DATE = "inward_date";
    public static final String KEY_WHOLE_SALE_STOCK_INWARD_PRODUCTID = "product_id";
    public static final String KEY_WHOLE_SALE_STOCK_INWARD_QUANTITY = "quantity";
    public static final String KEY_WHOLE_SALE_STOCK_INWARD_UNIT = "unit";
    public static final String KEY_WHOLE_SALE_STOCK_INWARD_BATCH_NO = "batch_no";
    public static final String KEY_WHOLE_SALE_STOCK_INWARD_IS_WHOLE_SALEACKSTATUS = "whole_sale_ack_status";
    public static final String KEY_WHOLE_SALE_STOCK_INWARD_WHOLE_SALEACKDATE = "whole_sale_ack_date";
    public static final String KEY_WHOLE_SALE_STOCK_INWARD_WHOLE_SALERECEIVEIQUANTITY = "whole_sale_receive_quantity";
    public static final String KEY_WHOLE_SALE_STOCK_INWARD_CREATEDBY = "created_by";
    public static final String KEY_WHOLE_SALE_STOCK_INWARD_DELIEVERY_CHELLANID = "delivery_challan_id";
    public static final String KEY_WHOLE_SALE_STOCK_INWARD_RETRY_COUNT = "retry_count";
    public static final String KEY_WHOLE_SALE_STOCK_INWARD_RETRY_TIME = "retry_time";


    //Stock outward


    public static final String KEY_WHOLE_SALE_STOCK_OUTWARD_WHOLE_SALEID = "whole_sale_id";
    public static final String KEY_WHOLE_SALE_STOCK_OUTWARD_OUTWARD_DATE = "outward_date";
    public static final String KEY_WHOLE_SALE_STOCK_OUTWARD_PRODUCTID = "product_id";
    public static final String KEY_WHOLE_SALE_STOCK_OUTWARD_QUANTITY = "quantity";
    public static final String KEY_WHOLE_SALE_STOCK_OUTWARD_UNIT = "unit";


   //Stock History
    public static final String KEY_STOCK_HISTORY_PRODUCT_ID = "product_id";
    public static final String KEY_STOCK_HISTORY_OPEN_BALANCE = "opening_balance";
    public static final String KEY_STOCK_HISTORY_CLOSE_BALANCE = "closing_balance";
    public static final String KEY_STOCK_HISTORY_CHANGE_BALANCE = "change_in_balance";
    public static final String KEY_STOCK_HISTORY_ACTION = "action";
    public static final String KEY_STOCK_HISTORY_DATE = "created_date";
    public static final String KEY_STOCK_DATE = "date_creation";

    //Language Table
    public static final String KEY_LANGUAGE_CODE = "code";
    public static final String KEY_LANGUAGE_MESSAGE = "message";
    public static final String KEY_LANGUAGE_L_MESSAGE = "local_message";
    public static final String KEY_TRANSACTION_TYPE_TXN_TYPE = "type";


    //Supplier table
    public static final String KEY_SUPPLIER_NAME = "supplier_name";
    public static final String KEY_SUPPLIER_CREATEDBY = "created_by";
    public static final String KEY_SUPPLIER_CREATEDDATE = "created_date";
    public static final String KEY_SUPPLIER_MODIFIEDDATE = "modified_date";
    public static final String KEY_SUPPLIER_STATUS = "status";
    public static final String KEY_SPECIAL_DISTRICT = "district_id";
    public static final String KEY_SPECIAL_TALUK = "taluk_id";
    public static final String KEY_SPECIAL_MINIMUM = "minimum_qty";
    public static final String KEY_SPECIAL_OVERRIDE = "override_price";
    public static final String KEY_SPECIAL_VILLAGE = "village_id";
    public static final String KEY_SPECIAL_CYLINDER = "cylinder_count";
    public static final String KEY_SPECIAL_IS_TALUK = "taluk";
    public static final String KEY_SPECIAL_IS_CITY = "city";
    public static final String KEY_SPECIAL_IS_MUNICIPALITY = "municipality";
    public static final String KEY_SPECIAL_IS_CITY_HEAD = "city_head_quarter";
    public static final String KEY_SPECIAL_IS_ADD = "is_add";
    public static final String KEY_SPECIAL_QUANTITY = "quantity";



    //Roll fields
    public static final String KEY_ROLE_TYPE = "role_type";

    //roll feature fields
    public static final String KEY_ROLE_USERID = "user_id";
    public static final String KEY_ROLE_FEATUREID = "role_feature_id";
    public static final String KEY_ROLE_NAME = "role_name";
    public static final String KEY_ROLE_PARENTID = "role_parent_id";



    //fps store fileds
    public static final String KEY_FPS_ACTIVE = "active";
    public static final String KEY_FPS_NAME = "name";
    public static final String KEY_FPS_ADDRESS = "address";
    public static final String KEY_FPS_CODE = "fps_code";
    public static final String KEY_FPS_GENERATED_CODE = "fps_generated_code";
    public static final String KEY_WHOLE_SALE_ID = "whole_sale_id";
    public static final String KEY_FPS_VILLAGEID = "village_id";
    public static final String KEY_FPS_VILLAGE_CODE ="village_code";
    public static final String KEY_FPS_VILLAGE_NAME ="village_name";
    public static final String KEY_FPS_TALUK_ID = "taluk_id";
    public static final String KEY_FPS_TALUK_CODE ="taluk_code";
    public static final String KEY_FPS_TALUK_NAME ="taluk_name";
    public static final String KEY_FPS_DISTRICT_ID = "district_id";
    public static final String KEY_FPS_DISTRICT_CODE ="district_code";
    public static final String KEY_FPS_DISTRICT_NAME ="district_name";

    public static final String KEY_FPS_CATEGORY = "fps_category";
    public static final String KEY_FPS_CATEGORY_TYPE = "fps_category_type";
    public static final String KEY_FPS_TYPE = "fps_type";
    public static final String KEY_FPS_BATCH_NO = "batch_no";
    public static final String KEY_FPS_STATUS = "status";
    public static final String KEY_FPS_CREATEDBY = "create_by";
    public static final String KEY_FPS_CONTACT_NAME = "contact_name";
    public static final String KEY_FPS_MOBILE_NO = "mobile_no";
    public static final String KEY_FPS_EMAILID = "email_id";
    public static final String KEY_FPS_LAT = "latitude";
    public static final String KEY_FPS_LONG = "longitude";




    //Kerosene bunk fileds
    public static final String KEY_KEROSENE_BUNK_ACTIVE = "active";
    public static final String KEY_KEROSENE_BUNK_ADDRESS = "address";
    public static final String KEY_KEROSENE_BUNK_NAME = "name";
    public static final String KEY_KEROSENE_BUNK_CODE = "kerosene_bunk_code";
    public static final String KEY_KEROSENE_BUNK_GENERATED_CODE = "kerosene_generated_code";
    public static final String KEY_KEROSENE_BUNK_TYPE = "kerosene_bunk_type";
    public static final String KEY_KEROSENE_WHOLE_SALE_ID = "whole_sale_id";
    public static final String KEY_KEROSENE_BUNK_VILLAGEID = "village_id";
    public static final String KEY_KBUNK_TALUK_ID = "taluk_id";
    public static final String KEY_KBUNK_TALUK_CODE ="taluk_code";
    public static final String KEY_KBUNK_TALUK_NAME ="taluk_name";
    public static final String KEY_KEROSENE_BUNK_DISTRICT_ID = "district_id";
    public static final String KEY_KEROSENE_BUNK_CATEGORY = "kerosene_bunk_category";
    public static final String KEY_KEROSENE_BUNK_BATCH_NO = "batch_no";
    public static final String KEY_KEROSENE_BUNK_STATUS = "status";
    public static final String KEY_KEROSENE_BUNK_CREATEDBY = "create_by";
    public static final String KEY_KEROSENE_BUNK_CONTACT_NAME = "contact_name";
    public static final String KEY_KEROSENE_BUNK_MOBILE_NO = "mobile_no";
    public static final String KEY_KEROSENE_BUNK_EMAILID = "email_id";
    public static final String KEY_KEROSENE_BUNK_LAT = "latitude";
    public static final String KEY_KEROSENE_BUNK_LONG = "longitude";




    //RRC fileds
    public static final String KEY_RRC_ACTIVE = "active";
    public static final String KEY_RRC_NAME = "name";
    public static final String KEY_RRC_ADDRESS = "address";
    public static final String KEY_RRC_CODE = "rrc_code";
    public static final String KEY_RRC_GENERATED_CODE = "rrc_generated_code";
    public static final String KEY_RRC_TYPE = "rrc_type";
    public static final String KEY_RRC_WHOLE_SALE_ID = "whole_sale_id";
    public static final String KEY_RRC_VILLAGEID = "village_id";
    public static final String KEY_RRC_DISTRICT_ID = "district_id";
    public static final String KEY_RRC_TALUK_ID = "taluk_id";
    public static final String KEY_RRC_TALUK_CODE ="taluk_code";
    public static final String KEY_RRC_TALUK_NAME ="taluk_name";
    public static final String KEY_RRC_CATEGORY = "rrc_category";
    public static final String KEY_RRC_BATCH_NO = "batch_no";
    public static final String KEY_RRC_STATUS = "status";
    public static final String KEY_RRC_CREATEDBY = "create_by";
    public static final String KEY_RRC_CONTACT_NAME = "contact_name";
    public static final String KEY_RRC_MOBILE_NO = "mobile_no";
    public static final String KEY_RRC_EMAILID = "email_id";
    public static final String KEY_RRC_LAT = "latitude";
    public static final String KEY_RRC_LONG = "longitude";



}
