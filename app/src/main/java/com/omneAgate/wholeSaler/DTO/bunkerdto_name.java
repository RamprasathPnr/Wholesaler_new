package com.omneAgate.wholeSaler.DTO;

import android.database.Cursor;

import com.omneAgate.wholeSaler.Util.Constants.WholeSaleConstants;

import java.io.Serializable;

import lombok.Data;

/**
 * Created by user1 on 10/2/16.
 */
@Data
public class bunkerdto_name extends BaseDto implements Serializable {

    String shop_type;

    String shop_code;

    String shop_name;

    String contact_persion;

    String contact_number;

    public bunkerdto_name(){


    }

    public bunkerdto_name(Cursor c) {

        shop_code = c.getString(c.getColumnIndex(WholeSaleConstants.KEY_KEROSENE_BUNK_CODE));
        shop_name = c.getString(c.getColumnIndex(WholeSaleConstants.KEY_KEROSENE_BUNK_NAME));
        contact_persion = c.getString(c.getColumnIndex(WholeSaleConstants.KEY_KEROSENE_BUNK_CONTACT_NAME));
        contact_number = c.getString(c.getColumnIndex(WholeSaleConstants.KEY_KEROSENE_BUNK_MOBILE_NO));

    }
}
