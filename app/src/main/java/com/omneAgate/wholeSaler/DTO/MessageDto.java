package com.omneAgate.wholeSaler.DTO;

import android.database.Cursor;
import com.omneAgate.wholeSaler.Util.Constants.WholeSaleConstants;
import lombok.Data;

/**
 * Created by user1 on 6/3/15.
 */
@Data
public class MessageDto extends BaseDto {

    long id;


    long languageCode;


    String description;


    String localDescription;


    public MessageDto() {

    }

    public MessageDto(Cursor cur) {

        languageCode = cur.getLong(cur
                .getColumnIndex(WholeSaleConstants.KEY_LANGUAGE_CODE));

        description = cur.getString(cur
                .getColumnIndex(WholeSaleConstants.KEY_LANGUAGE_MESSAGE));

        localDescription = cur.getString(cur
                .getColumnIndex(WholeSaleConstants.KEY_LANGUAGE_L_MESSAGE));
    }

}
