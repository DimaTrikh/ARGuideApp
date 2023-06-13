package com.example.arguideapp;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegistrationViewModel extends ViewModel
{
    private FirebaseAuth auth;

    private MutableLiveData<String> error = new MutableLiveData<>();

    private MutableLiveData<FirebaseUser> user = new MutableLiveData<>();

    private static FirebaseDatabase firebaseDatabase;

    private static DatabaseReference usersReference;

    private static DatabaseReference achievementsReference;

    private static DatabaseReference usersAchievementsReference;



    public RegistrationViewModel()
    {
        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        usersReference = firebaseDatabase.getReference("Users");
        usersAchievementsReference = firebaseDatabase.getReference("UsersAchievements");
        achievementsReference = firebaseDatabase.getReference("Achievements");

        auth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null)
                {
                    user.setValue(firebaseAuth.getCurrentUser());
                }
            }
        });



    }

    public LiveData<String> getError() {
        return error;
    }

    public LiveData<FirebaseUser> getUser() {
        return user;
    }

    public void SignUp(String email, String password)
    {
        auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {

                        FirebaseUser firebaseUser = authResult.getUser();
                        if (firebaseUser == null)
                        {
                            return;
                        }
//                        Achievement achievement;
                        usersReference.child(firebaseUser.getUid()).setValue("");
                        usersAchievementsReference.child(firebaseUser.getUid()).setValue("");

                        achievementsReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                                {
                                    if (dataSnapshot.child("achievementType").getValue(String.class).equals("Object"))
                                    {
                                        //Log.e("","Obj");
                                        UserAchievement userAchievement = new UserAchievement(false);
                                        usersAchievementsReference.child(firebaseUser.getUid()).child(dataSnapshot.getKey()).setValue(userAchievement);



                                    }
                                    else if (dataSnapshot.child("achievementType").getValue(String.class).equals("manyObjects"))
                                    {
                                        //Log.e("","Objs");
                                        UserAchievementObjs userAchievementObjs = new UserAchievementObjs(false, 0);
                                        usersAchievementsReference.child(firebaseUser.getUid()).child(dataSnapshot.getKey()).setValue(userAchievementObjs);
                                    }
                                    else if(dataSnapshot.child("achievementType").getValue(String.class).equals("number"))
                                    {
                                        UserAchievementObjs userAchievementObjs = new UserAchievementObjs(false, 0);
                                        usersAchievementsReference.child(firebaseUser.getUid()).child(dataSnapshot.getKey()).setValue(userAchievementObjs);
                                        //Log.e("","num");
                                    }

//                                    Log.e("", dataSnapshot.child("achievementType").getValue(String.class));
//                                    usersAchievementsReference.child(firebaseUser.getUid()).child(dataSnapshot.getKey()).setValue("");
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                error.setValue(e.getMessage());
            }
        });
    }

}
