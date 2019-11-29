package com.daita.melanomore.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Diagnosis {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String diagnosis;

    @ColumnInfo(name = "image_file_path")
    private String imageFilePath;

    private String date;

    private String notes;

    public Diagnosis(String diagnosis, String imageFilePath, String date, String notes) {
        this.diagnosis = diagnosis;
        this.imageFilePath = imageFilePath;
        this.date = date;
        this.notes = notes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getImageFilePath() {
        return imageFilePath;
    }

    public void setImageFilePath(String imageFilePath) {
        this.imageFilePath = imageFilePath;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
