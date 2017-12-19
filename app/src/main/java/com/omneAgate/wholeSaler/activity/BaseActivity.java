package com.omneAgate.wholeSaler.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.omneAgate.wholeSaler.DTO.EnumDTO.RequestType;
import com.omneAgate.wholeSaler.DTO.EnumDTO.ServiceListenerType;
import com.omneAgate.wholeSaler.DTO.LogoutDto;
import com.omneAgate.wholeSaler.Util.CustomProgressDialog;
import com.omneAgate.wholeSaler.Util.NetworkConnection;
import com.omneAgate.wholeSaler.Util.SessionId;
import com.omneAgate.wholeSaler.Util.TamilUtil;
import com.omneAgate.wholeSaler.Util.Util;
import com.omneAgate.wholeSaler.Util.WholesaleDBHelper;
import com.omneAgate.wholeSaler.activity.dialog.LogoutDialog;
import com.omneAgate.wholeSaler.service.HttpClientWrapper;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HTTP;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


// BaseActivity is the base class for all activities

public abstract class BaseActivity extends AppCompatActivity {

    //Network connectivity
    NetworkConnection networkConnection;

    //HttpConnection service
    HttpClientWrapper httpConnection;

    //Global application context for this application
    GlobalAppState appState;

    //Progressbar for waiting
    CustomProgressDialog progressBar;


    //User menu popup
    PopupWindow popupMessage;

    //Layout Popup
    View layoutOfPopup;

    //User profile imageview
    ImageView imageViewUserProfile;
    FrameLayout frameLayout;
    DrawerLayout mDrawerLayout;
    NavigationView navigationView;
    private TextView profileName, WholesalerCode, LastLogin;
    private Button profileInfo, logout;
    static boolean status = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String languageCode = WholesaleDBHelper.getInstance(this).getMasterData("language");
        Log.e("Base activity", "land code" + languageCode);
        if (languageCode == null) {
            languageCode = "ta";
        }
        Util.changeLanguage(this, languageCode);
        GlobalAppState.language = languageCode;
        Log.e("Base activity", "" + GlobalAppState.language);
    }

    @Override
    public void setContentView(int id) {
        if (status) {
            super.setContentView(R.layout.activity_base);
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View inflate_layout = inflater.inflate(id, null);
            frameLayout = (FrameLayout) findViewById(R.id.content_frame);
            mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
            navigationView = (NavigationView) findViewById(R.id.nav_view);
            frameLayout.addView(inflate_layout);

        } else {
            super.setContentView(id);
        }

    }


    /*
       * abstract method for all activity
       * */
    protected abstract void processMessage(Bundle message, ServiceListenerType what);

    /**
     * Tamil text textView typeface
     * input  textView name and id for string.xml
     */
    public void setTamilText(TextView textName, int id) {
        if (GlobalAppState.language.equalsIgnoreCase("ta")) {
            Typeface tfBamini = Typeface.createFromAsset(getAssets(), "fonts/Bamini.ttf");
            textName.setTypeface(tfBamini, Typeface.BOLD);
            textName.setText(TamilUtil.convertToTamil(TamilUtil.BAMINI, getString(id)));
        } else {
            textName.setText(getString(id));
        }
    }

    public void setTamil(TextView textName, int id) {
        Typeface tfBamini = Typeface.createFromAsset(getAssets(), "fonts/Bamini.ttf");
        textName.setTypeface(tfBamini);
        textName.setText(TamilUtil.convertToTamil(TamilUtil.BAMINI, getString(id)));
    }

    public void setTamilHeader(TextView textName, int id) {
        Typeface tfBamini = Typeface.createFromAsset(getAssets(), "Impact.ttf");
        textName.setTypeface(tfBamini);
        textName.setText(getString(id));
    }


    public void setTamilText(EditText textName, String text, int size) {
        textName.setHint(text);
    }


    /*Handler used to get response from server*/
    protected final Handler SyncHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ServiceListenerType type = (ServiceListenerType) msg.obj;
            Log.e("Baseactivity", "service Type" + type);
            switch (type) {
                case LOGIN_USER:
                    processMessage(msg.getData(), ServiceListenerType.LOGIN_USER);
                    break;
                case LOGOUT_USER:
                    processMessage(msg.getData(), ServiceListenerType.LOGOUT_USER);
                    break;

                case CHECKVERSION:
                    processMessage(msg.getData(), ServiceListenerType.CHECKVERSION);
                    break;

                case CARD_ACTIVATION:
                    processMessage(msg.getData(), ServiceListenerType.CARD_ACTIVATION);
                    break;

                case OUTWARD:
                    processMessage(msg.getData(), ServiceListenerType.OUTWARD);
                    break;

                case DEVICE_REGISTER:
                    processMessage(msg.getData(), ServiceListenerType.DEVICE_REGISTER);
                    break;

                case DEVICE_STATUS:
                    processMessage(msg.getData(), ServiceListenerType.DEVICE_STATUS);
                    break;

                default:
                    //GlobalAppState.localLogin=true;
                    Log.e("BaseActivity", "Global app state" + GlobalAppState.localLogin);
                    processMessage(msg.getData(), ServiceListenerType.ERROR_MSG);
                    break;
            }
        }

    };


    public void setUpPopUpPage() {
        try {
            profileName = (TextView) navigationView.findViewById(R.id.profileName);
            WholesalerCode = (TextView) navigationView.findViewById(R.id.WholsalerCode);
            LastLogin = (TextView) navigationView.findViewById(R.id.last_loginTime);
            profileInfo = (Button) navigationView.findViewById(R.id.profileInfo);
            logout = (Button) navigationView.findViewById(R.id.logout_button);


            if (StringUtils.isNotEmpty(SessionId.getInstance().getUserName()))
                profileName.setText(SessionId.getInstance().getUserName().toUpperCase());

            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
            if (SessionId.getInstance().getLastLoginTime() != null)
                LastLogin.setText(formatter.format(SessionId.getInstance().getLastLoginTime()).toUpperCase());

            if (SessionId.getInstance().getFpsCode() != null)
                WholesalerCode.setText(SessionId.getInstance().getWholesaleCode().toUpperCase());

            if (getLocalClassName().toString().contains("ProfileActivity")) {
                profileInfo.setVisibility(View.GONE);
            }


            profileInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    profilePage();

                }
            });
            logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                    drawer.closeDrawer(GravityCompat.END);
                    userLogoutResponse();
                }
            });

            View viewOnline = findViewById(R.id.onLineOffline);
            TextView textViewOnline = (TextView) findViewById(R.id.textOnline);
            networkConnection = new NetworkConnection(this);
            if (StringUtils.isNotEmpty(SessionId.getInstance().getSessionId()) && networkConnection.isNetworkAvailable()) {
                viewOnline.setBackgroundResource(R.drawable.rounded_circle_green);
                textViewOnline.setTextColor(Color.parseColor("#038203"));
                setTamilText(textViewOnline, R.string.onlineText);
            } else {
                viewOnline.setBackgroundResource(R.drawable.rounded_circle_red);
                textViewOnline.setTextColor(Color.parseColor("#FFFF0000"));
                setTamilText(textViewOnline, R.string.offlineText);
            }

        } catch (Exception e) {
            Log.e("BaseActivity", e.toString(), e);
        }
    }

    public void setUpPopUpPageForAdmin() {
        try {
            layoutOfPopup = LayoutInflater.from(this).inflate(R.layout.popup_admin_image, new LinearLayout(this), false);
            popupMessage = new PopupWindow(layoutOfPopup, LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            popupMessage.setContentView(layoutOfPopup);
            Log.e(BaseActivity.class.getSimpleName(), "Popup" + "Pop up called");
            if (StringUtils.isNotEmpty(SessionId.getInstance().getUserName()))
                ((TextView) layoutOfPopup.findViewById(R.id.popup_userName)).setText(SessionId.getInstance().getUserName().toUpperCase());
            setTamilText(((TextView) layoutOfPopup.findViewById(R.id.welcome_view)), R.string.welcome_view);
            setTamilText(((TextView) layoutOfPopup.findViewById(R.id.last_login)), R.string.last_login);
            setTamilText(((TextView) layoutOfPopup.findViewById(R.id.logout)), R.string.logout);
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy hh:mm a", Locale.getDefault());
            if (SessionId.getInstance().getLastLoginTime() != null)
                ((TextView) layoutOfPopup.findViewById(R.id.popup_last_login)).setText(formatter.format(SessionId.getInstance().getLastLoginTime()).toUpperCase());

            popupMessage.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            popupMessage.setOutsideTouchable(true);
            imageViewUserProfile = (ImageView) findViewById(R.id.imageViewUserProfile);
            imageViewUserProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupMessage.showAsDropDown(imageViewUserProfile, 0, 0);

                }
            });
            layoutOfPopup.findViewById(R.id.logout_layout).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupMessage.dismiss();
                    userLogoutResponse();
                }
            });

            View viewOnline = findViewById(R.id.onLineOffline);
            TextView textViewOnline = (TextView) findViewById(R.id.textOnline);
            networkConnection = new NetworkConnection(this);
            if (StringUtils.isNotEmpty(SessionId.getInstance().getSessionId()) && networkConnection.isNetworkAvailable()) {
                viewOnline.setBackgroundResource(R.drawable.rounded_circle_green);
                textViewOnline.setTextColor(Color.parseColor("#038203"));
                setTamilText(textViewOnline, R.string.onlineText);
            } else {
                viewOnline.setBackgroundResource(R.drawable.rounded_circle_red);
                textViewOnline.setTextColor(Color.parseColor("#FFFF0000"));
                setTamilText(textViewOnline, R.string.offlineText);
            }

        } catch (Exception e) {
            Log.e("BaseActivity", e.toString(), e);
        }
    }


    private void profilePage() {
        startActivity(new Intent(this, ProfileActivity.class));
        finish();
    }

    //Logout request from user success and send to server
    public void logOutSuccess() {
        // SessionId.getInstance().setSessionId("");
        networkConnection = new NetworkConnection(this);
        String logoutString = "OFFLINE_LOGOUT";
        if (networkConnection.isNetworkAvailable()) {
            try {
                String url = "/login/logout";
                logoutString = "ONLINE_LOGOUT";

                LogoutDto logoutdto = new LogoutDto();
                logoutdto.setSessionId(SessionId.getInstance().getSessionId());
                logoutdto.setLogoutStatus(logoutString);
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault());
                String dateStr = df.format(new Date());
                logoutdto.setLogoutTime(dateStr);

                String logout = new Gson().toJson(logoutdto);
                Log.e("base activity", "logout..." + logout);
                StringEntity se = new StringEntity(logout, HTTP.UTF_8);
                httpConnection = new HttpClientWrapper();
                httpConnection.sendRequest(url, null, ServiceListenerType.LOGOUT_USER,
                        SyncHandler, RequestType.POST, se, this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        WholesaleDBHelper.getInstance(this).updateLoginHistory(SessionId.getInstance().getTransactionId(), logoutString);
        //  WholesaleDBHelper.getInstance(this).closeConnection();
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    //After user give logout this method will call dialog
    private void userLogoutResponse() {
        LogoutDialog logout = new LogoutDialog(this);
        //Util.LoggingQueue(this, "Logout", "Logout dialog appearence");
        logout.show();
    }

}
