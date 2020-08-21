package com.csie.ipgeolocation;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class IPListAdapter extends ArrayAdapter<IP> {

    private Context _context;
    private int layout_Resource;

    public IPListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<IP> objects) {
        super(context, resource, objects);
        _context = context;
        layout_Resource=resource;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        final String ipAddress = getItem(position).getIpAddress();
        String country_Name = getItem(position).getCountry_Name();
        String country_Flag_Emoji = getItem(position).getCountry_FlagEmoji();
        String searched_Date = getItem(position).getSearched_Date();

        final Database database = Database.getInstance(_context);

        LayoutInflater inflater = LayoutInflater.from(_context);
        convertView = inflater.inflate(layout_Resource, parent, false);

        TextView tv_ipAddress = convertView.findViewById(R.id.ipAddress);
        final TextView tv_Country_Name = convertView.findViewById(R.id.country);
        TextView tv_Date = convertView.findViewById(R.id.date);

        tv_ipAddress.setText(ipAddress);
        tv_Date.setText(searched_Date);
        tv_Country_Name.setText(country_Name + " " + country_Flag_Emoji);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),LookupActivity.class);
                intent.putExtra("ipAddress",ipAddress);
                _context.startActivity(intent);
            }
        });

        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(_context);
                builder.setTitle("Modify country name");

                final EditText et_countryName = new EditText(_context);

                et_countryName.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(et_countryName);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newCountryName = et_countryName.getText().toString();

                        getItem(position).setCountry_Name(et_countryName.getText().toString());
                        tv_Country_Name.setText(newCountryName);
                        database.getDAO().updateIp(getItem(position).getIpId(),newCountryName);
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();

                return true;
            }
        });

        return convertView;
    }
}
