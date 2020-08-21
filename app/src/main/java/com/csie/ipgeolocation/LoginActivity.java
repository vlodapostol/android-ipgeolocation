package com.csie.ipgeolocation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private String username;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SharedPreferences sharedPreferences = getSharedPreferences("CustomPrefs", 0);
        boolean useDarkTheme = sharedPreferences.getBoolean("darkTheme",false);

        if(useDarkTheme){
            setTheme(R.style.AppTheme_Dark);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText et_Username = findViewById(R.id.et_Username);
        final EditText et_Password = findViewById(R.id.et_Password);
        Button btn_Login = findViewById(R.id.btn_Login);
        Button btn_Register = findViewById(R.id.btn_Register);

        final DatabaseReference firebaseDb = FirebaseDatabase.getInstance().getReference();

        btn_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = et_Username.getText().toString();
                firebaseDb.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        User user = new User();

                        for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                            if (userSnapshot.child("username").getValue().equals(username)) {
                                user = userSnapshot.getValue(User.class);
                            }
                        }

                        if (user.getUsername() != null) {
                            if (user.getPassword().equals(et_Password.getText().toString())) {
                                Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
                                mainIntent.putExtra("currentUserId", user.getUserId());
                                startActivity(mainIntent);
                            } else {
                                Toast.makeText(getApplicationContext(), "Incorrect password!", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "This user doesn't exist. Please register!", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        btn_Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = et_Username.getText().toString();
                password = et_Password.getText().toString();
                if (!(username.isEmpty() || password.isEmpty())) {

                    firebaseDb.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            User user = new User();

                            for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                                if (userSnapshot.child("username").getValue().equals(username)) {
                                    user = userSnapshot.getValue(User.class);
                                }
                            }
                            if (user.getUsername() == null) {
                                User newUser = new User(username, password);
                                writeUserToFirebase(newUser);
                                Toast.makeText(getApplicationContext(), "Registered!", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "That user already exists!", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), "Username or password field cannot be empty.",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void writeUserToFirebase(User user) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference firebaseDb = firebaseDatabase.getReference();
        String userId = firebaseDb.child("users").push().getKey();
        user.setUserId(userId);
        firebaseDb.child("users").child(userId).setValue(user);
    }
}


