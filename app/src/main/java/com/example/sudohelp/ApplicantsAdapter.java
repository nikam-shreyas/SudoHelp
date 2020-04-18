package com.example.sudohelp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ApplicantsAdapter extends RecyclerView.Adapter<ApplicationsViewHolder> {
    private List<ApplicationsObject> applicationsList;
    private Context context;

    public ApplicantsAdapter(List<ApplicationsObject> applicationsList, Context context) {
        this.applicationsList = applicationsList;
        this.context = context;
    }

    @NonNull
    @Override
    public ApplicationsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.applicationsitem, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(lp);
        ApplicationsViewHolder rcv = new ApplicationsViewHolder(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(@NonNull ApplicationsViewHolder holder, int position) {
        holder.applicantId.setText(applicationsList.get(position).getUserId());
        holder.applicantName.setText(applicationsList.get(position).getName());
        if(applicationsList.get(position).getProfileImageUrl()!= null)
            Glide.with(context).load(applicationsList.get(position).getProfileImageUrl()).into(holder.applicantImage);


    }

    @Override
    public int getItemCount() {
        return applicationsList.size();
    }
}