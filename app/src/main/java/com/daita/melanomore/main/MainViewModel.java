package com.daita.melanomore.main;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.daita.melanomore.data.DiagnosisRepository;

public class MainViewModel extends AndroidViewModel {

    private DiagnosisRepository diagnosisRepository;

    public MainViewModel(@NonNull Application application) {
        super(application);
        diagnosisRepository = new DiagnosisRepository(application);
    }

    public DiagnosisRepository getRepository(){
        return diagnosisRepository;
    }
}
