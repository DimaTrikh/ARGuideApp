package com.example.arguideapp;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistrationViewModel extends ViewModel
{
    private FirebaseAuth auth;

    private MutableLiveData<String> error = new MutableLiveData<>();

    private MutableLiveData<FirebaseUser> user = new MutableLiveData<>();

    private static FirebaseDatabase firebaseDatabase;

    private static DatabaseReference usersReference;

    private static DatabaseReference achievementsReference;



    public RegistrationViewModel()
    {
        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        usersReference = firebaseDatabase.getReference("Users");
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
                        Achievement achievement;
                        usersReference.child(firebaseUser.getUid()).setValue("");


                        achievementsReference.child(firebaseUser.getUid()).push().setValue(new Achievement(
                                "Moscow Kremlin",
                                "Станьте иностранным агентом",
                                "Сфотографируйте Московский Кремль",
                                false));
                        achievementsReference.child(firebaseUser.getUid()).push().setValue(new Achievement(
                                "Luzhniki Stadium",
                                "Русский Месси",
                                "Сфотографируйте самую крупную футбольную арену в России",
                                false));
                        achievementsReference.child(firebaseUser.getUid()).push().setValue(new Achievement(
                                "Pushkin Museum",
                                "Мамкин литератор",
                                "Сфотографируйте музей самого известного русского поэта",
                                false));
                        achievementsReference.child(firebaseUser.getUid()).push().setValue(new Achievement(
                                "Church of the Savior on Blood",
                                "Скатайте в Питер",
                                "Сфотографируйте Храм Спаса на Крови",
                                false));
                        achievementsReference.child(firebaseUser.getUid()).push().setValue(new Achievement(
                                "Lenin's Mausoleum",
                                "Гриб",
                                "Сфотографируйте Мавзолей Ленина",
                                false));
                        achievementsReference.child(firebaseUser.getUid()).push().setValue(new Achievement(
                                "Moscow Kremlin&&State Historical Museum&&Lenin's Mausoleum&&Saint Basil's Cathedral",
                                "Отец русского искусства",
                                "Сфотографируйте основные объекты на Красной площади",
                                false));
                        achievementsReference.child(firebaseUser.getUid()).push().setValue(new Achievement(
                                "Больше 10 объектов",
                                "Недопутешественник",
                                "Сфотографируйте 10 объектов",
                                false));
                        achievementsReference.child(firebaseUser.getUid()).push().setValue(new Achievement(
                                "Больше 50 объектов",
                                "Вам заняться нечем????",
                                "Сфотографируйте 50 объектов",
                                false));







                        /*for(int i = 0; i < 5; i++)
                        {
                            TODO: achievement = new Achievement("testName" + i,"TestDescription" + i, false);
                            TODO: achievementsReference.child(firebaseUser.getUid()).push().setValue(achievement);
                        }*/



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
