package com.example.sudohelp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.FirebaseApiNotAvailableException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Applications2 extends AppCompatActivity {
    private String currentUserId;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mApplicationsAdapter;
    private RecyclerView.LayoutManager mApplicationsLayoutManager;
    private DatabaseReference db1, infoDb;

    private ArrayList<AcceptedWriterProfile> list = new ArrayList<AcceptedWriterProfile>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applications2);
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        infoDb = FirebaseDatabase.getInstance().getReference().child("Users").child("Info");

        db1 = FirebaseDatabase.getInstance().getReference().child("Users").child("Exams");
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView2);
        mApplicationsLayoutManager = new LinearLayoutManager(Applications2.this);
        recyclerView.setLayoutManager(mApplicationsLayoutManager);


        getUserApplicantId();
        mApplicationsAdapter = new AcceptedWriterAdapter(getDataSetMatches(), Applications2.this);
        recyclerView.setAdapter(mApplicationsAdapter);


        ProgressDialog progressDialog = new ProgressDialog(Applications2.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        progressDialog.dismiss();
    }

    private void getUserApplicantId() {
        db1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot applicant : dataSnapshot.getChildren()) {
                        if (applicant.child("Student").getValue().toString().compareTo(currentUserId) == 0) {
                            String exam = null;
                            String writer = null;
                            String date = null;
                            if (dataSnapshot.child(applicant.getKey()).child("Subject") != null)
                                exam = dataSnapshot.child(applicant.getKey()).child("Subject").getValue().toString();
                            if (dataSnapshot.child(applicant.getKey()).child("Writer") != null)
                                writer = dataSnapshot.child(applicant.getKey()).child("Writer").getValue().toString();
                            else
                                continue;
                            if (dataSnapshot.child(applicant.getKey()).child("Date") != null)
                                date = dataSnapshot.child(applicant.getKey()).child("Date").getValue().toString();
                            AcceptedWriterProfile obj = new AcceptedWriterProfile(exam, writer, date);
                            list.add(obj);
                            mApplicationsAdapter.notifyDataSetChanged();
                        }
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Applications2.this, "Error updating", Toast.LENGTH_SHORT);
            }
        });
    }

    private void fetchapplicantsInformation(String key) {
        infoDb = infoDb.child(key);
        infoDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String exam = null;
                    String writer = null;
                    String date = null;
                    if(dataSnapshot.child("Name")!=null){
                        exam = dataSnapshot.child("Name").getValue().toString();
                    }
                    if(dataSnapshot.child("Email Id")!=null){
                        writer = dataSnapshot.child("Name").getValue().toString();
                    }
                    if(dataSnapshot.child("Usertype")!=null){
                        date = dataSnapshot.child("Name").getValue().toString();
                    }
                    AcceptedWriterProfile obj = new AcceptedWriterProfile(exam,writer,date);
                    list.add(obj);
                    mApplicationsAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private List<AcceptedWriterProfile> getDataSetMatches() {
        return list;
    }
}
