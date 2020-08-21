package com.csie.ipgeolocation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChartActivity extends AppCompatActivity {

    private ArrayList<String> chartLabels = new ArrayList<>();
    private ArrayList<BarEntry> chartEntries = new ArrayList<>();
    private int searchesCount = 0;
    BarChart barChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SharedPreferences sharedPreferences = getSharedPreferences("CustomPrefs", 0);
        boolean useDarkTheme = sharedPreferences.getBoolean("darkTheme",false);

        if(useDarkTheme){
            setTheme(R.style.AppTheme_Dark);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        barChart = findViewById(R.id.barChart);
        final Database roomDb = Database.getInstance(this);

        DatabaseReference firebaseDb = FirebaseDatabase.getInstance().getReference();
        firebaseDb.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int i = 0;
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    User user = userSnapshot.getValue(User.class);
                    if (user != null) {
                        chartLabels.add(user.getUsername());
                        searchesCount = roomDb.getDAO().getUserIPsCount(user.getUserId());
                        if (searchesCount != 0) {
                            chartEntries.add(new BarEntry(searchesCount,i));

                        }
                        i++;
                    }
                }

                BarDataSet barDataSet = new BarDataSet(chartEntries, "Searches");

                BarData data = new BarData(chartLabels,barDataSet);
                barChart.setData(data);
                barChart.setDescription("No. of searches by user");
                barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                barChart.animateY(2000);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
