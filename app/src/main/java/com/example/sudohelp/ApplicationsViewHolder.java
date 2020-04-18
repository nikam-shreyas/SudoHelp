package com.example.sudohelp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sudohelp.Chat.Chat;

public class ApplicationsViewHolder extends RecyclerView.ViewHolder {
    public TextView applicantId, applicantName;
    public ImageView applicantImage;

    public ApplicationsViewHolder(@NonNull View itemView) {
        super(itemView);
//        itemView.setOnClickListener(this);

        applicantId = (TextView) itemView.findViewById(R.id.applicantId);
        applicantName = (TextView) itemView.findViewById(R.id.applicantName);
        applicantImage = (ImageView) itemView.findViewById(R.id.applicantImage);
    }
//
//    @Override
//    public void onClick(View view) {
//        Intent intent = new Intent(view.getContext(), Chat.class);
//        Bundle b= new Bundle();
//        b.putString("applicantId",applicantId.getText().toString());
//        intent.putExtras(b);
//        view.getContext().startActivity(intent);
//    }
}
