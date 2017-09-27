package com.example.ahmed.ui;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ahmed.helper.SQLiteHandler;
import com.example.ahmed.helper.SessionManager;
//import com.example.ahmed.listener.AppUsageStats;
import com.example.ahmed.listener.SensorEvent;
//import com.example.ahmed.listener.UStats;
//import com.example.ahmed.listener.UStats;
import com.example.ahmed.service.AlarmReceiver;
import com.example.ahmed.service.FbReceiver;
//import com.example.ahmed.service.StopAlarmReceiver;
import com.example.ahmed.service.NotifyReceiverAppetite;
import com.example.ahmed.service.NotifyReceiverConsumption;
import com.example.ahmed.service.NotifyReceiverMood;
import com.example.ahmed.service.NotifyReceiverSleep;
import com.example.ahmed.service.NotifyReceiverWeight;
import com.example.ahmed.service.StopAlarmReceiver;
import com.example.ahmed.service.sendLocation;
import com.example.ahmed.therapiodatafordepression.R;
import com.example.ahmed.ui.Auth.Activity_Login;
import com.example.ahmed.ui.QuestionnaireBDI.Beginners;
import com.example.ahmed.ui.Welcome.InfoActivity;
import com.example.ahmed.ui.Welcome.WelcomeActivity;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.github.ybq.android.spinkit.style.DoubleBounce;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.honu.aloha.WelcomeHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static android.widget.Toast.LENGTH_SHORT;
import com.github.jlmd.animatedcircleloadingview.AnimatedCircleLoadingView;
import com.joanfuentes.hintcase.HintCase;
import com.joanfuentes.hintcase.ShapeAnimator;
import com.joanfuentes.hintcaseassets.contentholderanimators.FadeInContentHolderAnimator;
import com.joanfuentes.hintcaseassets.contentholderanimators.SlideInFromRightContentHolderAnimator;
import com.joanfuentes.hintcaseassets.contentholderanimators.SlideOutFromRightContentHolderAnimator;
import com.joanfuentes.hintcaseassets.hintcontentholders.SimpleHintContentHolder;
import com.joanfuentes.hintcaseassets.shapeanimators.RevealCircleShapeAnimator;
import com.joanfuentes.hintcaseassets.shapeanimators.UnrevealCircleShapeAnimator;
import com.joanfuentes.hintcaseassets.shapes.CircularShape;

import fr.quentinklein.slt.LocationTracker;
import fr.quentinklein.slt.TrackerSettings;


public class MainActivity extends AppCompatActivity {

    Intent alarmIntent;
    private PendingIntent pendingIntent;
    private PendingIntent pendingIntent2;
    private PendingIntent pendingStopIntent;
    private AlarmManager manager;
    private AlarmManager m;


    private JSONArray jsonArray;
    private JSONObject jsonObject = null;
    private CallbackManager mcallbackManager;
    private AccessTokenTracker mTokenTracker;
    private ProfileTracker mProfileTracker;
    private TextView largeTextView;
    //int numberOfLikes = 0;
    int numbOfPosts = 0;

    int numLocations = 0;


    Button StrB, StpB, AppBtn, whatsgoin;
    private TextView txtName;
    private TextView txtEmail;
    private Button btnLogout;

    private SQLiteHandler db;
    private SessionManager session;


    static String uID = Activity_Login.getUserID();
    public static String ALUSER = "";
    public static String accessTokenString;
    public static String userId = "";
    public static String userName = "";

    protected static double longit;
    protected static double latit;

    private AnimatedCircleLoadingView animatedCircleLoadingView;

/*    public static String getuID() {
        return uID;
    }*/



    private FacebookCallback<LoginResult> mCallback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            AccessToken accessToken = loginResult.getAccessToken();
            Profile profile = Profile.getCurrentProfile();
            //accessTokenString = accessToken.toString();
            /*Toast.makeText(getApplicationContext(), "access token :"+accessTokenString , LENGTH_SHORT).show();
            Log.d("Access token :   ", accessTokenString);*/

/*          userId = profile.getId().toString();
            userName = profile.getName().toString();*/

            //txtName.setText(constructWelcomeMessage(profile));
            //run();
        }

        @Override
        public void onCancel() {
        }

        @Override
        public void onError(FacebookException error) {

        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mcallbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        List<String> list = Arrays.asList("email", "user_posts");
        if (loginButton != null) {
            loginButton.setReadPermissions(list);
            loginButton.registerCallback(mcallbackManager,mCallback);
        }
        mTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldToken, AccessToken newToken) {
                //updateWithToken(newToken);
            }
        };
        mProfileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {
                //txtName.setText(constructWelcomeMessage(newProfile));
            }
        };
        mTokenTracker.startTracking();
        mProfileTracker.startTracking();


        //animatedCircleLoadingView = (AnimatedCircleLoadingView) findViewById(R.id.circle_loading_view);

        //txtName = (TextView) findViewById(R.id.textView6);
        //btnLogout = (Button) findViewById(R.id.btnLogout);

        Log.d("","");

        loadData();

        saveData();

        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // session manager
        session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn()) {
            logoutUser();
        }

        // Fetching user details from sqlite
        HashMap<String, String> user = db.getUserDetails();

        String name = user.get("name");

        // Displaying the user details on the screen
        //txtName.setText("Welcome "+ name);

        SharedPreferences prefs = getSharedPreferences("appName", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Intent intent;
        if (prefs.getBoolean("isInitialAppLaunch", true)) {
            //First Time App launched, you are putting isInitialAppLaunch to false and calling create splash activity.
            editor.putBoolean("isInitialAppLaunch", false);
            editor.apply();
            if (savedInstanceState == null)
                maybeShowWelcomeActivity();
        }


        /*try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.example.ahmed",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash", "KeyHash:"+ Base64.encodeToString(md.digest(),
                        Base64.DEFAULT));
                Toast.makeText(getApplicationContext(), Base64.encodeToString(md.digest(),
                        Base64.DEFAULT), Toast.LENGTH_LONG).show();
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }*/


        /* 1- APPETITE*/

        SharedPreferences notifprefsAppetite = PreferenceManager.getDefaultSharedPreferences(this);
        if (!prefs.getBoolean("firstTime", false)) {

            Intent alarmIntentAppet = new Intent(this, NotifyReceiverAppetite.class);
            alarmIntentAppet.setAction(Long.toString(System.currentTimeMillis()));
            PendingIntent AppetitependingIntent = PendingIntent.getBroadcast(this, 0, alarmIntentAppet, PendingIntent.FLAG_CANCEL_CURRENT);

            AlarmManager Appetitemanager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY, 20);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 1);

            // if the scheduler date is passed, move scheduler time to tomorrow
            if (System.currentTimeMillis() > calendar.getTimeInMillis()) {
                calendar.add(Calendar.DAY_OF_MONTH, 1);
            }

            Appetitemanager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, AppetitependingIntent);

            SharedPreferences.Editor notifeditor = notifprefsAppetite.edit();
            notifeditor.putBoolean("firstTime", true);
            notifeditor.apply();
        }

        /*2- MOOD*/

        SharedPreferences notifprefsMood = PreferenceManager.getDefaultSharedPreferences(this);
        if (!prefs.getBoolean("firstTime", false)) {

            Intent alarmIntent = new Intent(this, NotifyReceiverMood.class);
            alarmIntent.setAction(Long.toString(System.currentTimeMillis()));
            PendingIntent MoodpendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT);

            AlarmManager Moodmanager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY, 18);
            calendar.set(Calendar.MINUTE, 23);
            calendar.set(Calendar.SECOND, 1);

            // if the scheduler date is passed, move scheduler time to tomorrow
            if (System.currentTimeMillis() > calendar.getTimeInMillis()) {
                calendar.add(Calendar.DAY_OF_YEAR, 1);
            }

            Moodmanager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, MoodpendingIntent);

            SharedPreferences.Editor notifeditor = notifprefsMood.edit();
            notifeditor.putBoolean("firstTime", true);
            notifeditor.apply();
        }


        /*3- SLEEP*/

        SharedPreferences notifprefsSleep = PreferenceManager.getDefaultSharedPreferences(this);
        if (!prefs.getBoolean("firstTime", false)) {

            Intent alarmIntent = new Intent(this, NotifyReceiverSleep.class);
            alarmIntent.setAction(Long.toString(System.currentTimeMillis()));
            PendingIntent SleeppendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);

            AlarmManager Sleepmanager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY, 10);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 1);

            // if the scheduler date is passed, move scheduler time to tomorrow
            if (System.currentTimeMillis() > calendar.getTimeInMillis()) {
                calendar.add(Calendar.DAY_OF_YEAR, 1);
            }

            Sleepmanager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, SleeppendingIntent);

            SharedPreferences.Editor notifeditor = notifprefsSleep.edit();
            notifeditor.putBoolean("firstTime", true);
            notifeditor.apply();
        }

        /*4- CONSUMPTION*/

        SharedPreferences notifprefsConsumption = PreferenceManager.getDefaultSharedPreferences(this);
        if (!prefs.getBoolean("firstTime", false)) {

            Intent alarmIntent = new Intent(this, NotifyReceiverConsumption.class);
            alarmIntent.setAction(Long.toString(System.currentTimeMillis()));
            PendingIntent ConsumptionpendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);

            AlarmManager Consumptionmanager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY, 17);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 1);

            // if the scheduler date is passed, move scheduler time to tomorrow
            if (System.currentTimeMillis() > calendar.getTimeInMillis()) {
                calendar.add(Calendar.DAY_OF_YEAR, 1);
            }

            Consumptionmanager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY * 7, ConsumptionpendingIntent);

            SharedPreferences.Editor notifeditor = notifprefsConsumption.edit();
            notifeditor.putBoolean("firstTime", true);
            notifeditor.apply();
        }

        /*5- WEIGHT*/

        SharedPreferences notifprefsWeight = PreferenceManager.getDefaultSharedPreferences(this);
        if (!prefs.getBoolean("firstTime", false)) {

            Intent alarmIntent = new Intent(this, NotifyReceiverWeight.class);
            alarmIntent.setAction(Long.toString(System.currentTimeMillis()));
            PendingIntent weightpendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);

            AlarmManager Weightmanager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY, 16);
            calendar.set(Calendar.MINUTE, 32);
            calendar.set(Calendar.SECOND, 1);

            // if the scheduler date is passed, move scheduler time to tomorrow
            if (System.currentTimeMillis() > calendar.getTimeInMillis()) {
                calendar.add(Calendar.DAY_OF_YEAR, 1);
            }

            Weightmanager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY * 7, weightpendingIntent);

            SharedPreferences.Editor notifeditor = notifprefsWeight.edit();
            notifeditor.putBoolean("firstTime", true);
            notifeditor.apply();
        }

        /* start Alarm receiver*/
        // Calendar settings for AlarmReceiver
        Calendar cur_cal = new GregorianCalendar();
        cur_cal.setTimeInMillis(System.currentTimeMillis());//set the current time and date for this calendar
        Calendar cal = new GregorianCalendar();
        cal.add(Calendar.DAY_OF_YEAR, cur_cal.get(Calendar.DAY_OF_YEAR));
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 55);
        cal.set(Calendar.SECOND, cur_cal.get(Calendar.SECOND));
        cal.set(Calendar.MILLISECOND, cur_cal.get(Calendar.MILLISECOND));
        cal.set(Calendar.DATE, cur_cal.get(Calendar.DATE));
        cal.set(Calendar.MONTH, cur_cal.get(Calendar.MONTH));


        //int alarmId = 0; /* Dynamically assign alarm ids for multiple alarms */
        Intent alarmIntent = new Intent(this, AlarmReceiver.class);
        //alarmIntent.putExtra("alarmId", alarmId); /* So we can catch the id on BroadcastReceiver */
        AlarmManager alarm = (AlarmManager) getSystemService(ALARM_SERVICE);
        pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), AlarmManager.INTERVAL_FIFTEEN_MINUTES, pendingIntent);
        //animatedCircleLoadingView.startIndeterminate();

        /* stop Alarm receiver*/
        Intent stopIntent = new Intent(this , StopAlarmReceiver.class);
        AlarmManager stopManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        pendingStopIntent = PendingIntent.getBroadcast(this, 0, stopIntent, 0);
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 50) ;
        c.set(Calendar.SECOND, 0);
        stopManager.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), AlarmManager.INTERVAL_FIFTEEN_MINUTES , pendingStopIntent);  //set repeating every 24 hours
        //animatedCircleLoadingView.stopOk();



        /*Facebook receiver*/

        Intent myIntent = new Intent(this , FbReceiver.class);
        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        pendingIntent2 = PendingIntent.getBroadcast(this, 0, myIntent, 0);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 58);
        calendar.set(Calendar.SECOND, 0);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY  , pendingIntent2);  //set repeating every 24 hours

        //StrB = (Button) findViewById(R.id.StartSensingButton);
        //StpB = (Button) findViewById(R.id.StopSensingButton);
        //AppBtn = (Button) findViewById(R.id.AppUsageButton);
        whatsgoin = (Button) findViewById(R.id.whatsgoin);
        whatsgoin.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                launchFirstHint(v);
            }
        });


        // You can pass an ui Context but it is not mandatory getApplicationContext() would also works
        // Be aware if you target android 23, you'll need to handle the runtime-permissions !
        // see http://developer.android.com/reference/android/support/v4/content/ContextCompat.html
        if (    ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        // You need to ask the user to enable the permissions
            Intent gpsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(gpsIntent);
        } else {
            TrackerSettings settings =
                    new TrackerSettings()
                            .setUseGPS(true)
                            .setUseNetwork(true)
                            //.setUsePassive(true)
                            .setTimeBetweenUpdates(30 * 60 * 1000)
                            .setMetersBetweenUpdates(500);

            LocationTracker tracker = new LocationTracker(getApplicationContext(), settings) {

                @Override
                public void onLocationFound(Location location) {
                    // Do some stuff when a new location has been found.
                    // numLocations+=1;
                    //String Desc = String.valueOf(location.describeContents());

                    //Toast.makeText(getApplicationContext(), "NUMBER OF LOCATIONS : ", LENGTH_SHORT).show();
                    Log.d("NUMB LOCATIONS :", String.valueOf(numLocations));

                    longit = location.getLongitude();
                    latit = location.getLatitude();

                    //getAddress(latit,longit);


                    new sendLocation(getApplicationContext()).execute();

                }

                @Override
                public void onTimeout() {

                }

            };

            tracker.startListening();
        }

        //Check if permission enabled
       /* if (AppUsageStats.getUsageStatsList(this).isEmpty()) {
            Intent intentP = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
            startActivity(intentP);
        }*/


    /*    AppBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    UStats.printCurrentUsageStatus(MainActivity.this);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });*/


        if (!SensorEvent.isSensing) {
            SensorEvent.isSensing = true;

            //manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            int interval = 10000;
            alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);
            //Toast.makeText(getApplicationContext(), "Sensing On", LENGTH_SHORT).show();

            ProgressBar progressBar = (ProgressBar)findViewById(R.id.spin_kit);
            //WanderingCubes wanderingCubes = new WanderingCubes();
            DoubleBounce doubleBounce = new DoubleBounce();
            progressBar.setIndeterminateDrawable(doubleBounce);
            //animatedCircleLoadingView.startIndeterminate();
        }

        /*if (!SensorEvent.isSensing) {
            m = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            int interval = 50000;
            m.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingStopIntent);
            Toast.makeText(getApplicationContext(), "Sensing Off", LENGTH_SHORT).show();
        }*/



        /*StrB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!SensorEvent.isSensing) {
                    SensorEvent.isSensing = true;

                    manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    int interval = 10000;
                    manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);
                    Toast.makeText(getApplicationContext(), "Sensing On", LENGTH_SHORT).show();
                }


            }
        });*/


 /*       StpB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SensorEvent.isSensing) {
                    manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    manager.cancel(pendingIntent);
                    Toast.makeText(getApplicationContext(), "Sensing Off", LENGTH_SHORT).show();
                    SensorEvent.isSensing = false;
                }
             *//*   try {
                    AppUsageStats.printCurrentUsageStatus(MainActivity.this);
                } catch (IOException e) {
                    e.printStackTrace();
                }*//*
            }
        });*/


        // Logout button click event
/*        btnLogout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                logoutUser();
                if (SensorEvent.isSensing) {
                    manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    manager.cancel(pendingIntent);
                    Toast.makeText(getApplicationContext(), "Sensing Off", LENGTH_SHORT).show();
                    SensorEvent.isSensing = false;
                }
            }
        });*/

    }

    public class StopAlarm extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {

            if (SensorEvent.isSensing) {

                AlarmManager alarm = (AlarmManager) getSystemService(ALARM_SERVICE);
                Intent alarmIntent = new Intent(context, AlarmReceiver.class);
                pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0);
                alarm.cancel(pendingIntent);
                SensorEvent.isSensing = false;
            }
        }
    }

    public static double getLongit() {
        return longit;
    }

    public static double getLatit() {
        return latit;
    }

    public void getAddress(double lat, double lng) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            Address obj = addresses.get(0);
            String add = obj.getAddressLine(0);
/*            GUIStatics.currentAddress = obj.getSubAdminArea() + ","
                    + obj.getAdminArea();
            GUIStatics.latitude = obj.getLatitude();
            GUIStatics.longitude = obj.getLongitude();
            GUIStatics.currentCity= obj.getSubAdminArea();
            GUIStatics.currentState= obj.getAdminArea();*/
            add = add + "\n" + obj.getCountryName();
            add = add + "\n" + obj.getCountryCode();
            add = add + "\n" + obj.getAdminArea();
            //add = add + "\n" + obj.getPostalCode();
            //add = add + "\n" + obj.getSubAdminArea();
            //add = add + "\n" + obj.getLocality();
            //add = add + "\n" + obj.getSubThoroughfare();

            Log.d("IGA Address", add);
            //Toast.makeText(this, "Address=> " + add, Toast.LENGTH_SHORT).show();

            // TennisAppActivity.showDialog(add);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.d("Location", e.getMessage());
            //Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void maybeShowWelcomeActivity() {
        // FOR TESTING ONLY: resetting the versionCode will FORCE the welcome activity to display every time
        WelcomeHelper.clearLastRunVersionCode(this);

        if (WelcomeHelper.isWelcomeRequired(this)) {
            startActivity(new Intent(MainActivity.this, WelcomeActivity.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
    }

    private void launchAutomaticHint() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                View actionSearchView = findViewById(R.id.login_button);
                if (actionSearchView != null) {
                    SimpleHintContentHolder blockInfo =
                            new SimpleHintContentHolder.Builder(actionSearchView.getContext())
                                    .setContentTitle("Connect to your FB account")
                                    .setContentText("Please connect to your facebook to allow data collection, don't worry Therapio will not access any private informations")
                                    .setTitleStyle(R.style.auto)
                                    .setContentStyle(R.style.content)
                                    .setMarginByResourcesId(R.dimen.activity_vertical_margin,
                                            R.dimen.activity_horizontal_margin,
                                            R.dimen.activity_vertical_margin,
                                            R.dimen.activity_horizontal_margin)
                                    .build();
                    new HintCase(actionSearchView.getRootView())
                            .setTarget(actionSearchView, new CircularShape())
                            .setShapeAnimators(new RevealCircleShapeAnimator(),
                                    new UnrevealCircleShapeAnimator())
                            .setHintBlock(blockInfo)
                            .show();
                }
            }
        }, 500);
    }

    private void launchFirstHint(final View view) {
        SimpleHintContentHolder blockInfo = new SimpleHintContentHolder.Builder(view.getContext())
                .setContentTitle("Welcome to Therapio")
                .setContentText("Therapio will be collecting data on a daily basis to detect the likelihood of developing depression")
                .setTitleStyle(R.style.title)
                .setContentStyle(R.style.content)
                .build();
        new HintCase(view.getRootView())
                .setTarget(findViewById(R.id.textView), new CircularShape(), HintCase.TARGET_IS_NOT_CLICKABLE)
                .setBackgroundColor(getResources().getColor(R.color.yellow_50))
                .setShapeAnimators(new RevealCircleShapeAnimator(), ShapeAnimator.NO_ANIMATOR)
                .setHintBlock(blockInfo, new FadeInContentHolderAnimator(), new SlideOutFromRightContentHolderAnimator())
                .setOnClosedListener(new HintCase.OnClosedListener() {
                    @Override
                    public void onClosed() {
                        launchSecondHint(view);
                    }
                })
                .show();
    }

    private void launchSecondHint(View view) {
        SimpleHintContentHolder blockInfo = new SimpleHintContentHolder.Builder(view.getContext())
                .setContentTitle("Please")
                .setContentText("Do not logout from the app and keep it running whenever possible")
                .setTitleStyle(R.style.title)
                .setContentStyle(R.style.content)
                .build();
        new HintCase(view.getRootView())
                .setTarget(findViewById(R.id.textView), new CircularShape())
                .setBackgroundColor(getResources().getColor(R.color.yellow_50))
                .setShapeAnimators(ShapeAnimator.NO_ANIMATOR, new UnrevealCircleShapeAnimator())
                .setHintBlock(blockInfo, new SlideInFromRightContentHolderAnimator())
                .show();
    }

    private String constructWelcomeMessage(Profile profile) {
        StringBuffer stringBuffer = new StringBuffer();
        if (profile != null) {
            stringBuffer.append("Welcome " + profile.getName());
        }
        return stringBuffer.toString();
    }

    private void saveData() {
        SharedPreferences prefs =
                getSharedPreferences("appName",
                        Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("userid", uID);
        editor.commit();
        //Toast.makeText(this, "saaaaaave"+ uID, Toast.LENGTH_SHORT).show();
    }

    private void loadData() {
        SharedPreferences prefs =
                getSharedPreferences("appName",
                        Context.MODE_PRIVATE);
        uID = prefs.getString("userid", uID);
        //Toast.makeText(this, "Loaaaaaad"+ uID , Toast.LENGTH_SHORT).show();
        ALUSER = uID;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mcallbackManager.onActivityResult(requestCode,resultCode,data);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
        Profile profile = Profile.getCurrentProfile();
        //setButtonText();
        ProgressBar progressBar = (ProgressBar)findViewById(R.id.spin_kit);
        //WanderingCubes wanderingCubes = new WanderingCubes();
        DoubleBounce doubleBounce = new DoubleBounce();
        progressBar.setIndeterminateDrawable(doubleBounce);
        //animatedCircleLoadingView.startIndeterminate();
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveData();
    }

    @Override
    protected void onStop() {
        super.onStop();
        saveData();
        /*mTokenTracker.stopTracking();
        mProfileTracker.stopTracking();*/
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveData();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //saveData();
        loadData();
    }

    public void LaunchWhenNotLoggedIn(Profile profile){
        if (profile == null) {
            launchAutomaticHint();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        LaunchWhenNotLoggedIn(Profile.getCurrentProfile());
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Log.d("action", "setting clicked");
            Intent intent1 = new Intent(this, SettingsActivity.class);
            startActivity(intent1);
            return true;
        } else if (id == R.id.home) {
            Log.d("home", "clicked");
            NavUtils.navigateUpFromSameTask(this);
            return true;
        } else if (id == R.id.action_info) {
            startActivity(new Intent(this, InfoActivity.class));
            return true;
        }
        else if(id == R.id.action_logout)
        {
            Log.d("Action" , "Logout");
            //refresh();
            saveData();
            logoutUser();
        }
        else if(id == R.id.action_questionnaire)
        {
            Log.d("Action" , "Questionnaire");
            Intent intentQ = new Intent(this, Beginners.class);
            startActivity(intentQ);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

/*    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("Do you really want to Quit Therapio?.").setCancelable(
                false).setPositiveButton("Quit",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                        //MainActivity.this.finish();
                        //Intent intent = new Intent(MainActivity.this,MainActivity.class);
                        //startActivity(intent);
                    }
                }).setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
        super.onBackPressed();
    }*/

    private void logoutUser() {
        session.setLogin(false);
        db.deleteUsers();
        // Launching the login activity
        Intent intent = new Intent(MainActivity.this, Activity_Login.class);
        startActivity(intent);
        finish();
    }

}


