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
import com.omneAgate.wholeSaler.Util.TamilUtil;
import com.omneAgate.wholeSaler.Util.Util;
import com.omneAgate.wholeSaler.activity.BaseActivity;
import com.omneAgate.wholeSaler.activity.GlobalAppState;
import com.omneAgate.wholeSaler.activity.R;


/**
 * This dialog will appear on the time of user logout
 */
public class LogoutDialog extends Dialog implements
        View.OnClickListener {

    private final Activity context;  //    Context from the user

    /*Constructor class for this dialog*/
    public LogoutDialog(Activity _context) {
        super(_context);
        context = _context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setContentView(R.layout.dialog_logout);
        setCancelable(false);
        Button okButton = (Button) findViewById(R.id.buttonNwOk);
        okButton.setOnClickListener(this);
        Button cancelButton = (Button) findViewById(R.id.buttonNwCancel);
        cancelButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonNwOk:
                dismiss();
                ((BaseActivity) context).logOutSuccess();
                break;
            case R.id.buttonNwCancel:
                dismiss();
                break;
        }
    }

    /**
     * Tamil text textView typeface
     * input  textView name and id for string.xml
     */
    public void setTamilText(TextView textName, int id) {
        if (GlobalAppState.language.equals("ta")) {
            Typeface tfBamini = Typeface.createFromAsset(context.getAssets(), "fonts/Bamini.ttf");
            textName.setTypeface(tfBamini);
            textName.setText(TamilUtil.convertToTamil(TamilUtil.BAMINI, context.getString(id)));
        } else {
            textName.setText(context.getString(id));
        }
    }

}