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
import com.omneAgate.wholeSaler.activity.GlobalAppState;
import com.omneAgate.wholeSaler.activity.R;


/**
 * This dialog will appear on to reset  password
 */
public class ResetPasswordDialog extends Dialog implements
        View.OnClickListener {

    private final Activity context;  //    Context from the user

    /*Constructor class for this dialog*/
    public ResetPasswordDialog(Activity _context) {
        super(_context);
        context = _context;
        }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setContentView(R.layout.dialog_reset_password);
        setCancelable(false);

        Button okButton = (Button) findViewById(R.id.buttonNwOk);
        okButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonNwOk:
                dismiss();
                break;

        }
    }

    /**
     * Tamil text textView typeface
     * input  textView name and text string input
     */
    public void setTamilText(TextView textName, String text,int fontSize) {
        if (GlobalAppState.language.equalsIgnoreCase("ta")) {
            Typeface tfBamini = Typeface.createFromAsset(context.getAssets(), "fonts/Bamini.ttf");
            textName.setTypeface(tfBamini);
            textName.setTextSize(fontSize);
            textName.setText(TamilUtil.convertToTamil(TamilUtil.BAMINI,  text));
        } else {
            textName.setText(text);
        }
    }
}