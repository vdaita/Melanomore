package com.daita.melanomore.data;

import android.app.Application;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class DiagnosisRepository {

    private DiagnosisDao diagnosisDao;
    private final ThreadPoolExecutor mThreadPoolExecutor = new ThreadPoolExecutor(2, 2, 0, TimeUnit.MILLISECONDS, new LinkedBlockingDeque<Runnable>());

    public DiagnosisRepository(Application application){
        DiagnosisDatabase diagnosisDatabase = DiagnosisDatabase.getInstance(application);
        diagnosisDao = diagnosisDatabase.diagnosisDao();
    }

    public List<Diagnosis> getDiagnoses(){
        try {
            return (mThreadPoolExecutor.submit(new Callable<List<Diagnosis>>(){

                @Override
                public List<Diagnosis> call() throws Exception {
                    return diagnosisDao.getAll();
                }
            })).get();
        } catch (ExecutionException e){
            e.printStackTrace();;
            return null;
        } catch (InterruptedException e){
            e.printStackTrace();
            return null;
        }
    }

    public void insertDiagnosis(final Diagnosis diagnosis){
        mThreadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                diagnosisDao.insert(diagnosis);
            }
        });
    }

    public void deleteDiagnosis(final Diagnosis diagnosis){
        mThreadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                diagnosisDao.delete(diagnosis);
            }
        });
    }

}
