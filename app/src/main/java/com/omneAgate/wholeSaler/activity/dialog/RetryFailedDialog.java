package com.omneAgate.wholeSaler.activity.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.omneAgate.wholeSaler.activity.R;
import com.omneAgate.wholeSaler.activity.SyncPageActivity;

/**
 * This dialog will appear on the time of user logout
 */
public class RetryFailedDialog extends Dialog implements
        View.OnClickListener {


    private final Activity context;  //    Context from the user

    /*Constructor class for this dialog*/
    public RetryFailedDialog(Activity _context) {
        super(_context);
        context = _context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setContentView(R.layout.dialog_retry_failed);
        setCancelable(false);
        TextView message = (TextView) findViewById(R.id.textViewNwText);
        String userText = "You have exceeded your maximum attempts";
        ((TextView) findViewById(R.id.textViewNwTitle)).setText("Loading Failed");
        message.setText(userText);
        userText = "Please contact HelpDesk";
        ((TextView) findViewById(R.id.textViewNwTextSecond)).setText(userText);
        Button okButton = (Button) findViewById(R.id.buttonNwOk);
        okButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonNwOk:
                ((SyncPageActivity) context).logOut();
                dismiss();
                break;
        }
    }

}