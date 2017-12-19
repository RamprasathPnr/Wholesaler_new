package com.omneAgate.wholeSaler.DTO;


   import android.database.Cursor;

   import com.omneAgate.wholeSaler.Util.Constants.WholeSaleConstants;

   import java.io.Serializable;
   import lombok.Data;

@Data
public class RrcDto extends BaseDto implements Serializable {
    /**
     * for serialization
     */
    private static final long serialVersionUID = 1L;

    /** identity of the RRC */

    Long id;

    /** code of the Rrc */
    String code;

    /** name of the RRC **/
    String name;

    /** status of Rrc */

    Boolean status;

    /** contact number of the contact person */

    String contactNumber;

    /** email id of the contact person */

    String emailId;

    /** pincode of the rrc */
    String pincode;

    /** Date of creation */

    long createdDate;

    /** Date of modification */

    long modifiedDate;

    /** user id who created */
    Long createdBy;

    /** user id who modified */
    Long modifiedBy;

    /** longitude of the rrc */
    String longitude;

    /** latitude of the Rrc */
    String latitude;

    /** contactPersonName of the rrc */
    String contactPersonName;

    /** address of the rrc */
    String address;

    /** Taluk association */
    String talukName;


    String talukCode;


    String generatedCode;

    long talukId;

    public RrcDto(){

    }
    public RrcDto(Cursor c){
        code = c.getString(c.getColumnIndex(WholeSaleConstants.KEY_RRC_CODE));
      //  name = c.getString(c.getColumnIndex(WholeSaleConstants.KEY_RRC_NAME));
      //  contactPersonName = c.getString(c.getColumnIndex(WholeSaleConstants.KEY_RRC_CONTACT_NAME));
      //  contactNumber = c.getString(c.getColumnIndex(WholeSaleConstants.KEY_RRC_MOBILE_NO));

    }

}