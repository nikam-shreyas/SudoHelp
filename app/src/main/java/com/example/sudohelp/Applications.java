package com.example.sudohelp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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

public class Applications extends AppCompatActivity {
    private String currentUserId;
    private Button backout;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mApplicationsAdapter;
    private RecyclerView.LayoutManager mApplicationsLayoutManager;
    private DatabaseReference db1, db2, infoDb;
    private String userType;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applications);
        Intent intent = getIntent();
        backout=(Button) findViewById(R.id.return_back);
        backout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Applications.this,MainActivity.class);
                startActivity(intent);
                finish();
                return;
            }
        });
        userType = intent.getStringExtra("Usertype");
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        db2 = FirebaseDatabase.getInstance().getReference().child("Users").child("Exams");
        infoDb = FirebaseDatabase.getInstance().getReference().child("Users").child("Info");

        db1 = FirebaseDatabase.getInstance().getReference().child("Users").child("Writers").child(currentUserId).child("Applications");
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);
        mApplicationsLayoutManager = new LinearLayoutManager(Applications.this);
        recyclerView.setLayoutManager(mApplicationsLayoutManager);

        mApplicationsAdapter = new ApplicantsAdapter(getDataSetMatches(), Applications.this);
        recyclerView.setAdapter(mApplicationsAdapter);

        getUserApplicantId();

    }

    private void getUserApplicantId() {
        db1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot applicant : dataSnapshot.getChildren()) {
                        db2 = FirebaseDatabase.getInstance().getReference().child("Users").child("Exams");
                        db2 = db2.child(applicant.getKey());
                        db2.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){
                                    String uid = null;
                                    String name = null;
                                    String profileImageUrl = null;
                                    if(dataSnapshot.child("Subject")!=null){
                                        name = dataSnapshot.child("Subject").getValue().toString();
                                    }
                                    if(dataSnapshot.child("Date")!=null){
                                        uid = dataSnapshot.child("Date").getValue().toString();
                                    }
                                    ApplicationsObject obj = new ApplicationsObject(uid,name,profileImageUrl);
                                    resultsApplicants.add(obj);
                                    mApplicationsAdapter.notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void fetchapplicantsInformation(String key) {

    }

    private ArrayList<ApplicationsObject> resultsApplicants = new ArrayList<ApplicationsObject>();

    private List<ApplicationsObject> getDataSetMatches() {
        return resultsApplicants;
    }
}
