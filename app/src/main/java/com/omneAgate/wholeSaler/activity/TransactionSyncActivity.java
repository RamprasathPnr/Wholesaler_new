package com.omneAgate.wholeSaler.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.omneAgate.wholeSaler.DTO.EnumDTO.ServiceListenerType;
import com.omneAgate.wholeSaler.DTO.WholesalerPostingDto;
import com.omneAgate.wholeSaler.Util.PullToRefresh.LoadMoreListView;
import com.omneAgate.wholeSaler.Util.WholesaleDBHelper;
import com.omneAgate.wholeSaler.activity.dialog.OutwardSyncHistroyAdapter;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class TransactionSyncActivity extends BaseActivity {

    private Toolbar mToolbar;
    TextView title;
    private ActionBar mActionBar;

    LoadMoreListView OutwardSearch;

    private List<WholesalerPostingDto> keroseneStockOutwardList = null;

    OutwardSyncHistroyAdapter adapter;
    int loadMore = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        status = true;
        setContentView(R.layout.activity_transaction_synchistroy);
        initialisePage();
    }

    private void initialisePage() {
        setUpPopUpPage();
        SetTopLayout();

    }

    @Override
    protected void onStart() {
        super.onStart();
        configureData();
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

    private void configureData() {
        try {
            OutwardSearch = (LoadMoreListView) findViewById(R.id.listView_kerosene_outward);
            keroseneStockOutwardList = new ArrayList<>();
            adapter = new OutwardSyncHistroyAdapter(this, keroseneStockOutwardList);
            OutwardSearch.setAdapter(adapter);
            final int syncCount = WholesaleDBHelper.getInstance(this).getTransactionCount("T");

            ((TextView) findViewById(R.id.tvTotalTransaction)).setText("" + syncCount);

            OutwardSearch.setOnLoadMoreListener(new LoadMoreListView.OnLoadMoreListener() {
                @Override
                public void onLoadMore() {
                    loadMore++;

                    new SearchBillsUnsync().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                }
            });
            new SearchBillsUnsync().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void processMessage(Bundle message, ServiceListenerType what) {

    }

    private class SearchBillsUnsync extends AsyncTask<Long, Void, List<WholesalerPostingDto>> {

        // can use UI thread here
        protected void onPreExecute() {

            //progressBarSpin.setVisibility(View.VISIBLE);
        }

        // automatically done on worker thread (separate from UI thread)
        protected List<WholesalerPostingDto> doInBackground(final Long... args) {
            return WholesaleDBHelper.getInstance(TransactionSyncActivity.this).getAllKeroseneStockOutward("T", loadMore);
        }

        // can use UI thread here
        protected void onPostExecute(final List<WholesalerPostingDto> billDtos) {

            // progressBarSpin.setVisibility(View.GONE);
            if (billDtos.size() > 0) {
                keroseneStockOutwardList.addAll(billDtos);
                OutwardSearch.setVisibility(View.VISIBLE);
                (findViewById(R.id.linearLayoutNoRecords)).setVisibility(View.GONE);
                adapter.notifyDataSetChanged();
                OutwardSearch.invalidate();
                OutwardSearch.onLoadMoreComplete();
            } else if (billDtos.size() == 0 && keroseneStockOutwardList.size() == 0) {
                OutwardSearch.setVisibility(View.GONE);
                findViewById(R.id.linearLayoutNoRecords).setVisibility(View.VISIBLE);
            } else {
                OutwardSearch.onLoadMoreComplete();
            }
        }
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
