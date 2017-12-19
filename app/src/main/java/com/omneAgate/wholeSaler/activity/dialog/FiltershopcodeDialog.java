package com.omneAgate.wholeSaler.activity.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.omneAgate.wholeSaler.Util.AndroidDeviceProperties;
import com.omneAgate.wholeSaler.Util.TamilUtil;
import com.omneAgate.wholeSaler.Util.Util;
import com.omneAgate.wholeSaler.activity.GlobalAppState;
import com.omneAgate.wholeSaler.activity.R;

/**
 * Created by user1 on 26/1/16.
 */
public class FiltershopcodeDialog extends Dialog  {


    private final Activity context;  //    Context from the user

    /*Constructor class for this dialog*/
    public FiltershopcodeDialog(Activity _context) {
        super(_context);
        context = _context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setContentView(R.layout.filtershopcode);
        setCancelable(true);
    }
}

