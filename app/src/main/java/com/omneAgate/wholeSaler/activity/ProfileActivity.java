package com.omneAgate.wholeSaler.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.omneAgate.wholeSaler.DTO.EnumDTO.ServiceListenerType;
import com.omneAgate.wholeSaler.DTO.WholeSaleLoginResponseDto;
import com.omneAgate.wholeSaler.Util.SessionId;
import com.omneAgate.wholeSaler.Util.WholesaleDBHelper;


public class ProfileActivity extends BaseActivity {
    private Toolbar mToolbar;
    private ActionBar mActionBar;
    TextView title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_profile);
        SetTopLayout();
        setUpDashBoard();
        setUpPopUpPage();

    }

    @Override
    protected void processMessage(Bundle message, ServiceListenerType what) {

    }


    private void SetTopLayout() {
        try {
            mToolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(mToolbar);
            title = (TextView) findViewById(R.id.title_toolbar);
            title.setText(R.string.profile);
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

    private void setUpDashBoard() {
        try {
            WholeSaleLoginResponseDto loginResponseDto = WholesaleDBHelper.getInstance(this).getUserDetails(SessionId.getInstance().getUserId());
            loginResponseDto.getUserDetailDto().getUserId();
            ((TextView) findViewById(R.id.profile_fps_code)).setText("" + loginResponseDto.getUserDetailDto().getUserId());
            ((TextView) findViewById(R.id.profile_contactname)).setText("" + loginResponseDto.getUserDetailDto().getWholesalerDto().getContactPersonName());
            ((TextView) findViewById(R.id.profile_number)).setText("" + loginResponseDto.getUserDetailDto().getWholesalerDto().getContactNumber());
            ((TextView) findViewById(R.id.profile_address)).setText("" + loginResponseDto.getUserDetailDto().getWholesalerDto().getAddress());

            String ProfileName = loginResponseDto.getUserDetailDto().getWholesalerDto().getName().replaceAll("[-+^:,_]", " ");
            ((TextView) findViewById(R.id.profile1)).setText("" + ProfileName);
            if (loginResponseDto.getUserDetailDto().getWholesalerDto().getStatus().equals("true")) {
                ((TextView) findViewById(R.id.profile_status)).setText("ACTIVE");
            } else {
                ((TextView) findViewById(R.id.profile_status)).setText("ACTIVE");
            }


            findViewById(R.id.button_cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(ProfileActivity.this, DashboardActivity.class));
                    finish();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Called when user press back button
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
        startActivity(new Intent(this, DashboardActivity.class));
        finish();
    }

    public void backArrowPressed(View view) {
        onBackPressed();
    }


}
