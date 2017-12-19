package com.omneAgate.wholeSaler.DTO;

import android.database.Cursor;

import com.omneAgate.wholeSaler.Util.Constants.WholeSaleConstants;

import java.io.Serializable;

import lombok.Data;

@Data
public class WholesalerDto extends BaseDto implements Serializable {
    /**
     * for serialization
     */
    private static final long serialVersionUID = 1L;

    /** identity of the kerosene wholesaler */
    Long id;

    /** code of the kerosene wholesaler */
    String code;

    /** name of the kerosene wholesaler **/
    String name;

    /** status of kerosene wholesaler */
    Boolean status;

    /** contact number of the contact person */
    String contactNumber;

    /** contact PersonName of the kerosene Bunk */
    String contactPersonName;

    /** email id of the contact person */
    String emailId;

    /** pincode of the kerosene bunk */
    String pincode;

    /** address of the kerosene bunk */
    String address;

    /** created by */
    Long createdBy;

    /** Date of creation */
    long createdDate;

    /** Date of modified **/
    Long modifiedBy;

    /** Date of modification */

    long modifiedDate;

    /** longitude of the Kerosene wholesaler */
    String longitude;

    /** latitude of the Kerosene wholesaler */
    String latitude;

    /** Taluk name */
    String talukName;

    public WholesalerDto(){

    }
    public WholesalerDto(Cursor cur){

        id = cur.getLong(cur.getColumnIndex(WholeSaleConstants.KEY_USERS_ID));
        code = cur.getString(cur.getColumnIndex(WholeSaleConstants.KEY_USERS_CODE));
        name = cur.getString(cur.getColumnIndex(WholeSaleConstants.KEY_USERS_PROFILE));
        contactNumber = cur.getString(cur.getColumnIndex(WholeSaleConstants.KEY_USERS_PHONE_NUMBER));
        address = cur.getString(cur.getColumnIndex(WholeSaleConstants.KEY_USERS_ADDRESS));
        contactPersonName = cur.getString(cur.getColumnIndex(WholeSaleConstants.KEY_USERS_CONTACT_PERSON));
        if(cur.getInt(cur.getColumnIndex(WholeSaleConstants.KEY_USERS_IS_ACTIVE))==1){
            status = true;
        }else{
            status = false;
        }
    }

}
