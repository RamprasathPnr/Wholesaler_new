package com.omneAgate.wholeSaler.activity;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.omneAgate.wholeSaler.DTO.EnumDTO.ServiceListenerType;
import com.omneAgate.wholeSaler.DTO.WholesalerPostingDto;
import com.omneAgate.wholeSaler.Util.NoDefaultSpinner;
import com.omneAgate.wholeSaler.Util.RecipientCodeAdapter;
import com.omneAgate.wholeSaler.Util.Util;
import com.omneAgate.wholeSaler.Util.WholesaleDBHelper;
import com.omneAgate.wholeSaler.activity.dialog.DateSelectionDialog;

import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by user1 on 21/1/16.
 */
public class SearchHistoryActivity extends BaseActivity {


    private Toolbar mToolbar;
    TextView title;
    private ActionBar mActionBar;
    //Adapter - Recipient Type
    ArrayAdapter<String> outwardAdapter;
    //Spinner Outward to;
    private NoDefaultSpinner spinnerOutward;
    ImageView spinnerRecipientCode;
    AutoCompleteTextView autoCompleteTvRecipientCode;
    String recipientType = "";
    String fromdateStr, todateStr;
    EditText fromdate, todate;
    private List<WholesalerPostingDto> keroseneStockOutwardList = null;
    SimpleDateFormat dfDate = new SimpleDateFormat("yyyy-MM-dd");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        status = true;
        setContentView(R.layout.activity_searchhistory);
        setUpPopUpPage();
        SetTopLayout();
        spinnerRecipientCode = (ImageView) findViewById(R.id.codeSpinner);
        fromdate = (EditText) findViewById(R.id.fromdate);
        todate = (EditText) findViewById(R.id.todate);

        autoCompleteTvRecipientCode = (AutoCompleteTextView) findViewById(R.id.tvAutoCompleteCode);
        autoCompleteTvRecipientCode.setText("");
        final int unSynccount = WholesaleDBHelper.getInstance(this).getTransactionCount("R");
        final int syncCount = WholesaleDBHelper.getInstance(this).getTransactionCount("T");
        ((TextView) findViewById(R.id.tvUnsync)).setText("" + unSynccount);
        ((TextView) findViewById(R.id.tvSync)).setText("" + syncCount);
        initialpages();

        findViewById(R.id.fromdate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    new DateSelectionDialog(SearchHistoryActivity.this, "fromdate").show();
                } catch (Exception e) {

                }
            }
        });
        findViewById(R.id.todate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    new DateSelectionDialog(SearchHistoryActivity.this, "todate").show();
                } catch (Exception e) {

                }
            }
        });

      /*  findViewById(R.id.backArraow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });*/
        findViewById(R.id.btnSubmit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitSearch();

            }
        });
        findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();

            }
        });

        ((LinearLayout) findViewById(R.id.unsyncLayout)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (unSynccount > 0) {
                    Intent intent = new Intent(SearchHistoryActivity.this, TransactionUnsyncActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        });

        ((LinearLayout) findViewById(R.id.syncLayout)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (syncCount > 0) {
                    Intent intent = new Intent(SearchHistoryActivity.this, TransactionSyncActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        });


    }

    private void SetTopLayout() {
        try {
            mToolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(mToolbar);
            title = (TextView) findViewById(R.id.title_toolbar);
            title.setText(R.string.transactions);
            mActionBar = getSupportActionBar();
            if (getSupportActionBar() != null) {
                getSupportActionBar().setHomeAsUpIndicator(R.drawable.arrow_back);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }

            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBackPressed();
                }
            });
            setTitle("");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void processMessage(Bundle message, ServiceListenerType what) {

    }

    public void setTextDate(String textDate, String flag) {

        if (flag.equals("fromdate")) {
            ((EditText) findViewById(R.id.fromdate)).setText(textDate);
        } else {
            ((EditText) findViewById(R.id.todate)).setText(textDate);
        }


    }

    private void initialpages() {
        spinnerOutward = (NoDefaultSpinner) findViewById(R.id.outwardSpinner);
        String outwardArray[];
        if (GlobalAppState.language.equalsIgnoreCase("ta")) {
            outwardArray = new String[]{"மண்ணெண்ணெய் கடை", " நியாய விலைக் கடை", " ஆர்ஆர்சி"};
        } else {

            outwardArray = new String[]{"Kerosene Bunk", "FPS", "RRC"};//Select Recipient Type
        }
        outwardAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, outwardArray);
        outwardAdapter.setDropDownViewResource(R.layout.simple_selectable_list_item);
        spinnerOutward.setAdapter(outwardAdapter);
        spinnerOutward.setPrompt("");
        //spinnerOutward.setPrompt(getString(R.string.selectRecipientCode));
        spinnerOutward.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                recipientType = String.valueOf(parent.getItemAtPosition(position));

                ((TextView) findViewById(R.id.tvCodeLabel)).setText(recipientType + " " + getResources().getString(R.string.code) + " *");

                if (GlobalAppState.language.equalsIgnoreCase("ta")) {
                    if (recipientType.equalsIgnoreCase("மண்ணெண்ணெய் கடை")) {
                        recipientType = "Kerosene Bunk";
                    } else if (recipientType.equalsIgnoreCase(" நியாய விலைக் கடை")) {
                        recipientType = "FPS";
                    } else {
                        recipientType = "RRC";
                    }
                }
                LoadRecipientCodeAutoCompleteTextView(autoCompleteTvRecipientCode, recipientType);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }


        });
    }

    //Load Recipient code Auto Complete Textview
    private void LoadRecipientCodeAutoCompleteTextView(final AutoCompleteTextView autoCompleteTextView, String recipientType) {
        autoCompleteTvRecipientCode.setText("");
        Log.e("RecipientType", recipientType);
        ArrayList<String> listCode = WholesaleDBHelper.getInstance(this).getRecipientCode(recipientType);
        Log.e("CodeListSize", "" + listCode.size());
        RecipientCodeAdapter recipientCodeAdapter = null;
        recipientCodeAdapter = new RecipientCodeAdapter(this, R.layout.recipient_auto, listCode);
        autoCompleteTextView.setAdapter(recipientCodeAdapter);
        autoCompleteTextView.setThreshold(3);
        spinnerRecipientCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autoCompleteTextView.showDropDown();
                InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Service.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(autoCompleteTvRecipientCode.getWindowToken(), 0);
            }
        });

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, DashboardActivity.class));
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_right, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_drawer) {
            mDrawerLayout.openDrawer(Gravity.RIGHT);
        }
        return super.onOptionsItemSelected(item);
    }


    private void submitSearch() {
        String recipientCode = autoCompleteTvRecipientCode.getText().toString().trim();
        fromdateStr = fromdate.getText().toString().trim();
        todateStr = todate.getText().toString().trim();

        Log.e("SearchHistroyActivity", "fromdate:" + fromdateStr + "todateStr:" + todateStr);
        if (recipientType.equals("") && StringUtils.isEmpty(fromdateStr) &&
                StringUtils.isEmpty(todateStr) && StringUtils.isEmpty(recipientCode)) {
            Toast.makeText(this, getString(R.string.no_records), Toast.LENGTH_SHORT).show();
        } else if (!fromdateStr.equals("") && todateStr.equals("")) {
            Toast.makeText(this, getString(R.string.select_todate), Toast.LENGTH_SHORT).show();
            return;
        } else if (!todateStr.equals("") && fromdateStr.equals("")) {
            Toast.makeText(this, getString(R.string.select_fromdate), Toast.LENGTH_SHORT).show();
            return;
        } else if (!CheckDates(fromdateStr, todateStr)) {
            //Util.messageBar(this, getString(R.string.invaliddate));
            Toast.makeText(this, getString(R.string.date_validation), Toast.LENGTH_SHORT).show();
            return;
        } else {
            // keroseneStockOutwardList = WholesaleDBHelper.getInstance(this).showKeroseneStockOutward();
            keroseneStockOutwardList = WholesaleDBHelper.getInstance(this).showKeroseneStockOutwardSearch(recipientType, recipientCode, fromdateStr, todateStr);

            if (keroseneStockOutwardList.size() != 0) {
                Log.e("dataa", "" + keroseneStockOutwardList.toString());
                String login = new Gson().toJson(keroseneStockOutwardList);
                Intent intent = new Intent(SearchHistoryActivity.this, TransactionHistoryActivity.class);
                intent.putExtra("transationlist", login);
                startActivity(intent);
                finish();

            } else {
                Toast.makeText(this, getString(R.string.no_records), Toast.LENGTH_SHORT).show();
            }

        }
    }


    public static boolean CheckDates(String d1, String d2) {
        SimpleDateFormat dfDate = new SimpleDateFormat("yyyy-MM-dd");
        boolean b = false;
        try {
            if (d1.equals("") && d2.equals("")) {
                b = true;
            } else if (dfDate.parse(d1).before(dfDate.parse(d2))) {
                b = true;//If start date is before end date
            } else if (dfDate.parse(d1).equals(dfDate.parse(d2))) {
                b = true;//If two dates are equal
            } else {
                b = false;
            }
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return b;
    }

}


//from date todate validation

