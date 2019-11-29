package com.daita.melanomore.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Diagnosis.class}, version=1)
public abstract class DiagnosisDatabase extends RoomDatabase {

    private static DiagnosisDatabase INSTANCE;

    public abstract DiagnosisDao diagnosisDao();

    public static DiagnosisDatabase getInstance(final Context context){
        if (INSTANCE == null){
            synchronized (DiagnosisDatabase.class) {
                if (INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), DiagnosisDatabase.class, "diagnosis_database").fallbackToDestructiveMigration().build();
                }
            }
        }
        return INSTANCE;
    }

}
