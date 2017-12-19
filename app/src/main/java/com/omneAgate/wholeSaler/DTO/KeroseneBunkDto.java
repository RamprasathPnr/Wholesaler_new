package com.omneAgate.wholeSaler.DTO;

import android.database.Cursor;

import com.omneAgate.wholeSaler.Util.Constants.WholeSaleConstants;

import java.io.Serializable;

import lombok.Data;

@Data
public class KeroseneBunkDto extends BaseDto implements Serializable {


    /**
     * for serialization
     */
    private static final long serialVersionUID = 1L;

    /** identity of the Kerosene bunk */


    Long id;

    /** code of the kerosene bunk */
    String code;

    /** name of the kerosene bunk **/

    String name;

    /** status of kerosene bunk */

    Boolean status;

    /** contact number of the contact person */

    String contactNumber;

    /** email id of the contact person */
    String emailId;

    /** pincode of the kerosene bunk */
    String pincode;

    /** Date of creation */

    long createdDate;

    /** Date of modification */

    long modifiedDate;

    /** user id who created */


    Long createdBy;

    /** user id who modified */
    Long modifiedBy;

    /** longitude of the Kerosene bunk */

    String longitude;

    /** latitude of the Kerosene bunk */

    String latitude;

    /** contactPersonName of the kerosene bunk */
    String contactPersonName;

    /** address of the kerosene bunk */


    String address;

    /** Taluk association */
    String talukName;


    String talukCode;


    Long talukId;


    String generatedCode;



    public  KeroseneBunkDto(){

    }
    public KeroseneBunkDto(Cursor c){
        code = c.getString(c.getColumnIndex(WholeSaleConstants.KEY_KEROSENE_BUNK_CODE));
//        name = c.getString(c.getColumnIndex(WholeSaleConstants.KEY_KEROSENE_BUNK_NAME));
      //  contactPersonName = c.getString(c.getColumnIndex(WholeSaleConstants.KEY_KEROSENE_BUNK_CONTACT_NAME));
      //  contactNumber = c.getString(c.getColumnIndex(WholeSaleConstants.KEY_KEROSENE_BUNK_MOBILE_NO));
    }

}
