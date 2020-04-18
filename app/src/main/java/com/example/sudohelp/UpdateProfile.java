package com.example.sudohelp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UpdateProfile extends AppCompatActivity {
    private EditText name;
    private EditText phoneNo;
    private EditText emailId;
    private EditText dateOfBirth;
    private String userId;
    private Button confirm, back;
    private ImageView profileImage;
    private FirebaseAuth mAuth;
    private DatabaseReference userDatabase;
    private Uri resultUri;
    private String profileImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        name = (EditText) findViewById(R.id.name22);
        phoneNo = (EditText) findViewById(R.id.phoneNo2);
        emailId = (EditText) findViewById(R.id.emailId2);
        dateOfBirth = (EditText) findViewById(R.id.dateOfBirth2);

        profileImage = (ImageView) findViewById(R.id.profileImage2);

        confirm = (Button) findViewById(R.id.confirm2);
        back = (Button) findViewById(R.id.back2);

        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getCurrentUser().getUid();
        userDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child("Info").child(userId);
        getUserInfo();
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveUserInfo();
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
        ProgressDialog progressDialog = new ProgressDialog(UpdateProfile.this);
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

    private void saveUserInfo() {
        final ProgressDialog progressDialog = new ProgressDialog(UpdateProfile.this);
        progressDialog.setMessage("Updating...");
        progressDialog.show();
        String nameval = name.getText().toString();
        String phone = phoneNo.getText().toString();
        String email = emailId.getText().toString();
        String dob = dateOfBirth.getText().toString();

        Map userInfo = new HashMap();
        userInfo.put("Name", nameval);
        userInfo.put("Phone No", phone);
        userInfo.put("Email Id", email);
        userInfo.put("Date of Birth", dob);
        userDatabase.updateChildren(userInfo);
        if (resultUri != null) {
            final StorageReference filepath = FirebaseStorage.getInstance().getReference().child("profileImages").child(userId);
            Bitmap bitmap = null;

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(), resultUri);
            } catch (IOException e) {
                e.printStackTrace();
            }

            ByteArrayOutputStream baus = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baus);
            byte[] data = baus.toByteArray();
            UploadTask uploadTask = filepath.putBytes(data);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    finish();
                }
            });
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Map newImage = new HashMap();
                            newImage.put("profileImageUrl", uri.toString());
                            userDatabase.updateChildren(newImage);
                            finish();
                            return;
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(UpdateProfile.this, "Upload Failed", Toast.LENGTH_SHORT);
                            progressDialog.dismiss();
                            finish();
                            return;
                        }
                    });
                }
            });

        } else {
            finish();
        }
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
}
