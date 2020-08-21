package com.csie.ipgeolocation;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class FeedbackActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SharedPreferences sharedPreferences = getSharedPreferences("CustomPrefs", 0);
        boolean useDarkTheme = sharedPreferences.getBoolean("darkTheme",false);

        if(useDarkTheme){
            setTheme(R.style.AppTheme_Dark);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        final String email = sharedPreferences.getString("devEmail",null);

        TextView tv_Destination=findViewById(R.id.tv_Destination);
        tv_Destination.append(email);

        Button btnSubmit=findViewById(R.id.button_Submit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String subject=((EditText)findViewById(R.id.et_Subject)).getText().toString();
                String message=((EditText)findViewById(R.id.et_Message)).getText().toString();
                RatingBar ratingBar=findViewById(R.id.rb_Rating);
                float ratingValue=ratingBar.getRating();
                String rating=("Rating: "+ratingValue);
                String [] to={email};

                Intent email=new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"));
                email.putExtra(Intent.EXTRA_EMAIL,to);
                email.putExtra(Intent.EXTRA_SUBJECT,subject);
                email.putExtra(Intent.EXTRA_TEXT,(message+"\n\n"+rating));
                try {
                    startActivity(Intent.createChooser(email,"Send email using:"));
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
