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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.omneAgate.wholeSaler.DTO.EnumDTO.ServiceListenerType;
import com.omneAgate.wholeSaler.DTO.WholesalerPostingDto;
import com.omneAgate.wholeSaler.Util.NetworkConnection;
import com.omneAgate.wholeSaler.Util.WholesaleDBHelper;
import com.omneAgate.wholeSaler.activity.dialog.UnsyncHistroyAdapter;
import com.omneAgate.wholeSaler.service.HttpClientWrapper;
import com.omneAgate.wholeSaler.service.OfflineTransactionManager;
import com.omneAgate.wholeSaler.service.OutwardSyncManually;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class TransactionUnsyncActivity extends BaseActivity {


    UnsyncHistroyAdapter adapter;
    ListView Unsync_StockOutwardHistroy;
    private Toolbar mToolbar;
    TextView title;
    private ActionBar mActionBar;

    //List of  for fps stock inward
    private List<WholesalerPostingDto> keroseneStockOutwardList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        status = true;
        setContentView(R.layout.activity_transaction_unsync);
        initialisePage();
    }

    //Initialise Page
    private void initialisePage() {
        setUpPopUpPage();
        SetTopLayout();
        Unsync_StockOutwardHistroy = (ListView) findViewById(R.id.listView_kerosene_unsync_stock_outward);
        httpConnection = new HttpClientWrapper();
        networkConnection = new NetworkConnection(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        configureData(false);
        // removeAllServices();

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
            setUpPopUpPage();
            keroseneStockOutwardList = WholesaleDBHelper.getInstance(this).showKeroseneStockOutward("R");
            int unSynccount = WholesaleDBHelper.getInstance(this).getTransactionCount("R");
            Log.e("Kerosene Outward List", keroseneStockOutwardList.toString());
            Log.e("keroseneStockOutwaredList", "keroseneStockOut" + keroseneStockOutwardList.size());

            ((ImageView) findViewById(R.id.ivSync)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    startService(new Intent(TransactionUnsyncActivity.this, OfflineTransactionManager.class));
//                    Log.e(TransactionUnsyncActivity.class.getSimpleName(), OfflineTransactionManager.class.getSimpleName() + " Service started ");
                    if (networkConnection.isNetworkAvailable()) {
                        new OutwardSyncManually(com.omneAgate.wholeSaler.activity.TransactionUnsyncActivity.this).billSync();
                    } else {
                        if (GlobalAppState.language.equalsIgnoreCase("ta")) {
                            Toast.makeText(TransactionUnsyncActivity.this, "இணைய இணைப்பு இல்லை", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(TransactionUnsyncActivity.this, "No Network Connection", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            });


            if (keroseneStockOutwardList.size() == 0) {
                ((TextView) findViewById(R.id.tvTotalTransaction)).setText(String.format("%02d", unSynccount));

                RelativeLayout t = (RelativeLayout) findViewById(R.id.linearLayoutNoRecords);
                t.setVisibility(View.VISIBLE);
                return;
            }

            ((TextView) findViewById(R.id.tvTotalTransaction)).setText("" + keroseneStockOutwardList.size());

            adapter = new UnsyncHistroyAdapter(this, keroseneStockOutwardList);
            Unsync_StockOutwardHistroy.setAdapter(adapter);
            Unsync_StockOutwardHistroy.setOnItemClickListener(null);

        } catch (Exception e) {
            Log.e("Kerosene Stock Outward ", "Error:" + e.toString());
        }
    }


    @Override
    protected void processMessage(Bundle message, ServiceListenerType what) {

    }

    public void onClose(View view) {
        onBackPressed();
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

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, SearchHistoryActivity.class));
        finish();
    }

    /**
     * Used to stop Transaction Manager Service running currently
     */
    private void removeAllServices() {
        stopService(new Intent(this, OfflineTransactionManager.class));

    }
}
