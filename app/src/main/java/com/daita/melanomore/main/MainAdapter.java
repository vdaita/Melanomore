package com.daita.melanomore.main;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.daita.melanomore.R;
import com.daita.melanomore.data.Diagnosis;
import com.daita.melanomore.data.DiagnosisRepository;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder>{

    private List<Diagnosis> mDiagnosisList;
    private DiagnosisRepository diagnosisRepository;
    private MainFilter filter;

    public static final String TAG = "MainAdapter";

    public void setDiagnosisRepository(DiagnosisRepository diagnosisRepository) {
        this.diagnosisRepository = diagnosisRepository;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mItemDate, mItemDiagnosis, mItemNotes;
        public ImageView mItemImage, mDeleteButton;

        public ViewHolder(View view){
            super(view);
            mItemDate = view.findViewById(R.id.itemDate);
            mItemDiagnosis = view.findViewById(R.id.itemDiagnosis);
            mItemNotes = view.findViewById(R.id.itemNotes);
            mItemImage = view.findViewById(R.id.itemImageView);
            mDeleteButton = view.findViewById(R.id.itemDelete);
        }

        public void bindTo(Diagnosis diagnosis){
            mItemDate.setText(diagnosis.getDate());
            mItemDiagnosis.setText(diagnosis.getDiagnosis());
            mItemNotes.setText(diagnosis.getNotes());

            Picasso.get().load(diagnosis.getImageFilePath()).into(mItemImage);

            mDeleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    diagnosisRepository.deleteDiagnosis(diagnosis);
                    mDiagnosisList.remove(diagnosis);
                    notifyDataSetChanged();
                }
            });
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_view, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Diagnosis diagnosis = mDiagnosisList.get(position);
        holder.bindTo(diagnosis);
    }

    @Override
    public int getItemCount() {
        return mDiagnosisList.size();
    }

    public void setDiagnosisList(List<Diagnosis> mDiagnosisList) {
        filter = new MainFilter(mDiagnosisList, this);
        this.mDiagnosisList = mDiagnosisList;
    }

    public void filter(String charText){
        filter.filter(charText);
    }
}
