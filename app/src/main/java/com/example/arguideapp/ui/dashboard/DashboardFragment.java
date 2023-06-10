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
import com.example.arguideapp.R;
//import com.example.arguideapp.Registration;
//import com.example.arguideapp.User;
import com.example.arguideapp.User;
import com.example.arguideapp.UsersViewModel;
import com.example.arguideapp.databinding.FragmentDashboardBinding;
//import com.google.firebase.auth.AuthResult;
//import com.google.firebase.auth.FirebaseUser;
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

    private Boolean Shown;

    private static FirebaseDatabase firebaseDatabase;

    private static DatabaseReference usersReference;

    private UsersViewModel viewModel;



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



                    User user = new User(currentLink, ImageAnalysisQuickstart.getCurrentRequest());
                    FirebaseUser firebaseUser = viewModel.getUser().getValue();
                    if(firebaseUser != null)
                    {
                        usersReference.child(firebaseUser.getUid()).push().setValue(user);
                    }

                    //
                    //
                    //        User user = new User(firebaseUser.getUid());//
                    //        usersReference.child(user.getId()).setValue(user);












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

    /*public void uploadBlobFromFile(BlobContainerClient blobContainerClient) {
        BlobClient blobClient = blobContainerClient.getBlobClient("sampleBlob.txt");

        try {
            blobClient.uploadFromFile("filepath/local-file.png");
        } catch (UncheckedIOException ex) {
            System.err.printf("Failed to upload from file: %s%n", ex.getMessage());
        }
    }*/

    public static String getCurrentLink() {
        return currentLink;
    }



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        viewModel = new ViewModelProvider(this).get(UsersViewModel.class);
        firebaseDatabase = FirebaseDatabase.getInstance();
        usersReference = firebaseDatabase.getReference("Users");


        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        button_camera = view.findViewById(R.id.button_camera);
        imageview = view.findViewById(R.id.imageview);
        dostopremOut = view.findViewById(R.id.dostopremOut);
        button_gallerey = view.findViewById(R.id.button_gallerey);
        if (ContextCompat.checkSelfPermission(DashboardFragment.super.getContext(),
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) DashboardFragment.super.getContext(), new String[]{Manifest.permission.CAMERA}, 100);
        }

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap captureImage = (Bitmap) data.getExtras().get("data");
            imageview.setImageBitmap(captureImage);
            Azure(captureImage);

        } else if (requestCode == GALLERY_REQUEST && resultCode == Activity.RESULT_OK) {
            try {

                //Получаем URI изображения, преобразуем его в Bitmap
                //объект и отображаем в элементе ImageView нашего интерфейса:
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContext().getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                imageview.setImageBitmap(selectedImage);
                Azure(selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }




        }



    /*@Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;*/
    }

    public void Azure(Bitmap bitmap)
    {
        dostopremOut.setText("");
        seconds = 0;
        Shown = false;
        String connectionString = "DefaultEndpointsProtocol=https;AccountName=arguideappblobstorage;AccountKey=C8F9S2iM2aWv1iatRfHzIY5vGh0uaXDDFaSjUbz5AfF9+KvAxxofrXPzbjU8r/VB3GUkyKrAkQAD+ASt4FRXNw==;EndpointSuffix=core.windows.net";
        String containerName = "arguideapp";
        String accountName = "arguideappblobstorage";
        String accountKey = "C8F9S2iM2aWv1iatRfHzIY5vGh0uaXDDFaSjUbz5AfF9+KvAxxofrXPzbjU8r/VB3GUkyKrAkQAD+ASt4FRXNw==";



        BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
              .connectionString(connectionString)
              .buildClient();




        BlobContainerClient blobContainerClient = blobServiceClient.getBlobContainerClient(containerName);

        BlobClient blobClient = blobContainerClient.getBlobClient(java.util.UUID.randomUUID() + ".png");


        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        byte[] imageBytes = outputStream.toByteArray();

        blobClient.upload(new ByteArrayInputStream(imageBytes), imageBytes.length);


        //Log.e("123", blobClient.getBlobUrl() );

        //DashBoardFragmentLinkStorage.sLastLink = blobClient.getBlobUrl(); //

        currentLink = blobClient.getBlobUrl();
//
//        FirebaseUser firebaseUser = viewModel.getUser().getValue();
//
//
//        User user = new User(firebaseUser.getUid());//
//        usersReference.child(user.getId()).setValue(user);



        ImageAnalysisQuickstart.main();
        //imageAv








        mHandler.postDelayed(mRunnable, 1000);



    }

    private byte[] convertBitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        return stream.toByteArray();
    }



}