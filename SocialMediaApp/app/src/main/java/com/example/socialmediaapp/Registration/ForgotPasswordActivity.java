package com.example.socialmediaapp.Registration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.socialmediaapp.R;
import com.example.socialmediaapp.databinding.ActivityForgotPasswordBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {

    ActivityForgotPasswordBinding binding;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityForgotPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        auth = FirebaseAuth.getInstance();

        binding.forgotBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ForgotPassword();


            }
        });




    }

    private void ForgotPassword() {

        String email = binding.userEmail.getText().toString();

        if (email.isEmpty()){
            binding.userEmail.setError("Email is Required!");
            binding.userEmail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            binding.userEmail.setError("Please provide valid email");
            binding.userEmail.requestFocus();
            return;
        }

        binding.forgotProgress.setVisibility(View.VISIBLE);
        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()){
                    Toast.makeText(ForgotPasswordActivity.this,"Check your email to reset your password",Toast.LENGTH_SHORT).show();
                    binding.forgotProgress.setVisibility(View.GONE);

                }else{
                    Toast.makeText(ForgotPasswordActivity.this,"Try again! Something wrong happened",Toast.LENGTH_SHORT).show();
                    binding.forgotProgress.setVisibility(View.GONE);

                }

            }
        });



    }
}