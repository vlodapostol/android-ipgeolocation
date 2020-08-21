package com.csie.ipgeolocation;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;


public class MainActivity extends AppCompatActivity {

    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SharedPreferences sharedPreferences = getSharedPreferences("CustomPrefs", 0);
        if (!sharedPreferences.contains("devEmail")) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("devEmail", "apostolvlad17@stud.ase.ro");
            editor.apply();
        }
        boolean useDarkTheme = sharedPreferences.getBoolean("darkTheme",false);

        if(useDarkTheme){
            setTheme(R.style.AppTheme_Dark);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        final EditText et_IP = findViewById(R.id.et_IP);

        @SuppressLint("StaticFieldLeak") FindCurrentIP findCurrentIP = new FindCurrentIP() {
            @Override
            protected void onPostExecute(Void aVoid) {
                String result = getResult();
                et_IP.setText(result);
            }
        };
        try {
            findCurrentIP.execute(new URL("http://ipv4bot.whatismyipaddress.com/"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        currentUserId = getIntent().getStringExtra("currentUserId");

        Button btnLookup = findViewById(R.id.button_Lookup);
        btnLookup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Patterns.IP_ADDRESS.matcher(et_IP.getText().toString()).matches()) {
                    Intent lookupIntent = new Intent(MainActivity.this, LookupActivity.class);
                    lookupIntent.putExtra("ipAddress", et_IP.getText().toString());
                    lookupIntent.putExtra("currentUserId", currentUserId);
                    startActivity(lookupIntent);
                } else {
                    Toast.makeText(getApplicationContext(), "Please provide a valid IPv4 address.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        Button btnHistory = findViewById(R.id.button_History);
        btnHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent historyIntent = new Intent(MainActivity.this, HistoryActivity.class);
                historyIntent.putExtra("currentUserId", currentUserId);
                startActivity(historyIntent);
            }
        });

        Button btnFeedback = findViewById(R.id.button_Feedback);
        btnFeedback.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, FeedbackActivity.class));
            }
        });

        Button btnStatistics = findViewById(R.id.button_Statistics);
        btnStatistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chartIntent = new Intent(getApplicationContext(),ChartActivity.class);
                startActivity(chartIntent);
            }
        });

        Switch switchButton = findViewById(R.id.switchTheme);
        switchButton.setChecked(useDarkTheme);
        switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                switchTheme(isChecked);
            }
        });

    }

    private void switchTheme (boolean darkTheme){
        SharedPreferences.Editor editor = getSharedPreferences("CustomPrefs",0).edit();
        editor.putBoolean("darkTheme",darkTheme);
        editor.apply();

        Intent intent = getIntent();
        finish();

        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.add(0,1,1,"Users");
        menu.add(0,2,2,"IP Addresses");
        menu.add(0,3,3,"Change background");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.info:
                FragmentManager fragmentManager = getSupportFragmentManager();
                InfoFragment infoFragment = new InfoFragment();
                infoFragment.show(fragmentManager, null);
                break;
            case 1:
                Intent usersReportIntent = new Intent(this, UsersReportActivity.class);
                usersReportIntent.putExtra("currentUserId",currentUserId);
                startActivity(usersReportIntent);
                break;
            case 2:
                Intent ipsReportIntent = new Intent(this,IPsReportActivity.class);
                startActivity(ipsReportIntent);
                break;
            case 3:
                Intent galleryIntent = new Intent(Intent.ACTION_PICK);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, 2);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
                try {
                    Uri imageUri = data.getData();
                    InputStream inputStream = getContentResolver().openInputStream(imageUri);
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    Drawable selectedImage = new BitmapDrawable(getResources(),bitmap);

                    ConstraintLayout constraintLayout = findViewById(R.id.mainLayout);
                    constraintLayout.setBackground(selectedImage);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
