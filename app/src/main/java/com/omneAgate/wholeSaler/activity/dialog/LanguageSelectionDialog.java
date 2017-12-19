package com.omneAgate.wholeSaler.activity.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.omneAgate.wholeSaler.Util.TamilUtil;
import com.omneAgate.wholeSaler.Util.Util;
import com.omneAgate.wholeSaler.activity.GlobalAppState;
import com.omneAgate.wholeSaler.activity.R;


/**
 * This dialog will appear on the time of user logout
 */
public class LanguageSelectionDialog extends Dialog implements
        View.OnClickListener {


    private final Activity context;  //    Context from the user

    private boolean tamil = false;

    private ToggleButton languageSelection;

    /*Constructor class for this dialog*/
    public LanguageSelectionDialog(Activity _context) {
        super(_context);
        context = _context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setContentView(R.layout.dialog_language);
        setCancelable(false);

        languageSelection =(ToggleButton)findViewById(R.id.changeLanguage);
        languageSelection.setOnClickListener(this);
        Button okButton = (Button) findViewById(R.id.buttonNwOk);
        okButton.setOnClickListener(this);
        Button cancelButton = (Button) findViewById(R.id.buttonNwCancel);
        cancelButton.setOnClickListener(this);

        if (GlobalAppState.language.equals("ta")) {
            tamil = true;
            languageSelection.setChecked(false);
        }else {
            languageSelection.setChecked(true);
        }




    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonNwOk:
                changeLanguage();
                dismiss();
                break;
            case R.id.buttonNwCancel:
                dismiss();
                break;

            case R.id.changeLanguage:
                Log.e("LanguageSelection","status : "+languageSelection.isChecked());
                if (languageSelection.isChecked()) {
                    tamil = false;
                } else {
                    tamil = true;
                }
                break;
        }
    }

    /**
     * Tamil text textView typeface
     * input  textView name and id for string.xml
     */
    public void setTamilText(RadioButton textName, int id) {
        Typeface tfBamini = Typeface.createFromAsset(context.getAssets(), "fonts/Bamini.ttf");
        textName.setTypeface(tfBamini);
        textName.setText(TamilUtil.convertToTamil(TamilUtil.BAMINI, context.getString(id)));
    }

    //Re-starts the application where language change take effects
    private void restartApplication() {
        Intent restart = context.getBaseContext().getPackageManager()
                .getLaunchIntentForPackage(context.getBaseContext().getPackageName());
        restart.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(restart);
        context.finish();
    }

    /**
     * Used to change language
     */
    private void changeLanguage() {
        if (tamil) {
         // Util.LoggingQueue(context, "Selected", "Tamil");
            Util.changeLanguage(context, "ta");
            Log.e("Tamil","Tamil");
        } else {
         // Util.LoggingQueue(context, "Selected", "English");
            Util.changeLanguage(context, "en");
            Log.e("English","English");
        }
      restartApplication();
    }



    /**
     * Tamil text textView typeface
     * input  textView name and id for string.xml
     */
    public void setTamilText(TextView textName, int id) {
        Typeface tfBamini = Typeface.createFromAsset(context.getAssets(), "fonts/Bamini.ttf");
        textName.setTypeface(tfBamini);
        textName.setText(TamilUtil.convertToTamil(TamilUtil.BAMINI,   context.getString(id)));
    }
}