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





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievement);

        firebaseDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        List<Achievement.GetAchievement> achievements = new ArrayList<>();
        usersAchievementsReference = firebaseDatabase.getReference("UsersAchievements");
        achievementsReference = firebaseDatabase.getReference("Achievements");
        recyclerViewAchievements = findViewById(R.id.recyclerviewAchievements);
        achievementsAdapter = new AchievementsAdapter();
        recyclerViewAchievements.setAdapter(achievementsAdapter);
        recyclerViewAchievements.setLayoutManager(new GridLayoutManager(this, 2));

        achievementsAdapter.setOnAchievementClickListener(new AchievementsAdapter.OnAchievementClickListener() {
            @Override
            public void onAchievementClick(Achievement.GetAchievement achievement) {
                Intent intent = AchievementDetailActivity.newIntent(AchievementActivity.this, achievement);
                startActivity(intent);
            }
        });


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
                                                String link;
                                                if (status) {
                                                    link = dataSnapshotachieve.child("completedLinkImage").getValue(String.class);
                                                } else {
                                                    link = dataSnapshotachieve.child("notCompletedLinkImage").getValue(String.class);
                                                }
                                                //Log.e(link, dataSnapshotachieve.child("achievementName").getValue(String.class));
                                                Integer progress;
                                                String name = dataSnapshotachieve.child("achievementName").getValue(String.class);
                                                String description = dataSnapshotachieve.child("achievementDescription").getValue(String.class);
                                                String type = dataSnapshotachieve.child("achievementType").getValue(String.class);
                                                String foundname = dataSnapshotachieve.child("foundName").getValue(String.class);
                                                if(type.equals("Object"))
                                                {
                                                    progress = 0;
                                                }
                                                else progress = usersAchievementsSnapshot1.child("progress").getValue(Integer.class);


                                                Achievement.GetAchievement achievement1 = new Achievement.GetAchievement(link,
                                                        name,
                                                        type,
                                                        description,
                                                        foundname,
                                                        status,
                                                        progress);



                                                achievements.add(achievement1);



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