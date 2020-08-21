package com.csie.ipgeolocation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class IPsReportActivity extends AppCompatActivity {

    ArrayList<IP> searchedIPsList= new ArrayList<>();
    public ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SharedPreferences sharedPreferences = getSharedPreferences("CustomPrefs", 0);
        boolean useDarkTheme = sharedPreferences.getBoolean("darkTheme",false);

        if(useDarkTheme){
            setTheme(R.style.AppTheme_Dark);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ips_report);

        Database database = Database.getInstance(this);

        searchedIPsList = (ArrayList<IP>) database.getDAO().getAllIPs();

        IPListAdapter ipListAdapter = new IPListAdapter(this,R.layout.adapter_history_layout,searchedIPsList);

        listView = findViewById(R.id.lv_IPs);
        listView.setAdapter(ipListAdapter);
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
                saveTxtReport("IPs.txt",searchedIPsList);
                Toast.makeText(this,"Created .txt report",Toast.LENGTH_SHORT).show();
                break;
            case 2:
                saveDataReport("IPs.dat",searchedIPsList);
                Toast.makeText(this,"Created .dat report",Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void saveTxtReport(String fileName , ArrayList<IP> ips){
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(this.openFileOutput(fileName,MODE_PRIVATE));

            for (IP ip:ips) {
                outputStreamWriter.write(ip.getIpId()+" ");
                outputStreamWriter.write(ip.getIpAddress()+" ");
                outputStreamWriter.write(ip.getCountry_Name()+ " ");
                outputStreamWriter.write(ip.getCity()+" ");
                outputStreamWriter.write(ip.getSearched_Date()+" ");
                outputStreamWriter.write(ip.getUserCreatorId());
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
