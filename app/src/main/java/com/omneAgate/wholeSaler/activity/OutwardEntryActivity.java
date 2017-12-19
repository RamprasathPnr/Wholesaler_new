package com.omneAgate.wholeSaler.activity;

import android.app.Service;
import android.content.Intent;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.zxing.Dimension;
import com.omneAgate.wholeSaler.DTO.EnumDTO.ServiceListenerType;
import com.omneAgate.wholeSaler.DTO.ProductDto;
import com.omneAgate.wholeSaler.DTO.WholesalerPostingDto;
import com.omneAgate.wholeSaler.Util.NoDefaultSpinner;
import com.omneAgate.wholeSaler.Util.RecipientCodeAdapter;
import com.omneAgate.wholeSaler.Util.SessionId;
import com.omneAgate.wholeSaler.Util.Util;
import com.omneAgate.wholeSaler.Util.WholesaleDBHelper;

import org.apache.commons.lang3.StringUtils;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class OutwardEntryActivity extends BaseActivity implements View.OnClickListener {

    private Toolbar mToolbar;
    TextView title;
    private ActionBar mActionBar;
    //Recipient Type
    String recipientType = "";
    //Product Type
    String productType = "";
    //Keyboard Relative Layout
    //   RelativeLayout keyBoardCustom;
    //Keyboard View
    //   KeyboardView keyView;
    //Adapter - Recipient Type
    ArrayAdapter<String> outwardAdapter;
    //Product Type Adapter
    ArrayAdapter<String> typeAdapter;
    //Recipient Code Adapter
    ArrayAdapter<String> recipientcodeAdapter;
    //   int keyBoardFocused;
    //Activity Name
    String activityName = "";

    Boolean keyboardFlag = false;
    //Wholesaler Posting Details
    WholesalerPostingDto wholesalerPostingDto;
    //Recipient Type Spinner
    ImageView spinnerRecipientCode;
    //Outward  Spinner is selected or not
    private boolean outwardSpinnerIsSeleced;
    //Product Type Spinner is selected or not
    private boolean productTypeSpinnerIsSeleced;
    //Recipient Code Spinner is selected or not
    private boolean codeSpinnerIsSeleced;
    //Spinner Outward to;
    private NoDefaultSpinner spinnerOutward;
    //Autocomplete Textview Recipient Code
    private AutoCompleteTextView autoCompleteTvRecipientCode;
    //Spinner Recipient Type
    private NoDefaultSpinner spinnerProductType;
    //Edittext - Quantity to be sent
    private EditText edtQuantity;
    //Edittext - Vehicle Number
    private EditText edtVehicleNumber;
    //Edittext - Vehicle Driver Name
    private EditText edtDriverName;
    //Edittext - Driver Contact Number
    private EditText edtContactNumber;
    //Edittext - Driver Contact Number
    private EditText edtTransporterName;
    ProductDto productDto;
    String lang;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        status = true;
        setContentView(R.layout.activity_outward);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        activityName = getIntent().getExtras().getString("activityName");
//        keyView = (KeyboardView) findViewById(R.id.customkeyboard);
//        keyBoardCustom = (RelativeLayout) findViewById(R.id.key_board_custom);

        if ((activityName.equals(DashboardActivity.class.getSimpleName()))) {
            initialisePage(wholesalerPostingDto, true);
        }

        if (activityName.equals(OutwardConformationActivity.class.getSimpleName())) {
            String intentResponse = getIntent().getExtras().getString("IntentPostingRequest");
            Log.e("Intent Response", "OutwardConfirmationActivity" + intentResponse);
            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson = gsonBuilder.create();
            wholesalerPostingDto = gson.fromJson(intentResponse, WholesalerPostingDto.class);
            Log.e("WholesalerPostingDto", "OutwardConfirmationActivity" + wholesalerPostingDto);
            initialisePage(wholesalerPostingDto, false);
            outwardSpinnerIsSeleced = true;
            productTypeSpinnerIsSeleced = true;
            recipientType = wholesalerPostingDto.getRecipientType();
            lang = WholesaleDBHelper.getInstance(this).getMasterData("language");
            if (lang.equals("ta")) {
                Log.e("product_id", "" + wholesalerPostingDto.getProductId());
                ProductDto proctname = WholesaleDBHelper.getInstance(this).getProductDetails(wholesalerPostingDto.getPruductName());
                productType = proctname.getLocalProductName();
                spinnerProductType.setSelection(typeAdapter.getPosition(productType));
            } else {
                productType = wholesalerPostingDto.getPruductName();
            }


        }
        ((Button) findViewById(R.id.btnSubmit)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSubmit(v);
            }
        });

    }
    private void SetTopLayout() {
        try {
            mToolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(mToolbar);
            title = (TextView) findViewById(R.id.title_toolbar);
            title.setText(R.string.outward);
            title.setTextSize(TypedValue.COMPLEX_UNIT_PX,getResources().getDimension(R.dimen.outward_size));
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

    //Initialise Page
    private void initialisePage(WholesalerPostingDto wholesalerPostingDto, boolean isSelectedListener) {
        setUpPopUpPage();
        SetTopLayout();

        // ((TextView) findViewById(R.id.top_textView)).setText(getString(R.string.outward));
        ((TextView) findViewById(R.id.tvOutwardLabel)).setText(getString(R.string.outwardTo));
        ((TextView) findViewById(R.id.tvCodeLabel)).setText(getString(R.string.selectCode));
        ((TextView) findViewById(R.id.tvProductTypeLabel)).setText(getString(R.string.productType));
        ((TextView) findViewById(R.id.tvQuantityLabel)).setText(getString(R.string.selectQuanity));
        ((TextView) findViewById(R.id.tvNameLabel)).setText(getString(R.string.driverName));
        ((TextView) findViewById(R.id.tvContactNumberLabel)).setText(getString(R.string.driverContactNumber));
        ((TextView) findViewById(R.id.tvTransporterNameLabel)).setText(getString(R.string.transporterName));
        ((TextView) findViewById(R.id.tvVehicleLabel)).setText(getString(R.string.vehicleNo));
        ((Button) findViewById(R.id.btnCancel)).setText(getString(R.string.cancel));
        ((Button) findViewById(R.id.btnSubmit)).setText(getString(R.string.submit));
        spinnerOutward = (NoDefaultSpinner) findViewById(R.id.outwardSpinner);
        spinnerProductType = (NoDefaultSpinner) findViewById(R.id.productTypeSpinner);
        autoCompleteTvRecipientCode = (AutoCompleteTextView) findViewById(R.id.tvAutoCompleteCode);
        spinnerRecipientCode = (ImageView) findViewById(R.id.codeSpinner);
        edtQuantity = (EditText) findViewById(R.id.edtSelectQuantity);
        edtVehicleNumber = (EditText) findViewById(R.id.edtVehicleNo);
        edtDriverName = (EditText) findViewById(R.id.edtNameDriver);
        edtContactNumber = (EditText) findViewById(R.id.edtContactNumber);
        edtTransporterName = (EditText) findViewById(R.id.edtTransporterName);
        final NumberFormat format = new DecimalFormat("#0.000");
        format.setMaximumFractionDigits(3);
        //  edtQuantity.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(9, 3)});
        edtQuantity.requestFocus();
        edtQuantity.setOnClickListener(this);
        //edtQuantity.setShowSoftInputOnFocus(false);
        edtContactNumber.setOnClickListener(this);
        //   edtContactNumber.setShowSoftInputOnFocus(false);
        // edtQuantity.setOnFocusChangeListener(this);
        //  edtContactNumber.setOnFocusChangeListener(this);

      /*  autoCompleteTvRecipientCode.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                keyBoardCustom.setVisibility(View.GONE);
            }
        });

        edtVehicleNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                keyBoardCustom.setVisibility(View.GONE);
            }
        });

        edtDriverName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                keyBoardCustom.setVisibility(View.GONE);
            }
        });
        edtTransporterName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                keyBoardCustom.setVisibility(View.GONE);
            }
        });
*/
        getStateOfTheActivity(wholesalerPostingDto, isSelectedListener);
    }


    //Get all values Entered while moving from OutwardConfirmation Activity to this activity
    private void getStateOfTheActivity(WholesalerPostingDto wholesalerPostingDto, boolean isSelectedListener) {

        //  String outwardArray[] = {getResources().getString(R.string.selectRecipientType), "Kerosene Bunk", "FPS", "RRC"};//Select Recipient Type
        String outwardArray[];
        if (GlobalAppState.language.equalsIgnoreCase("ta")) {
            outwardArray = new String[]{"மண்ணெண்ணெய் கடை", " நியாய விலைக் கடை", " ஆர்ஆர்சி"};
        } else {

            outwardArray = new String[]{"Kerosene Bunk", "FPS", "RRC"};//Select Recipient Type
        }

        outwardAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, outwardArray);
        outwardAdapter.setDropDownViewResource(R.layout.simple_selectable_list_item);
        spinnerOutward.setAdapter(outwardAdapter);
        spinnerOutward.setPrompt(getResources().getString(R.string.selectRecipientType));
        List<ProductDto> listProducts = WholesaleDBHelper.getInstance(this).getAllProducts();
        List<String> listProductType = new ArrayList<>();
        lang = WholesaleDBHelper.getInstance(this).getMasterData("language");

        //  Toast.makeText(getApplicationContext(),""+productDto.getLocalProductName(),Toast.LENGTH_SHORT).show();

        for (ProductDto productDto : listProducts) {
            if (lang.equals("ta")) {
                listProductType.add(productDto.getLocalProductName());
            } else {
                listProductType.add(productDto.getName());
            }
        }
        typeAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, listProductType);
        typeAdapter.setDropDownViewResource(R.layout.simple_selectable_list_item);
        spinnerProductType.setAdapter(typeAdapter);
        spinnerProductType.setPrompt(getResources().getString(R.string.selectProductType));
        // clickListener();
        if (isSelectedListener == true) {
            LoadOutwardSpinner(spinnerOutward, -1, true);
            LoadProductTypeSpinner(spinnerProductType, -1, true);
            clear();
        } else {
            String receipientType = wholesalerPostingDto.getRecipientType();
            if (GlobalAppState.language.equalsIgnoreCase("ta")) {
                if (receipientType.equalsIgnoreCase("Kerosene Bunk")) {
                    receipientType = "மண்ணெண்ணெய் கடை";
                } else if (receipientType.equalsIgnoreCase("FPS")) {
                    receipientType = " நியாய விலைக் கடை";
                } else {
                    receipientType = " ஆர்ஆர்சி";
                }
            }
            LoadOutwardSpinner(spinnerOutward, outwardAdapter.getPosition(receipientType), false);
            LoadProductTypeSpinner(spinnerProductType, typeAdapter.getPosition(wholesalerPostingDto.getPruductName()), false);
            autoCompleteTvRecipientCode.setText(wholesalerPostingDto.getCode());
            NumberFormat format = new DecimalFormat("#0.000");
            edtQuantity.setText("" + format.format(wholesalerPostingDto.getQuantity()));
            edtVehicleNumber.setText(wholesalerPostingDto.getVehicleNumber());
            edtDriverName.setText(wholesalerPostingDto.getDriverName());
            edtContactNumber.setText(wholesalerPostingDto.getDrivermobileNumber());
            edtTransporterName.setText(wholesalerPostingDto.getTransportName());
        }
    }

    //Clear all Edittext and Spinner Values
    private void clear() {
        autoCompleteTvRecipientCode.setText("");
        //spinnerProductType.setSelection(0, false);
        edtQuantity.setText("");
        edtVehicleNumber.setText("");
        edtDriverName.setText("");
        edtContactNumber.setText("");
        edtTransporterName.setText("");
    }

    //On Submit


    public void onSubmit(View view) {
        //  try {
        if (outwardSpinnerIsSeleced == false) {
            Toast.makeText(this,getString(R.string.selectRecipientType),Toast.LENGTH_SHORT).show();
            return;
        }

        String recipientCode = autoCompleteTvRecipientCode.getText().toString().trim();
        Log.e("RecipientCOde", recipientCode);


        if (StringUtils.isEmpty(recipientCode) || recipientCode == null) {
            Toast.makeText(this,getString(R.string.selectRecipientCode),Toast.LENGTH_SHORT).show();
            return;
        }

        ArrayList<String> listCode = WholesaleDBHelper.getInstance(this).getRecipientCode(recipientType);

        if (!listCode.contains(recipientCode)) {
            Toast.makeText(this,getString(R.string.noShopsAvailable),Toast.LENGTH_SHORT).show();
            return;
        }

        if (productType.equals("")) {
            Toast.makeText(this,getString(R.string.selectProductType),Toast.LENGTH_SHORT).show();
            return;
        }
        String quantity = edtQuantity.getText().toString();

        if (StringUtils.isEmpty(quantity) || quantity == null) {
            Toast.makeText(this,getString(R.string.enterQuantity),Toast.LENGTH_SHORT).show();
            return;
        }
        if (StringUtils.isEmpty(quantity) || quantity == null) {
            Toast.makeText(this,getString(R.string.enterQuantity),Toast.LENGTH_SHORT).show();
            return;
        }
        if (quantity.equalsIgnoreCase(".")) {
            Toast.makeText(this,getString(R.string.enterQuantity),Toast.LENGTH_SHORT).show();
            return;
        }

        if (quantity != null) {
            if (Float.parseFloat(quantity) == 0) {
                Toast.makeText(this,getString(R.string.invalidQuantity),Toast.LENGTH_SHORT).show();
                return;
            }
        }

        String contactNumber = edtContactNumber.getText().toString();
        if (StringUtils.isEmpty(contactNumber) || contactNumber == null || StringUtils.length(contactNumber) < 10 || contactNumber.startsWith("0")) {
            edtContactNumber.requestFocus();

            if (StringUtils.isEmpty(contactNumber)) {
                Toast.makeText(this,getString(R.string.enter10digitMobileNumber),Toast.LENGTH_SHORT).show();
                return;
            }


            if (StringUtils.length(contactNumber) < 10) {
                Toast.makeText(this,getString(R.string.invalidMobileNumber),Toast.LENGTH_SHORT).show();
                return;
            }

            if (contactNumber.startsWith("0")) {
                Toast.makeText(this,getString(R.string.invalidMobileNumber),Toast.LENGTH_SHORT).show();
                return;
            }
        }

        String vehicleNumber = edtVehicleNumber.getText().toString().trim();
        if (StringUtils.isEmpty(vehicleNumber) || vehicleNumber == null) {
            Toast.makeText(this,getString(R.string.enterVehicleNumber),Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(this, OutwardConformationActivity.class);
        WholesalerPostingDto wholesalerPostingDto = new WholesalerPostingDto();
        wholesalerPostingDto.setId(SessionId.getInstance().getWholesaleId());
        wholesalerPostingDto.setWholesalerCode(SessionId.getInstance().getWholesaleCode());

        if (lang.equals("ta")) {
            ProductDto productDto = WholesaleDBHelper.getInstance(this).getProductDetails_tamil(productType);
            wholesalerPostingDto.setPruductName(productDto.getName());
            wholesalerPostingDto.setProductUnit(productDto.getProductUnit());
            wholesalerPostingDto.setProductId(productDto.getId());
        } else {
            ProductDto productDto = WholesaleDBHelper.getInstance(this).getProductDetails(productType);
            wholesalerPostingDto.setPruductName(productDto.getName());
            wholesalerPostingDto.setProductUnit(productDto.getProductUnit());
            wholesalerPostingDto.setProductId(productDto.getId());
        }

        wholesalerPostingDto.setQuantity(Double.parseDouble(edtQuantity.getText().toString()));
        SimpleDateFormat regDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault());
        wholesalerPostingDto.setOutwardDate(regDate.format(new Date()));
        if (recipientType.equalsIgnoreCase("Kerosene Bunk")) {
            wholesalerPostingDto.setKbCode(recipientCode);
        } else if (recipientType.equalsIgnoreCase("FPS")) {
            wholesalerPostingDto.setFpsCode(recipientCode);
        } else {
            wholesalerPostingDto.setRrcCode(recipientCode);
        }
        wholesalerPostingDto.setCode(recipientCode);
        wholesalerPostingDto.setTransportName(edtTransporterName.getText().toString().trim());
        wholesalerPostingDto.setVehicleNumber(edtVehicleNumber.getText().toString().trim());
        wholesalerPostingDto.setDriverName(edtDriverName.getText().toString().trim());
        wholesalerPostingDto.setDrivermobileNumber(edtContactNumber.getText().toString());
        wholesalerPostingDto.setRecipientType(recipientType);
        wholesalerPostingDto.setCode(recipientCode);
        DateFormat dateFormat = new SimpleDateFormat("MM");
        DateFormat dateFormatyy = new SimpleDateFormat("yyyy");
        Date date = new Date();
        Log.d("Month", dateFormat.format(date));
        Log.d("Year", dateFormatyy.format(date));
        wholesalerPostingDto.setMonth("" + dateFormat.format(date));
        wholesalerPostingDto.setYear("" + dateFormatyy.format(date));
        wholesalerPostingDto.setInwardType("R");
        String login = new Gson().toJson(wholesalerPostingDto);
        intent.putExtra("IntentPostingRequest", login);
        startActivity(intent);
        finish();

       /* } catch (Exception e) {
            Log.e(OutwardEntryActivity.class.getSimpleName(), e.toString());
        }*/


    }

    //Cancel
    public void onCancel(View view) {
        startActivity(new Intent(this, DashboardActivity.class));
        finish();
    }

    private void LoadOutwardSpinner(Spinner spinnerOutward, int position, boolean selectedListener) {

        spinnerOutward.setAdapter(outwardAdapter);
        spinnerOutward.setPrompt(getResources().getString(R.string.selectRecipientType));
        if (position != -1) {
            spinnerOutward.setSelection(position, selectedListener);
        }

        spinnerOutward.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                     @Override
                                                     public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                                         // keyBoardCustom.setVisibility(View.GONE);

                                                         if (position >= 0) {
                                                             //  keyBoardCustom.setVisibility(View.GONE);
                                                             outwardSpinnerIsSeleced = true;
                                                             recipientType = String.valueOf(parent.getItemAtPosition(position));

                                                             ((TextView) findViewById(R.id.tvCodeLabel)).setText(recipientType + " " + getResources().getString(R.string.code) + "*");
                                                             if (GlobalAppState.language.equalsIgnoreCase("ta")) {
                                                                 if (recipientType.equalsIgnoreCase("மண்ணெண்ணெய் கடை")) {
                                                                     recipientType = "Kerosene Bunk";
                                                                 } else if (recipientType.equalsIgnoreCase(" நியாய விலைக் கடை")) {
                                                                     recipientType = "FPS";
                                                                 } else {
                                                                     recipientType = "RRC";
                                                                 }
                                                             }
                                                             LoadRecipientCodeAutoCompleteTextView(autoCompleteTvRecipientCode, recipientType, outwardSpinnerIsSeleced);
                                                         }
                                                         clear();
                                                     }

                                                     @Override
                                                     public void onNothingSelected(AdapterView<?> parent) {

                                                     }
                                                 }

        );


    }

    private void LoadProductTypeSpinner(Spinner spinnerProductType, int position, boolean isSelectedListener) {

        typeAdapter.setDropDownViewResource(R.layout.simple_selectable_list_item);
        //  keyBoardCustom.setVisibility(View.GONE);
        if (position != -1) {
            spinnerProductType.setAdapter(typeAdapter);
        }
        spinnerProductType.setSelection(position, isSelectedListener);
        spinnerProductType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()

        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position,
                                       long id) {
                //   keyBoardCustom.setVisibility(View.GONE);
                if (position >= 0) {
                    productType = String.valueOf(parent.getItemAtPosition(position));
                    productTypeSpinnerIsSeleced = true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    //Load Recipient code Auto Complete Textview
    private void LoadRecipientCodeAutoCompleteTextView(final AutoCompleteTextView autoCompleteTextView, String recipientType, boolean isSelected) {

        ArrayList<String> listCode = WholesaleDBHelper.getInstance(this).getRecipientCode(recipientType);
        Log.e("CodeListSize", "" + listCode.size());
        RecipientCodeAdapter recipientCodeAdapter = null;
        if (isSelected == true) {
            recipientCodeAdapter = new RecipientCodeAdapter(this, R.layout.recipient_auto, listCode);
        }
        autoCompleteTextView.setAdapter(recipientCodeAdapter);
        autoCompleteTextView.setThreshold(3);
        spinnerRecipientCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  keyBoardCustom.setVisibility(View.GONE);
                autoCompleteTextView.showDropDown();
                InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Service.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(autoCompleteTvRecipientCode.getWindowToken(), 0);
            }
        });
    }
    //set Auto generated Reference Number


    public void onClick(View view) {
    /*    keyBoardFocused = view.getId();
        checkVisibility();
        keyBoardAppear();*/
        Log.e("keyboard_click", "click");
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

   /* private void checkVisibility() {
        if (keyBoardCustom.getVisibility() == View.GONE) {
            keyBoardCustom.setVisibility(View.VISIBLE);
            InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Service.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(autoCompleteTvRecipientCode.getWindowToken(), 0);
        }
    }
*/

    /* private void keyBoardAppear() {
         Keyboard keyboard = new Keyboard(this, R.layout.keyboardopenstock);
         keyView.setKeyboard(keyboard);
         keyView.setPreviewEnabled(false);
         keyView.bringToFront();
         keyView.setVisibility(KeyboardView.VISIBLE);
         keyView.setOnKeyboardActionListener(new KeyList());
     }
 */
    @Override
    protected void processMessage(Bundle message, ServiceListenerType what) {

    }

    public void onBackArrow(View view) {
        onBackPressed();
    }


    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, DashboardActivity.class));
        finish();
    }

  /*  @Override
    public void onFocusChange(View view, boolean focusChanged) {
        if (focusChanged  && keyboardFlag) {
            keyBoardFocused = view.getId();
            checkVisibility();
            keyBoardAppear();
            Log.e("keyboard_Focus", "focus");

        }
        else
        {
            keyboardFlag =true;

        }
    }
*/
   /* class KeyList implements KeyboardView.OnKeyboardActionListener {
        public void onKey(View v, int keyCode, KeyEvent event) {

        }

        public void onText(CharSequence text) {

        }

        public void swipeLeft() {

        }

        public void onKey(int primaryCode, int[] keyCodes) {

        }

        public void swipeUp() {

        }

        public void swipeDown() {

        }

        public void swipeRight() {

        }

        public void onPress(int primaryCode) {
            try {
                // Back Space key
                if (primaryCode == 8) {
                    String number = ((EditText) findViewById(keyBoardFocused)).getText().toString();
                    Log.e("number_count for dot", "" + number.length());
                    if (number.length() > 0) {

                        number = number.substring(0, number.length() - 1);
                        ((EditText) findViewById(keyBoardFocused)).setText(number);
                        ((EditText) findViewById(keyBoardFocused)).setSelection(number.length());
                    }

                } else if (primaryCode == 46) { //Done key
                    keyBoardCustom.setVisibility(View.GONE);
                } else if (primaryCode == 21) { //Dot Key
                    String value = ((EditText) findViewById(keyBoardFocused)).getText().toString().trim();
                    if (!StringUtils.contains(value, "."))
                        ((EditText) findViewById(keyBoardFocused)).append(".");
                } else { //Other Keys
                    char ch = (char) primaryCode;
                    String number = ((EditText) findViewById(keyBoardFocused)).getText().toString();
                    String testArray[] = StringUtils.split(number, ".");
                    if (testArray.length == 0 || testArray.length == 1) {
                        ((EditText) findViewById(keyBoardFocused)).append("" + ch);
                    } else if (testArray.length > 1 && testArray[1].length() <= 2)
                        ((EditText) findViewById(keyBoardFocused)).append("" + ch);

                }
            } catch (Exception e) {
                Log.e("onPress", e.toString(), e);
            }
        }

        public void onRelease(int primaryCode) {

        }
    }
*/

}
