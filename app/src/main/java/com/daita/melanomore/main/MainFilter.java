package com.daita.melanomore.main;

import android.util.Log;
import android.widget.Filter;

import com.daita.melanomore.data.Diagnosis;

import java.util.ArrayList;
import java.util.List;

public class MainFilter extends Filter {

    private List<Diagnosis> diagnosisList;
    private List<Diagnosis> filteredDiagnosisList;
    private MainAdapter mainAdapter;

    public static final String TAG = "MainFilter";

    public MainFilter(List<Diagnosis> diagnosisList, MainAdapter mainAdapter){
        this.diagnosisList = diagnosisList;
        this.mainAdapter = mainAdapter;
        filteredDiagnosisList = new ArrayList<>();
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        filteredDiagnosisList.clear();
        final FilterResults results = new FilterResults();

        if(constraint.length() == 0) {
            filteredDiagnosisList.addAll(diagnosisList);
            Log.v(TAG, filteredDiagnosisList.toString());
        } else {
            for (final Diagnosis item : diagnosisList){
                if (item.getNotes().toLowerCase().contains(constraint) || item.getDiagnosis().toLowerCase().contains(constraint)){
                    filteredDiagnosisList.add(item);
                }
            }
        }

        results.values = filteredDiagnosisList;
        results.count = filteredDiagnosisList.size();

        return null;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        mainAdapter.setDiagnosisList(filteredDiagnosisList);
        mainAdapter.notifyDataSetChanged();
    }
}
