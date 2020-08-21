package com.csie.ipgeolocation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    ArrayList<IP> searchedIPsList= new ArrayList<>();
    private String currentUserId;
    public ListView listView;
    Database database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SharedPreferences sharedPreferences = getSharedPreferences("CustomPrefs", 0);
        boolean useDarkTheme = sharedPreferences.getBoolean("darkTheme",false);

        if(useDarkTheme){
            setTheme(R.style.AppTheme_Dark);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        database = Database.getInstance(this);

        currentUserId=getIntent().getStringExtra("currentUserId");

        listView = findViewById(R.id.history_ListView);
        searchedIPsList= (ArrayList<IP>) database.getDAO().getUserIPs(currentUserId);
        IPListAdapter ipListAdapter = new IPListAdapter(this,R.layout.adapter_history_layout,searchedIPsList);
        listView.setAdapter(ipListAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0,1,1,"Clear history");
        menu.add(0,2,2,"Save .txt report");
        menu.add(0,3,3,"Save .dat report");

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case 1:
                database.getDAO().deleteAllIPs(currentUserId);
                searchedIPsList.clear();
                listView.invalidateViews();
                Toast.makeText(this,"History deleted for the current user",Toast.LENGTH_SHORT).show();
                break;
            case 2:
                saveTxtReport("historyUser"+currentUserId+".txt",searchedIPsList);
                Toast.makeText(this,"Created .txt report for the user with id "+currentUserId,Toast.LENGTH_SHORT).show();
                break;
            case 3:
                saveDataReport("historyUser"+currentUserId+".dat",searchedIPsList);
                Toast.makeText(this,"Created .dat report for the user with id "+currentUserId,Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void saveTxtReport(String fileName ,ArrayList<IP> ips){
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(this.openFileOutput(fileName,MODE_PRIVATE));

            for (IP ip:ips) {
                outputStreamWriter.write(ip.getIpId() + " ");
                outputStreamWriter.write(ip.getType() + " ");
                outputStreamWriter.write(ip.getContinent_Name() + " ");
                outputStreamWriter.write(ip.getCountry_Name() + " ");
                outputStreamWriter.write(ip.getCity() + " ");
                outputStreamWriter.write(ip.getLatitude().toString() + " ");
                outputStreamWriter.write(ip.getLongitude().toString());
                outputStreamWriter.write("\n");
            }

            outputStreamWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveDataReport(String fileName,ArrayList<IP> ips){
        try {
            FileOutputStream file = openFileOutput(fileName,MODE_PRIVATE);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(file);

            objectOutputStream.writeObject(ips);
            objectOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
