package com.csie.ipgeolocation;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface DAO {

    @Insert
    public void insertIP(IP ip);

    @Insert
    public void insertIPs(List<IP> ips);

    @Query("SELECT * FROM ip")
    public List<IP> getAllIPs();

    @Query("SELECT * FROM ip WHERE userCreatorId = :userCreatorId")
    public List<IP> getUserIPs(String userCreatorId);

    @Query("SELECT count(*) FROM ip WHERE userCreatorId = :userCreatorId")
    public int getUserIPsCount(String userCreatorId);

    @Query("DELETE FROM ip WHERE userCreatorId=:userCreatorId")
    public void deleteAllIPs(String userCreatorId);

    @Delete
    public void deleteIP(IP ip);

    @Query("UPDATE ip SET country_Name=:newCountryName WHERE ipId=:ipId")
    public void updateIp(int ipId, String newCountryName);
}
