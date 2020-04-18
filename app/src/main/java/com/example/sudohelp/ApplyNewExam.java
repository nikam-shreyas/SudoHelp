package com.example.sudohelp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class ApplyNewExam extends AppCompatActivity {

    private EditText exam, lang, loc, date;
    private Button apply,back;
    private DatabaseReference userDatabase, examDatabase;
    private FirebaseAuth mAuth;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_new_exam);

        exam = (EditText) findViewById(R.id.subject);
        lang = (EditText) findViewById(R.id.lang);
        loc = (EditText) findViewById(R.id.location);
        date = (EditText) findViewById(R.id.date_of_exam1);
        apply = (Button) findViewById(R.id.apply_form);
        back = (Button) findViewById(R.id.back2);
        final ProgressDialog progressDialog2 = new ProgressDialog(ApplyNewExam.this);

        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getCurrentUser().getUid();
        examDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child("Exams");

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ApplyNewExam.this,Main2Activity.class);
                startActivity(intent);
                finish();
                return;
            }
        });
        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog2.setMessage("Applying...");
                progressDialog2.show();
                String exam1, lang1, loc1, date1;
                exam1 = exam.getText().toString();
                lang1 = lang.getText().toString();
                loc1 = loc.getText().toString();
                date1 = date.getText().toString();

                Map userInfo = new HashMap();
                userInfo.put("Student",userId);
                userInfo.put("Status", "Unallotted");
                userInfo.put("Subject", exam1);
                userInfo.put("Date", date1);
                userInfo.put("Location", loc1);
                userInfo.put("Language", lang1);
                userInfo.put("Writer", "");


                DatabaseReference ref = examDatabase.push();

                ref.updateChildren(userInfo);

                userInfo.remove("Student");

                DatabaseReference mydb = FirebaseDatabase.getInstance().getReference().child("Users").child("Students").child(userId);
                mydb.child("Exams").updateChildren(userInfo);

                finish();
                return;
            }
        });
    }
}
