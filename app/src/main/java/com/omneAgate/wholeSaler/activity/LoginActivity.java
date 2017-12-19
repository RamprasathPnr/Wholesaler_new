package com.omneAgate.wholeSaler.activity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListPopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.omneAgate.wholeSaler.ActivityBusinessClass.LoginCheck;
import com.omneAgate.wholeSaler.DTO.ApplicationType;
import com.omneAgate.wholeSaler.DTO.DeviceRegistrationRequestDto;
import com.omneAgate.wholeSaler.DTO.DeviceRegistrationResponseDto;
import com.omneAgate.wholeSaler.DTO.EnumDTO.DeviceStatus;
import com.omneAgate.wholeSaler.DTO.EnumDTO.RequestType;
import com.omneAgate.wholeSaler.DTO.EnumDTO.ServiceListenerType;
import com.omneAgate.wholeSaler.DTO.LoginDto;
import com.omneAgate.wholeSaler.DTO.LoginHistoryDto;
import com.omneAgate.wholeSaler.DTO.MenuDataDto;
import com.omneAgate.wholeSaler.DTO.UserDetailDto;
import com.omneAgate.wholeSaler.DTO.VersionUpgradeDto;
import com.omneAgate.wholeSaler.DTO.WholeSaleLoginDto;
import com.omneAgate.wholeSaler.DTO.WholeSaleLoginResponseDto;
import com.omneAgate.wholeSaler.DTO.WholesalerDto;
import com.omneAgate.wholeSaler.Util.AndroidDeviceProperties;
import com.omneAgate.wholeSaler.Util.Constants.WholeSaleConstants;
import com.omneAgate.wholeSaler.Util.CustomProgressDialog;
import com.omneAgate.wholeSaler.Util.LoginData;
import com.omneAgate.wholeSaler.Util.NetworkConnection;
import com.omneAgate.wholeSaler.Util.SessionId;
import com.omneAgate.wholeSaler.Util.SyncPageUpdate;
import com.omneAgate.wholeSaler.Util.TamilUtil;
import com.omneAgate.wholeSaler.Util.Util;
import com.omneAgate.wholeSaler.Util.WholesaleDBHelper;
import com.omneAgate.wholeSaler.activity.dialog.ChangeUrlDialog;
import com.omneAgate.wholeSaler.activity.dialog.DateChangeDialog;
import com.omneAgate.wholeSaler.activity.dialog.DeviceIdDialog;
import com.omneAgate.wholeSaler.activity.dialog.LanguageSelectionDialog;
import com.omneAgate.wholeSaler.activity.dialog.LogoutDialog;
import com.omneAgate.wholeSaler.activity.dialog.MenuAdapter;
import com.omneAgate.wholeSaler.activity.dialog.ResetPasswordDialog;
import com.omneAgate.wholeSaler.service.HttpClientWrapper;
import com.omneAgate.wholeSaler.service.OfflineTransactionManager;
import com.omneAgate.wholeSaler.service.UpdateDataService;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HTTP;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;


public class LoginActivity extends BaseActivity implements AdapterView.OnItemClickListener, NavigationView.OnNavigationItemSelectedListener {

    //Popup window for menu
    ListPopupWindow popupWindow;

    //Wholesaler Loging Response Dto
    WholeSaleLoginResponseDto loginResponse;

    //User textBox for entering username and password
    private EditText userNameTextBox, passwordTextBox;

    private DrawerLayout drawer;
    private NavigationView navigationView;
    private ImageView navigation_menu;
    private static final String TAG = LoginActivity.class.getCanonicalName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        status = false;
        setContentView(R.layout.activity_navigation_drawer);
        ((TextView) findViewById(R.id.tvForgetPassword)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ResetPasswordDialog(LoginActivity.this).show();
            }
        });
        try {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            networkConnection = new NetworkConnection(this);
            userNameTextBox = (EditText) findViewById(R.id.login_username);
            passwordTextBox = (EditText) findViewById(R.id.login_password);
            drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, null, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            navigation_menu = (ImageView) findViewById(R.id.navigation_menu);
            navigation_menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawer.openDrawer(Gravity.RIGHT);
                }
            });
            drawer.setDrawerListener(toggle);
            toggle.syncState();
            navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setItemIconTintList(null);
            View header = navigationView.getHeaderView(0);

            Menu menu = navigationView.getMenu();
            navigationView.setNavigationItemSelectedListener(this);
            httpConnection = new HttpClientWrapper();
            //initialisePage();

        } catch (Exception e) {
            Log.e(TAG, "Exception in oncreate " + e.toString(), e);
        }

    }


    @Override
    protected void onStart() {
        super.onStart();
        GlobalAppState.localLogin = false;
        Log.e(TAG, "Global app state " + GlobalAppState.localLogin);
        removeAllServices();
    }

    private void initialisePage() {
        TextView tvWholesaleTitle = (TextView) findViewById(R.id.titleWholesaleApp);
        TextView titleTop = (TextView) findViewById(R.id.titlePds);
        Button loginButton = (Button) findViewById(R.id.login_loginButton);
        TextView tvForgetPassword = (TextView) findViewById(R.id.tvForgetPassword);

       /* if (GlobalAppState.language.equalsIgnoreCase("ta")) {
            setTamilText(tvWholesaleTitle,"மண்ணெண்ணெய் மொத்த விற்பனை பயன்பாடு",30);
            setTamilText(titleTop ,"பொது விநியோகத் திட்டம்",30);
            setTamilText(loginButton ,"உள்நுழை",30);
            setTamilText(userNameTextBox,"பயனர் பெயர்",22);
            setTamilText(passwordTextBox,"கடவுச்சொல்",22);
            setTamilText(tvForgetPassword,"கடவுச்சொல்லை மறந்துவிட்டீர்களா?",20);
            Log.e("Language", GlobalAppState.language);
        }else{
            setTamilText(tvWholesaleTitle,"Kerosene Wholesale Application",27);
            setTamilText(titleTop ,"PUBLIC DISTRIBUTION SYSTEM",25);
            setTamilText(loginButton ,"Login",30);
            setTamilText(userNameTextBox,"Username",25);
            setTamilText(passwordTextBox,"Password",25);
            setTamilText(tvForgetPassword,"Forgot Password?",22);
            Log.e("Language",GlobalAppState.language);
        }*/

    }


    //onclick event for login button
    public void userLogin(View view) {
        try {


            InputMethodManager im = (InputMethodManager) getSystemService(
                    INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(view.getWindowToken(), 0);

            if (ActivityCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                // Camera permission has not been granted.
                requestCameraPermission();
            } else {
                WholeSaleLoginDto loginCredentials = getUsernamePassword();
                if (loginCredentials == null) {

                    return;
                }
                authenticateUser(loginCredentials);
            }

        } catch (Exception e) {
            Log.e(TAG, "Login button " + e.toString(), e);
        }
    }

    /**
     * Menu creation
     * Used to change language
     * *
     */

   /* public void showPopupMenu(View v) {
        List<MenuDataDto> menuDto = new ArrayList<>();
        menuDto.add(new MenuDataDto("Language", R.drawable.icon_language, "மொழி"));
        menuDto.add(new MenuDataDto("Change URL", R.drawable.icon_server, "யுஆர்யல் மாற்று"));
        menuDto.add(new MenuDataDto("Device Details", R.drawable.icon_device_details, "சாதனத் தகவல்"));
        popupWindow = new ListPopupWindow(this);
        ListAdapter adapter = new MenuAdapter(this, menuDto); // The view ids to map the data to
        popupWindow.setAnchorView(v);
        popupWindow.setAdapter(adapter);
        popupWindow.setWidth(300); // note: don't use pixels, use a dimen resource
        popupWindow.setOnItemClickListener(this); // the callback for when a list item is selected
        popupWindow.show();
        Log.e(LoginActivity.class.getSimpleName() + "Login Entry", "Inside Popup Window");
    }
*/

    /**
     * Menu item click listener
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        popupWindow.dismiss();
        switch (position) {
            case 0:
                new LanguageSelectionDialog(this).show();
                break;
            case 1:
                new ChangeUrlDialog(this).show();
                break;
            case 2:
                new DeviceIdDialog(this).show();
                break;
            case 3:
                new LogoutDialog(this).show();
                break;

        }
    }


    //return login DTO if it is valid else null
    private WholeSaleLoginDto getUsernamePassword() {
        WholeSaleLoginDto loginCredentials = new WholeSaleLoginDto();
        String userName = userNameTextBox.getText().toString();
        String password = passwordTextBox.getText().toString();

        //Username field empty
        if (StringUtils.isEmpty(userName)) {
            Toast.makeText(LoginActivity.this, getString(R.string.loginUserNameEmpty), Toast.LENGTH_SHORT).show();
            return null;
        }
        //password field empty
        if (StringUtils.isEmpty(password)) {
            Toast.makeText(LoginActivity.this, getString(R.string.loginPasswordEmpty), Toast.LENGTH_SHORT).show();
            return null;
        }
        loginCredentials.setUserId(userName);
        loginCredentials.setPassword(password);

        return loginCredentials;
    }

    /**
     * sending login details to server if network connection available
     *
     * @params WholeSaleLoginDto
     */
    private void authenticateUser(WholeSaleLoginDto loginCredentials) {

        try {
            progressBar = new CustomProgressDialog(this);
            progressBar.setCanceledOnTouchOutside(false);
            if (networkConnection.isNetworkAvailable()) {
                String url = "/login/wholesale";
                loginCredentials.setMacId(Settings.Secure.getString(
                        getContentResolver(), Settings.Secure.ANDROID_ID).toUpperCase());
                loginCredentials.setAppType(ApplicationType.KeroseneWholeSaler);
                String login = new Gson().toJson(loginCredentials);
                Log.e("LoginRequest", login);
                StringEntity se = new StringEntity(login, HTTP.UTF_8);
                progressBar.show();
                httpConnection.sendRequest(url, null, ServiceListenerType.LOGIN_USER,
                        SyncHandler, RequestType.POST, se, this);
            } else {
                progressBar.setCanceledOnTouchOutside(false);
                SharedPreferences mySharedPreferences = getSharedPreferences("FPS_POS", MODE_PRIVATE);
                if (mySharedPreferences.getBoolean("sync_complete", false)) {
                    Log.e("Loginactivty", "local login service while offline");
                    LoginCheck loginLocal = new LoginCheck(this, progressBar);
                    loginLocal.localLogin(loginCredentials);
                } else {
                    Toast.makeText(LoginActivity.this, getString(R.string.connectionRefused), Toast.LENGTH_SHORT).show();
                }

            }
        } catch (Exception e) {
            dismissProgress();
            Log.e("LoginActivity", e.toString(), e);
        }
    }


    /**
     * sending registration details to server if network connection available
     * else no network connection error message will appear
     *
     * @param loginCredentials for user
     */
    private void registerDevice(LoginDto loginCredentials) {
        try {
            if (networkConnection.isNetworkAvailable()) {
                String url = "/device/registerwholesaleapp";
                AndroidDeviceProperties deviceProperties = new AndroidDeviceProperties(this);
                DeviceRegistrationRequestDto deviceRegister = new DeviceRegistrationRequestDto();
                deviceRegister.setLoginDto(loginCredentials);
                deviceRegister.setDeviceDetailsDto(deviceProperties.getDeviceProperties());
                String login = new Gson().toJson(deviceRegister);
                StringEntity se = new StringEntity(login, HTTP.UTF_8);
                Log.e(LoginActivity.class.getSimpleName() + "Device Register request", login);
                httpConnection.sendRequest(url, null, ServiceListenerType.DEVICE_REGISTER,
                        SyncHandler, RequestType.POST, se, this);
            } else {
                Toast.makeText(LoginActivity.this, getString(R.string.connectionError), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.e("LoginActivity", e.toString(), e);
        }
    }


    private void checkUserApk() {
        try {
            if (networkConnection.isNetworkAvailable()) {
                SessionId.getInstance().setSessionId(loginResponse.getSessionid());
                /*Log.e("WholesalerId",""+loginResponse.getWholeSaler().getId());
                SessionId.getInstance().setWholesaleId(loginResponse.getWholeSaler().getId());*/
                VersionUpgradeDto version = new VersionUpgradeDto();
                version.setAppType(ApplicationType.KeroseneWholeSaler);
                String url = "/versionUpgrade/view";
                String checkVersion = new Gson().toJson(version);
                StringEntity se = new StringEntity(checkVersion, HTTP.UTF_8);
                Log.e(LoginActivity.class.getSimpleName(), "Device Register Version" + "Checking version of apk in device");
                httpConnection.sendRequest(url, null, ServiceListenerType.CHECKVERSION,
                        SyncHandler, RequestType.POST, se, this);
            } else {

                dismissProgress();

                Toast.makeText(LoginActivity.this, getString(R.string.connectionError), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            dismissProgress();
            Toast.makeText(LoginActivity.this, getString(R.string.inCorrectUnamePword), Toast.LENGTH_SHORT).show();
        }
    }

    /*Concrete method*/
    @Override
    public void processMessage(Bundle message, ServiceListenerType what) {
        switch (what) {
            case LOGIN_USER:
                userLoginResponse(message);
                break;

            case DEVICE_REGISTER:
                userRegisterResponse(message);
                break;

            case CHECKVERSION:
                checkData(message);
                break;

            default:
                dismissProgress();
                SharedPreferences prefs = getSharedPreferences("WHOLESALER", MODE_PRIVATE);
                if (!prefs.getBoolean("approved", false)) {
                    Toast.makeText(LoginActivity.this, getString(R.string.connectionRefused), Toast.LENGTH_SHORT).show();
                } else {
                    SharedPreferences mySharedPreferences = getSharedPreferences("FPS_POS",
                            MODE_PRIVATE);
                    if (mySharedPreferences.getBoolean("sync_complete", false)) {
                        Log.e("Loginactivty", "local login service while service in active");
                        LoginCheck loginLocal = new LoginCheck(this, progressBar);
                        loginLocal.localLogin(getUsernamePassword());
                    } else {
                        Toast.makeText(LoginActivity.this, getString(R.string.connectionRefused), Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }

    }


    private void checkData(Bundle message) {
        try {
            String response = message.getString(WholeSaleConstants.RESPONSE_DATA);

            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson = gsonBuilder.create();

            VersionUpgradeDto versionUpgradeDto = gson.fromJson(response, VersionUpgradeDto.class);
            Log.e("CheckAUpgradeData", "" + versionUpgradeDto.toString());
            if (versionUpgradeDto == null || versionUpgradeDto.getVersion() == 0 || StringUtils.isEmpty(versionUpgradeDto.getLocation())) {
                dismissProgress();
                Toast.makeText(LoginActivity.this, getString(R.string.errorUpgrade), Toast.LENGTH_SHORT).show();
            } else {
                if (versionUpgradeDto.getStatusCode() == 0) {
                    PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                    Log.e("NewVersion", "" + pInfo.versionCode);
                    if (versionUpgradeDto.getVersion() > pInfo.versionCode) {

                        dismissProgress();
                        Intent intent = new Intent(this, AutoUpgrationActivity.class);
                        intent.putExtra("downloadPath", versionUpgradeDto.getLocation());
                        intent.putExtra("newVersion", versionUpgradeDto.getVersion());
                        startActivity(intent);
                        finish();
                    } else {

                        if (loginResponse.getDeviceStatus() == DeviceStatus.UNASSOCIATED) {
                            checkDeviceStatus();
                        } else {
                            authenticationSuccess();
                        }
                    }
                } else {

                    dismissProgress();
                    Toast.makeText(LoginActivity.this, getString(R.string.errorUpgrade), Toast.LENGTH_SHORT).show();
                }

            }
        } catch (Exception e) {
            dismissProgress();
            Toast.makeText(LoginActivity.this, getString(R.string.errorUpgrade), Toast.LENGTH_SHORT).show();
            Log.e("LoginActivity", e.toString(), e);
        }

    }


    /**
     * After login response received from server successfully in android
     *
     * @params bundle of message that received
     */
    private void userLoginResponse(Bundle message) {
        try {
            String response = message.getString(WholeSaleConstants.RESPONSE_DATA);
            dismissProgress();
            Log.e("LoginActivity", "Login Request response:" + response);
            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson = gsonBuilder.create();
            loginResponse = gson.fromJson(response, WholeSaleLoginResponseDto.class);
            SessionId.getInstance().setSessionId(loginResponse.getSessionid());
            if (loginResponse != null) {

                if (loginResponse.getUserDetailDto() != null && loginResponse.getUserDetailDto().getProfile().equalsIgnoreCase("Admin")) {
                    if (loginResponse.getDeviceStatus() != null && loginResponse.getDeviceStatus() == DeviceStatus.UNASSOCIATED) {

                        long diff = new Date().getTime() - new Date(loginResponse.getServerTime()).getTime();//as given
                        long seconds = TimeUnit.MILLISECONDS.toSeconds(diff);
                        if (seconds < 300 && seconds > -300) {
                            checkUserApk();
                        } else {
                            new DateChangeDialog(this, loginResponse.getServerTime()).show();
                        }

                    } else if (loginResponse.getStatusCode() == 1002) {
                        Toast.makeText(LoginActivity.this, getString(R.string.inCorrectUnamePword), Toast.LENGTH_SHORT).show();
                    } else {
                        checkUserApk();
                    }

                } else {

                    if (loginResponse.isAuthenticationStatus()) {
                        if (loginResponse.getDeviceStatus() != null) {
                            if (loginResponse.getDeviceStatus() == DeviceStatus.UNASSOCIATED) {
                                WholesaleDBHelper.getInstance(this).updateMaserData("status", "UNASSOCIATED");
                                WholesaleDBHelper.getInstance(this).updateStoreActive(userNameTextBox.getText().toString());
                                WholesaleDBHelper.getInstance(this).updateUserActive(userNameTextBox.getText().toString());


                                long diff = new Date().getTime() - new Date(loginResponse.getServerTime()).getTime();//as given
                                long seconds = TimeUnit.MILLISECONDS.toSeconds(diff);
                                if (seconds < 300 && seconds > -300) {
                                    checkUserApk();
                                } else {
                                    new DateChangeDialog(this, loginResponse.getServerTime()).show();
                                }


                            } else if (loginResponse.getDeviceStatus() == DeviceStatus.ACTIVE) {
                                WholesaleDBHelper.getInstance(this).updateMaserData("status", "ACTIVE");
                                checkUserApk();

                            }
                        } else {
                            Toast.makeText(LoginActivity.this, getString(R.string.deviceInvalid), Toast.LENGTH_SHORT).show();
                        }
                    } else if (loginResponse.getStatusCode() == 32000) {

                        WholesaleDBHelper.getInstance(this).updateStoreActiveStatusDetails(userNameTextBox.getText().toString());
                        Toast.makeText(LoginActivity.this, getString(R.string.storeInactive), Toast.LENGTH_SHORT).show();


                    } else if (loginResponse.getDeviceStatus() == DeviceStatus.INACTIVE) {

                        dismissProgress();
                        if (loginResponse.getStatusCode() == 5004) {
                            WholesaleDBHelper.getInstance(this).updateMaserData("status", "INACTIVE");
                            WholesaleDBHelper.getInstance(this).updateStoreActive(userNameTextBox.getText().toString());
                            WholesaleDBHelper.getInstance(this).updateUserActive(userNameTextBox.getText().toString());
                            Toast.makeText(LoginActivity.this, getString(R.string.deviceInvalid), Toast.LENGTH_SHORT).show();
                        } else if (loginResponse.getStatusCode() == 1002) {
                            Toast.makeText(LoginActivity.this, getString(R.string.inCorrectUnamePword), Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e("loginactivity", "Device is Inactive");
                            Toast.makeText(LoginActivity.this, getString(R.string.deviceInvalid), Toast.LENGTH_SHORT).show();
                        }
                    } else if (loginResponse.getStatusCode() == 5017) {
                        WholesaleDBHelper.getInstance(this).updateMaserData("status", "INACTIVE");
                        Toast.makeText(LoginActivity.this, getString(R.string.deviceInvalid), Toast.LENGTH_SHORT).show();
                    } else {

                        dismissProgress();
                        Toast.makeText(LoginActivity.this, getString(R.string.inCorrectUnamePword), Toast.LENGTH_SHORT).show();
                    }
                }

            } else {

                dismissProgress();
                Toast.makeText(LoginActivity.this, getString(R.string.serviceNotAvailable), Toast.LENGTH_SHORT).show();

            }
        } catch (Exception e) {

            dismissProgress();
            Toast.makeText(LoginActivity.this, getString(R.string.inCorrectUnamePword), Toast.LENGTH_SHORT).show();
            Log.e("LoginActivity", e.toString(), e);
        }

    }


    private void checkDeviceStatus() {

        SharedPreferences prefs = getSharedPreferences("WHOLESALER", MODE_PRIVATE);
        Log.e("status", "Inside device status");
        if (loginResponse.getDeviceStatus() == DeviceStatus.UNASSOCIATED) {

            Log.e(LoginActivity.class.getSimpleName() + "Device Association", "Device Not associated");
            if (!prefs.getBoolean("approved", false)) {
                checkForRegistration(loginResponse.getUserDetailDto());
            } else {
                if (loginResponse.getUserDetailDto().getProfile().equalsIgnoreCase("Admin")) {
                    dismissProgress();
                    authenticationSuccess();
                } else {
                    dismissProgress();
                    Toast.makeText(LoginActivity.this, getString(R.string.unassociated), Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            dismissProgress();
            Toast.makeText(LoginActivity.this, getString(R.string.inCorrectUnamePword), Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * Used to check whether device is registered or not
     *
     * @param userDetails from server
     */
    private void checkForRegistration(UserDetailDto userDetails) {
        if (userDetails != null && StringUtils.equalsIgnoreCase(userDetails.getProfile(), "ADMIN")) {
            LoginDto loginCredentials = new LoginDto();
            loginCredentials.setUserName(getUsernamePassword().getUserId());
            loginCredentials.setPassword(getUsernamePassword().getPassword());

            if (loginCredentials == null) {
                return;
            }
            SharedPreferences prefs = getSharedPreferences("WHOLESALER", MODE_PRIVATE);
            if (!prefs.getBoolean("register", false)) {
                Log.e(LoginActivity.class.getSimpleName() + "Device Register", "Device Registration process started");
                registerDevice(loginCredentials);
            } else {

                dismissProgress();
                Log.e(LoginActivity.class.getSimpleName() + "Device Register", "Device Registration already done");
                startActivity(new Intent(this, RegistrationActivity.class));
                finish();
            }
        } else {

            dismissProgress();
            Log.e(LoginActivity.class.getSimpleName() + "Device Register", "Insufficient Credentials");
            Toast.makeText(LoginActivity.this, getString(R.string.inCorrectUserCredential), Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * After registration response received from server successfully in android
     *
     * @param message bundle that received
     */
    private void userRegisterResponse(Bundle message) {
        try {
            String response = message.getString(WholeSaleConstants.RESPONSE_DATA);

            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson = gsonBuilder.create();
            DeviceRegistrationResponseDto deviceRegistrationResponse = gson.fromJson(response,
                    DeviceRegistrationResponseDto.class);

            dismissProgress();
            if (deviceRegistrationResponse.getStatusCode() == 0) {
                Util.storePreferenceRegister(this);
                startActivity(new Intent(this, RegistrationActivity.class));
                finish();
            } else if (deviceRegistrationResponse.getStatusCode() == 5056) {
                Log.e(LoginActivity.class.getSimpleName() + "Device Register", "Device already registered");
                Util.storePreferenceApproved(this);
                checkTime();
            } else {
                Toast.makeText(LoginActivity.this, getString(R.string.inCorrectUnamePword), Toast.LENGTH_SHORT).show();

            }

        } catch (Exception e) {

            dismissProgress();
            Log.e(LoginActivity.class.getSimpleName() + "Registered error", e.toString());

        }
    }


    /**
     * After successful login of user
     * Userdetails will be stored in Singleton class
     */
    private void authenticationSuccess() {
        try {

            Log.e(LoginActivity.class.getSimpleName() + "Login response", loginResponse.toString());
            SessionId.getInstance().setSessionId(loginResponse.getSessionid());
            SessionId.getInstance().setUserId(loginResponse.getUserDetailDto().getId());
            String lastLoginTime = WholesaleDBHelper.getInstance(this).getLastLoginTime(loginResponse.getUserDetailDto().getId());
            if (StringUtils.isNotEmpty(lastLoginTime)) {
                SessionId.getInstance().setLastLoginTime(new Date(Long.parseLong(lastLoginTime)));
            } else {
                SessionId.getInstance().setLastLoginTime(new Date());
            }
            SessionId.getInstance().setLoginTime(new Date());

            if (loginResponse.getWholeSaler() != null) {
                Log.e("wholesalerid", "" + loginResponse.getWholeSaler().getId());
                WholesaleDBHelper.getInstance(this).insertMaserData("wholesalerid", "" + loginResponse.getWholeSaler().getId());
                SessionId.getInstance().setWholesaleId(loginResponse.getWholeSaler().getId());
                SessionId.getInstance().setWholesaleCode(loginResponse.getWholeSaler().getCode());
                Util.storePreferenceApproved(this);
            } else {
                loginResponse.setWholeSaler(new WholesalerDto());
            }
            Log.e("wholesaleridout", "" + loginResponse.getWholeSaler().getId());
            SessionId.getInstance().setUserName(loginResponse.getUserDetailDto().getUserId());
            LoginData.getInstance().setLoginData(loginResponse);
            loginResponse.getUserDetailDto().setPassword(loginResponse.getUserDetailDto().getPassword());

            WholesaleDBHelper.getInstance(this).insertLoginUserData(loginResponse, passwordTextBox.getText().toString().trim());
            WholesaleDBHelper.getInstance(this).setLastLoginTime(loginResponse.getUserDetailDto().getId());
            checkTime();


        } catch (Exception e) {
            Log.e(LoginActivity.class.getSimpleName() + "Log in success error", e.toString());
        } finally {

            dismissProgress();
        }
    }

    //Check Server Time with local time
    private void checkTime() {
        long diff = new Date().getTime() - new Date(loginResponse.getServerTime()).getTime();//as given
        Log.e("Time difference", "Seconds:" + diff);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(diff);
        if (seconds < 300 && seconds > -300) {
            loginSuccess();
        } else {

            dismissProgress();
            new DateChangeDialog(this, loginResponse.getServerTime()).show();
        }
    }

    /**
     * After login success the user navigation to Sale activity page
     * And also start the connection heartBeat service
     */
    private void loginSuccess() {
        Log.e("***login", "entry");
        //  Util.storePreferenceApproved(this);
        SharedPreferences mySharedPreferences = getSharedPreferences("FPS_POS", MODE_PRIVATE);
        LoginData.getInstance().setLoginData(loginResponse);

        if (mySharedPreferences.getBoolean("sync_complete", false)) {
            navigationToAdmin();
            Log.e("***login", "Execution 1");
        } else {
            Log.e("***login", "Execution 2");
            // insertLoginHistoryDetails();

            dismissProgress();
            String lastModifiedDate = WholesaleDBHelper.getInstance(this).getMasterData("syncTime");
            Log.e("***login", "Execution 3 lastmodified date " + lastModifiedDate);
            if (StringUtils.isNotEmpty(lastModifiedDate)) {
                Log.e("***login", "Execution 4 is not empty");
                SyncPageUpdate syncPage = new SyncPageUpdate(this);
                syncPage.setSync();
                navigationToAdmin();
            } else {
                Log.e("***login", "Execution 5 profile type" + loginResponse.getUserDetailDto().getProfile());
                if (loginResponse.getUserDetailDto().getProfile().equalsIgnoreCase("ADMIN")) {
                    Log.e(LoginActivity.class.getSimpleName() + "Logged in", "Admin login");
                    Intent homeIntent = new Intent(this, SyncPageActivity.class);
                    startActivity(homeIntent);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, getString(R.string.inCorrectUserCredential), Toast.LENGTH_SHORT).show();
                }
            }
        }

    }

    private void navigationToAdmin() {


        if (loginResponse.getUserDetailDto().getProfile().equalsIgnoreCase("ADMIN")) {
            Log.e(LoginActivity.class.getSimpleName() + "Logged in", "Admin login");
            // insertLoginHistoryDetails();

            dismissProgress();
            startActivity(new Intent(this, AdminActivity.class));
            finish();
        } else if (!loginResponse.getUserDetailDto().getActive()) {
            dismissProgress();
            Toast.makeText(LoginActivity.this, getString(R.string.userInactive), Toast.LENGTH_SHORT).show();
        } else if (!loginResponse.getWholeSaler().getStatus()) {
            dismissProgress();
            Toast.makeText(LoginActivity.this, getString(R.string.storeInactive), Toast.LENGTH_SHORT).show();
        } else {

            moveToDashboard();

        }
    }


    private void moveToDashboard() {
        try {
            insertLoginHistoryDetails();
            dismissProgress();
            startService(new Intent(this, UpdateDataService.class));
            startService(new Intent(this, OfflineTransactionManager.class));
            startActivity(new Intent(this, DashboardActivity.class));
            finish();

        } catch (Exception e) {
            Log.e("LoginActivity", e.toString(), e);
        }
    }

    private void insertLoginHistoryDetails() {
        LoginHistoryDto loginHistoryDto = new LoginHistoryDto();
        if (loginResponse.getUserDetailDto() != null)
            loginHistoryDto.setFpsId("" + loginResponse.getWholeSaler().getId());
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault());
        loginHistoryDto.setLoginTime(df.format(new Date()));
        loginHistoryDto.setLoginType("ONLINE_LOGIN");
        loginHistoryDto.setUserId("" + loginResponse.getUserDetailDto().getId());
        df = new SimpleDateFormat("ddMMyyHHmmss", Locale.getDefault());
        String transactionId = df.format(new Date());
        loginHistoryDto.setTransactionId(transactionId);
        SessionId.getInstance().setTransactionId(transactionId);
        WholesaleDBHelper.getInstance(this).insertLoginHistory(loginHistoryDto);
    }

    //This function dismiss Progress Dialog
    private void dismissProgress() {
        if (progressBar != null) {
            if (progressBar.isShowing()) {
                progressBar.dismiss();
            }
        }
    }

    @Override
    public void onBackPressed() {

    }

    /**
     * Used to stop all services running currently
     */
    private void removeAllServices() {
        stopService(new Intent(this, UpdateDataService.class));
        stopService(new Intent(this, OfflineTransactionManager.class));

    }

    /**
     * Tamil text textView typeface
     * input  textView name and text string input
     */
    public void setTamilText(TextView textName, String text, int fontSize) {
        if (GlobalAppState.language.equalsIgnoreCase("ta")) {
            Typeface tfBamini = Typeface.createFromAsset(getAssets(), "fonts/Bamini.ttf");
            textName.setTypeface(tfBamini);
            textName.setTextSize(fontSize);
            textName.setText(TamilUtil.convertToTamil(TamilUtil.BAMINI, text));
        } else {
            textName.setTextSize(fontSize);
            textName.setText(text);

        }
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.select_language:
                Select_language();
                break;
            case R.id.Change_Url:
                Change_url();
                break;
            case R.id.Device_info:
                if (ActivityCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    // Camera permission has not been granted.
                    requestCameraPermission();
                } else {
                    Device_info();
                }
                break;

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.END);
        return true;

    }

    private void requestCameraPermission() {
        // Camera permission has not been granted yet. Request it directly.
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 0);
    }

    private void Select_language() {
        new LanguageSelectionDialog(this).show();

    }

    private void Change_url() {
        new ChangeUrlDialog(this).show();

    }

    private void Device_info() {
        new DeviceIdDialog(this).show();

    }
}
