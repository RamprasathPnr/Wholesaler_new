package com.omneAgate.wholeSaler.DTO;

import lombok.Data;

/**
 * This class is encapsulates request send for registration
 * 
 * @author user1
 * 
 */
@Data
public class DeviceRegistrationRequestDto extends BaseDto{

	/** login details of the user */
	LoginDto loginDto; 					
 
	 /** device details*/
	DeviceDetailsDto deviceDetailsDto;

}
