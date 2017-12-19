package com.omneAgate.wholeSaler.DTO;

import android.database.Cursor;

import com.omneAgate.wholeSaler.Util.Constants.WholeSaleConstants;
import com.omneAgate.wholeSaler.Util.WholesaleDBHelper;

import java.io.Serializable;

import lombok.Data;

/**
 * This class is used to transfer UserDetails 
 * @author user1
 */
@Data
public class UserDetailDto  extends BaseDto implements Serializable {
		
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
/**Unique id to identify the user*/
	Long id; 	
	
	/**User Id to identify the user uniquely*/
	String userId;

    //Users password is hashed and stored.
    String encryptedPassword;

	/** Users name */
	String username;		
	
  /** Users password is hashed and stored.*/
	String password;		
	
 /** an enum values for FPSUSER, ADMIN, SUPERADMIN */
	String profile;

    Boolean status;

    Boolean active;

    /** Wholesaler id */
	WholesalerDto wholesalerDto;



    public UserDetailDto(Cursor cur) {
        id = cur.getLong(cur.getColumnIndex(WholesaleDBHelper.KEY_ID));
        username = cur.getString(cur.getColumnIndex(WholeSaleConstants.KEY_USERS_ID));
        userId = cur.getString(cur.getColumnIndex(WholeSaleConstants.KEY_USERS_NAME));
        password = cur.getString(cur.getColumnIndex(WholeSaleConstants.KEY_USERS_PASS_HASH));
        profile = cur.getString(cur.getColumnIndex(WholeSaleConstants.KEY_USERS_PROFILE));
        encryptedPassword = cur.getString(cur.getColumnIndex("encrypted_password"));
        if(cur.getInt(cur.getColumnIndex(WholeSaleConstants.KEY_USERS_IS_ACTIVE))==1){
            status = true;
        }else{
            status = false;
        }

        if(cur.getInt(cur.getColumnIndex(WholeSaleConstants.KEY_USERS_IS_USERACTIVE))==1){
            active = true;
        }else {
            active=false;
        }

        wholesalerDto = new WholesalerDto(cur);

    }
   
}
