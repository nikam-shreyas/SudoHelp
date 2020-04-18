package com.example.sudohelp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Cards cards_data[];
    private arrayAdapter arrayAdapter;
    private int i;
    private FirebaseAuth mAuth;
    private Button signout;
    ListView listView;
    private Button apply;
    private String currentUid;
    List<Cards> row_items;
    private String name;
    private DatabaseReference usersDb, writersDb, otherDb, studentsDb, myDb, examDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        usersDb = FirebaseDatabase.getInstance().getReference().child("Users");
        writersDb = FirebaseDatabase.getInstance().getReference().child("Users").child("Writers");
        otherDb = FirebaseDatabase.getInstance().getReference().child("Users").child("Exams");
        studentsDb = FirebaseDatabase.getInstance().getReference().child("Users").child("Students");
        myDb = FirebaseDatabase.getInstance().getReference().child("Users");
        examDb = FirebaseDatabase.getInstance().getReference().child("Users").child("Exams");
        CheckType();
        row_items = new ArrayList<Cards>();
        arrayAdapter = new arrayAdapter(this, R.layout.item, row_items);

        SwipeFlingAdapterView flingContainer = (SwipeFlingAdapterView) findViewById(R.id.frame);
        apply = findViewById(R.id.apply);
        flingContainer.setAdapter(arrayAdapter);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                // this is the simplest way to delete an object from the Adapter (/AdapterView)
                Log.d("LIST", "removed object!");
                row_items.add(row_items.remove(0));
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                //Do something on the left!
                //You also have access to the original object.
                //If you want to use it just cast it (String) dataObject

            }

            @Override
            public void onRightCardExit(Object dataObject) {
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
                // Ask for more data here
            }

            @Override
            public void onScroll(float scrollProgressPercent) {

            }
        });


        // Optionally add an OnItemClickListener
        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {
            }
        });
        mAuth = FirebaseAuth.getInstance();
        currentUid = mAuth.getCurrentUser().getUid();
        writersDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    name=dataSnapshot.child(currentUid).child("Name").getValue().toString();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        signout = (Button) findViewById(R.id.signout);
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                Intent intent = new Intent(MainActivity.this, LoginOrRegister.class);
                startActivity(intent);
                finish();
                return;
            }
        });


        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this,"Please Wait...",Toast.LENGTH_SHORT).show();
                Cards card = row_items.get(0);
                final String examId = card.getId();
                String key = FirebaseDatabase.getInstance().getReference().child("Chat").push().getKey();
                examDb.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists())
                        {
                            if(dataSnapshot.child(examId).child("Status").getValue().toString().compareTo("Allotted")==0)
                            {
                                Toast.makeText(MainActivity.this, "Exam already allocated. Swipe to next!", Toast.LENGTH_LONG).show();
                            }
                            else
                            {
                                examDb.child(examId).child("Status").setValue("Allotted");
                                examDb.child(examId).child("Writer").setValue(name);
//                                studentsDb.child(currentUid).child("Applications").child(row_items.get(0).getId()).setValue("Applied");
                                writersDb.child(currentUid).child("Applications").child(row_items.get(0).getId()).setValue("Applied");
                                Toast.makeText(MainActivity.this,"Applied!",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }

    private String userType;
    private String otherUserType;


    public void getOtherType() {
        otherDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.exists()) {
                        Cards item = new Cards(dataSnapshot.child("Subject").getValue().toString(), dataSnapshot.child("Language").getValue().toString(), dataSnapshot.child("Location").getValue().toString(), dataSnapshot.child("Date").getValue().toString(), dataSnapshot.getKey(), dataSnapshot.child("Student").getValue().toString(),dataSnapshot.child("Status").getValue().toString());
                        row_items.add(item);
                        arrayAdapter.notifyDataSetChanged();
                    }

            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void CheckType() {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        studentsDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.getKey().equals(user.getUid())) {
                    userType = "Students";
                    otherUserType = "Writers";
                    getOtherType();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        writersDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.getKey().equals(user.getUid())) {
                    userType = "Writers";
                    otherUserType = "Students";
                    getOtherType();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void isApplied(String examId) {
        if (userType.compareTo("Students") == 0) {
            myDb = myDb.child("Students").child(currentUid).child("Applications");
            myDb.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        Toast.makeText(MainActivity.this, "Application exists", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });

        }
    }

    public void OpenProfile(View view) {
        Intent intent = new Intent(MainActivity.this, Profile.class);
        intent.putExtra("Usertype","Writers");
        startActivity(intent);
        return;
    }

    public void OpenApplications(View view) {

        Intent intent = new Intent(MainActivity.this, Applications.class);
        startActivity(intent);
        return;
    }

}