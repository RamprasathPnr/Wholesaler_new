package com.omneAgate.wholeSaler.DTO;


import android.database.Cursor;

import com.omneAgate.wholeSaler.Util.Constants.WholeSaleConstants;

import java.io.Serializable;

import lombok.Data;

@Data
public class WholesalerPostingDto extends BaseDto implements Serializable {

   //Wholesaler Id
    Long id;

    /** Name of the product */
    String pruductName;

    /** Unit of the product */
    String productUnit;

    /** id of the product */
    Long productId;

    /** quantity of the product */
    double quantity;

    /** unique Wholesaler code */
    String wholesalerCode;

    /** unique fps Generatecode */
    String fpsCode;

    /** unique rrc Generatecode */
    String rrcCode;

    /** unique kerosene bunk Generatecode */
    String kbCode;

    /** Contact Number */
    String contactNumber;

    /** Transporter Name */
    String transportName;

    Long createdBy;

    /** Stock outward time from the kerosene wholesaler */
    String outwardDate;


    String referenceNo;

    /** vehicle number */
    String vehicleNumber;

    /** Driver Name */
    String driverName;

    /** Driver mobile number */
    String drivermobileNumber;

    //Recipeint Type- KBUNK,FPS,RRC
    String recipientType;

    //Recipient Code
    String code;

    //Transfered -T ,Otherwise - R
    String status;


    String year;


    String month;


    String inwardType ;

    public WholesalerPostingDto() {

    }

    public WholesalerPostingDto(Cursor cursor) {

        id = cursor.getLong(cursor.getColumnIndex(WholeSaleConstants.KEY_WHOLE_SALE_STOCK_OUTWARD_WHOLE_SALEID));
        outwardDate = cursor.getString(cursor.getColumnIndex(WholeSaleConstants.KEY_CREATED_DATE));
        productId = cursor.getLong(cursor.getColumnIndex(WholeSaleConstants.KEY_WHOLE_SALE_STOCK_OUTWARD_PRODUCTID));
        quantity = cursor.getDouble(cursor.getColumnIndex(WholeSaleConstants.KEY_WHOLE_SALE_STOCK_OUTWARD_QUANTITY));
        referenceNo = cursor.getString(cursor.getColumnIndex("referenceNo"));
        productUnit = cursor.getString(cursor.getColumnIndex(WholeSaleConstants.KEY_WHOLE_SALE_STOCK_OUTWARD_UNIT));
        recipientType = cursor.getString(cursor.getColumnIndex("recipient_type"));
        code = cursor.getString(cursor.getColumnIndex("recipient_code"));
        transportName = cursor.getString(cursor.getColumnIndex("transporterName"));
        vehicleNumber = cursor.getString(cursor.getColumnIndex("vehicleNumber"));
        driverName = cursor.getString(cursor.getColumnIndex("driverName"));
        contactNumber = cursor.getString(cursor.getColumnIndex("driverContactNumber"));
        status =  cursor.getString(cursor.getColumnIndex("status"));
        month = cursor.getString(cursor.getColumnIndex("month"));
        year = cursor.getString(cursor.getColumnIndex("year"));
        inwardType =  cursor.getString(cursor.getColumnIndex("inward_type"));


    }




}
