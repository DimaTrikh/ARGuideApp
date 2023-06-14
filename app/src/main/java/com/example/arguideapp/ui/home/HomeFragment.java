package com.example.arguideapp.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.arguideapp.AchievementActivity;
import com.example.arguideapp.R;
import com.example.arguideapp.UsersViewModel;
import com.example.arguideapp.databinding.FragmentHomeBinding;
import com.example.arguideapp.history;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
//import com.squareup.picasso.Picasso;


public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    private UsersViewModel viewModel;

    private FirebaseAuth mAuth;
    private Button button_history;
    private ImageView lastimage;

    private Button achievementButton;
    private ImageView achievementImage;
    private TextView show_achievement;

    private String link;

    private TextView show_name_text;
    private static DatabaseReference usersAchievementsReference;
    private static DatabaseReference achievementsReference;

    private static FirebaseDatabase firebaseDatabase;

    private static DatabaseReference usersReference;

    //private String mCurrentUser;

    private MutableLiveData<FirebaseUser> user = new MutableLiveData<>();

    public LiveData<FirebaseUser> getUser() {
        return user;
    }



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        firebaseDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        usersAchievementsReference = firebaseDatabase.getReference("UsersAchievements");
        achievementsReference = firebaseDatabase.getReference("Achievements");
        usersReference = firebaseDatabase.getReference("Users");

        //AchievementListeners();

        //button_history = findViewById()


        return root;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        initViews(view);
        viewModel = new ViewModelProvider(this).get(UsersViewModel.class);







        Thread thread = new Thread(() -> {


                    if (mAuth.getCurrentUser() != null) {


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

                                                            Glide.with(getContext()).load(link)
                                                                    .into(achievementImage);
                                                            show_achievement.setText(dataSnapshotachieve
                                                                    .child("achievementName")
                                                                    .getValue(String.class));


                                                        }

                                                    }


//
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });


                                        }


                                    }


                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        usersReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {


                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    //Log.e(dataSnapshot.getKey(), user.getUid());
                                    if (mAuth.getUid().equals(dataSnapshot.getKey())) {

                                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                            if (isAdded()) {

//                                                Log.e(dataSnapshot1.child("link").getValue(String.class), dataSnapshot1.child("name")
//                                                        .getValue(String.class));


                                                Glide.with(getContext()).load(dataSnapshot1.child("link")
                                                                .getValue(String.class))
                                                        .into(lastimage);
                                                show_name_text.setText(dataSnapshot1
                                                        .child("name")
                                                        .getValue(String.class));
                                            }
                                        }
                                    }
                                }
                            }


                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


                    }

        });
        thread.start();



        SetupClickListeners();

    }

    private void SetupClickListeners()
    {
        button_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = history.newIntent(HomeFragment.this.getContext());
                startActivity(intent);

            }
        });

        achievementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = AchievementActivity.newIntent(HomeFragment.this.getContext());
                startActivity(intent);
            }
        });
    }
    private void initViews(View view)
    {
        achievementButton = view.findViewById(R.id.achievementButton);
        achievementImage = view.findViewById(R.id.achievementImage);
        show_achievement = view.findViewById(R.id.show_achievement);
        button_history = getView().findViewById(R.id.button_history);
        lastimage = getView().findViewById(R.id.lastimage);
        show_name_text = getView().findViewById(R.id.show_name_text);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}