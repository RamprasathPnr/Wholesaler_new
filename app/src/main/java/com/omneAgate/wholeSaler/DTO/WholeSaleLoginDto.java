package com.omneAgate.wholeSaler.DTO;

import java.io.Serializable;

import lombok.Data;


@Data
public class WholeSaleLoginDto extends BaseDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** User's unique user id */

	String userId;

	/** Password */
	String password;
	
	/** Device Mac Id */
	String macId;
	

   /* *//** Application type  *//*
	String appType;*/
}
