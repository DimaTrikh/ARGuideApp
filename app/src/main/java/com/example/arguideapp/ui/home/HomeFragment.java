package com.example.arguideapp.ui.home;

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

import com.example.arguideapp.R;
import com.example.arguideapp.User;
import com.example.arguideapp.UsersViewModel;
import com.example.arguideapp.databinding.FragmentHomeBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import reactor.core.scheduler.Schedulers;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    private UsersViewModel viewModel;

    private FirebaseAuth mAuth;
    private Button button_history;
    private ImageView lastimage;

    private TextView show_name_text;


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


        mAuth = FirebaseAuth.getInstance();
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

       firebaseDatabase = FirebaseDatabase.getInstance();
       usersReference = firebaseDatabase.getReference("Users");



        //button_history = findViewById()


        return root;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        viewModel = new ViewModelProvider(this).get(UsersViewModel.class);
        button_history = getView().findViewById(R.id.button_history);
        lastimage = getView().findViewById(R.id.lastimage);
        show_name_text = getView().findViewById(R.id.show_name_text);






        //TODO: ОБРАБОТАТЬ ОШИБКУ С ПЕРВЫМ ПОЛЬЗОВАТЕЛЕМ !!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        //TODO: ОБРАБОТАТЬ ОШИБКУ С ПЕРВЫМ ПОЛЬЗОВАТЕЛЕМ !!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        //TODO: ОБРАБОТАТЬ ОШИБКУ С ПЕРВЫМ ПОЛЬЗОВАТЕЛЕМ !!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        //TODO: ОБРАБОТАТЬ ОШИБКУ С ПЕРВЫМ ПОЛЬЗОВАТЕЛЕМ !!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        //TODO: ОБРАБОТАТЬ ОШИБКУ С ПЕРВЫМ ПОЛЬЗОВАТЕЛЕМ !!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        //TODO: ОБРАБОТАТЬ ОШИБКУ С ПЕРВЫМ ПОЛЬЗОВАТЕЛЕМ !!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        //TODO: ОБРАБОТАТЬ ОШИБКУ С ПЕРВЫМ ПОЛЬЗОВАТЕЛЕМ !!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        //TODO: ОБРАБОТАТЬ ОШИБКУ С ПЕРВЫМ ПОЛЬЗОВАТЕЛЕМ !!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        //TODO: ОБРАБОТАТЬ ОШИБКУ С ПЕРВЫМ ПОЛЬЗОВАТЕЛЕМ !!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        //TODO: ОБРАБОТАТЬ ОШИБКУ С ПЕРВЫМ ПОЛЬЗОВАТЕЛЕМ !!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        //TODO: ОБРАБОТАТЬ ОШИБКУ С ПЕРВЫМ ПОЛЬЗОВАТЕЛЕМ !!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        mAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {

                    usersReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            List<User> userList = new ArrayList<>();
                            //if (snapshot.getValue(String.class) == user.getUid()) {

                            for (DataSnapshot dataSnapshot : snapshot.getChildren())
                            {
                                //Log.e(dataSnapshot.getKey(), user.getUid());
                                if(user.getUid().equals(dataSnapshot.getKey()))
                                {
                                    //Log.e("УРААААААА","");
                                    //Log.e("!!!!", dataSnapshot.getValue().toString());
                                    //User user1 = dataSnapshot.getValue(User.class);
                                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                                    {
                                        Picasso.with(getContext()).load(dataSnapshot1.child("link").getValue(String.class)).into(lastimage);
                                        show_name_text.setText(dataSnapshot1.child("name").getValue(String.class));

                                    }


                                }

                            }
                        }


                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });



                }
            }
        });



        SetupClickListeners();

    }

    private void SetupClickListeners()
    {
        button_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}