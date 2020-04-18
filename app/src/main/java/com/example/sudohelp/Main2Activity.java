package com.example.sudohelp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class Main2Activity extends AppCompatActivity {
    private Button mprofile, mApplications, msignout, mApply;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        mAuth = FirebaseAuth.getInstance();
        mprofile = (Button) findViewById(R.id.profile2);
        mApplications = (Button) findViewById(R.id.applications2);
        msignout = (Button) findViewById(R.id.signout2);
        mApply = (Button) findViewById(R.id.apply2);

        msignout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                Intent intent = new Intent(Main2Activity.this, LoginOrRegister.class);
                startActivity(intent);
                finish();
                return;
            }
        });

        mprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Main2Activity.this, Profile.class);
                startActivity(intent);
                return;
            }
        });

        mApplications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Main2Activity.this, Applications2.class);
                startActivity(intent);
                return;
            }
        });

        mApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Main2Activity.this, ApplyNewExam.class);
                startActivity(intent);
                return;
            }
        });
    }
}
