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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.omneAgate.wholeSaler.DTO.EnumDTO.ServiceListenerType;
import com.omneAgate.wholeSaler.DTO.Wholesaler_allData;
import com.omneAgate.wholeSaler.DTO.bunkerdto_name;
import com.omneAgate.wholeSaler.DTO.fpsstore_name;
import com.omneAgate.wholeSaler.DTO.rrcdto_name;
import com.omneAgate.wholeSaler.Util.NoDefaultSpinner;
import com.omneAgate.wholeSaler.Util.WholesaleDBHelper;
import com.omneAgate.wholeSaler.activity.dialog.AssociatedCardAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by user1 on 26/1/16.
 */
public class AssociatedCardActivity extends BaseActivity {

    private Toolbar mToolbar;
    TextView title, wholesaletotalcount;
    private ActionBar mActionBar;

    Wholesaler_allData wholetype_data1 = new Wholesaler_allData();

    private NoDefaultSpinner spinnerOutward;
    ArrayAdapter<String> outwardAdapter;
    List<Wholesaler_allData> allData = new ArrayList<>();
    String recipientType = "";
    ListView lvassociatedcard;

    AssociatedCardAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_associatedcard);
        setUpPopUpPage();
        SetTopLayout();
        // setTamilText((TextView) findViewById(R.id.top_textView), R.string.associatedShops);
        spinnerOutward = (NoDefaultSpinner) findViewById(R.id.outwardSpinner);
        lvassociatedcard = (ListView) findViewById(R.id.listView_kerosene_stock_outward);
        wholesaletotalcount = (TextView) findViewById(R.id.shopCount);
        String outwardArray[];
        if (GlobalAppState.language.equalsIgnoreCase("ta")) {
            outwardArray = new String[]{"மண்ணெண்ணெய் கடை", " நியாய விலைக் கடை", " ஆர்ஆர்சி"};
        } else {

            outwardArray = new String[]{"Kerosene Bunk", "FPS", "RRC"};//Select Recipient Type
        }
        outwardAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, outwardArray);
        outwardAdapter.setDropDownViewResource(R.layout.simple_selectable_list_item);
        spinnerOutward.setAdapter(outwardAdapter);
        spinnerOutward.setPrompt(getString(R.string.selectRecipientType));


        spinnerOutward.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                recipientType = String.valueOf(parent.getItemAtPosition(position));
                if (GlobalAppState.language.equalsIgnoreCase("ta")) {
                    if (recipientType.equalsIgnoreCase("மண்ணெண்ணெய் கடை")) {
                        recipientType = "Kerosene Bunk";
                    } else if (recipientType.equalsIgnoreCase(" நியாய விலைக் கடை")) {
                        recipientType = "FPS";
                    } else {
                        recipientType = "RRC";
                    }
                }
                filtershoptype(recipientType);


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }


        });
        initialpage();

    }

    private void SetTopLayout() {
        try {
            mToolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(mToolbar);
            title = (TextView) findViewById(R.id.title_toolbar);
            title.setText(R.string.associatedShops);
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

    private void initialpage() {
        try {
            List<rrcdto_name> rrc_data = new ArrayList<>();
            List<bunkerdto_name> kerosene_data = new ArrayList<>();
            List<fpsstore_name> fpsstore_data = new ArrayList<>();
            rrc_data = WholesaleDBHelper.getInstance(this).getallRRC();
            if (rrc_data == null)
                return;
            kerosene_data = WholesaleDBHelper.getInstance(this).getallBunker();
            fpsstore_data = WholesaleDBHelper.getInstance(this).getallFPSStore();

            Log.e("rrc_data", "" + rrc_data.size());
            Log.e("kerosene_data", "" + kerosene_data.size());
            Log.e("fpsstore_data", "" + fpsstore_data.size());

            for (int i = 0; i < rrc_data.size(); i++) {
                Wholesaler_allData wholetype_data1 = new Wholesaler_allData();
                wholetype_data1.setShop_type("RRC");
                wholetype_data1.setShop_code(rrc_data.get(i).getShop_code());
                wholetype_data1.setShop_name(rrc_data.get(i).getShop_name());
                wholetype_data1.setContact_persion(rrc_data.get(i).getContact_persion());
                wholetype_data1.setContact_number(rrc_data.get(i).getContact_number());
                allData.add(wholetype_data1);
            }

            for (int i = 0; i < kerosene_data.size(); i++) {
                Wholesaler_allData wholetype_data2 = new Wholesaler_allData();
                wholetype_data2.setShop_type("Kerosene Bunk");
                wholetype_data2.setShop_code(kerosene_data.get(i).getShop_code());
                wholetype_data2.setShop_name(kerosene_data.get(i).getShop_name());
                wholetype_data2.setContact_persion(kerosene_data.get(i).getContact_persion());
                wholetype_data2.setContact_number(kerosene_data.get(i).getContact_number());
                allData.add(wholetype_data2);
            }

            for (int i = 0; i < fpsstore_data.size(); i++) {
                Wholesaler_allData wholetype_data3 = new Wholesaler_allData();
                wholetype_data3.setShop_type("FPS");
                wholetype_data3.setShop_code(fpsstore_data.get(i).getShop_code());
                wholetype_data3.setShop_name(fpsstore_data.get(i).getShop_name());
                wholetype_data3.setContact_persion(fpsstore_data.get(i).getContact_persion());
                wholetype_data3.setContact_number(fpsstore_data.get(i).getContact_number());
                allData.add(wholetype_data3);
            }

        } catch (Exception e) {
            Log.e("error", e.toString(), e);
        }
        Log.e("totalrecords", "" + allData.toString() + "---->" + allData.size());
        Log.e("totalsize", "---->" + allData.size());
        wholesaletotalcount.setText("" + allData.size());

        adapter = new AssociatedCardAdapter(this, allData);
        lvassociatedcard.setAdapter(adapter);
        lvassociatedcard.setOnItemClickListener(null);

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
        startActivity(new Intent(this, DashboardActivity.class));
        finish();
    }

    @Override
    protected void processMessage(Bundle message, ServiceListenerType what) {

    }

    public void onClose(View view) {
        onBackPressed();
    }

    private void filtershoptype(String data) {
        if (!data.equals("Select Recipient Type")) {
            Log.e("Check list size", "" + allData.size());
            List<Wholesaler_allData> alldatalist = new ArrayList<Wholesaler_allData>();

            for (Wholesaler_allData shoptype : allData) {
                if (shoptype.getShop_type().toLowerCase(Locale.ENGLISH).contains(data.toLowerCase())) {
                    alldatalist.add(shoptype);

                }

            }

            configureData(alldatalist);
        } else {
            configureData(allData);
        }

    }

    private void configureData(List<Wholesaler_allData> list) {
        try {
            wholesaletotalcount.setText("" + list.size());
            adapter = new AssociatedCardAdapter(this, list);
            lvassociatedcard.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            lvassociatedcard.setOnItemClickListener(null);

        } catch (Exception e) {
            Log.e("Error", e.toString());
        }
    }


}
