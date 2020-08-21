package com.csie.ipgeolocation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class UsersReportActivity extends AppCompatActivity {

    private String currentUserId;
    private List<User> firebaseUsers = new ArrayList<>();
    private long usersCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SharedPreferences sharedPreferences = getSharedPreferences("CustomPrefs", 0);
        boolean useDarkTheme = sharedPreferences.getBoolean("darkTheme",false);

        if(useDarkTheme){
            setTheme(R.style.AppTheme_Dark);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        currentUserId = getIntent().getStringExtra("currentUserId");

        final TextView tv_usersCount=findViewById(R.id.usersCount);
        DatabaseReference firebaseDb = FirebaseDatabase.getInstance().getReference();

        firebaseDb.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usersCount = dataSnapshot.getChildrenCount();
                tv_usersCount.append(" "+usersCount);

                for(DataSnapshot userDataSnapshot : dataSnapshot.getChildren()) {
                    User user = userDataSnapshot.getValue(User.class);
                    firebaseUsers.add(user);
                }

                final ArrayAdapter<User> firebaseUsersArrayAdapter = new ArrayAdapter<User>(getApplicationContext(),android.R.layout.simple_list_item_1,firebaseUsers){
                    @NonNull
                    @Override
                    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                        View row = super.getView(position, convertView, parent);

                        if(getItem(position).getUserId().equals(currentUserId)){
                            row.setBackgroundColor(Color.parseColor("#467F68"));
                        }
                        return row;
                    }
                };

                ListView listViewFirebase = findViewById(R.id.lv_UsersFirebase);
                listViewFirebase.setAdapter(firebaseUsersArrayAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0,1,1,"Save .txt report");
        menu.add(0,2,2,"Save .dat report");

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case 1:
                saveTxtReport("users.txt",firebaseUsers);
                Toast.makeText(this,"Created .txt report",Toast.LENGTH_SHORT).show();
                break;
            case 2:
                saveDataReport("users.dat",firebaseUsers);
                Toast.makeText(this,"Created .dat report",Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void saveTxtReport(String fileName , List<User> users){
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(this.openFileOutput(fileName,MODE_PRIVATE));

            outputStreamWriter.write("Total no. of user: "+usersCount);
            outputStreamWriter.write("\n");
            for (User user:users) {
                outputStreamWriter.write(user.getUserId()+ " ");
                outputStreamWriter.write(user.getUsername()+ " ");
                outputStreamWriter.write("\n");
            }

            outputStreamWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveDataReport(String fileName,List<User> users){
        try {
            FileOutputStream file = openFileOutput(fileName,MODE_PRIVATE);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(file);

            objectOutputStream.writeObject(users);
            objectOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
