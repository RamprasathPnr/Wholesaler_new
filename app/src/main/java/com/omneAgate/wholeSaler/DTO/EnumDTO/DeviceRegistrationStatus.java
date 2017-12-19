package com.omneAgate.wholeSaler.DTO.EnumDTO;

import lombok.Getter;
import lombok.Setter;



/**
 * This defines the status of device activation
 * 
 * @author user1
 *
 */

public enum DeviceRegistrationStatus {
	
     NEW("Device registered sucessfully.Please contact helpdesk for approval"),	// initial state when the request is submitted
	
	INPROCESS("Device waiting with admin for approval"),		// Waiting in approval queue
	
	APPROVED ("Device is already approved"),					// Device associated to FPS
	
	REJECTED("Device Rejected"), 								//Device rejected from approval
;
	
	@Getter @Setter
	private String description;									// provides description for enum state
	
	/**
	 * constructor which accepts description
	 * @param description
	 */
	private DeviceRegistrationStatus(String description){
		this.description=description;
	}


}
