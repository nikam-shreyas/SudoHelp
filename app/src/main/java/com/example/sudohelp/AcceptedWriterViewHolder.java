package com.example.sudohelp;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class AcceptedWriterViewHolder extends RecyclerView.ViewHolder
{
    TextView exam, writer,date;
    Button chat;

    public AcceptedWriterViewHolder(View view) {
        super(view);
        exam = (TextView) view.findViewById(R.id.exam_name);
        writer = (TextView) view.findViewById(R.id.name_writer);
        date = (TextView) view.findViewById(R.id.date_of_exam);
    }
}
