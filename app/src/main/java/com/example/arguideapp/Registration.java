package com.example.arguideapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
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

import java.util.logging.ErrorManager;

public class Registration extends AppCompatActivity
{

    private TextView TextOnBoard;
    private Button button_register;
    private EditText EmailReg;
    private EditText PasswordReg;

    private RegistrationViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(RegistrationViewModel.class);
        setContentView(R.layout.activity_registration);
        initViews();
        SetupClickListeners();
        observeViewModel();





    }

    private void SetupClickListeners()
    {
        button_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(EmailReg.getText().toString().trim().isEmpty() || PasswordReg.getText().toString().trim().isEmpty())
                {
                    Toast.makeText(Registration.this, "Fields can't be empty", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    String email = EmailReg.getText().toString().trim();
                    String password = PasswordReg.getText().toString().trim();
                    viewModel.SignUp(email, password);
                }


            }
        });


    }

    public void initViews() {
        TextOnBoard = findViewById(R.id.TextOnBoard);
        button_register = findViewById(R.id.button_register);
        EmailReg = findViewById(R.id.EmailReg);
        PasswordReg = findViewById(R.id.PasswordReg);

    }
    public static Intent newIntent(Context context)
    {
        return new Intent(context, Registration.class);
    }
    private void observeViewModel()
    {
        viewModel.getError().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String errorMessage) {
                if (errorMessage != null)
                {
                    Toast.makeText(Registration.this, errorMessage, Toast.LENGTH_SHORT).show();
                }
            }
        });
        viewModel.getUser().observe(this, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                if(firebaseUser != null)
                {
                    Intent intent = BottomActivity.newIntent(Registration.this);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

}