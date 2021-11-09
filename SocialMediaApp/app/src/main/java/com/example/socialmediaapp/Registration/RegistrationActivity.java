package com.example.socialmediaapp.Registration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.socialmediaapp.R;
import com.example.socialmediaapp.databinding.ActivityRegistrationBinding;
import com.example.socialmediaapp.model.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class RegistrationActivity extends AppCompatActivity {


    ActivityRegistrationBinding binding;
    FirebaseDatabase firebaseDatabase;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegistrationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();


        binding.signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = binding.userName.getText().toString();
                String profession = binding.profession.getText().toString();
                String email = binding.userEmail.getText().toString();
                String password = binding.userPassword.getText().toString();

                if (name.isEmpty()){
                    binding.userName.setError("Name is required!");
                    binding.userName.requestFocus();
                    return;
                }
                if (profession.isEmpty()){
                    binding.profession.setError("Profession is required!");
                    binding.profession.requestFocus();
                    return;
                }
                if (email.isEmpty()){
                    binding.userEmail.setError("Email is required!");
                    binding.userEmail.requestFocus();
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    binding.userEmail.setError("please provide valid email");
                    binding.userEmail.requestFocus();
                    return;
                }

                if (password.isEmpty()) {
                    binding.userPassword.setError("Password is required!");
                    binding.userPassword.requestFocus();
                    return;
                }
                if (password.length() < 6) {
                    binding.userPassword.setError("Min password should be 6 character!");
                    binding.userPassword .requestFocus();
                    return;
                }




                binding.progressBar.setVisibility(View.VISIBLE);
                auth.createUserWithEmailAndPassword(binding.userEmail.getText().toString(), binding.userPassword.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    UserModel model = new UserModel(binding.userName.getText().toString(),binding.profession.getText().toString(),binding.userEmail.getText().toString(),binding.userPassword.getText().toString());
                                    String id = task.getResult().getUser().getUid();
                                    firebaseDatabase.getReference().child("Users").child(id).setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(RegistrationActivity.this, "Data save successfully", Toast.LENGTH_SHORT).show();
                                            binding.progressBar.setVisibility(View.GONE);
                                            startActivity(new Intent(getApplicationContext(),LoginActivity.class));

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull  Exception e) {
                                            Toast.makeText(RegistrationActivity.this, "Data is not add Error", Toast.LENGTH_SHORT).show();
                                            binding.progressBar.setVisibility(View.GONE);

                                        }
                                    });


                                }else {
                                    Toast.makeText(RegistrationActivity.this, "Failed to register! Try again!", Toast.LENGTH_SHORT).show();
                                    binding.progressBar.setVisibility(View.GONE);
                                }

                            }
                        });


            }
        });

        binding.login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });
    }
}