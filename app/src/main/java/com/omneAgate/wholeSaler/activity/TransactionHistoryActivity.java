package com.omneAgate.wholeSaler.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.omneAgate.wholeSaler.DTO.EnumDTO.ServiceListenerType;
import com.omneAgate.wholeSaler.DTO.WholesalerPostingDto;
import com.omneAgate.wholeSaler.Util.WholesaleDBHelper;
import com.omneAgate.wholeSaler.activity.dialog.UnsyncHistroyAdapter;
import com.omneAgate.wholeSaler.activity.dialog.outWardHistroyAdapter;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

//This shows Transaction History
public class TransactionHistoryActivity extends BaseActivity {

    private Toolbar mToolbar;
    TextView title;
    private ActionBar mActionBar;

    //List of  for fps stock inward
    private List<WholesalerPostingDto> keroseneStockOutwardList = null;
    ListView lv_stock_outward_histroy;
    outWardHistroyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        status = true;
        setContentView(R.layout.activity_transaction_history);
        initialisePage();
    }


    //Initialise Page
    private void initialisePage() {
        setUpPopUpPage();
        SetTopLayout();
        lv_stock_outward_histroy = (ListView) findViewById(R.id.listView_kerosene_stock_outward_histroy);

    }

    @Override
    protected void onStart() {
        super.onStart();
        configureData(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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

    private void SetTopLayout() {
        try {
            mToolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(mToolbar);
            title = (TextView) findViewById(R.id.title_toolbar);
            title.setText(R.string.transactions);
            title.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.outward_size));
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

    /*Data from server has been set inside this function*/
    private void configureData(boolean ackStatus) {
        try {

            String intentResponse = getIntent().getExtras().getString("transationlist");
            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson = gsonBuilder.create();
            keroseneStockOutwardList = gson.fromJson(intentResponse, new TypeToken<List<WholesalerPostingDto>>() {
            }.getType());

            final int unSynccount = WholesaleDBHelper.getInstance(this).getTransactionCount("R");
            final int syncCount = WholesaleDBHelper.getInstance(this).getTransactionCount("T");
            Log.e("Kerosene Outward List", keroseneStockOutwardList.toString());

            if (keroseneStockOutwardList.size() == 0) {
                ((TextView) findViewById(R.id.tvTotalTransaction)).setText(String.format("%02d", keroseneStockOutwardList.size()));
                RelativeLayout t = (RelativeLayout) findViewById(R.id.linearLayoutNoRecords);
                t.setVisibility(View.VISIBLE);
                return;
            }

            ((TextView) findViewById(R.id.tvTotalTransaction)).setText("" + keroseneStockOutwardList.size());

            adapter = new outWardHistroyAdapter(this, keroseneStockOutwardList);
            lv_stock_outward_histroy.setAdapter(adapter);
            lv_stock_outward_histroy.setOnItemClickListener(null);

        } catch (Exception e) {
            Log.e(" Stock Outward activity", "Error:" + e.toString());
        }
    }

    @Override
    protected void processMessage(Bundle message, ServiceListenerType what) {

    }

    public void onClose(View view) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, SearchHistoryActivity.class));
        finish();
    }


}
