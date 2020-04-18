package com.example.sudohelp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class AcceptedWriterAdapter extends RecyclerView.Adapter<AcceptedWriterViewHolder> {
    private List<AcceptedWriterProfile> applicationsList;
    private Context context;

    public AcceptedWriterAdapter(List<AcceptedWriterProfile> applicationsList, Context context) {
        this.applicationsList = applicationsList;
        this.context = context;
    }

    @NonNull
    @Override
    public AcceptedWriterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(lp);
        AcceptedWriterViewHolder rcv = new AcceptedWriterViewHolder(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(@NonNull AcceptedWriterViewHolder holder, int position) {
            holder.exam.setText(applicationsList.get(position).getExam());
            holder.writer.setText(applicationsList.get(position).getWriter());
            holder.date.setText(applicationsList.get(position).getDate());
//        if(applicationsList.get(position).getProfileImageUrl()!= null)
//            Glide.with(context).load(applicationsList.get(position).getProfileImageUrl()).into(holder.applicantImage);


    }

    @Override
    public int getItemCount() {
        if(applicationsList!=null)
            return applicationsList.size();
        return 0;
    }
}
