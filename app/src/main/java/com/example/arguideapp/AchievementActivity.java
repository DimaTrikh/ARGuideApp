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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class AchievementActivity extends AppCompatActivity {

    private RecyclerView recyclerViewAchievements;
    private AchievementsAdapter achievementsAdapter;
    private static FirebaseDatabase firebaseDatabase;
    private static DatabaseReference usersAchievementsReference;
    private static DatabaseReference achievementsReference;
    private FirebaseAuth mAuth;
    String link;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievement);

        firebaseDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        List<Achievement> achievements = new ArrayList<>();
        usersAchievementsReference = firebaseDatabase.getReference("UsersAchievements");
        achievementsReference = firebaseDatabase.getReference("Achievements");
        recyclerViewAchievements = findViewById(R.id.recyclerviewAchievements);
        achievementsAdapter = new AchievementsAdapter();
        recyclerViewAchievements.setAdapter(achievementsAdapter);
        recyclerViewAchievements.setLayoutManager(new GridLayoutManager(this, 2));

        if(mAuth.getCurrentUser() != null)
        {
            usersAchievementsReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        if (mAuth.getUid().equals(dataSnapshot.getKey())) {

                            for (DataSnapshot usersAchievementsSnapshot1 : dataSnapshot.getChildren()) {
                                Boolean status = usersAchievementsSnapshot1.child("status").getValue(Boolean.class);

                                achievementsReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshotachieve) {

                                        for (DataSnapshot dataSnapshotachieve : snapshotachieve.getChildren()) {
                                            if (dataSnapshotachieve.getKey().equals(usersAchievementsSnapshot1.getKey())) {
                                                if (status) {
                                                    link = dataSnapshotachieve.child("completedLinkImage").getValue(String.class);
                                                } else {
                                                    link = dataSnapshotachieve.child("notCompletedLinkImage").getValue(String.class);
                                                }
                                                Log.e(link, dataSnapshotachieve.child("achievementName").getValue(String.class));


                                                Achievement achievement = new Achievement(dataSnapshotachieve.child("achievementName").getValue(String.class), link);
                                                achievements.add(achievement);



//                                                Glide.with(getContext()).load(link)
//                                                        .into(achievementImage);
//                                                show_achievement.setText(dataSnapshotachieve
//                                                        .child("achievementName")
//                                                        .getValue(String.class));
//

                                            }
                                            achievementsAdapter.setAchievements(achievements);



                                        }








                                    }


                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });



                            }
                            break;

                        }
                    }




                }


                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }

            });


        }





    }



    public static Intent newIntent(Context context)
    {
        return new Intent(context, AchievementActivity.class);
    }
}