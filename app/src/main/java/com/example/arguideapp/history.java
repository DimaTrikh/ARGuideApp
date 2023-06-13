package com.example.arguideapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class history extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private static DatabaseReference usersReference;
    private static FirebaseDatabase firebaseDatabase;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        usersReference = firebaseDatabase.getReference("Users");
        List<User> userList = new ArrayList<>();
        RecyclerView recyclerviewHistory = findViewById(R.id.recyclerviewHistory);
        HistoryAdapter historyAdapter = new HistoryAdapter();
        recyclerviewHistory.setAdapter(historyAdapter);
        recyclerviewHistory.setLayoutManager(new GridLayoutManager(history.this, 2));
        mAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {


                    usersReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren())
                            {

                                if(user.getUid().equals(dataSnapshot.getKey()))
                                {
                                    //Log.e("DASDADASDASDASDADASASDADASDA","DASDADASDADADAA");

                                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                                    {
                                        String link = dataSnapshot1.child("link").getValue(String.class);
                                        String name = dataSnapshot1.child("name").getValue(String.class);

                                        User user1 = new User(link, name);
                                        userList.add(user1);

                                    }

                                    break;

                                }

                            }

                            historyAdapter.setUser(userList);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });





                }
            }
        });






    }

    public static Intent newIntent(Context context)
    {
        return new Intent(context, history.class);
    }

}