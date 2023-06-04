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

public class ForgetPassword extends AppCompatActivity {

    private TextView TextOnBoard;
    private Button button_ResetPassword;
    private EditText EmailReset;

    private ForgetPasswordViewModel viewModel;

    private static final String EXTRA_EMAIL = "email";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        initViews();

        viewModel = new ViewModelProvider(this).get(ForgetPasswordViewModel.class);

        String email = getIntent().getStringExtra(EXTRA_EMAIL);
        EmailReset.setText(email);
        observeViewModel();
        button_ResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (EmailReset.getText().toString().trim().isEmpty())
                {
                    Toast.makeText(ForgetPassword.this, "Fields can't be empty", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    String email = EmailReset.getText().toString().trim();
                    viewModel.resetPassword(email);
                }

            }
        });



    }


    private void observeViewModel()
    {
        viewModel.getError().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String errorMessage) {
                if (errorMessage != null)
                {
                    Toast.makeText(ForgetPassword.this, errorMessage, Toast.LENGTH_SHORT).show();
                }
            }
        });
        viewModel.getSuccess().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean success) {
                if (success)
                {
                    Toast.makeText(ForgetPassword.this, "The reset link has been successfully sent", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void initViews()
    {
        TextOnBoard  = findViewById(R.id.TextOnBoard);
        button_ResetPassword = findViewById(R.id.button_ResetPassword);
        EmailReset = findViewById(R.id.EmailReset);
    }

    public static Intent newIntent(Context context, String email)
    {
        Intent intent = new Intent(context, ForgetPassword.class);
        intent.putExtra(EXTRA_EMAIL, email);
        return intent;

    }
}