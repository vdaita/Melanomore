package com.daita.melanomore.main;

import android.content.Intent;
import android.os.Bundle;

import com.daita.melanomore.data.Diagnosis;
import com.daita.melanomore.diagnose.DiagnoseActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.daita.melanomore.R;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton mFab;
    RecyclerView mRecyclerView;
    MainViewModel mViewModel;
    MainAdapter mAdapter;
    EditText mSearchEditText;
    List<Diagnosis> mDiagnosisList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mViewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        mSearchEditText = findViewById(R.id.searchBar);

        mFab = findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, DiagnoseActivity.class);
                startActivity(intent);
            }
        });

        mAdapter = new MainAdapter();

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        mDiagnosisList = mViewModel.getRepository().getDiagnoses();
        mAdapter.setDiagnosisList(mDiagnosisList);
        mAdapter.setDiagnosisRepository(mViewModel.getRepository());
        mAdapter.notifyDataSetChanged();

        // Still need to properly enable search in the recyclerview.
        mSearchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mAdapter.filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}
