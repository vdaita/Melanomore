package com.daita.melanomore.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DiagnosisDao {

    @Insert
    void insert(Diagnosis diagnosis);

    @Delete
    void delete(Diagnosis diagnosis);

    @Query("SELECT * FROM diagnosis ORDER BY id")
    List<Diagnosis> getAll();

}
