package com.csie.ipgeolocation;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "ip")
public class IP implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int ipId;

    private String userCreatorId;

    private String ipAddress="";
    private String type="";
    private String continent_Name="";
    private String country_Name="";
    private String country_Code="";
    private String region_Name="";
    private String city="";
    private String zip="";
    private Double latitude;
    private Double longitude;
    private String country_FlagEmoji="";
    private String searched_Date="";
    private boolean is_Eu;

    @Ignore
    public IP() {
        Date date= Calendar.getInstance().getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy - hh:mm:ss");
        searched_Date=dateFormat.format(date);
    }

    public IP(String type, String continent_Name, String country_Name, String country_Code, String region_Name, String city, String zip, Double latitude, Double longitude, String country_FlagEmoji) {
        this.type = type;
        this.continent_Name = continent_Name;
        this.country_Name = country_Name;
        this.country_Code = country_Code;
        this.region_Name = region_Name;
        this.city = city;
        this.zip = zip;
        this.latitude = latitude;
        this.longitude = longitude;
        this.country_FlagEmoji = country_FlagEmoji;
    }

    public IP(String ipAddress, String country_name, String searched_date, String country_flag_emoji) {
        this.ipAddress=ipAddress;
        this.country_Name=country_name;
        this.searched_Date=searched_date;
        this.country_FlagEmoji=country_flag_emoji;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContinent_Name() {
        return continent_Name;
    }

    public void setContinent_Name(String continent_Name) {
        this.continent_Name = continent_Name;
    }

    public String getCountry_Name() {
        return country_Name;
    }

    public void setCountry_Name(String country_Name) {
        this.country_Name = country_Name;
    }

    public String getCountry_Code() {
        return country_Code;
    }

    public void setCountry_Code(String country_Code) {
        this.country_Code = country_Code;
    }

    public String getRegion_Name() {
        return region_Name;
    }

    public void setRegion_Name(String region_Name) {
        this.region_Name = region_Name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getCountry_FlagEmoji() {
        return country_FlagEmoji;
    }

    public void setCountry_FlagEmoji(String country_FlagEmoji) {
        this.country_FlagEmoji = country_FlagEmoji;
    }

    public String getSearched_Date() {
        return searched_Date;
    }

    public void setSearched_Date(String searched_Date){
        this.searched_Date=searched_Date;
    }

    public int getIpId() {
        return ipId;
    }

    public void setIpId(int ipId) {
        this.ipId = ipId;
    }

    public String getUserCreatorId() {
        return userCreatorId;
    }

    public void setUserCreatorId(String userCreatorId) {
        this.userCreatorId = userCreatorId;
    }

    public boolean isIs_Eu() {
        return is_Eu;
    }

    public void setIs_Eu(boolean is_Eu) {
        this.is_Eu = is_Eu;
    }
}
