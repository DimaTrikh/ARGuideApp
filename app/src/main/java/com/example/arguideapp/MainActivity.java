package com.example.arguideapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {


    private TextView TextOnBoard;
    private Button button_sign;
    private EditText EmailSign;
    private EditText PasswordSign;
    private  TextView ForgetPassword;
    private TextView Registration;

    private LoginViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        SetupClickListeners();

        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        observeViewModel();

    }


    private void initViews()
    {
        TextOnBoard = findViewById(R.id.TextOnBoard);
        button_sign = findViewById(R.id.button_sign);
        Registration = findViewById(R.id.Registration);
        EmailSign = findViewById(R.id.EmailSign);
        PasswordSign = findViewById(R.id.PasswordSign);
        ForgetPassword = findViewById(R.id.ForgetPassword);
    }

    private void SetupClickListeners()
    {
        button_sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*try {
                    String email = EmailSign.getText().toString().trim();
                    String password = PasswordSign.getText().toString().trim();
                    viewModel.login(email, password);
                }
                catch ()
                {

                }*/

                if(EmailSign.getText().toString().trim().isEmpty() || PasswordSign.getText().toString().trim().isEmpty())
                {
                    Toast.makeText(MainActivity.this, "Fields can't be empty", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    String email = EmailSign.getText().toString().trim();
                    String password = PasswordSign.getText().toString().trim();
                    viewModel.login(email, password);
                }







            }
        });

        ForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = com.example.arguideapp.ForgetPassword.newIntent(MainActivity.this, EmailSign.getText().toString().trim());
                startActivity(intent);
            }
        });

        Registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = com.example.arguideapp.Registration.newIntent(MainActivity.this);
                startActivity(intent);

            }
        });


    }
    public static Intent newIntent(Context context)
    {
        return new Intent(context, MainActivity.class);
    }

    private void observeViewModel()
    {
        viewModel.getError().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String errorMessage) {
                if(errorMessage != null)
                {
                    Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                }
            }
        });
        viewModel.getUser().observe(this, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                if(firebaseUser != null)
                {
                    Intent intent = BottomActivity.newIntent(MainActivity.this);
                    startActivity(intent);
                    finish();


                }
            }
        });
    }



}