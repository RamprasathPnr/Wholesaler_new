package com.omneAgate.wholeSaler.DTO;

import lombok.Data;

/**
 * Created by root on 22/8/16.
 */
@Data
public class LogoutDto {

    String sessionId;

    String logoutStatus;

    String logoutTime;

    ApplicationType appType = ApplicationType.KeroseneWholeSaler;


}
