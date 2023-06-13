package com.example.arguideapp.ui.notifications;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.arguideapp.BottomActivity;
import com.example.arguideapp.MainActivity;
import com.example.arguideapp.R;
import com.example.arguideapp.UserAchievement;
import com.example.arguideapp.UserAchievementObjs;
import com.example.arguideapp.UsersViewModel;
import com.example.arguideapp.databinding.FragmentNotificationsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class NotificationsFragment extends Fragment {

    private FragmentNotificationsBinding binding;

    private UsersViewModel viewModel;

    private FirebaseAuth auth;
    private Button button_achievements;
    private Button button_logout;
    private static DatabaseReference usersReference;
    private static DatabaseReference achievementsReference;
    private static DatabaseReference usersAchievementsReference;
    private static FirebaseDatabase firebaseDatabase;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NotificationsViewModel notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        usersReference = firebaseDatabase.getReference("Users");
        usersAchievementsReference = firebaseDatabase.getReference("UsersAchievements");
        achievementsReference = firebaseDatabase.getReference("Achievements");

        //final TextView textView = binding.textNotifications;
        //notificationsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(this).get(UsersViewModel.class);
        ObserveViewModel();

    }


    private void ObserveViewModel()
    {
        viewModel.getUser().observe(this, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                if (firebaseUser == null)
                {
                    Intent intent = MainActivity.newIntent(NotificationsFragment.super.getContext());
                    startActivity(intent);
                    getActivity().finish();

                }
            }
        });


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        button_logout = view.findViewById(R.id.button_logout);
        button_achievements = view.findViewById(R.id.button_achievements);
        button_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(NotificationsFragment.super.getContext());
                builder.setMessage("Вы уверены?").setCancelable(false).setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        viewModel.logout();
                    }
                }).setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.setTitle("СТОП!");
                dialog.show();

            }
        });
        button_achievements.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(NotificationsFragment.super.getContext());
                builder.setMessage("Вы уверены?").setCancelable(false).setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                        .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (auth.getCurrentUser() != null)
                        {
                            usersReference.child(auth.getUid()).setValue("");
                            usersAchievementsReference.child(auth.getUid()).setValue("");

                            achievementsReference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot dataSnapshot : snapshot.getChildren())
                                    {
                                        if (dataSnapshot.child("achievementType").getValue(String.class).equals("Object"))
                                        {
                                            //Log.e("","Obj");
                                            UserAchievement userAchievement = new UserAchievement(false);
                                            usersAchievementsReference.child(auth.getUid()).child(dataSnapshot.getKey()).setValue(userAchievement);



                                        }
                                        else if (dataSnapshot.child("achievementType").getValue(String.class).equals("manyObjects"))
                                        {
                                            //Log.e("","Objs");
                                            UserAchievementObjs userAchievementObjs = new UserAchievementObjs(false, 0);
                                            usersAchievementsReference.child(auth.getUid()).child(dataSnapshot.getKey()).setValue(userAchievementObjs);
                                        }
                                        else if(dataSnapshot.child("achievementType").getValue(String.class).equals("number"))
                                        {
                                            UserAchievementObjs userAchievementObjs = new UserAchievementObjs(false, 0);
                                            usersAchievementsReference.child(auth.getUid()).child(dataSnapshot.getKey()).setValue(userAchievementObjs);
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

                    }
                });

                AlertDialog dialog = builder.create();
                dialog.setTitle("СТОП!");
                dialog.show();




            }
        });


    }
}