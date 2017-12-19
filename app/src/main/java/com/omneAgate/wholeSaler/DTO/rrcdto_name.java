package com.omneAgate.wholeSaler.DTO;

import android.database.Cursor;

import com.omneAgate.wholeSaler.Util.Constants.WholeSaleConstants;

import java.io.Serializable;

import lombok.Data;

/**
 * Created by user1 on 10/2/16.
 */
@Data
public class rrcdto_name extends BaseDto implements Serializable {

    String shop_type;

    String shop_code;

    String shop_name;

    String contact_persion;

    String contact_number;

    public rrcdto_name(){

    }

    public rrcdto_name(Cursor c) {

        shop_code = c.getString(c.getColumnIndex(WholeSaleConstants.KEY_RRC_CODE));
        shop_name = c.getString(c.getColumnIndex(WholeSaleConstants.KEY_RRC_NAME));
        contact_persion = c.getString(c.getColumnIndex(WholeSaleConstants.KEY_RRC_CONTACT_NAME));
        contact_number = c.getString(c.getColumnIndex(WholeSaleConstants.KEY_RRC_MOBILE_NO));

    }
}

