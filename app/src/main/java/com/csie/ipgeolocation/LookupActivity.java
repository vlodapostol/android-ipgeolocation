package com.csie.ipgeolocation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class LookupActivity extends AppCompatActivity {

    private String apiKey = "your-ipstack-key";
    private String apiURL = "http://api.ipstack.com/";

    private String currentUserId;
    private IP returnedIP;

    TextView tv_Continent, tv_Country, tv_Country_Code, tv_Region, tv_City, tv_Zipcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SharedPreferences sharedPreferences = getSharedPreferences("CustomPrefs", 0);
        boolean useDarkTheme = sharedPreferences.getBoolean("darkTheme",false);

        if(useDarkTheme){
            setTheme(R.style.AppTheme_Dark);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lookup);

        tv_Continent = findViewById(R.id.tv_Continent);
        tv_Country = findViewById(R.id.tv_Country);
        tv_Country_Code = findViewById(R.id.tv_CountryCode);
        tv_Region = findViewById(R.id.tv_Region);
        tv_City = findViewById(R.id.tv_City);
        tv_Zipcode = findViewById(R.id.tv_ZipCode);

        final String ipAddress = getIntent().getStringExtra("ipAddress");
        currentUserId = getIntent().getStringExtra("currentUserId");

        final TextView tv_IP = findViewById(R.id.tv_IP);
        tv_IP.append(ipAddress);

        String apiRequestURL = apiURL + ipAddress + "?access_key=" + apiKey;
        Log.v("apiurl", apiRequestURL);

        @SuppressLint("StaticFieldLeak") final LookupIP lookupIP = new LookupIP() {
            @Override
            protected void onPostExecute(String s) {

                if (ip.getType().equals("null") || ip.getType().equals("ipv6") || ip.getContinent_Name().equals("null")) {
                    Toast.makeText(getApplicationContext(), "Please introduce a valid IPv4 address.", Toast.LENGTH_LONG).show();
                    findViewById(R.id.progressBarLayout).setVisibility(View.GONE);
                } else {
                    Bundle bundle = new Bundle();
                    if(ip.getLatitude()!=null && ip.getLongitude()!=null) {
                        bundle.putDouble("latitude", ip.getLatitude());
                        bundle.putDouble("longitude", ip.getLongitude());
                    }
                    MapFragment mapFragment = new MapFragment();
                    mapFragment.setArguments(bundle);
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragmentContainer, mapFragment).commit();

                    tv_Continent.setText(ip.getContinent_Name());
                    tv_Country.setText(" " + ip.getCountry_Name() + " " + ip.getCountry_FlagEmoji());
                    tv_Country_Code.setText(ip.getCountry_Code());
                    tv_Region.setText(ip.getRegion_Name());
                    tv_City.setText(ip.getCity());
                    tv_Zipcode.setText(ip.getZip());

                    tv_IP.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.eu_flag,0);
/*                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }*/
                    findViewById(R.id.progressBarLayout).setVisibility(View.GONE);

                    returnedIP = ip;
                    returnedIP.setUserCreatorId(currentUserId);
                    Database database = Database.getInstance(getApplicationContext());
                    database.getDAO().insertIP(returnedIP);
                }
            }
        };

        try {
            lookupIP.execute(new URL(apiRequestURL));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }

//    @Override
//    public void onBackPressed() {
//        if (returnedIP != null) {
//            Intent intent = new Intent();
//            intent.putExtra("returnedIPAddress", returnedIP);
//            setResult(RESULT_OK, intent);
//            finish();
//        }
//        else{
//            finish();
//        }
//    }
}