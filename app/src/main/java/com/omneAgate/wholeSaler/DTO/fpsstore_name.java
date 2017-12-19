package com.omneAgate.wholeSaler.DTO;

import android.database.Cursor;

import com.omneAgate.wholeSaler.Util.Constants.WholeSaleConstants;

import org.bouncycastle.jce.provider.symmetric.Grain128;

import java.io.Serializable;

import lombok.Data;

/**
 * Created by user1 on 10/2/16.
 */
@Data
public class fpsstore_name extends BaseDto implements Serializable {

    String shop_type;

    String shop_code;

    String shop_name;

    String contact_persion;

    String contact_number;

    public fpsstore_name(){


    }

    public fpsstore_name(Cursor c) {

        shop_code = c.getString(c.getColumnIndex(WholeSaleConstants.KEY_FPS_GENERATED_CODE));
        contact_persion = c.getString(c.getColumnIndex(WholeSaleConstants.KEY_FPS_CONTACT_NAME));
        contact_number = c.getString(c.getColumnIndex(WholeSaleConstants.KEY_FPS_MOBILE_NO));
        shop_name = c.getString(c.getColumnIndex(WholeSaleConstants.KEY_FPS_NAME));
    }
}
