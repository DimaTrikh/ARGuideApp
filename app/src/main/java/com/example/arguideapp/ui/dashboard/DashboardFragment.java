package com.example.arguideapp.ui.dashboard;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
//import android.util.Log;
import android.text.BoringLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;



import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;


//import com.azure.core.util.BinaryData;
//import com.azure.identity.DefaultAzureCredential;
//import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
//import com.azure.storage.blob.BlobContainerClientBuilder;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
//import com.azure.identity.*;
//import com.azure.storage.blob.*;
//import com.azure.storage.blob.models.*;
import java.io.*;

//import com.azure.storage.common.StorageSharedKeyCredential;
//import com.example.arguideapp.BottomActivity;
//import com.example.arguideapp.MainActivity;
import com.bumptech.glide.Glide;
import com.example.arguideapp.Achievement;
import com.example.arguideapp.R;
//import com.example.arguideapp.Registration;
//import com.example.arguideapp.User;
import com.example.arguideapp.User;
import com.example.arguideapp.UserAchievement;
import com.example.arguideapp.UserAchievementObjs;
import com.example.arguideapp.UsersViewModel;
import com.example.arguideapp.databinding.FragmentDashboardBinding;
//import com.google.firebase.auth.AuthResult;
//import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
//import java.util.List;

//import kotlin.UInt;


public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;

    private Button button_camera;

    private static String currentLink;

    private static TextView dostopremOut;

    private Button button_gallerey;
    private ImageView imageview;

    private static DatabaseReference achievementsReference;

    private static DatabaseReference usersAchievementsReference;

    private Boolean Shown;

    private static FirebaseDatabase firebaseDatabase;

    private static DatabaseReference usersReference;

    private UsersViewModel viewModel;

    private FirebaseAuth mAuth;

    //private Boolean Scanned;
    private Integer numLinks;






    private static final int CAMERA_REQUEST = 1888;
    private static final int GALLERY_REQUEST = 1889;

    private Integer seconds;

    private Handler mHandler = new Handler();

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if(((ImageAnalysisQuickstart.getCurrentRequest() != null) && (ImageAnalysisQuickstart.getCurrentRequest() != "") ) && (!Shown))
            {
                Shown = true;
                if (!ImageAnalysisQuickstart.Mistake)
                {

                    dostopremOut.setText(ImageAnalysisQuickstart.getCurrentRequest());



                    FirebaseUser firebaseUser = viewModel.getUser().getValue();
                    if(firebaseUser != null)
                    {
                        Thread thread1 = new Thread(() ->{ AchievementListeners();});
                        //AchievementListeners();
                        thread1.start();
                        //User user = new User(currentLink, ImageAnalysisQuickstart.getCurrentRequest());
                        //usersReference.child(firebaseUser.getUid()).push().setValue(user);
                        //thread.stop();
                    }


                }
                else {
                    dostopremOut.setText("Упс... Что-то пошло не так!");
                    Toast.makeText(DashboardFragment.super.getContext(), ImageAnalysisQuickstart.getCurrentRequest(), Toast.LENGTH_SHORT).show();
                }


            }
            else
            {
                if (seconds <= 3)
                {
                    seconds += 1;
                    mHandler.postDelayed(this, 1000);
                }
                else {
                    Toast.makeText(DashboardFragment.super.getContext(), "Не удалось распознать", Toast.LENGTH_SHORT).show();
                    Shown = true;
                    dostopremOut.setText("Упс... Что-то пошло не так!");


                }
            }
        }
    };



    public static String getCurrentLink() {
        return currentLink;
    }



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);
        //Scanned = false;
        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        viewModel = new ViewModelProvider(this).get(UsersViewModel.class);
        firebaseDatabase = FirebaseDatabase.getInstance();
        usersReference = firebaseDatabase.getReference("Users");
        usersAchievementsReference = firebaseDatabase.getReference("UsersAchievements");
        achievementsReference = firebaseDatabase.getReference("Achievements");
        mAuth = FirebaseAuth.getInstance();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);



        if (ContextCompat.checkSelfPermission(DashboardFragment.super.getContext(),
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) DashboardFragment.super.getContext(), new String[]{Manifest.permission.CAMERA}, 100);
        }

        ClickListeners();


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap captureImage = (Bitmap) data.getExtras().get("data");
            imageview.setImageBitmap(captureImage);
            Azure(captureImage);

        } else if (requestCode == GALLERY_REQUEST && resultCode == Activity.RESULT_OK) {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContext().getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                imageview.setImageBitmap(selectedImage);
                Azure(selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public void Azure(Bitmap bitmap)
    {
        //Scanned = false;
        dostopremOut.setText("");
        seconds = 0;
        Shown = false;
        String connectionString = "DefaultEndpointsProtocol=https;AccountName=arguideappblobstorage;AccountKey=C8F9S2iM2aWv1iatRfHzIY5vGh0uaXDDFaSjUbz5AfF9+KvAxxofrXPzbjU8r/VB3GUkyKrAkQAD+ASt4FRXNw==;EndpointSuffix=core.windows.net";
        String containerName = "arguideapp";
        BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
              .connectionString(connectionString)
              .buildClient();
        BlobContainerClient blobContainerClient = blobServiceClient.getBlobContainerClient(containerName);
        BlobClient blobClient = blobContainerClient.getBlobClient(java.util.UUID.randomUUID() + ".png");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        byte[] imageBytes = outputStream.toByteArray();
        blobClient.upload(new ByteArrayInputStream(imageBytes), imageBytes.length);


        currentLink = blobClient.getBlobUrl();
        ImageAnalysisQuickstart.main();
        mHandler.postDelayed(mRunnable, 1000);



    }

    /*private byte[] convertBitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        return stream.toByteArray();
    }*/

    private void initViews(View view)
    {
        button_camera = view.findViewById(R.id.button_camera);
        imageview = view.findViewById(R.id.imageview);
        dostopremOut = view.findViewById(R.id.dostopremOut);
        button_gallerey = view.findViewById(R.id.button_gallerey);
    }

    private void ClickListeners()
    {
        button_gallerey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, GALLERY_REQUEST);
            }
        });

        button_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, CAMERA_REQUEST);

            }
        });
    }


    private void AchievementListeners()
    {
        FirebaseUser user123 = mAuth.getCurrentUser();
        if (user123 != null) {
            usersReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot)
                {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren())
                    {
                        if (dataSnapshot.getKey().equals(user123.getUid()))
                        {
                            Boolean exists = false;
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                            {
                                String name = dataSnapshot1.child("name").getValue(String.class);
                                Log.e("", name);

                                if (name.equals(ImageAnalysisQuickstart.getCurrentRequest()))
                                {
                                    exists = true;
                                    Log.e(name, ImageAnalysisQuickstart.getCurrentRequest());
                                }

                            }

                            Log.e("", exists.toString());
                            if (!exists)
                            {

                                ifnotexistis(user123);
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

    private void ifnotexistis(FirebaseUser user123)
    {

        usersAchievementsReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot123) {

                Log.e("dad","dada");

                for (DataSnapshot usersAchievementsSnapshot : snapshot123.getChildren()) {
                    Log.e(usersAchievementsSnapshot.getKey(), user123.getUid());
                    if (usersAchievementsSnapshot.getKey().equals(user123.getUid())) {

                        for (DataSnapshot usersAchievementsSnapshot1 : usersAchievementsSnapshot.getChildren()) {
                            Boolean status = usersAchievementsSnapshot1.child("status").getValue(Boolean.class);
                            Log.e(status.toString(),"");
                            if (!status) {
                                Log.e("123", usersAchievementsSnapshot1.getKey());
                                //String Key = usersAchievementsSnapshot1.getKey();
                                achievementsReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshotachieve) {

                                        for (DataSnapshot dataSnapshotachieve : snapshotachieve.getChildren()) {
                                            if (dataSnapshotachieve.getKey().equals(usersAchievementsSnapshot1.getKey())) {
                                                if (dataSnapshotachieve.child("achievementType")
                                                        .getValue(String.class)
                                                        .equals("Object")) {//Если объекты совпадают установить true

                                                    Log.e("123", "123");

                                                    Log.e(ImageAnalysisQuickstart
                                                            .getCurrentRequest(), dataSnapshotachieve.child("foundName")
                                                            .getValue(String.class));

                                                    if (dataSnapshotachieve.child("foundName")
                                                            .getValue(String.class)
                                                            .equals(ImageAnalysisQuickstart
                                                                    .getCurrentRequest())) {
                                                        UserAchievement userAchievement = new UserAchievement(true);
                                                        usersAchievementsReference.child(user123.getUid()).child(usersAchievementsSnapshot1.getKey()).setValue(userAchievement);
                                                    }
                                                } else if (dataSnapshotachieve.child("achievementType")
                                                        .getValue(String.class)
                                                        .equals("manyObjects")) {
                                                    Log.e("1234", "1234");

                                                    String[] Objects = dataSnapshotachieve.child("foundName").getValue(String.class).split("&&");
                                                    for (String name : Objects) {
                                                        if (name.equals(ImageAnalysisQuickstart.getCurrentRequest())) {
                                                            Integer progress = usersAchievementsSnapshot1.child("progress").getValue(Integer.class);
                                                            progress++;
                                                            Boolean status;
                                                            if (progress == Objects.length) {
                                                                status = true;
                                                            } else {
                                                                status = false;
                                                            }

                                                            UserAchievementObjs userAchievementObjs = new UserAchievementObjs(status, progress);
                                                            usersAchievementsReference.child(user123.getUid()).child(usersAchievementsSnapshot1.getKey()).setValue(userAchievementObjs);

                                                        }
                                                    }
                                                } else if (dataSnapshotachieve.child("achievementType")
                                                        .getValue(String.class)
                                                        .equals("number")) {
                                                    Log.e("1234", "1234");
                                                    Integer progress = usersAchievementsSnapshot1.child("progress").getValue(Integer.class);
                                                    progress++;
                                                    Boolean status;
                                                    if (progress == Integer.parseInt(dataSnapshotachieve.child("foundName").getValue(String.class))) {
                                                        status = true;
                                                    } else {
                                                        status = false;
                                                    }

                                                    UserAchievementObjs userAchievementObjs = new UserAchievementObjs(status, progress);
                                                    usersAchievementsReference.child(user123.getUid()).child(usersAchievementsSnapshot1.getKey()).setValue(userAchievementObjs);
                                                }


                                                Log.e("", dataSnapshotachieve.child("achievementType").getValue(String.class));

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
                    }

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        User user = new User(currentLink, ImageAnalysisQuickstart.getCurrentRequest());
        usersReference.child(user123.getUid()).push().setValue(user);
    }

//    private void AchievementListeners() {
//        mAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//
//
//                FirebaseUser user123 = mAuth.getCurrentUser();
//                if (user123 != null) {
//                    usersReference.addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot snapshot) {
//                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                                if (dataSnapshot.getKey().equals(user123.getUid())) {
//                                    Boolean exists = false;
//                                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
//                                        String name = dataSnapshot1.child("name").getValue(String.class);
//                                        Log.e("", name);
//                                        if (name.equals(ImageAnalysisQuickstart.getCurrentRequest())) {
//                                            exists = true;
//                                        }
//
//                                    }
//                                    if (!exists) {
//                                        usersAchievementsReference.addListenerForSingleValueEvent(new ValueEventListener() {
//                                            @Override
//                                            public void onDataChange(@NonNull DataSnapshot snapshot123) {
//
//                                                for (DataSnapshot usersAchievementsSnapshot : snapshot123.getChildren()) {
//                                                    if (usersAchievementsSnapshot.getKey().equals(user123.getUid())) {
//                                                        for (DataSnapshot usersAchievementsSnapshot1 : usersAchievementsSnapshot.getChildren()) {
//                                                            Boolean status = usersAchievementsSnapshot1.child("status").getValue(Boolean.class);
//                                                            if (!status) {
////                                                        Log.e("123", usersAchievementsSnapshot1.getKey());
//                                                                //String Key = usersAchievementsSnapshot1.getKey();
//                                                                achievementsReference.addListenerForSingleValueEvent(new ValueEventListener() {
//                                                                    @Override
//                                                                    public void onDataChange(@NonNull DataSnapshot snapshotachieve) {
//
//                                                                        for (DataSnapshot dataSnapshotachieve : snapshotachieve.getChildren()) {
//                                                                            if (dataSnapshotachieve.getKey().equals(usersAchievementsSnapshot1.getKey())) {
//                                                                                if (dataSnapshotachieve.child("achievementType")
//                                                                                        .getValue(String.class)
//                                                                                        .equals("Object")) {//Если объекты совпадают установить true
//
//                                                                                    Log.e("123", "123");
//
//                                                                                    Log.e(ImageAnalysisQuickstart
//                                                                                            .getCurrentRequest(), dataSnapshotachieve.child("foundName")
//                                                                                            .getValue(String.class));
//
//                                                                                    if (dataSnapshotachieve.child("foundName")
//                                                                                            .getValue(String.class)
//                                                                                            .equals(ImageAnalysisQuickstart
//                                                                                                    .getCurrentRequest())) {
//                                                                                        UserAchievement userAchievement = new UserAchievement(true);
//                                                                                        usersAchievementsReference.child(user123.getUid()).child(usersAchievementsSnapshot1.getKey()).setValue(userAchievement);
//                                                                                    }
//                                                                                } else if (dataSnapshotachieve.child("achievementType")
//                                                                                        .getValue(String.class)
//                                                                                        .equals("manyObjects")) {
//                                                                                    Log.e("1234", "1234");
//
//                                                                                    String[] Objects = dataSnapshotachieve.child("foundName").getValue(String.class).split("&&");
//                                                                                    for (String name : Objects) {
//                                                                                        if (name.equals(ImageAnalysisQuickstart.getCurrentRequest())) {
//                                                                                            Integer progress = usersAchievementsSnapshot1.child("progress").getValue(Integer.class);
//                                                                                            progress++;
//                                                                                            Boolean status;
//                                                                                            if (progress == Objects.length) {
//                                                                                                status = true;
//                                                                                            } else {
//                                                                                                status = false;
//                                                                                            }
//
//                                                                                            UserAchievementObjs userAchievementObjs = new UserAchievementObjs(status, progress);
//                                                                                            usersAchievementsReference.child(user123.getUid()).child(usersAchievementsSnapshot1.getKey()).setValue(userAchievementObjs);
//
//                                                                                        }
//                                                                                    }
//                                                                                } else if (dataSnapshotachieve.child("achievementType")
//                                                                                        .getValue(String.class)
//                                                                                        .equals("number")) {
//                                                                                    Log.e("1234", "1234");
//                                                                                    Integer progress = usersAchievementsSnapshot1.child("progress").getValue(Integer.class);
//                                                                                    progress++;
//                                                                                    Boolean status;
//                                                                                    if (progress == Integer.parseInt(dataSnapshotachieve.child("foundName").getValue(String.class))) {
//                                                                                        status = true;
//                                                                                    } else {
//                                                                                        status = false;
//                                                                                    }
//
//                                                                                    UserAchievementObjs userAchievementObjs = new UserAchievementObjs(status, progress);
//                                                                                    usersAchievementsReference.child(user123.getUid()).child(usersAchievementsSnapshot1.getKey()).setValue(userAchievementObjs);
//                                                                                }
//
//
//                                                                                Log.e("", dataSnapshotachieve.child("achievementType").getValue(String.class));
////                                                                        for (DataSnapshot dataSnapshotachieve1 : dataSnapshotachieve.getChildren())
////                                                                        {
////
////                                                                        }
//
//                                                                                break;
//                                                                            }
//                                                                        }
//
//
//                                                                    }
//
//                                                                    @Override
//                                                                    public void onCancelled(@NonNull DatabaseError error) {
//
//                                                                    }
//                                                                });
//                                                            }
//
//
//                                                        }
//                                                    }
//
//
//                                                    break;
//                                                }
//
//
//                                            }
//
//                                            @Override
//                                            public void onCancelled(@NonNull DatabaseError error) {
//
//                                            }
//                                        });
//
//
//                                    }
//
//
//                                    break;
//
//                                }
//                            }
//
//
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError error) {
//
//                        }
//                    });
//
//
//                    User user = new User(currentLink, ImageAnalysisQuickstart.getCurrentRequest());
//                    usersReference.child(user123.getUid()).push().setValue(user);
//
//                }
//            }
//        });
//    }
//
//





}
//}