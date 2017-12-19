  package com.omneAgate.wholeSaler.activity.dialog;

  import android.app.Activity;
  import android.app.Dialog;
  import android.content.Intent;
  import android.graphics.Typeface;
  import android.graphics.drawable.ColorDrawable;
  import android.os.Bundle;
  import android.view.View;
  import android.view.Window;
  import android.widget.Button;
  import android.widget.TextView;
  import com.omneAgate.wholeSaler.Util.TamilUtil;
  import com.omneAgate.wholeSaler.Util.Util;
  import com.omneAgate.wholeSaler.activity.DashboardActivity;
  import com.omneAgate.wholeSaler.activity.GlobalAppState;
  import com.omneAgate.wholeSaler.activity.R;

  /**
 * This dialog will appear on the time of outward updation in Server
 */
public class OutwardUpdationDialog extends Dialog implements
        View.OnClickListener {

    // Activity Context
    private final Activity context;

    //Reference Number
    private final String refNumber;

    /*Constructor class for this dialog*/
    public OutwardUpdationDialog(Activity _context,String refNo) {
        super(_context);
        context = _context;
        refNumber = refNo;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setContentView(R.layout.dialog_outward_dialog);
        setCancelable(false);
        ((TextView) findViewById(R.id.textViewRefNo)).setText(refNumber);
        Button okButton = (Button) findViewById(R.id.buttonNwOk);
        okButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonNwOk:
                dismiss();
                context.startActivity(new Intent(context, DashboardActivity.class));
                context.finish();
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