package com.omneAgate.wholeSaler.DTO;

import java.io.Serializable;

import lombok.Data;

@Data
public class LoginDto extends BaseDto  implements Serializable{


	String userName;		// Users name

	String password;		//Users password is hashed and stored.
    
    /**The device from which the request came for login*/
    String deviceId;
    

}
