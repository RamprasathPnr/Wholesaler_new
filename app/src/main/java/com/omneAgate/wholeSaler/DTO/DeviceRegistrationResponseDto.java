package com.omneAgate.wholeSaler.DTO;

import com.omneAgate.wholeSaler.DTO.EnumDTO.DeviceRegistrationStatus;
import lombok.Data;

/**
 * This class encapsulates response of device Registration
 * 
 * @author user1
 *
 */
@Data
public class DeviceRegistrationResponseDto extends BaseDto{
	/**Device Registration status*/
	String deviceRegistrationStatus;

}
