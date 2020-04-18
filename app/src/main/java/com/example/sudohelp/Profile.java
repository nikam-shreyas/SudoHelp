package com.example.sudohelp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class Profile extends AppCompatActivity {
    private TextView name;
    private TextView phoneNo;
    private TextView emailId;
    private TextView dateOfBirth;
    private String userId;
    private Button update, back;
    private ImageView profileImage;
    private FirebaseAuth mAuth;
    private DatabaseReference userDatabase;
    private Uri resultUri;
    private String profileImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        name = (TextView) findViewById(R.id.name2);
        phoneNo = (TextView) findViewById(R.id.phoneNo);
        emailId = (TextView) findViewById(R.id.emailId);
        dateOfBirth = (TextView) findViewById(R.id.dateOfBirth);

        profileImage = (ImageView) findViewById(R.id.profileImage);

        update = (Button) findViewById(R.id.update);
        back = (Button) findViewById(R.id.back);

        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getCurrentUser().getUid();
        userDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child("Info").child(userId);
        getUserInfo();
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent up = new Intent(Profile.this, UpdateProfile.class);
                startActivity(up);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                return;
            }
        });
    }

    private void getUserInfo() {
        ProgressDialog progressDialog = new ProgressDialog(Profile.this);
        progressDialog.setMessage("Getting Info...");
        progressDialog.show();
        userDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    if (map.get("Name") != null)
                        name.setText(map.get("Name").toString());
                    if (map.get("Phone No") != null)
                        phoneNo.setText(map.get("Phone No").toString());
                    if (map.get("Email Id") != null)
                        emailId.setText(map.get("Email Id").toString());
                    if (map.get("Date of Birth") != null)
                        dateOfBirth.setText(map.get("Date of Birth").toString());
                    if (map.get("profileImageUrl") != null) {
                        profileImageUri = map.get("profileImageUrl").toString();
                        Glide.with(getApplication()).load(profileImageUri).into(profileImage);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        progressDialog.dismiss();
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            final Uri imageUri = data.getData();
            resultUri = imageUri;
            profileImage.setImageURI(resultUri);


        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUserInfo();

    }
}
