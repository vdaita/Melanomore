package com.daita.melanomore.diagnose;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.daita.melanomore.data.DiagnosisRepository;

public class DiagnoseViewModel extends AndroidViewModel {

    private DiagnosisRepository mRepository;

    public DiagnoseViewModel(@NonNull Application application) {
        super(application);
        mRepository = new DiagnosisRepository(application);
    }

    public DiagnosisRepository getRepository(){
        return mRepository;
    }
}
