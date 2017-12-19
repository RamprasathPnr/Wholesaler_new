package com.omneAgate.wholeSaler.DTO;

import java.util.Date;

import lombok.Data;

/**
 * Used to set log details15.
 */
@Data
public class LoggingDto {

    long id;                        //Primary Key value

    String deviceId;                    //device idgmt

    String logMessage;                //error message

    String errorType;                //type of error- could be FATAL, WARN

    Date createDate;
}
